package com.company.analyzer.utils;

import com.company.analyzer.entity.Employee;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeLoaderTest {

    @Test
    void testLoadEmployeesFromCsv() throws Exception {

        String csv = """
                Id,firstName,lastName,salary,managerId
                1,John,CEO,150000,
                2,Alice,Smith,60000,1
                3,Bob,Jones,55000,1
                """;

        Path temp = Files.createTempFile("employees", ".csv");
        Files.writeString(temp, csv);

        Map<Integer, Employee> employees = EmployeeLoader.load(temp.toString());

        assertEquals(3, employees.size(), "Should load exactly 3 employees");

        Employee ceo = employees.get(1);
        assertNotNull(ceo);
        assertEquals("John CEO", ceo.getFullName());
        assertNull(ceo.getManagerId(), "CEO must have null managerId");

        Employee a = employees.get(2);
        assertNotNull(a);
        assertEquals(1, a.getManagerId());

        Employee b = employees.get(3);
        assertNotNull(b);
        assertEquals(1, b.getManagerId());
    }
}
