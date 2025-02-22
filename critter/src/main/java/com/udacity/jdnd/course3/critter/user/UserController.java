package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    PetService petService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer= new Customer();
//        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhoneNumber());
        List<Long> petids = customerDTO.getPetIds();
        return convertToCustomerDTO(customerService.createCustomer(customer, petids));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){

        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer: customers){
            customerDTOS.add(convertToCustomerDTO(customer));
        }

        return customerDTOS;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable Long petId){

        Pet pet = petService.getPetById(petId);

        Customer customer= pet.getCustomer();

        return convertToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setAvailableDays(employeeDTO.getDaysAvailable());
        return convertToEmployeeDTO(employeeService.save(employee));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee= employeeService.findById(employeeId);
        return convertToEmployeeDTO(employee);

    }

    @PutMapping("/employee/{employeeId}")
    public Set<DayOfWeek> setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee= employeeService.findById(employeeId);

        employee.setAvailableDays(daysAvailable);
       Employee newEmp =  employeeService.save(employee);
        System.out.println("newEmp availablity"+newEmp.getAvailableDays());
        return newEmp.getAvailableDays();

    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.getEmployeesBySkillAndDay(
                employeeDTO.getSkills(), employeeDTO.getDate().getDayOfWeek());

        return employees.stream().map(this::convertToEmployeeDTO).collect(Collectors.toList());
    }

//    public Customer convertToCustomer(CustomerDTO customerDTO){
//        Customer customer= new Customer();
//        customer.setId(customerDTO.getId());
//        customer.setName(customerDTO.getName());
//        customer.setPhone(customerDTO.getPhoneNumber());
//
////        List<Long>petids = customerDTO.getPetIds();
//        return customer;
//    }


    public CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhone());
        customerDTO.setId(customer.getId());

        List<Long> petids=new ArrayList<>();
        for(Pet pet: customer.getPets()){
            petids.add(pet.getId());
        }

        customerDTO.setPetIds(petids);
        return customerDTO;
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setDaysAvailable(employee.getAvailableDays());
        employeeDTO.setSkills(employee.getSkills());
        return employeeDTO;
    }

}
