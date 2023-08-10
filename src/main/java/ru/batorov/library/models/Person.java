package ru.batorov.library.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class Person {
    @Id
    @Column(name = "personId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;
    
    @OneToOne(optional = false, mappedBy = "person", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Credentials credentials;
    
    @Column(name = "fullName")
    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String fullName;
    
    @Column(name = "age")
    @Min(value = 0, message = "Age > 0")
    private Integer age;
    
    @OneToMany(mappedBy = "owner")
    private List<Book> books;
    
    @Column(name = "created_at")
	private LocalDateTime created_at;
	@Column(name = "updated_at")
	private LocalDateTime updated_at;
    
    public Person() {
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
    
}
