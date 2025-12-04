package com.company.analyzer.utils;

import com.company.analyzer.entity.Employee;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HierarchyBuilderTest {

    @Test
    void testHierarchyBuilding() {

        Employee ceo = new Employee(1, "John", "CEO", 100000, null);
        Employee a = new Employee(2, "Alice", "Smith", 60000, 1);
        Employee b = new Employee(3, "Bob", "Jones", 55000, 1);

        Map<Integer, Employee> map = Map.of(
                1, ceo,
                2, a,
                3, b
        );

        Employee root = HierarchyBuilder.build(map);

        assertEquals(ceo, root, "Root employee must be the CEO");

        assertEquals(2, root.getSubordinates().size(),
                "CEO should have exactly 2 direct subordinates");

        assertTrue(root.getSubordinates().contains(a),
                "Alice must be a subordinate of the CEO");
        assertTrue(root.getSubordinates().contains(b),
                "Bob must be a subordinate of the CEO");

        // Verify reverse linkage
        assertEquals(1, a.getManagerId());
        assertEquals(1, b.getManagerId());
    }
}
