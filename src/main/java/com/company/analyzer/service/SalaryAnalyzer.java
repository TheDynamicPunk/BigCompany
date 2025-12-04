package com.company.analyzer.service;

import com.company.analyzer.entity.Employee;
import com.company.analyzer.record.SalaryIssue;

import java.util.ArrayList;
import java.util.List;

public class SalaryAnalyzer {

    public static List<SalaryIssue> analyze(Employee ceo) {
        List<SalaryIssue> issues = new ArrayList<>();
        traverse(ceo, issues, true); // CEO is root → skip evaluation
        return issues;
    }

    private static void traverse(Employee manager, List<SalaryIssue> issues, boolean isRoot) {
        if (!isRoot) {
            var subs = manager.getSubordinates();

            if (!subs.isEmpty()) {
                double avg = subs.stream()
                        .mapToDouble(Employee::getSalary)
                        .average()
                        .orElse(0);

                double minAllowed = avg * 1.20;
                double maxAllowed = avg * 1.50;

                double salary = manager.getSalary();

                if (salary < minAllowed) {
                    issues.add(new SalaryIssue(manager, avg, minAllowed - salary, SalaryIssue.IssueType.UNDERPAID));
                } else if (salary > maxAllowed) {
                    issues.add(new SalaryIssue(manager, avg, salary - maxAllowed, SalaryIssue.IssueType.OVERPAID));
                }
            }
        }

        for (Employee sub : manager.getSubordinates()) {
            traverse(sub, issues, false);
        }
    }
}
