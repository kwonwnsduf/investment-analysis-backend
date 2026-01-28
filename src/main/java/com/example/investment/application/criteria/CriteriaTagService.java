package com.example.investment.application.criteria;

import com.example.investment.domain.criteria.CriteriaTag;
import com.example.investment.domain.criteria.CriteriaTagRepository;
import com.example.investment.presentation.criteria.dto.CriteriaTagCreateRequest;
import com.example.investment.presentation.criteria.dto.CriteriaTagResponse;
import com.example.investment.presentation.criteria.dto.CriteriaTagUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CriteriaTagService {
    private final CriteriaTagRepository criteriaTagRepository;
    public CriteriaTagResponse create(CriteriaTagCreateRequest req) {
        String name = req.getName().trim();

        if (criteriaTagRepository.existsByName(name)) {
            throw new IllegalArgumentException("criteria tag already exists: " + name);
        }

        CriteriaTag tag = CriteriaTag.create(name);
        criteriaTagRepository.save(tag);

        return CriteriaTagResponse.from(tag);
    }
    @Transactional(readOnly = true)
    public List<CriteriaTagResponse> list() {
        return criteriaTagRepository.findAll().stream()
                .map(CriteriaTagResponse::from)
                .toList();
    }
    @Transactional(readOnly = true)
    public CriteriaTagResponse get(Long id) {
        CriteriaTag tag = criteriaTagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("criteria tag not found: " + id));
        return CriteriaTagResponse.from(tag);
    }
    public CriteriaTagResponse update(Long id, CriteriaTagUpdateRequest req) {
        CriteriaTag tag = criteriaTagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("criteria tag not found: " + id));

        String newName = req.getName().trim();

        if (!tag.getName().equals(newName) && criteriaTagRepository.existsByName(newName)) {
            throw new IllegalArgumentException("criteria tag already exists: " + newName);
        }

        tag.changeName(newName);
        return CriteriaTagResponse.from(tag);
    }

    public void delete(Long id) {
        CriteriaTag tag = criteriaTagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("criteria tag not found: " + id));
        criteriaTagRepository.delete(tag);
    }


}
