# Technical Report - Class Assignment 1

## Index

- [Overview](#overview)
- [Analysis, Design and Implementation](#analysis-design-and-implementation)
    - [Part 1](#part-1)
      - [Step 1: Adding a new field to the Employee class](#step-1-adding-a-new-field-to-the-employee-class)
      - [Step 2: Supporting the addition of a new field to the Employee class](#step-2-supporting-the-addition-of-a-new-field-to-the-employee-class)
      - [Step 3: Adding a validation of the attributes in the constructor of the Employee class and unit tests](#step-3-adding-a-validation-of-the-attributes-in-the-constructor-of-the-employee-class-and-unit-tests)
      - [Step 4: Debug the server and client parts of the solution](#step-4-debug-the-server-and-client-parts-of-the-solution)
    - [Part 2](#part-2)
- [Analysis of the Alternative Solution](#analysis-of-the-alternative-solution)
- [Implementation of the Alternative Solution](#implementation-of-the-alternative-solution)

## Overview

This Class Assignment consisted in integrating a new feature into an existing React.js and Spring Data REST Application
along with the use of Git commands.
The first part of this exercise involves adding a new field to record the years of an employee in the company,
implementing support for the new field,
adding unit tests, and ensuring functionality across server and client parts of the solution. This former task should be
done in the master branch.

## Analysis, Design and Implementation

### Part 1

#### Step 1: Adding a new field to the Employee class

- To add a new field to record the years of the employee in the company, first it is necessary to update
  the [Employee object](tut-react-and-spring-data-rest/basic/src/main/java/com/greglturnquist/payroll/Employee.java)
  adding the following modifications:
    1. Adding a new attribute int jobYears to the class
    ```java
       private int jobYears; 
    ```
    2. Changing the signature of the constructor to public and updating the constructor to instantiate the new attribute
    ```java
       public Employee(String firstName, String lastName, String description)`  
    ```
    3. Updating the constructor to instantiate the new attribute
    ```java
       this.jobYears = jobYears;
    ```
    4. Modifying `equals()` method to include the new attribute
    5. Updating `hashCode()` method to include the new attribute
    6. Modifying `toString()` method to include the new attribute
    7. Adding new getter and setter method to include the new attribute

#### Step 2: Supporting the addition of a new field to the Employee class
- To support the addition to this new feature it was necessary to update other classes
   1. Updating [DatabaseLoader class](tut-react-and-spring-data-rest/basic/src/main/java/com/greglturnquist/payroll/DatabaseLoader.java)
    ```java
        @Override
        public void run(String... strings) throws Exception { // <4>
            this.repository.save(new Employee("Frodo", "Baggins", "ring bearer", 2));
        }
    ```
    2. Updating UI Components
    ```java
       class EmployeeList extends React.Component {
         render() {
             const employees = this.props.employees.map(employee =>
                <Employee key={employee._links.self.href} employee={employee}/>
            );
            return (
              <table>
                  <tbody>
                  <tr>
                     <th>First Name</th>
                     <th>Last Name</th>
                     <th>Description</th>
                     <th>Job Years</th>
                 </tr>
                 {employees}
                 </tbody>
             </table>
             )
        }
       }

        class Employee extends React.Component {
          render() {
              return (
               <tr>
                <td>{this.props.employee.firstName}</td>
                <td>{this.props.employee.lastName}</td>
                <td>{this.props.employee.description}</td>
                <td>{this.props.employee.jobYears}</td>
             </tr>
            )
          }
        }
    ```

#### Step 3: Adding a validation of the attributes in the constructor of the Employee class and unit tests
- To add a validation of the attributes in the creation of Employees, another modification to
  the [Employee object](tut-react-and-spring-data-rest/basic/src/main/java/com/greglturnquist/payroll/Employee.java) is
  needed
    1. Adding a new method to validate the Constructor Arguments
    ```java
        private boolean validateConstructorArguments(String firstName, String lastName, String description, int jobYears) {
            if (firstName == null || firstName.isEmpty())
                return false;
            if (lastName == null || lastName.isEmpty())
                return false;
            if (description == null || description.isEmpty())
                return false;
            if (jobYears < 0)
                return false;
            return true;
        }
    ```
    2. Updating the contructor of the Employee object
    ```java
        public Employee(String firstName, String lastName, String description, int jobYears) throws InstantiationException {
          if (!validateConstructorArguments(firstName, lastName, description, jobYears))
              throw new InstantiationException("Invalid Constructor Arguments");
          this.firstName = firstName;
          this.lastName = lastName;
          this.description = description;
          this.jobYears = jobYears;
       }
    ```

- To add unit tests for the creation of Employees it was considered a success case (when the Employee object was
created succesfully)
and failure cases (when the parameter of the construction was null or empty and an exception was throw), according
to the following examples:
    1. Success case:
    ```java
          @Test
          void newEmployeeWithValidArguments() throws InstantiationException {
            String firstName = "Frodo";
            String lastName = "Baggins";
            String description = "Ring Bearer";
            int jobYears = 0;
            assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears));
        }
    ```
    2. Failure case:
    ```java
          @Test
          void newEmployeeWithInvalidNullFirstName() {
            String firstName = null;
            String lastName = "Baggins";
            String description = "Ring Bearer";
            int jobYears = 2;
            assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears));
          }
    ```

#### Step 4: Debug the server and client parts of the solution
- To debug the server and client parts of the solution and assure data consistency:
    1. First we run the application using the command `./mvnw spring-boot:run` command in the root of
       the [basic project](tut-react-and-spring-data-rest/basic) using a bash terminal to start the server
    2. Then we open the browser with `localhost:8080` to start the client
    3. The new feature should be included in a new column of the table shown
    4. When attempting to create a new employee with invalid attributes that will not pass in the unit tests, such as
       negative values of jobYears, using a curl command the creation is completed, revealing a weakness in the data
       integrity
    ```bash
       curl -X POST localhost:8080/api/employees -d "{\"firstName\": \"Harry\", \"lastName\": \"Potter\", \"description\": \"Wizard\", \"jobYears\": \"-2\"}" -H "Content-Type:application/json"
    ```

### Part 2

## Analysis of the Alternative Solution
An alternative solution for version control that is not based on Git Hub is Mercurial.
In the same way as Git, Mercurial is a distributed version control system, where every user has a complete copy of the repository, allowing them to work independently and offline,
having the ability to commit changes locally before pushing them to central server.
Mercurial supports branching and merging, similarly to Git. However, Mercurial's branching model is simpler and more consistent compared to Git,
making it easier to understand and use. It also has built-it tools for managing and visualizing branches, simplifying this process.
Mercurial is known for its performance, especially for operations such as committing changes, branching, and merging. It is designed to be fast and efficient, even with large repositories and history.


## Implementation of the Alternative Solution
Having all this in consideration, the implementation of the alternative solution with Mercurial, would follow a similar approach with some minor adaptations:
- In the first place, it would be necessary to set up a central Mercurial repository to store the project file and track all the changes.
In order to create a local copy, it would be necessary to clone the repository;
- After following the steps that were described in the Part 1, which means making changes in the local copy of the repository and commit them locally, to share the changes in the central repository,
it is necessary to push the commit;
- To implement the Part 2 of this assignment using branches the process is also very similar to Git, being more straightforward and intuitive. First, a branch is created locally, after the changes 
in the `email-field` branch, it is possible to merge them back into the main/master branch when ready. Mercurial provides built-in tools for managing branches and resolving conflicts. The same process with
`fix-email-field` branch would be made.
- Mercurial also provides mechanisms for sharing changes between repositories efficiently, such as the push and pull commands. In addition, it provides merge tool and a resolve command.
It is possible to use these tools to resolve conflicts and ensure that changes are integrated smoothly in to the main/master branch.