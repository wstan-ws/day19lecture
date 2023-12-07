package vttp.iss.day19lecture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.iss.day19lecture.model.Employee;
import vttp.iss.day19lecture.repo.EmployeeRepo;

@SpringBootApplication
public class Day19lectureApplication implements CommandLineRunner {

	@Autowired
		EmployeeRepo empRepo;

	public static void main(String[] args) {
		SpringApplication.run(Day19lectureApplication.class, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run(String... args) throws Exception {
		
		//===================================SIMPLE=======================================//

		String pathFileName = "C:\\Users\\wstan\\Data\\employees.json";

		File file = new File(pathFileName);

		if (!file.exists()) {
			System.err.print("File Not Found");
			System.exit(1);
		}

		InputStream is = new FileInputStream(file);

		StringBuilder resultStringBuilder = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line);
			}
		}
		
		String data = resultStringBuilder.toString();
		// System.out.println(data);

		JSONParser jsonParser = new JSONParser();
		Object object = jsonParser.parse(data);

		JSONArray jsonArray = (JSONArray) object;
		// System.out.printf(">>> jsonArray Size: %s\n", jsonArray.size());
		// System.out.printf("--- jsonArray List of Objects: %s\n", jsonArray);

		List<Employee> list = new ArrayList<>();

		jsonArray.forEach(emp -> {
			Employee em = parseEmployeeObject((JSONObject) emp);
			list.add(em);
		});

		// System.out.printf(">>> List of Employees: %s\n", list);

		for (Employee emp : list) {
			empRepo.saveRecord(emp);
		}

		Employee empRetrieved = empRepo.getRecord(12345);
		// System.out.printf(">>> Employee Retrieved: %s\n", empRetrieved);

		Map<String, Employee> map = empRepo.getAll();
		// System.out.printf(">>> All Employees: %s\n", map);

		//===================================GLASSFISH=======================================//

		String pathFileName2 = "C:\\Users\\wstan\\Data\\employee2.json";

		File file2 = new File(pathFileName2);

		if (!file2.exists()) {
			System.err.print("File Not Found");
			System.exit(1);
		}

		InputStream is2 = new FileInputStream(file2);
		JsonReader jsonReader = Json.createReader(is2);
		JsonArray data2 = jsonReader.readArray();

		System.out.printf(">>> data = %s\n", data2.toString());

		List<Employee> list2 = new ArrayList<>();

		for (JsonValue value : data2) {
			JsonObject jsonEmp = (JsonObject) value;
			System.out.printf(">>> jsonEmp: %s\n", jsonEmp);

			Employee employee = new Employee();
			
			employee.setEmployeeId(Integer.valueOf(jsonEmp.get("employeeId").toString()));
			employee.setEmployeeName(jsonEmp.get("employeeName").toString());

			list2.add(employee);
		}

		for (Employee emp : list2) {
			System.out.println(emp.getEmployeeId());
			System.out.println(emp.getEmployeeName());
		}
		
	}

	private Employee parseEmployeeObject(JSONObject jsonEmployee) {
		
		Employee employee = new Employee();

		JSONObject jsonEmployeeObject = (JSONObject) jsonEmployee.get("employee");
		employee.setEmployeeId(Integer.parseInt(jsonEmployeeObject.get("employeeId").toString()));
		employee.setEmployeeName((jsonEmployeeObject.get("employeeName").toString()));

		// System.out.println(jsonEmployeeObject);
		// System.out.println(jsonEmployeeObject.get("employeeId"));
		// System.out.println(jsonEmployeeObject.get("employeeName"));
		// System.out.println(employee);

		return employee;
	}
}