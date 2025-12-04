package com.company.analyzer.record;

import com.company.analyzer.entity.Employee;

public record DepthIssue(
        Employee employee,
        int levelsBetween,
        int excessLevels
) {}
