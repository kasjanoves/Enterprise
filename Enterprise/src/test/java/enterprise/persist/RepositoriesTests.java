package enterprise.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import enterprise.SpringBootWebApplication;
import enterprise.domain.Department;
import enterprise.domain.DepartmentLogEntry;
import enterprise.domain.Employee;
import enterprise.domain.Gender;
import enterprise.dto.DepartmentDTO;
import enterprise.dto.EmployeeDTO;
import enterprise.service.DepartmentsLogger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootWebApplication.class)
public class RepositoriesTests
{
    @Autowired
    DepartmentsRepository departmentsRepository;

    @Autowired
    EmployeesRepository employeesRepository;

    @Autowired
    EmployeePositionsRepository emplPositionsRepository;

    @Autowired
    DepartmentsLogger depsLogger;

    @Autowired
    DepartmentsLogRepository depsLogsRepo;

    @Test
    public void DepartmentsRepositoryTest()
    {
        LocalDateTime createDate = LocalDateTime.of(2019, 10, 30, 0, 0);

        //create
        Department head = new Department();
        head.setCreationDate(createDate);
        head.setName("Дирекция");

        departmentsRepository.save(head);
        assertNotNull(head.getId());

        //logger
        depsLogger.write(head, "created new");
        List<DepartmentLogEntry> logs = depsLogsRepo.getAll(head);
        assertTrue(logs.size() == 1);
        assertEquals(logs.get(0).getObject(), head);
        assertEquals(logs.get(0).getMessage(), "created new");

        //read 
        Department saved = departmentsRepository.get(head);
        assertNotNull(saved);
        assertEquals(saved, head);
        assertEquals(saved.getName(), "Дирекция");
        assertEquals(saved.getCreationDate(), createDate);

        //change name
        saved.setName("Главная дирекция");
        departmentsRepository.save(saved);

        List<Department> all = departmentsRepository.getAll();
        assertNotNull(all);
        assertTrue(all.size() == 1);
        assertEquals(head, all.get(0));
        assertEquals("Главная дирекция", all.get(0).getName());

        //add sub departments
        Department sub1 = new Department("Дирекция");
        sub1.setCreationDate(createDate.plusDays(1));
        departmentsRepository.save(sub1);

        Department sub2 = new Department("Бухгалтерия");
        sub1.setCreationDate(createDate.plusDays(1));
        departmentsRepository.save(sub2);

        saved = departmentsRepository.get(sub1);
        assertNotNull(saved);
        assertEquals(saved, sub1);

        //attach to parent
        DepartmentDTO qbe = new DepartmentDTO();
        qbe.setName("Дирекция");
        qbe.setParent(head.toDTO());
        departmentsRepository.save(qbe);

        qbe = new DepartmentDTO();
        qbe.setName("Бухгалтерия");
        qbe.setParent(head.toDTO());
        departmentsRepository.save(qbe);

        head = departmentsRepository.get(head);
        assertTrue(head.getSubDepartments().size() == 2);
        assertNotNull(head.getSubDepartments().get(0));
        assertEquals(head.getSubDepartments().get(0), sub1);
        assertEquals(head.getSubDepartments().get(0).getParent(), head);
        assertNotNull(head.getSubDepartments().get(1));
        assertEquals(head.getSubDepartments().get(1), sub2);
        assertEquals(head.getSubDepartments().get(1).getParent(), head);

        //move department
        qbe = new DepartmentDTO();
        qbe.setName("Бухгалтерия");
        qbe.setParent(sub1.toDTO());
        departmentsRepository.save(qbe);

        head = departmentsRepository.get(head);
        assertTrue(head.getSubDepartments().size() == 1);
        Department sub1read = head.getSubDepartments().get(0);
        assertEquals(sub1read, sub1);
        assertTrue(sub1read.getSubDepartments().size() == 1);
        assertNotNull(sub1read.getSubDepartments().get(0));
        assertEquals(sub1read.getSubDepartments().get(0), sub2);
        assertEquals(sub1read.getSubDepartments().get(0).getParent(), sub1read);

        //get all sub deps
        List<Department> allSubs = head.getSubDepsRecursively();
        assertTrue(allSubs.size() == 2);
        assertTrue(allSubs.contains(sub1));
        assertTrue(allSubs.contains(sub2));

        //get all parents
        sub2 = departmentsRepository.get(sub2);
        List<Department> allParents = sub2.getParentsRecursively();
        assertTrue(allParents.size() == 2);
        assertTrue(allParents.contains(head));
        assertTrue(allParents.contains(sub1));

        //delete
        sub1 = departmentsRepository.get(sub1);
        departmentsRepository.delete(sub1);
        assertNull(departmentsRepository.get(sub1));
        head = departmentsRepository.get(head);
        assertTrue(head.getSubDepartments().isEmpty());
        sub2 = departmentsRepository.get(sub2);
        assertNull(sub2.getParent());

        //get by name & dto
        Department headByName = departmentsRepository.get(new DepartmentDTO("Главная дирекция"));
        assertNotNull(headByName);
        assertEquals(head, headByName);
    }

