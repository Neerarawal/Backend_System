package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.response.*;
import com.zorvyn.finance.enums.RecordType;
import com.zorvyn.finance.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository recordRepository;

    public DashboardSummaryResponse getSummary() {
        BigDecimal totalIncome = recordRepository.sumByType(RecordType.INCOME);
        BigDecimal totalExpenses = recordRepository.sumByType(RecordType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        List<CategorySummary> categoryBreakdown = buildCategoryBreakdown();
        List<MonthlyTrend> trends = buildMonthlyTrends();
        List<FinancialRecordResponse> recent = buildRecentActivity();

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .categoryBreakdown(categoryBreakdown)
                .monthlyTrends(trends)
                .recentActivity(recent)
                .build();
    }

    private List<CategorySummary> buildCategoryBreakdown() {
        return recordRepository.sumGroupedByCategory().stream()
                .map(row -> new CategorySummary(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .collect(Collectors.toList());
    }

    private List<MonthlyTrend> buildMonthlyTrends() {
        return recordRepository.monthlyTrendRaw().stream()
                .map(row -> new MonthlyTrend(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        RecordType.valueOf((String) row[2]),
                        (BigDecimal) row[3]
                ))
                .collect(Collectors.toList());
    }

    private List<FinancialRecordResponse> buildRecentActivity() {
        return recordRepository.findTop10ByDeletedFalseOrderByDateDesc().stream()
                .map(r -> FinancialRecordResponse.builder()
                        .id(r.getId())
                        .amount(r.getAmount())
                        .type(r.getType())
                        .category(r.getCategory())
                        .date(r.getDate())
                        .notes(r.getNotes())
                        .createdBy(r.getCreatedBy().getUsername())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

