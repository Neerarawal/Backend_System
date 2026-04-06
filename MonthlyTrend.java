package com.zorvyn.finance.dto.response;

import com.zorvyn.finance.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyTrend {
    private int year;
    private int month;
    private RecordType type;
    private BigDecimal total;
}
