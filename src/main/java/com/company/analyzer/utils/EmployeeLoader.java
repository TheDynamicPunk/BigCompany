package com.company.analyzer.utils;

import com.company.analyzer.entity.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EmployeeLoader {

    public static Map<Integer, Employee> load(String filePath) throws IOException {
        Map<Integer, Employee> employees = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                if (isHeader) { // skip the first row
                    isHeader = false;
                    continue;
                }

                if (line.isBlank()) continue;

                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                double salary = Double.parseDouble(parts[3].trim());

                Integer managerId = null;
                if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                    managerId = Integer.parseInt(parts[4].trim());
                }

                Employee emp = new Employee(id, firstName, lastName, salary, managerId);
                employees.put(id, emp);
            }
        }

        return employees;
    }
}
