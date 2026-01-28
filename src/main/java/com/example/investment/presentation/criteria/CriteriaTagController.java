package com.example.investment.presentation.criteria;

import com.example.investment.application.criteria.CriteriaTagService;
import com.example.investment.presentation.criteria.dto.CriteriaTagCreateRequest;
import com.example.investment.presentation.criteria.dto.CriteriaTagResponse;
import com.example.investment.presentation.criteria.dto.CriteriaTagUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/criteria-tags")

public class CriteriaTagController {
    private final CriteriaTagService criteriaTagService;
    public ResponseEntity<CriteriaTagResponse> create(@RequestBody @Valid CriteriaTagCreateRequest req) {
        CriteriaTagResponse created = criteriaTagService.create(req);
        return ResponseEntity
                .created(URI.create("/api/criteria-tags/" + created.getId()))
                .body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CriteriaTagResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(criteriaTagService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CriteriaTagResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid CriteriaTagUpdateRequest req
    ) {
        return ResponseEntity.ok(criteriaTagService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        criteriaTagService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
