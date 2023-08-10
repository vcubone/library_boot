package ru.batorov.library.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonUserDTO {
	private Integer personId;
	
    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String fullName;
    
    @Min(value = 0, message = "Age > 0")
    private Integer age;

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

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
}
