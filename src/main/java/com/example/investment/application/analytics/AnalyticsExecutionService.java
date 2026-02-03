package com.example.investment.application.analytics;

import com.example.investment.domain.analytics.AnalyticsSnapshot;
import com.example.investment.domain.analytics.AnalyticsSnapshotRepository;
import com.example.investment.domain.analytics.SnapshotType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsExecutionService {
    private final AnalyticsService analyticsService;
    private final AnalyticsSnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public void executeAll(Long userId) {
        executeEmotion(userId);
        executeCriteria(userId);
    }

    @Transactional
    public void executeEmotion(Long userId) {
        List<EmotionAnalyticsResponse> result = analyticsService.analyzeByEmotion(userId);
        saveSnapshot(userId, SnapshotType.EMOTION_STATS, result);
    }

    @Transactional
    public void executeCriteria(Long userId) {
        List<CriteriaAnalyticsResponse> result = analyticsService.analyzeCriteria(userId);
        saveSnapshot(userId, SnapshotType.CRITERIA_STATS, result);
    }
    @Transactional(readOnly = true)
    public List<EmotionAnalyticsResponse> getEmotionStats(Long userId) {
        return snapshotRepository.findTopByUserIdAndSnapshotTypeOrderByComputedAtDesc(userId, SnapshotType.EMOTION_STATS)
                .map(s -> readList(s.getPayloadJson(), new TypeReference<List<EmotionAnalyticsResponse>>() {}))
                .orElseGet(() -> {
                    // 스냅샷 없으면 즉시 계산 후 저장까지
                    // (readOnly=true라서 여기선 저장 못 함 → 아래처럼 분리 호출 권장)
                    return analyticsService.analyzeByEmotion(userId);
                });
    }

    @Transactional(readOnly = true)
    public List<CriteriaAnalyticsResponse> getCriteriaStats(Long userId) {
        return snapshotRepository.findTopByUserIdAndSnapshotTypeOrderByComputedAtDesc(userId, SnapshotType.CRITERIA_STATS)
                .map(s -> readList(s.getPayloadJson(), new TypeReference<List<CriteriaAnalyticsResponse>>() {}))
                .orElseGet(() -> analyticsService.analyzeCriteria(userId));
    }

    private void saveSnapshot(Long userId, SnapshotType type, Object payload) {
        String json = writeJson(payload);
        AnalyticsSnapshot snapshot = AnalyticsSnapshot.of(userId, type, json, LocalDateTime.now());
        snapshotRepository.save(snapshot);
    }
    private String writeJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new IllegalStateException("failed to serialize analytics payload", e);
        }
    }
    private <T> T readList(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new IllegalStateException("failed to deserialize analytics payload", e);
        }
    }

}
