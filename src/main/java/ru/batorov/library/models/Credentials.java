package ru.batorov.library.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "credentials")
public class Credentials {
	@Id
	@Column(name = "personId")
	private Integer personId;
	
    @OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "personId", referencedColumnName = "personId")
    private Person person;
    
    @Column(name = "username")
    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String username;
    
    @Column(name = "password")
    @NotEmpty(message = "password shouldn't be empty")
    @Size(min = 2, max = 70, message = "password between 2 and 70 field")
    private String password;
	
	@Column(name = "role")
	private String role;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
