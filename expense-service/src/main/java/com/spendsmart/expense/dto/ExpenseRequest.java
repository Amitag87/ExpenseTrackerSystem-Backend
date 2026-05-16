package com.spendsmart.expense.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * DTO for creating and updating expenses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private double amount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO 4217 code")
    private String currency = "INR";

    @Pattern(regexp = "^(EXPENSE|SPLIT)$", message = "Type must be EXPENSE or SPLIT")
    private String type = "EXPENSE";

    @Pattern(regexp = "^(CASH|CARD|UPI|BANK|WALLET)$", message = "Payment method must be CASH, CARD, UPI, BANK, or WALLET")
    private String paymentMethod;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    @Pattern(regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*", message = "Receipt URL must be a valid URL")
    private String receiptUrl;

    private boolean isRecurring = false;
}
