package ru.batorov.library.dto.person;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonRegistrationDTO implements PersonDTOInterface {
    @NotEmpty(message = "username shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String username;

    @NotEmpty(message = "password shouldn't be empty")
    @Size(min = 2, max = 10, message = "password between 2 and 10 field")
    private String password;

    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String fullName;

    @Min(value = 0, message = "yearOfBirth > 0")
    private Integer yearOfBirth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

}
