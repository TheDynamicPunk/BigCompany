package com.company.analyzer;

import com.company.analyzer.entity.Employee;
import com.company.analyzer.record.DepthIssue;
import com.company.analyzer.record.SalaryIssue;
import com.company.analyzer.service.DepthAnalyzer;
import com.company.analyzer.service.SalaryAnalyzer;
import com.company.analyzer.utils.EmployeeLoader;
import com.company.analyzer.utils.HierarchyBuilder;

import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.err.println("Usage: java -jar employee-analyzer.jar <csv-file> [--format=pretty|json]");
            return;
        }

        String filePath = args[0];
        String format = "pretty"; // default

        if (args.length > 1 && args[1].startsWith("--format=")) {
            format = args[1].substring("--format=".length());
        }

        // Load & build hierarchy
        Map<Integer, Employee> employees = EmployeeLoader.load(filePath);
        Employee ceo = HierarchyBuilder.build(employees);

        // Run analysis
        List<SalaryIssue> salaryIssues = SalaryAnalyzer.analyze(ceo);
        List<DepthIssue> depthIssues = DepthAnalyzer.analyze(employees, ceo);

        // Output selection
        switch (format) {
            case "json" -> printJson(salaryIssues, depthIssues);
            case "pretty" -> printPretty(salaryIssues, depthIssues);
            default -> {
                System.out.println("Unknown format. Using pretty.");
                printPretty(salaryIssues, depthIssues);
            }
        }
    }

    // ----------------------------------------------------------------------
    // OPTION A: Pretty Human-Readable Output (Default)
    // ----------------------------------------------------------------------
    private static void printPretty(List<SalaryIssue> salaryIssues, List<DepthIssue> depthIssues) {

        System.out.println("=== Managers Underpaid ===");
        salaryIssues.stream()
                .filter(i -> i.type() == SalaryIssue.IssueType.UNDERPAID)
                .forEach(i -> System.out.printf(
                        "- %s (%d): short by $%.2f (avg subordinate salary: %.2f)%n",
                        i.manager().getFullName(),
                        i.manager().getId(),
                        i.difference(),
                        i.averageSubordinateSalary()
                ));

        System.out.println("\n=== Managers Overpaid ===");
        salaryIssues.stream()
                .filter(i -> i.type() == SalaryIssue.IssueType.OVERPAID)
                .forEach(i -> System.out.printf(
                        "- %s (%d): excess of $%.2f (avg subordinate salary: %.2f)%n",
                        i.manager().getFullName(),
                        i.manager().getId(),
                        i.difference(),
                        i.averageSubordinateSalary()
                ));

        System.out.println("\n=== Employees With Excessive Reporting Depth ===");
        depthIssues.forEach(i -> System.out.printf(
                "- %s (%d): %d levels too long (actual = %d, allowed = 4)%n",
                i.employee().getFullName(),
                i.employee().getId(),
                i.excessLevels(),
                i.levelsBetween()
        ));
    }

    // ----------------------------------------------------------------------
    // OPTION B: JSON-Like Structured Output
    // ----------------------------------------------------------------------
    private static void printJson(List<SalaryIssue> salaryIssues, List<DepthIssue> depthIssues) {

        System.out.println("{");

        // Salary issues
        System.out.println("  \"salaryIssues\": [");
        for (int i = 0; i < salaryIssues.size(); i++) {
            SalaryIssue s = salaryIssues.get(i);
            System.out.printf(
                    "    {\"managerId\": %d, \"name\": \"%s\", \"type\": \"%s\", \"difference\": %.2f, \"avgSubSalary\": %.2f}",
                    s.manager().getId(),
                    s.manager().getFullName(),
                    s.type(),
                    s.difference(),
                    s.averageSubordinateSalary()
            );
            if (i < salaryIssues.size() - 1) System.out.print(",");
            System.out.println();
        }
        System.out.println("  ],");

        // Depth issues
        System.out.println("  \"depthIssues\": [");
        for (int i = 0; i < depthIssues.size(); i++) {
            DepthIssue d = depthIssues.get(i);
            System.out.printf(
                    "    {\"employeeId\": %d, \"name\": \"%s\", \"levelsBetween\": %d, \"excessLevels\": %d}",
                    d.employee().getId(),
                    d.employee().getFullName(),
                    d.levelsBetween(),
                    d.excessLevels()
            );
            if (i < depthIssues.size() - 1) System.out.print(",");
            System.out.println();
        }
        System.out.println("  ]");

        System.out.println("}");
    }
}
