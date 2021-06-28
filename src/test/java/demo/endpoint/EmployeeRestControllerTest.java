/*
package demo.endpoint;

import demo.entity.Department;
import demo.entity.Employee;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static demo.constants.EmployeeTestConstants.ALL;
import static demo.constants.EmployeeTestConstants.BASE_PATH;
import static demo.constants.EmployeeTestConstants.BASE_URL;
import static demo.constants.EmployeeTestConstants.CREATE;
import static demo.constants.EmployeeTestConstants.ID_PARAM;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeRestControllerTest {

    @Test
    public void addEmployeeSuccessTest() {
        Map<String, Object> empOne = new HashMap<>();
        empOne.put("firstName", "Sam");
        empOne.put("lastName", "Smith");
        empOne.put("salary", 20000L);

        given().contentType(JSON).body(empOne).when().
                post(BASE_URL + BASE_PATH + CREATE).
                then().
                assertThat().contentType(JSON)
                .statusCode(200);
        System.out.println(empOne.toString() + "Added to Database.");
    }

    @Test
    public void addEmployeesSuccessTest() throws JSONException {
        Department department = new Department(1L, "Testing", "Delhi");

        Employee empOne = new Employee();
        empOne.setFirstName("Sam");
        empOne.setLastName("Smith");
        empOne.setSalary(20000L);
        empOne.setDepartment(department);

        Employee empTwo = new Employee();
        empTwo.setFirstName("Sam");
        empTwo.setLastName("Smith");
        empTwo.setSalary(20000L);
        empTwo.setDepartment(department);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(empOne);
        jsonArray.put(empTwo);

        Response response = given().accept(JSON).body(asList(empOne, empTwo)).when().
                post(BASE_URL + BASE_PATH + CREATE);
                //then().
                //assertThat().contentType(JSON);
                //.statusCode(200);

        System.out.println(response.getStatusCode() + "Added to Database.");
    }

    @Test
    public void getEmployeesSuccessTest() {
        ValidatableResponse test = when().
                get(BASE_URL + BASE_PATH + ALL).
                then().
                assertThat().contentType(JSON)
                .statusCode(200);
        System.out.println(test.toString());
    }

    @Test
    public void updateEmployeesSuccessTest() {
        Map<String, Object> empOne = new HashMap<>();
        empOne.put("salary", 25000L);

        given().contentType(JSON).body(empOne).when().
                put(BASE_URL + BASE_PATH + ID_PARAM, 1L).
                then().
                assertThat()
                .statusCode(200);
    }

    @Test
    public void deleteEmployeesSuccessTest() {
        when().
                delete(BASE_URL + BASE_PATH + ID_PARAM, 1L).
                then().
                assertThat().contentType(ContentType.TEXT)
                .statusCode(200);
    }


}
*/
