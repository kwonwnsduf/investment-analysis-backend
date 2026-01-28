package com.example.investment.application.decision;

import com.example.investment.domain.criteria.CriteriaTag;
import com.example.investment.domain.criteria.CriteriaTagRepository;
import com.example.investment.domain.criteria.DecisionCriteria;
import com.example.investment.domain.criteria.DecisionCriteriaRepository;
import com.example.investment.domain.decision.Decision;
import com.example.investment.domain.decision.DecisionRepository;
import com.example.investment.domain.decision.EmotionTag;
import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.symbol.SymbolRepository;
import com.example.investment.domain.user.User;
import com.example.investment.domain.user.UserRepository;
import com.example.investment.global.exception.ApiException;
import com.example.investment.global.exception.ErrorCode;
import com.example.investment.presentation.decision.dto.DecisionCreateRequest;
import com.example.investment.presentation.decision.dto.DecisionResponse;
import com.example.investment.presentation.decision.dto.DecisionUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class DecisionService {
    private final DecisionRepository decisionRepository;
    private final UserRepository userRepository;
    private final SymbolRepository symbolRepository;
    private final CriteriaTagRepository criteriaTagRepository;
    private final DecisionCriteriaRepository decisionCriteriaRepository;
    public DecisionResponse create(Long userId, DecisionCreateRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Symbol symbol = symbolRepository.findById(req.getSymbolId())
                .orElseThrow(() -> new ApiException(ErrorCode.SYMBOL_NOT_FOUND));

        // emotions는 null로 들어올 수 있음 → 빈 Set로 처리
        Set<EmotionTag> emotions = (req.getEmotions() == null)
                ? new HashSet<>()
                : new HashSet<>(req.getEmotions());

        Decision decision = Decision.create(
                user,
                symbol,
                req.getType(),
                emotions,
                req.getConfidence(),   // null 가능
                req.getReason(),       // null/blank 가능
                LocalDateTime.now()
        );

        Decision saved = decisionRepository.save(decision);
        attachCriteria(decision, req.getCriteriaTagIds());
        return DecisionResponse.from(saved);
    }
    @Transactional(readOnly = true)
    public DecisionResponse getOne(Long userId, Long decisionId) {
        Decision decision = decisionRepository.findByIdAndUser_Id(decisionId, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.DECISION_NOT_FOUND));

        return DecisionResponse.from(decision);
    }
    public DecisionResponse replaceCriteria(Long userId, Long decisionId, Set<Long> criteriaTagIds) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new IllegalArgumentException("decision not found: " + decisionId));

        // TODO: 소유권 체크 필요하면 여기서 (decision.getUser().getId().equals(userId))

        decision.clearCriteriaLinks(); // orphanRemoval=true로 기존 링크 삭제

        attachCriteria(decision, criteriaTagIds);

        return DecisionResponse.from(decision);
    }
    @Transactional(readOnly = true)
    public List<DecisionResponse> listBySymbol(Long userId, Long symbolId) {
        return decisionRepository.findByUser_IdAndSymbol_IdOrderByDecidedAtDesc(userId, symbolId)
                .stream()
                .map(DecisionResponse::from)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<DecisionResponse> listAll(Long userId) {
        return decisionRepository.findByUser_IdOrderByDecidedAtDesc(userId)
                .stream()
                .map(DecisionResponse::from)
                .toList();
    }
    public void delete(Long userId, Long decisionId) {
        Decision decision = decisionRepository.findByIdAndUser_Id(decisionId, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.DECISION_NOT_FOUND));

        decisionRepository.delete(decision);
    }
    public DecisionResponse update(Long userId, Long decisionId, DecisionUpdateRequest req) {
        Decision decision = decisionRepository.findByIdAndUser_Id(decisionId, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.DECISION_NOT_FOUND));

        if (req.getType() != null) {
            decision.changeType(req.getType());
        }

        if (req.getConfidence() != null) {
            decision.changeConfidence(req.getConfidence());
        }

        if (req.getReason() != null) {
            decision.changeReason(req.getReason());
        }

        if (req.getEmotions() != null) {
            // JPA 컬렉션은 교체보다 clear/addAll 방식이 안전한 편
            decision.replaceEmotions(req.getEmotions());
        }


        // 트랜잭션 끝나면 더티체킹으로 UPDATE 반영됨
        return DecisionResponse.from(decision);
    }
    private void attachCriteria(Decision decision, Set<Long> criteriaTagIds) {
        if (criteriaTagIds == null || criteriaTagIds.isEmpty()) return;

        // 요청 중복 제거
        Set<Long> uniqueIds = new HashSet<>(criteriaTagIds);

        // 성능: findAllById로 한 번에 조회
        List<CriteriaTag> tags = criteriaTagRepository.findAllById(uniqueIds);

        // 존재하지 않는 id 검증(원하면 빼도 됨)
        if (tags.size() != uniqueIds.size()) {
            Set<Long> found = new HashSet<>(tags.stream().map(CriteriaTag::getId).toList());
            Set<Long> missing = new HashSet<>(uniqueIds);
            missing.removeAll(found);
            throw new IllegalArgumentException("criteria tag not found: " + missing);
        }

        for (CriteriaTag tag : tags) {
            // DB unique가 최종 방어지만 서비스에서도 1차 방지
            if (decisionCriteriaRepository.existsByDecisionIdAndCriteriaTagId(decision.getId(), tag.getId())) {
                continue;
            }

            DecisionCriteria link = DecisionCriteria.link(decision, tag);
            decision.addCriteriaLink(link);

            // cascade=ALL이 있으니 decision 저장으로도 되지만,
            // 명확하게 여기서 save해도 안전
            decisionCriteriaRepository.save(link);
        }
    }

}
