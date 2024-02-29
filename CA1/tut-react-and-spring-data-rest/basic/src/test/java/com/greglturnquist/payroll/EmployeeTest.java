package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    @Test
    void newEmployeeWithValidArguments() throws InstantiationException {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 0;
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidNullFirstName() {
        String firstName = null;
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidEmptyFirstName() {
        String firstName = "";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidNullLastName() {
        String firstName = "Frodo";
        String lastName = null;
        String description = "Ring Bearer";
        int jobYears = 2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidEmptyLastName() {
        String firstName = "Frodo";
        String lastName = "";
        String description = "Ring Bearer";
        int jobYears = 2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidNullDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = null;
        int jobYears = 2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidEmptyDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "";
        int jobYears = 2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @Test
    void newEmployeeWithInvalidJobYears() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = -2;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

}