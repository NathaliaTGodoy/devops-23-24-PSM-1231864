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
      - [Step 1: Creating a new branch to develop a new feature](#step-1-creating-a-new-branch-to-develop-a-new-feature)
      - [Step 2: Adding a new field to the Employee class](#step-2-adding-a-new-field-to-the-employee-class)
      - [Step 3: Supporting the addition of a new field to the Employee class](#step-3-supporting-the-addition-of-a-new-field-to-the-employee-class)
      - [Step 4: Adding a validation of the attributes in the constructor of the Employee class and unit tests](#step-4-adding-a-validation-of-the-attributes-in-the-constructor-of-the-employee-class-and-unit-tests)
      - [Step 5: Debug the server and client parts of the solution](#step-5-debug-the-server-and-client-parts-of-the-solution)
      - [Step 6: Merge the created branch along with the master branch](#step-6-merge-the-created-branch-along-with-the-master-branch)
      - [Step 7: Creating a new branch for fixing bugs](#step-7-creating-a-new-branch-for-fixing-bugs)
      - [Step 8: Implementing a new rule for validation of the new feature in the Employee class](#step-8-implementing-a-new-rule-for-validation-of-the-new-feature-in-the-employee-class)
      - [Step 9: Debug the server and client parts of the solution](#step-9-debug-the-server-and-client-parts-of-the-solution)
      - [Step 10: Merge the created branch along with the master branch](#step-10-merge-the-created-branch-along-with-the-master-branch)
- [Analysis of the Alternative Solution](#analysis-of-the-alternative-solution)
- [Implementation of the Alternative Solution](#implementation-of-the-alternative-solution)

## Overview

This report outlines a class assignment focused on enhancing an existing React.js and Spring Data REST Application
by integrating new features using version control provided by GitHub.
The assignment unfolds in two parts: firstly, adding a new field to record the years of an employee in the company,
including support implementation, unit tests, and ensuring functionality across server and client parts of the solution 
directly in the master branch.
The second part of this exercise also involves introducing a new feature to the employee entity, utilizing branching strategies
for all the steps of the development before merging into the master branch.
The report provides insights into the process, challenges, solutions encountered, and presents an alternative solution for the version control.


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
created successfully)
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

#### Step 1: Creating a new branch to develop a new feature
- Using the commands of Git (`git branch email-field` | `git checkout email-field`), a new branch called `email-field` should be created

#### Step 2: Adding a new field to the Employee class
- Following the same instructions described in the [Step 1: Adding a new field to the Employee class](#step-1-adding-a-new-field-to-the-employee-class) of [Part 1](#part-1), a new field to record the email of the employees in the company should be implemented.

#### Step 3: Supporting the addition of a new field to the Employee class
- Following the same instructions described in the [Step 2: Supporting the addition of a new field to the Employee class](#step-2-supporting-the-addition-of-a-new-field-to-the-employee-class) of [Part 1](#part-1), the support of the addition of the new field (`String email`) should be
achieved by updating the other classes that are related

#### Step 4: Adding a validation of the attributes in the constructor of the Employee class and unit tests
- Following the same instructions described in the [Step 3: Adding a validation of the attributes in the constructor of the Employee class and unit tests](#step-3-adding-a-validation-of-the-attributes-in-the-constructor-of-the-employee-class-and-unit-tests) of [Part 1](#part-1), a validation of the implemented attribute (`String email`) 
should be achieved by updating the constructor of the [Employee object](tut-react-and-spring-data-rest/basic/src/main/java/com/greglturnquist/payroll/Employee.java) and creating unit tests

#### Step 5: Debug the server and client parts of the solution
- Following the same instructions described in the [Step 4: Debug the server and client parts of the solution](#step-4-debug-the-server-and-client-parts-of-the-solution) of [Part 1](#part-1), a debug of the server and client parts of the solution should be made to guarantee data consistency

#### Step 6: Merge the created branch along with the master branch
- Using the commands of Git, after committing the changes implemented in the created branch, a merge need to be made with the master branch
  1. `git push origin email-field`
  2. `git checkout master`
  3. `git merge --no-ff email-field`
  4. `git push`

#### Step 7: Creating a new branch for fixing bugs
- Using the same instructions described in the [Step 1: Creating a new branch to develop a new feature](#step-1-creating-a-new-branch-to-develop-a-new-feature) of [Part 2](#part-2), a new branch called `fix-email-field` should be created

#### Step 8: Implementing a new rule for validation of the new feature in the Employee class
- A new rule in the constructor of [Employee object](tut-react-and-spring-data-rest/basic/src/main/java/com/greglturnquist/payroll/Employee.java) should be added (`!email.contains("@")`). This modification needs to be tested in the unit tests, for instance:
    ```java
       @Test
        void newEmployeeWithInvalidEmailFormat() {
           String firstName = "Frodo";
           String lastName = "Baggins";
           String description = "Ring Bearer";
           int jobYears = 2;
           String email = "frodo.gmail.com";
           assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
       }
    ```

#### Step 9: Debug the server and client parts of the solution
- Following the same instructions described in the [Step 5: Debug the server and client parts of the solution](#step-5-debug-the-server-and-client-parts-of-the-solution) of [Part 2](#part-2), a debug of the server and client parts of the solution should be made to guarantee data consistency

#### Step 10: Merge the created branch along with the master branch
- Following the same instructions described in the [Step 6: Merge the created branch along with the master branch](#step-6-merge-the-created-branch-along-with-the-master-branch) of [Part 2](#part-2), a merge of the branch `fix-email-field` should be merge in the master branch

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