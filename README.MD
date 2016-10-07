#Spring Todo Web-project
Todo sample project allows:
 - view, edit tasks for register users
 - user management
 
##Application is build with
 * Spring Boot
 * Spring MVC (Thymeleaf, with HTML5 template from http://html5up.net)
 * Spring Security
 * Spring Date (JPA, Hibernate)
 * Lombok
 * Unit tests with JUnit, Mockito
 * Integration tests with MockMVC, H2

##Run application
 * mvn spring-boot:run
 * go to http://localhost:8081/todo
 * default user credentials: demo@user.com 123

##Run application with Docker
* Instructions: Docker/README.md

 ![Screenshot](https://dl.dropboxusercontent.com/u/15427688/screentodo.png)  

##Tests
 * JUnit test: mvn test
 * Integration test: mvn integration-test

## Build test report (target/site/)
* mvn surefire-report:report-only
* mvn surefire-report:failsafe-report-only