package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.pet.PetType;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.List;


@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(length = 20)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private PetType type;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private Customer customer;

    @ManyToMany(mappedBy = "pets")
    private List<Schedule> schedules;

    public Pet() {
    }

    public Pet(Long id, String name, PetType type, Customer customer, List<Schedule> schedules) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.customer = customer;
        this.schedules = schedules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
