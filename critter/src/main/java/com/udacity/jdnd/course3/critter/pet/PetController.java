package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        Customer customer = new Customer();

        // Fetch or set the customer
        if (petDTO.getOwnerId() == 0) {
            pet.setCustomer(customerService.getCustomerById(1L)); // Ensure customer with ID 2 exists
        } else {
            customer = customerService.getCustomerById(petDTO.getOwnerId());
            pet.setCustomer(customer);
        }

        // Check if customer exists
        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + petDTO.getOwnerId());
        }

        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());

        // Save the pet
        Pet newPet = petService.savePet(pet);

        // Add the pet to the customer's list of pets
        if (customer.getPets() == null) {
            customer.setPets(new ArrayList<>());
        }
        customer.getPets().add(newPet);

        // Convert and return the saved pet as DTO
        return convertToPetTDO(pet);
    }



        @GetMapping("/{petId}")
        public PetDTO getPet(@PathVariable long petId) {
            Pet pet = petService.getPetById(petId);
            return convertToPetTDO(pet);

        }

        @GetMapping
        public List<PetDTO> getPets() {
            List<Pet> pets = petService.getAllPets();
            return pets.stream().map(this::convertToPetTDO).collect(Collectors.toList());
        }

        @GetMapping("/owner/{ownerId}")
        public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
            List<Pet> pets = petService.getPetByOwner(ownerId);
            List<PetDTO> petDTOS= new ArrayList<>();
            pets.forEach(pet -> petDTOS.add(convertToPetTDO(pet)));
            return petDTOS;

        }

    private PetDTO convertToPetTDO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setName(pet.getName());
        petDTO.setType(pet.getType());

        if(pet.getCustomer() == null){
            petDTO.setOwnerId(0);
        }else{
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }
}
