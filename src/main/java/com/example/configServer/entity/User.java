package com.example.configServer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter 
@Setter
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name="user")
public class User {

	@Id
	@Column(name = "user_id")
	private String userId;

//	@Id
//	@Column(name = "user_number")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long userNumber;

	@Column
	private String email;

	@Column 
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "mobile_number")
	private String mobileNumber;

	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}

}
