package com.company.jmix_nghia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@JmixEntity
@Table(name = "STUDENT_ENTITY", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_STUDENT_ENTITY_UNQ", columnNames = {"CCCD"})
})
@Entity
public class StudentEntity {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @Column(name = "STUDENT_CODE")
    private String studentCode;
    @InstanceName
    @Column(name = "NAME")
    private String name;
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    @Column(name = "CCCD")
    private String cccd;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @JoinTable(name = "CLASS_ENTITY_STUDENT_ENTITY_LINK",
            joinColumns = @JoinColumn(name = "STUDENT_ENTITY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "CLASS_ENTITY_ID", referencedColumnName = "ID"))
    @ManyToMany
    private Set<ClassEntity> classes;
    @OneToMany(mappedBy = "student")
    private List<SubjectEntity> subjects;

    public void setSubjects(List<SubjectEntity> subjects) {
        this.subjects = subjects;
    }

    public List<SubjectEntity> getSubjects() {
        return subjects;
    }

    public Set<ClassEntity> getClasses() {
        return classes;
    }

    public void setClasses(Set<ClassEntity> classes) {
        this.classes = classes;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCccd() {
        return cccd;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}