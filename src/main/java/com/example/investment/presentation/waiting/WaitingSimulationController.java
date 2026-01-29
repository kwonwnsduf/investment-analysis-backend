package com.example.investment.presentation.waiting;

import com.example.investment.application.waiting.WaitingSimulationService;
import com.example.investment.presentation.waiting.dto.WaitingSimulationRequest;
import com.example.investment.presentation.waiting.dto.WaitingSimulationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/decisions")
public class WaitingSimulationController {

    private final WaitingSimulationService waitingSimulationService;
    @PostMapping("/{id}/waiting-simulations")
    public WaitingSimulationResponse simulate(
            @PathVariable("id") Long decisionId,
            @RequestBody @Valid WaitingSimulationRequest request
    ) {
        return waitingSimulationService.simulate(decisionId, request.waitDays());
    }
}
