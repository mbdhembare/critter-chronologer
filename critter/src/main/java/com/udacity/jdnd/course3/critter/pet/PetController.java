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
        if(petDTO.getOwnerId()==0) {
            pet.setCustomer(customerService.getCustomerById(2L));
        }else{
            customer = customerService.getCustomerById(petDTO.getOwnerId());
            pet.setCustomer(customer);
        }

        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        Pet newPet = petService.savePet(pet);
        if (customer.getPets() == null) {
            customer.setPets(new ArrayList<>());
        }
        customer.getPets().add(newPet);
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

        public PetDTO convertToPetTDO(Pet pet) {
            PetDTO petDTO = new PetDTO();
            petDTO.setId(pet.getId());
            petDTO.setName(pet.getName());
            petDTO.setType(pet.getType());

            if (pet.getCustomer() != null) {
                petDTO.setOwnerId(pet.getCustomer().getId());
            } else {
                petDTO.setOwnerId(0);
            }

            return petDTO;

        }
}
