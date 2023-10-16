package ru.batorov.library.models;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "title")
    @NotEmpty(message = "title shouldn't be empty")
    @Size(min = 2, max = 30, message = "title between 2 and 30")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "author shouldn't be empty")
    @Size(min = 2, max = 30, message = "author between 2 and 30")
    private String author;

    @Column(name = "release_year")
    @Min(value = 0, message = "release_year > 0")
    private Integer releaseYear;

    @Column(name = "take_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takeTime;

    @Transient
    private boolean expired;

    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

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
    
    static public Book shallowCopy(Book that){
        Book result = new Book();
        result.setAuthor(that.getAuthor());
        result.setCreated_at(that.getCreated_at());
        result.setExpired(that.isExpired());
        result.setId(that.getId());
        result.setOwner(that.getOwner());
        result.setReleaseYear(that.getReleaseYear());
        result.setTakeTime(that.getTakeTime());
        result.setTitle(that.getTitle());
        result.setUpdated_at(that.getUpdated_at());
        return result;
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

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
