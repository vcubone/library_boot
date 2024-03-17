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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
