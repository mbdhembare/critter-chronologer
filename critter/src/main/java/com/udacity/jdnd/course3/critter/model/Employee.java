package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Nationalized;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Nationalized
    @Column(length = 20)
    private String name;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    @Cascade(CascadeType.ALL)
    private Set<EmployeeSkill> skills;

    @Column
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @CollectionTable(name="employee_availableDays", joinColumns = @JoinColumn(name = "employee_id"))
    private Set<DayOfWeek> availableDays;

    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules;

    public Employee() {
    }

    public Employee(Long id, String name, Set<EmployeeSkill> skills, Set<DayOfWeek> availableDays, List<Schedule> schedules) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.availableDays = availableDays;
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

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Set<DayOfWeek> availableDays) {
        this.availableDays = availableDays;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
