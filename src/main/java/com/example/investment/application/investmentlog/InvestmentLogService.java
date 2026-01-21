package com.example.investment.application.investmentlog;

import com.example.investment.domain.investmentlog.InvestmentLog;
import com.example.investment.domain.investmentlog.InvestmentLogRepository;
import com.example.investment.presentation.investmentlog.dto.CreateInvestmentLogRequest;
import com.example.investment.presentation.investmentlog.dto.InvestmentLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentLogService {
    private final InvestmentLogRepository repository;
    public Long create(CreateInvestmentLogRequest req){
        InvestmentLog log=InvestmentLog.builder().symbol(req.getSymbol())
                .decisionType(req.getDecisionType())
                .decisionPrice(req.getDecisionPrice())
                .reason(req.getReason())
                .decidedAt(req.getDecidedAt())
                .build();
        repository.save(log);
        return log.getId();

    }
    @Transactional(readOnly = true)
    public InvestmentLogResponse get(Long id){
        InvestmentLog log=repository.findById(id).orElseThrow(()->new IllegalArgumentException("log not found"));
        return InvestmentLogResponse.from(log);
    }
}
