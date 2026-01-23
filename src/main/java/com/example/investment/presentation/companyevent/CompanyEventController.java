package com.example.investment.presentation.companyevent;

import com.example.investment.application.companyevent.CompanyEventService;
import com.example.investment.presentation.companyevent.dto.CompanyEventCreateRequest;
import com.example.investment.presentation.companyevent.dto.CompanyEventResponse;
import com.example.investment.presentation.companyevent.dto.CompanyEventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class CompanyEventController {
    private final CompanyEventService companyEventService;

    @PostMapping
    public CompanyEventResponse create(@AuthenticationPrincipal Long userId,
                                       @RequestBody CompanyEventCreateRequest req) {
        return companyEventService.create(userId, req);
    }
    @GetMapping("/timeline")
    public List<CompanyEventResponse> timeline(@AuthenticationPrincipal Long userId,
                                               @RequestParam Long symbolId) {
        return companyEventService.timeline(userId, symbolId);
    }
    @PatchMapping("/{eventId}")
    public CompanyEventResponse update(@AuthenticationPrincipal Long userId,
                                       @PathVariable Long eventId,
                                       @RequestBody CompanyEventUpdateRequest req) {
        return companyEventService.update(userId, eventId, req);
    }
    @DeleteMapping("/{eventId}")
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable Long eventId) {
        companyEventService.delete(userId, eventId);
    }

}
