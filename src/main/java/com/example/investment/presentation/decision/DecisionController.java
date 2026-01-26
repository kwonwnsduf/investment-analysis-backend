package com.example.investment.presentation.decision;

import com.example.investment.application.decision.DecisionService;

import com.example.investment.presentation.decision.dto.DecisionCreateRequest;
import com.example.investment.presentation.decision.dto.DecisionResponse;
import com.example.investment.presentation.decision.dto.DecisionUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/decisions")
public class DecisionController {
    private final DecisionService decisionService;
    @PostMapping
    public ResponseEntity<DecisionResponse> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DecisionCreateRequest req
    ) {
        DecisionResponse res = decisionService.create(userId, req);

        return ResponseEntity
                .created(URI.create("/api/decisions/" + res.getId()))
                .body(res);
    }
    @GetMapping
    public List<DecisionResponse> listBySymbol(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long symbolId
    ) {
        return decisionService.listBySymbol(userId, symbolId);
    }
    @GetMapping("/me")
    public List<DecisionResponse> listAll(
            @AuthenticationPrincipal Long userId
    ) {
        return decisionService.listAll(userId);
    }
    @PatchMapping("/{decisionId}")
    public DecisionResponse update(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long decisionId,
            @RequestBody @Valid DecisionUpdateRequest req
    ) {
        return decisionService.update(userId, decisionId, req);
    }
    @DeleteMapping("/{decisionId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long decisionId
    ) {
        decisionService.delete(userId, decisionId);
        return ResponseEntity.noContent().build();
    }

}
