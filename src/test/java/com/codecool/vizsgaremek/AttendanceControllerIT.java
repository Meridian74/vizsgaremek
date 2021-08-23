package com.codecool.vizsgaremek;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.codecool.vizsgaremek.dto.*;
import com.codecool.vizsgaremek.model.Attendance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AttendanceControllerIT {

   @LocalServerPort
   private int port;

   private String employeesUrl;
   private String shiftUrl;
   private String attendanceUrl;


   @Autowired
   private TestRestTemplate testRestTemplate;

   @Autowired
   JdbcTemplate jdbcTemplate;

   @BeforeEach
   public void setUp() {
      this.employeesUrl = "http://localhost:" + port + "/api/employees";
      this.shiftUrl = "http://localhost:" + port + "/api/shift";
      this.attendanceUrl = "http://localhost:" + port + "/api/attendances";
   }

   @Test
   public void addShiftToEmployee_ReturnSizeOfAttendenceListOfEmployee() {
      jdbcTemplate.update("INSERT INTO EMPLOYEES (birth_date, name) VALUES ('2002-02-16','John Doe')");
      jdbcTemplate.update("INSERT INTO SHIFTS (shift_name, expected_start_time, duration_in_hours, rest_time_in_minutes) " +
            "VALUES ('DEFAULT SHIFT', '08:30:00', 8, 30)");

      List<EmployeeDTO> employees = List.of(testRestTemplate.getForObject(employeesUrl + "?prefix=John", EmployeeDTO[].class));
      long employeeId = employees.get(0).getId();

      List<ShiftDTO> shifts = List.of(testRestTemplate.getForObject(shiftUrl + "?prefix=DEFAULT", ShiftDTO[].class));
      long shiftId = shifts.get(0).getId();

      LocalDate dateOfWorkDay = LocalDate.parse("2021-08-24");

      CreateDateCommand command = new CreateDateCommand(dateOfWorkDay);

      AttendanceOfEmployeeDTO attendance = testRestTemplate.postForObject(attendanceUrl +
            "?emp_id=" + employeeId +
            "&shift_id=" + shiftId, command, AttendanceOfEmployeeDTO.class);

      EmployeeWithAttendencesDTO result = testRestTemplate.getForObject(attendanceUrl +
            "?emp_id=" + employeeId, EmployeeWithAttendencesDTO.class);


      assertEquals(1, result.getDailyAttendances().size());

   }


}