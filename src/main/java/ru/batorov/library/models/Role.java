package ru.batorov.library.models;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	@NotEmpty(message = "name shouldn't be empty")
	private String name;

	@ManyToMany(mappedBy = "roles")
	private Collection<Person> persons;

	public Role() {
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Collection<Person> getPersons() {
		return persons;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersons(Collection<Person> persons) {
		this.persons = persons;
	}
}
