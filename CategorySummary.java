package com.zorvyn.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategorySummary {
    private String category;
    private BigDecimal total;
}
