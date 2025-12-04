package com.company.analyzer.service;

import com.company.analyzer.entity.Employee;
import com.company.analyzer.record.DepthIssue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DepthAnalyzerTest {

    @Test
    void testExcessiveReportingDepth() {

        // Build chain: emp → M5 → M4 → M3 → M2 → M1 → CEO
        Employee ceo = new Employee(1, "John", "CEO", 100000, null);

        Employee m1 = new Employee(2, "M1", "Manager", 80000, 1);
        Employee m2 = new Employee(3, "M2", "Manager", 75000, 2);
        Employee m3 = new Employee(4, "M3", "Manager", 70000, 3);
        Employee m4 = new Employee(5, "M4", "Manager", 65000, 4);
        Employee m5 = new Employee(6, "M5", "Manager", 60000, 5);

        Employee emp = new Employee(7, "E", "Worker", 40000, 6);

        Map<Integer, Employee> map = Map.of(
                1, ceo,
                2, m1,
                3, m2,
                4, m3,
                5, m4,
                6, m5,
                7, emp
        );

        // Build links manually
        ceo.getSubordinates().add(m1);
        m1.getSubordinates().add(m2);
        m2.getSubordinates().add(m3);
        m3.getSubordinates().add(m4);
        m4.getSubordinates().add(m5);
        m5.getSubordinates().add(emp);

        List<DepthIssue> issues = DepthAnalyzer.analyze(map, ceo);

        // Only emp should be flagged
        assertEquals(1, issues.size(), "Exactly one employee should exceed max depth");

        DepthIssue issue = issues.get(0);
        assertEquals(emp, issue.employee(), "Correct employee must be flagged");

        assertEquals(5, issue.levelsBetween(),
                "Emp should have 5 managers between them and the CEO");

        assertEquals(1, issue.excessLevels(),
                "Emp depth should exceed max by exactly 1");
    }
}
