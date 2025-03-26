package com.edu.qlda.repository;

import com.edu.qlda.dto.*;
import com.edu.qlda.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = "select e.employee_id as employee_id, e.name, p.name as position, r.name as role, e.email," +
            "e.address,e.gender,e.phone,e.status,e.birthday,e.createdate,e.updatedate" +
            " from employee e inner join position p on p.position_id =e.position_id inner join role r on e.role_id = r.role_id ",
            nativeQuery = true)
    List<EmployeelistDto> findAllEmployee(Pageable pageable);
    boolean existsByEmail(String email);
    @Query(value = "select e.employee_id as employee_id, e.name, p.name as position, r.name as role, e.email," +
            "e.address,e.gender,e.phone,e.status,e.birthday,e.createdate,e.updatedate" +
            " from employee e inner join position p on p.position_id =e.position_id inner join role r on e.role_id = r.role_id ",
            nativeQuery = true)
    List<EmployeelistDto> findAllEmployees();
    @Query(value = "SELECT * FROM employee u WHERE u.role =1 and  (LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) " +
            "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) OR :email IS NULL)", nativeQuery = true)
    List<EmployeelistDto> searchemployees (@Param("name") String name,
                                           @Param("email") String email);

    @Query(value =
            "select e.employee_id as employee_id, e.name, p.name as position, r.name as role, e.email,e.address,e.gender,e.phone,e.status,e.birthday,e.createdate,e.password,e.updatedate,e.position_id, e.role_id from employee e inner join position p on p.position_id =e.position_id inner join role r on e.role_id = r.role_id where e.employee_id =?", nativeQuery = true)
    EmployeelistDto findByID(int id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM employee WHERE employee_id=?", nativeQuery = true)
    void deleteemployee(Integer id);
    @Query(value = "select * from employee e  where (e.name like%:name% OR :name is null)",
            countQuery = "select count(*) from employee e where (e.name like%:name% OR :name is null)", nativeQuery = true)
    Page<Employee> search(@Param(value = "name") String name, Pageable pageable);


    @Query(value = "select e.employee_id as employee_id, e.name, p.name as position, r.name as role, e.email," +
            "e.address,e.gender,e.phone,e.status,e.birthday,e.createdate,e.updatedate" +
            " from employee e inner join position p on p.position_id =e.position_id inner join role r on e.role_id = r.role_id " +
            "where (e.name like %:name% OR :name is null)",
            nativeQuery = true)
    List<EmployeelistDto> searchemployee( @Param("name") String name);

    @Query(value = "select * from employee where email = ? ", nativeQuery = true)
    Employee findByName(String email);
    @Transactional
    @Modifying
    @Query(value = "UPDATE employee AS e SET e.name= :#{#employee.name}, e.position_id= :#{#employee.position}, " +
            "e.role_id = :#{#employee.role}, e.password = :#{#employee.password}, e.address = :#{#employee.address}, " +
            "e.birthday = :#{#employee.birthday}, e.email = :#{#employee.email}, e.gender = :#{#employee.gender}, " +
            "e.phone = :#{#employee.phone}, e.status = :#{#employee.status}, e.updatedate = :#{#employee.updatedate} " +
            "WHERE e.employee_id = :employeeId",
            nativeQuery = true)
    void editEmployee(@Param("employee") EmployeeEditDto request,
                      @Param("update") LocalDate updatedate,
                      @Param("employeeId") Integer employeeId);

    @Query(value = "select e.employee_id as employee_id, e.name, p.name as position, r.name as role, e.email," +
            "e.address,e.gender,e.phone,e.status,e.birthday,e.createdate,e.updatedate" +
            " from employee e inner join position p on p.position_id =e.position_id inner join role r on e.role_id = r.role_id" +

            " where (e.name like %:#{#search.name}% OR :#{#search.name} is null)" +
            " and  (e.gender like %:#{#search.gender}% OR :#{#search.gender} is null)" +
            " and (e.position_id like %:#{#search.position}% OR :#{#search.position} is null)" +
            " and (e.role_id like %:#{#search.role}% OR :#{#search.role} is null)" +
            " and (e.status like %:#{#search.status}% OR :#{#search.status} is null)" +
            " and (e.phone like %:#{#search.phone}% OR :#{#search.phone} is null)" +
            " and (e.address like %:#{#search.address}% OR :#{#search.address} is null)" +
            "and e.createdate >= :#{#search.startDate} AND e.createdate <=: #{#search.endDate}",
            nativeQuery = true)

  List<EmployeelistDto> searchEmployeesWithDateRange(@Param("search") EmployeesearchDto search);



    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `employee` (`name`, `phone`, `email`, `status`, `role_id`, `position_id`, `address`, `createdate`, `updatedate`, `password`, `gender`, `birthday`) " +
            "VALUES (:#{#employee.name}, :#{#employee.phone}, :#{#employee.email}, :#{#employee.status}, :#{#employee.role}, :#{#employee.position}, " +
            ":#{#employee.address}, :createdate, :updatedate, :#{#employee.password}, :#{#employee.gender}, :#{#employee.birthday})",
            nativeQuery = true)
    void createEmployee(@Param("employee") EmployeecreateDto request, @Param("createdate") LocalDate createdate,
                        @Param("updatedate") LocalDate updatedate);
}