package com.example.investment.application.decision;

import com.example.investment.domain.criteria.CriteriaTag;
import com.example.investment.domain.criteria.CriteriaTagRepository;
import com.example.investment.domain.criteria.DecisionCriteriaRepository;
import com.example.investment.domain.decision.Decision;
import com.example.investment.domain.decision.DecisionRepository;
import com.example.investment.domain.decision.EmotionTag;
import com.example.investment.domain.investmentlog.DecisionType;
import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.symbol.SymbolRepository;
import com.example.investment.domain.user.User;
import com.example.investment.domain.user.UserRepository;
import com.example.investment.presentation.decision.dto.DecisionCreateRequest;
import com.example.investment.presentation.decision.dto.DecisionResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DecisionService.class)
public class DecisionServiceTest {

    @Autowired DecisionService decisionService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    SymbolRepository symbolRepository;
    @Autowired
    CriteriaTagRepository criteriaTagRepository;
    @Autowired
    DecisionRepository decisionRepository;
    @Autowired
    DecisionCriteriaRepository decisionCriteriaRepository;

    @Autowired
    EntityManager em;

    @Test
    void create_attachesCriteria_and_emotionsNullBecomesEmpty_and_dedupCriteriaIds() {
        // given
        User user = userRepository.save(User.builder()
                .email("a@test.com")
                .passwordHash("pw")
                .nickname("nick")
                .build());

        Symbol symbol = symbolRepository.save(Symbol.create("AAPL", "Apple", "NASDAQ"));

        CriteriaTag t1 = criteriaTagRepository.save(CriteriaTag.create("VALUATION"));
        CriteriaTag t2 = criteriaTagRepository.save(CriteriaTag.create("EARNINGS"));

        DecisionCreateRequest req = new DecisionCreateRequest();
        req.setSymbolId(symbol.getId());
        req.setType(DecisionType.BUY);
        req.setEmotions(null); // <- null 들어오는 케이스
        req.setCriteriaTagIds(Set.of(t1.getId(), t1.getId(), t2.getId())); // <- 중복 포함

        // when
        DecisionResponse res = decisionService.create(user.getId(), req);

        // then
        assertThat(res.getId()).isNotNull();
        assertThat(res.getSymbolId()).isEqualTo(symbol.getId());
        assertThat(res.getType()).isEqualTo(DecisionType.BUY);

        // emotions null -> empty set
        assertThat(res.getEmotions()).isNotNull();
        assertThat(res.getEmotions()).isEmpty();

        // criteria 중복 제거되어 2개만 연결되어야 함
        assertThat(res.getCriteriaTags()).hasSize(2);

        // DB에도 링크가 2개만 있어야 함
        assertThat(decisionCriteriaRepository.findByDecisionId(res.getId())).hasSize(2);
    }

    @Test
    void replaceCriteria_clearsOldLinks_and_attachesNewLinks() {
        // given
        User user = userRepository.save(User.builder()
                .email("b@test.com")
                .passwordHash("pw")
                .nickname("nick")
                .build());

        Symbol symbol = symbolRepository.save(Symbol.create("TSLA", "Tesla", "NASDAQ"));

        CriteriaTag old1 = criteriaTagRepository.save(CriteriaTag.create("TECHNICAL"));
        CriteriaTag old2 = criteriaTagRepository.save(CriteriaTag.create("SENTIMENT"));
        CriteriaTag new1 = criteriaTagRepository.save(CriteriaTag.create("MACRO"));

        DecisionCreateRequest req = new DecisionCreateRequest();
        req.setSymbolId(symbol.getId());
        req.setType(DecisionType.HOLD);
        req.setEmotions(Set.of(EmotionTag.CALM));
        req.setCriteriaTagIds(Set.of(old1.getId(), old2.getId()));

        DecisionResponse created = decisionService.create(user.getId(), req);

        // when
        DecisionResponse updated = decisionService.replaceCriteria(
                user.getId(), created.getId(), Set.of(new1.getId())
        );

        // then
        assertThat(updated.getCriteriaTags()).hasSize(1);

        // 영속성 컨텍스트 영향 제거 후 DB 기준으로 재검증
        em.flush();
        em.clear();

        Decision reloaded = decisionRepository.findById(created.getId()).orElseThrow();
        assertThat(reloaded.getCriteriaLinks()).hasSize(1);
        assertThat(reloaded.getCriteriaLinks().iterator().next().getCriteriaTag().getName())
                .isEqualTo("MACRO");
    }
}
