package ru.batorov.library.models;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Book {
    @Id
    @Column(name = "bookId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    
    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "personId")
    private Person owner;
    
    @Column(name = "title")
    @NotEmpty(message = "title shouldn't be empty")
    @Size(min = 2, max = 30, message = "title between 2 and 30")
    private String title;
    
    @Column(name = "author")
    @NotEmpty(message = "author shouldn't be empty")
    @Size(min = 2, max = 30, message = "author between 2 and 30")
    private String author;
    
    @Column(name = "releaseYear")
    @Min(value = 0, message = "release_year > 0")
    private int releaseYear;
    
    @Column(name = "takeTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takeTime;

    @Transient
    private boolean expired;

    public Book() {
    }

    public Book(
            @NotEmpty(message = "title shouldn't be empty") @Size(min = 2, max = 30, message = "title between 2 and 30") String title,
            @NotEmpty(message = "author shouldn't be empty") @Size(min = 2, max = 30, message = "author between 2 and 30") String author,
            @Min(value = 0, message = "release_year > 0") int releaseYear) {
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Date getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Date takeTime) {
        this.takeTime = takeTime;
    }
    
    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
