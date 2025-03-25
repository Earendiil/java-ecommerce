package com.application.ecommerece.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email")})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iser_id")
	private Long userId;
	
	@NotBlank
	@Size(max = 20)
	@Column(name = "username")
	private String userName;
	
	@NotBlank
	@Size(max = 50)
	@Email
	@Column(name = "email")
	private String email;
	
	@NotBlank
	@Size(max = 100)
	@Column(name = "password")
	private String password;
	
	@ToString.Exclude
	@OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private  Cart cart;
	
	 public User(String userName, String email, String password) {
	        this.userName = userName;
	        this.email = email;
	        this.password = password;
	    }


	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@ToString.Exclude
	@OneToMany(mappedBy = "user", 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			orphanRemoval = true)//when a user is deleted his products will as well
	private Set<Product> products;
	
	
	@OneToMany(mappedBy = "user", cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//	@JoinTable(name = "user_address",
//				joinColumns = @JoinColumn(name  = "user_id"),
//				inverseJoinColumns = @JoinColumn(name = "address_id"))
	private List<Address> addresses = new ArrayList<>();
	
	
	
	
}
