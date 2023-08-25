package ru.batorov.library.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "jwtblacklist")
public class JwtBlackList implements Serializable{
	@Id
	@Column(name = "name")
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// @Column(name = "created_at")
	// private LocalDateTime createdAt;
	
	
}
