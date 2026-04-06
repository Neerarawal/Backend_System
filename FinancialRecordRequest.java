package com.zorvyn.finance.dto.request;

import com.zorvyn.finance.enums.RecordType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialRecordRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @NotNull(message = "Record type is required (INCOME or EXPENSE)")
    private RecordType type;

    @NotBlank(message = "Category is required")
    @Size(max = 60, message = "Category name too long")
    private String category;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Size(max = 1000, message = "Notes too long")
    private String notes;
}
