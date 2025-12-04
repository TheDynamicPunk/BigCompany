package com.company.analyzer.service;

import com.company.analyzer.entity.Employee;
import com.company.analyzer.record.SalaryIssue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaryAnalyzerTest {

    @Test
    void testSalaryIssues() {

        // CEO should NOT be included in salary issue evaluation
        Employee ceo = new Employee(1, "CEO", "Boss", 100000, null);

        // UNDERPAID manager
        Employee m1 = new Employee(2, "Under", "Paid", 30000, 1);
        Employee s1 = new Employee(3, "S1", "Emp", 40000, 2);
        Employee s2 = new Employee(4, "S2", "Emp", 42000, 2);

        // OVERPAID manager
        Employee m2 = new Employee(5, "Over", "Paid", 90000, 1);
        Employee s3 = new Employee(6, "S3", "Emp", 30000, 5);

        // Build hierarchy
        ceo.getSubordinates().add(m1);
        ceo.getSubordinates().add(m2);
        m1.getSubordinates().add(s1);
        m1.getSubordinates().add(s2);
        m2.getSubordinates().add(s3);

        // Analyze
        List<SalaryIssue> issues = SalaryAnalyzer.analyze(ceo);

        // ----- Assertions -----

        // Expect exactly 2 issues: m1 (underpaid) and m2 (overpaid)
        assertEquals(2, issues.size(), "Exactly two salary issues should exist");

        // Extract underpaid and overpaid
        SalaryIssue under = issues.stream()
                .filter(i -> i.type() == SalaryIssue.IssueType.UNDERPAID)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected one UNDERPAID manager"));

        SalaryIssue over = issues.stream()
                .filter(i -> i.type() == SalaryIssue.IssueType.OVERPAID)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected one OVERPAID manager"));

        // Ensure correct managers are reported
        assertEquals(m1, under.manager(), "m1 must be the underpaid manager");
        assertEquals(m2, over.manager(), "m2 must be the overpaid manager");

        // CEO must NOT be included in issues
        assertTrue(
                issues.stream().noneMatch(i -> i.manager().equals(ceo)),
                "CEO should NOT be flagged as an overpaid/underpaid manager"
        );

        // Optional: verify numeric values

        // Expected avg = (40000 + 42000) / 2 = 41000
        // minAllowed = 41000 * 1.2 = 49200
        // difference = 49200 - 30000 = 19200
        assertEquals(49200 - 30000, under.difference(), 0.01);

        // Expected avg = 30000
        // maxAllowed = 45000
        // difference = 90000 - 45000 = 45000
        assertEquals(90000 - 45000, over.difference(), 0.01);
    }
}
