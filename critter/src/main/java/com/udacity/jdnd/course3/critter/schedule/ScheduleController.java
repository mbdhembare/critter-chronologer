package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Employee> employees = getEmployeesByIds(scheduleDTO.getEmployeeIds());
        schedule.setEmployees(employees);

        List<Pet> pets = getPetsByIds(scheduleDTO.getPetIds());
        schedule.setPets(pets);

        for(Pet pet: pets){
            Customer customer = customerService.getCustomerById(pet.getCustomer().getId());
            if(schedule.getCustomers()==null){
                schedule.setCustomers(new ArrayList<>());
            }
            schedule.getCustomers().add(customer);
        }

        Schedule newSchedule = scheduleService.save(schedule);

        for(Employee employee : employees){
            if(employee.getSchedules() == null) employee.setSchedules(new ArrayList<>());
            employee.getSchedules().add(schedule);
            employeeService.save(employee);
        }

        for(Pet pet : pets){
            if(pet.getSchedules() == null) pet.setSchedules(new ArrayList<>());
            pet.getSchedules().add(schedule);
            petService.savePet(pet);
        }

        for(Customer customer: schedule.getCustomers()){
            if(customer.getSchedules() == null) customer.setSchedules(new ArrayList<>());
            customer.getSchedules().add(schedule);
            customerService.save(customer);
        }
        System.out.println("newSchedule"+newSchedule);


        return convertToScheduleDTO(newSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        List<Schedule> schedules = scheduleService.findAllSchedules();
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {


        Pet pet= petService.getPetById(petId);
        List<Schedule> schedules = pet.getSchedules();
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
            List<Schedule> schedules = employee.getSchedules();

            return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer= customerService.getCustomerById(customerId);
        List<Schedule> schedules = customer.getSchedules();
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    public List<Employee> getEmployeesByIds(List<Long> empIds){
        List<Employee> employees = new ArrayList<>();
        for(Long empid: empIds){
            employees.add(employeeService.findById(empid));
        }
        return employees;
    }

    public List<Pet> getPetsByIds(List<Long> petIds){
        List<Pet> pets = new ArrayList<>();
        for(Long petid: petIds){
            pets.add(petService.getPetById(petid));
        }
        return pets;
    }

    public ScheduleDTO convertToScheduleDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setPetIds(convertToPetIds(schedule.getPets()));
        scheduleDTO.setEmployeeIds(convertToEmployeeIds(schedule.getEmployees()));
        return scheduleDTO;
    }
    public List<Long> convertToEmployeeIds(List<Employee> employees){
        List<Long> employeesId = new ArrayList<>();
        for(Employee employee:employees){
            employeesId.add(employee.getId());
        }
        return employeesId;
    }
    public List<Long> convertToPetIds(List<Pet> pets) {
        List<Long> petsId = new ArrayList<>();
        for (Pet pet : pets) {
            petsId.add(pet.getId());
        }
        return petsId;
    }
}
