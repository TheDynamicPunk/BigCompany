package com.company.analyzer.service;

import com.company.analyzer.entity.Employee;
import com.company.analyzer.record.DepthIssue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepthAnalyzer {

    public static List<DepthIssue> analyze(Map<Integer, Employee> employees, Employee ceo) {
        List<DepthIssue> issues = new ArrayList<>();

        for (Employee emp : employees.values()) {
            if (emp == ceo) continue; // CEO has no managers

            int depth = countManagersBetween(emp, employees, ceo);

            if (depth > 4) {
                issues.add(new DepthIssue(
                        emp,
                        depth,
                        depth - 4
                ));
            }
        }

        return issues;
    }

    private static int countManagersBetween(Employee emp, Map<Integer, Employee> employees, Employee ceo) {
        int count = 0;
        Employee current = emp;

        while (current.getManagerId() != null) {
            current = employees.get(current.getManagerId());

            // Stop when we hit the CEO
            if (current == ceo) break;

            count++;
        }

        return count;
    }
}