    @Test
    public void EmployeesRepositoryTest()
    {
        //create
        Employee emp1 = new Employee();
        emp1.setName("Тест");
        emp1.setLastName("Тестов");
        emp1.setPatronymic("Тестович");
        emp1.setBornDate(LocalDateTime.of(1975, 1, 1, 0, 0));
        emp1.setGender(Gender.MALE);
        emp1.setEmail("testov@mail.ru");
        emp1.setPhone("79381111222");
        emp1.setChief(true);
        emp1.setEmploymentDate(LocalDateTime.of(2010, 1, 1, 0, 0));
        emp1.setPosition(emplPositionsRepository.get("Директор"));
        emp1.setSalary(100500.0);

        employeesRepository.save(emp1);
        assertNotNull(emp1.getId());

        Employee savedEmp1 = employeesRepository.get(emp1);
        assertNotNull(savedEmp1);
        assertEquals(emp1, savedEmp1);
        assertEquals("Тест", savedEmp1.getName());
        assertEquals("Тестов", savedEmp1.getLastName());
        assertEquals("Тестович", savedEmp1.getPatronymic());
        assertEquals(LocalDateTime.of(1975, 1, 1, 0, 0), savedEmp1.getBornDate());
        assertEquals(Gender.MALE, savedEmp1.getGender());
        assertEquals("testov@mail.ru", savedEmp1.getEmail());
        assertEquals("79381111222", savedEmp1.getPhone());
        assertTrue(savedEmp1.isChief());
        assertEquals(LocalDateTime.of(2010, 1, 1, 0, 0), savedEmp1.getEmploymentDate());
        assertEquals(emplPositionsRepository.get("Директор"), savedEmp1.getPosition());
        assertEquals(new Double(100500.0), savedEmp1.getSalary());

        Employee emp2 = new Employee();
        emp2.setName("Еквалс");
        emp2.setLastName("Тестов");
        emp2.setPatronymic("Тестович");
        emp2.setBornDate(LocalDateTime.of(1980, 1, 1, 0, 0));
        emp2.setGender(Gender.MALE);
        emp2.setEmail("testovequals@mail.ru");
        emp2.setPhone("79382222222");
        emp2.setChief(false);
        emp2.setEmploymentDate(LocalDateTime.of(2011, 1, 1, 0, 0));
        emp2.setPosition(emplPositionsRepository.get("Инженер"));
        emp2.setSalary(50000.0);

        employeesRepository.save(emp2);
        assertNotNull(emp2.getId());

        Employee emp3 = new Employee();
        emp3.setName("Елизвета");
        emp3.setLastName("Нуллова");
        emp3.setPatronymic("Ассертовна");
        emp3.setBornDate(LocalDateTime.of(1999, 1, 1, 0, 0));
        emp3.setGender(Gender.FEMALE);
        emp3.setEmail("testovequals@mail.ru");
        emp3.setPhone("79382222333");
        emp3.setChief(false);
        emp3.setEmploymentDate(LocalDateTime.of(2013, 1, 1, 0, 0));
        emp3.setPosition(emplPositionsRepository.get("Старшый товаровед"));
        emp3.setSalary(30000.0);

        employeesRepository.save(emp3);
        assertNotNull(emp3.getId());

        //get by criteria & edit
        EmployeeDTO qbe = new EmployeeDTO();
        qbe.setName("Еквалс");
        qbe.setLastName("Тестов");
        qbe.setPatronymic("Тестович");
        Employee emp2fetched = employeesRepository.get(qbe);
        assertNotNull(emp2fetched);
        assertEquals(emp2, emp2fetched);
        qbe.setEmail("testovequals2@mail.ru");
        qbe.setPhone("79382221111");
        qbe.setSalary(80000.0);
        employeesRepository.save(qbe);
        emp2fetched = employeesRepository.get(qbe);
        assertEquals("testovequals2@mail.ru", emp2fetched.getEmail());
        assertEquals("79382221111", emp2fetched.getPhone());
        assertEquals(new Double(80000.0), emp2fetched.getSalary());

        //attach to departments
        DepartmentDTO headDTO = new DepartmentDTO("Дирекция");
        departmentsRepository.save(headDTO);
        departmentsRepository.save(new DepartmentDTO(headDTO, "Техотдел"));
        departmentsRepository.save(new DepartmentDTO(headDTO, "Склад"));
        departmentsRepository.save(new DepartmentDTO(new DepartmentDTO("Техотдел"), "Гараж"));

        Department head = departmentsRepository.get(headDTO);
        assertNotNull(head);
        head.addEmployee(emp1);
        departmentsRepository.save(head);
        head = departmentsRepository.get(head);
        assertTrue(head.getEmployees().size() == 1);
        assertEquals(emp1, head.getEmployees().get(0));
        assertEquals(head, head.getEmployees().get(0).getDepartment());

        Department sub1 = departmentsRepository.get(new DepartmentDTO("Техотдел"));
        sub1.addEmployee(emp2);
        departmentsRepository.save(sub1);
        sub1 = departmentsRepository.get(sub1);
        assertTrue(sub1.getEmployees().size() == 1);
        assertEquals(emp2, sub1.getEmployees().get(0));
        assertEquals(sub1, sub1.getEmployees().get(0).getDepartment());

        Department sub2 = departmentsRepository.get(new DepartmentDTO("Склад"));
        sub2.addEmployee(emp3);
        departmentsRepository.save(sub2);
        sub2 = departmentsRepository.get(sub2);
        assertTrue(sub2.getEmployees().size() == 1);
        assertEquals(emp3, sub2.getEmployees().get(0));
        assertEquals(sub2, sub2.getEmployees().get(0).getDepartment());

        //move employee
        Department sub3 = departmentsRepository.get(new DepartmentDTO("Гараж"));
        sub3.addEmployee(emp3);
        departmentsRepository.save(sub3);
        sub2 = departmentsRepository.get(sub2);
        assertTrue(sub2.getEmployees().isEmpty());
        sub3 = departmentsRepository.get(sub3);
        assertTrue(sub3.getEmployees().size() == 1);
        assertEquals(emp3, sub3.getEmployees().get(0));
        assertEquals(sub3, sub3.getEmployees().get(0).getDepartment());

        //get boss
        Employee emp4 = new Employee();
        emp4.setName("Иван");
        emp4.setLastName("Ексептов");
        emp4.setPatronymic("Тестович");
        emp4.setBornDate(LocalDateTime.of(1985, 1, 1, 0, 0));
        emp4.setGender(Gender.MALE);
        emp4.setSalary(100000.0);
        employeesRepository.save(emp4);
        head = departmentsRepository.get(head);
        head.addEmployee(emp4);
        departmentsRepository.save(head);
        head = departmentsRepository.get(head);
        assertTrue(head.getEmployees().size() == 2);
        assertTrue(head.getEmployees().contains(emp4));
        emp4 = employeesRepository.get(emp4);
        assertEquals(head, emp4.getDepartment());
        assertEquals(emp1, emp4.getDepartment().getChief());

        //get salary fund
        head.updateSalaryTotal();
        departmentsRepository.save(head);
        head = departmentsRepository.get(head);
        assertEquals(new Double(emp1.getSalary() + emp4.getSalary()), head.getSalaryFundTotal());

        //dismissal 
        emp3 = employeesRepository.get(emp3);
        Department dep = departmentsRepository.get(emp3.getDepartment());
        dep.removeEmployee(emp3);
        departmentsRepository.save(dep);
        emp3.setDismissalDate(LocalDateTime.of(2019, 10, 31, 0, 0));
        employeesRepository.save(emp3);
        dep = departmentsRepository.get(dep);
        assertTrue(dep.getEmployees().isEmpty());
        emp3 = employeesRepository.get(emp3);
        assertNull(emp3.getDepartment());
        assertEquals(LocalDateTime.of(2019, 10, 31, 0, 0), emp3.getDismissalDate());

        //delete
        Employee delEmpl = employeesRepository.get(emp4);
        employeesRepository.delete(delEmpl);
        head = departmentsRepository.get(head);
        assertTrue(head.getEmployees().size() == 1);
        assertNull(employeesRepository.get(delEmpl));
    }
}
