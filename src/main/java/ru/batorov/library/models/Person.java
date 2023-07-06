package ru.batorov.library.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Person {
    @Id
    @Column(name = "personId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    
    @Column(name = "fullName")
    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String fullName;
    
    @Column(name = "age")
    @Min(value = 0, message = "Age > 0")
    private int age;
    
    @OneToMany(mappedBy = "owner")
    private List<Book> books;
    
    public Person() {
    }
    public Person(
            @NotEmpty(message = "name shouldn't be empty") @Size(min = 2, max = 30, message = "Name between 2 and 30") String fullName,
            @Min(value = 0, message = "Age > 0") int age) {
        this.fullName = fullName;
        this.age = age;
    }
    public int getPersonId() {
        return personId;
    }
    public void setPersonId(int personId) {
        this.personId = personId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
