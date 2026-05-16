package com.spendsmart.analytics.util;


public class FinancialCalculator {

    // Savings Rate
    public static double calculateSavingsRate(double income, double expenses) {
        if (income == 0) return 0;
        return ((income - expenses) / income) * 100;
    }

    // Financial Health Score (as per your doc ✔)
    public static int calculateHealthScore(double savingsRate,
                                           double budgetAdherence,
                                           double expenseRatio) {

        return (int) (
                (savingsRate * 0.4) +
                (budgetAdherence * 0.4) +
                (expenseRatio * 0.2)
        );
    }

    // Status based on score
    public static String getHealthStatus(int score) {
        if (score >= 75) return "GOOD";
        if (score >= 50) return "AVERAGE";
        return "POOR";
    }

    // Expense Ratio
    public static double calculateExpenseRatio(double income, double expenses) {
        if (income == 0) return 0;
        return (expenses / income) * 100;
    }

    // 3-Month Forecast (Trailing Average + Momentum)
    public static double forecastSpending(double[] last3Months) {

        if (last3Months.length < 3) return 0;

        double avg = (last3Months[0] + last3Months[1] + last3Months[2]) / 3;

        // simple momentum (last - first)
        double momentum = last3Months[2] - last3Months[0];

        return avg + (momentum * 0.2);
    }
}
