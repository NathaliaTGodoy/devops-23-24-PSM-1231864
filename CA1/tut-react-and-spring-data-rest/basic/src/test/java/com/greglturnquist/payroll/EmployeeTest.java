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
        String email = "frodo@gmail.com";
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidNullFirstName() {
        String firstName = null;
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidEmptyFirstName() {
        String firstName = "";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidNullLastName() {
        String firstName = "Frodo";
        String lastName = null;
        String description = "Ring Bearer";
        int jobYears = 2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidEmptyLastName() {
        String firstName = "Frodo";
        String lastName = "";
        String description = "Ring Bearer";
        int jobYears = 2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidNullDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = null;
        int jobYears = 2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidEmptyDescription() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "";
        int jobYears = 2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidJobYears() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = -2;
        String email = "frodo@gmail.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidNullEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 2;
        String email = null;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void newEmployeeWithInvalidEmptyEmail() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 2;
        String email = "";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

}