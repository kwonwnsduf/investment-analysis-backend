package com.example.investment.presentation.waiting.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WaitingSimulationRequest(@NotNull
                                        @Min(1)
                                        @Max(3650) // 10년까지
                                        Integer waitDays) {
}
