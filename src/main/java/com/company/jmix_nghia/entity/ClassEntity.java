package com.company.jmix_nghia.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@JmixEntity
@Table(name = "CLASS_ENTITY", indexes = {
        @Index(name = "IDX_CLASS_ENTITY_TEACHER", columnList = "TEACHER_ID"),
        @Index(name = "IDX_CLASS_ENTITY_SCHOOL", columnList = "SCHOOL_ID")
})
@Entity
public class ClassEntity {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @JoinColumn(name = "TEACHER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TeacherEntity teacher;
    @Column(name = "CLASS_CODE")
    private String classCode;
    @InstanceName
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "SCHOOL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolEntity school;

    @JoinTable(name = "CLASS_ENTITY_STUDENT_ENTITY_LINK",
            joinColumns = @JoinColumn(name = "CLASS_ENTITY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "STUDENT_ENTITY_ID", referencedColumnName = "ID"))
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<StudentEntity> students;

    public void setStudents(Set<StudentEntity> students) {
        this.students = students;
    }

    public Set<StudentEntity> getStudents() {
        return students;
    }

    public SchoolEntity getSchool() {
        return school;
    }

    public void setSchool(SchoolEntity school) {
        this.school = school;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}