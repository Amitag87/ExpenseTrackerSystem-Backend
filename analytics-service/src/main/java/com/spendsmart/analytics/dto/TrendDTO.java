package com.spendsmart.analytics.dto;



import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrendDTO {

    private int month;
    private double income;
    private double expenses;
}