package com.company.analyzer.utils;

import com.company.analyzer.entity.Employee;

import java.util.Map;

public class HierarchyBuilder {

    public static Employee build(Map<Integer, Employee> employees) {
        Employee ceo = null;

        // Link subordinates
        for (Employee emp : employees.values()) {
            Integer managerId = emp.getManagerId();

            if (managerId == null) {
                ceo = emp; // CEO found
            } else {
                Employee manager = employees.get(managerId);
                if (manager != null) {
                    manager.getSubordinates().add(emp);
                } else {
                    throw new IllegalStateException(
                            "Manager with id " + managerId + " not found for employee " + emp.getId()
                    );
                }
            }
        }

        if (ceo == null) {
            throw new IllegalStateException("No CEO found (employee with null managerId).");
        }

        return ceo;
    }
}
