package com.zorvyn.finance.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardSummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private List<CategorySummary> categoryBreakdown;
    private List<MonthlyTrend> monthlyTrends;
    private List<FinancialRecordResponse> recentActivity;
}
