package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    public Customer getCustomerById(Long id){
        return customerRepository.getOne(id);
    }

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer createCustomer(Customer customer, List<Long>petids){
        List<Pet> pets = new ArrayList<>();
        if(petids!=null && !petids.isEmpty()){
            for(Long id: petids){
                pets.add(petRepository.getOne(id));
            }
        }

        customer.setPets(pets);
        Customer c= customerRepository.save(customer);
        return c;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

}
