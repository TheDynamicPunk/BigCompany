package com.company.analyzer.record;

import com.company.analyzer.entity.Employee;

public record SalaryIssue(
        Employee manager,
        double averageSubordinateSalary,
        double difference,
        IssueType type
) {
    public enum IssueType {
        UNDERPAID, OVERPAID
    }
}
