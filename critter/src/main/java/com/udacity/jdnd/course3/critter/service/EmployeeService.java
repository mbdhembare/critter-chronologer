package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    public Employee findById(Long id){
        return employeeRepository.getOne(id);
    }

    public Employee save(Employee employee){

        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployeesBySkillAndDay(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek) {
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(dayOfWeek);
        List<Employee> employees = employeeRepository.findAll();

        List<Employee> employeeList = new ArrayList<>();
        employees.forEach(thisEmployee ->{
            if(thisEmployee.getSkills().containsAll(skills) && thisEmployee.getAvailableDays().containsAll(dayOfWeeks)){
                employeeList.add(thisEmployee);
            }
        });
        return employeeList;
    }
}
