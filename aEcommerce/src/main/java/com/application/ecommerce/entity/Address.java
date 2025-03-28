package com.application.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	
	@NotBlank
	@Size(min = 5, message = "Street name must be at least 5 characters long")
	private String street;
	
	@NotBlank
	@Size(min = 5, message = "Building name must be at least 5 characters long")
	private String buildingName;
	
	@NotBlank
	@Size(min = 4, message = "City name must be at least 4 characters long")
	private String city;
	
	@NotBlank
	@Size(min = 2, message = "State name must be at least 2 characters long")
	private String state;
	
	@NotBlank
	@Size(min = 4, message = "Country name must be at least 4 characters long")
	private String country;
	
	@NotBlank
	@Size(min = 5, max = 10 , message = "Postalcode must be between 5-10 digits")
	private String postalcode;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;


	public Address(@NotBlank @Size(min = 5, message = "Street name must be at least 5 characters long") String street,
			@NotBlank @Size(min = 5, message = "Building name must be at least 5 characters long") String buildingName,
			@NotBlank @Size(min = 4, message = "City name must be at least 4 characters long") String city,
			@NotBlank @Size(min = 2, message = "State name must be at least 2 characters long") String state,
			@NotBlank @Size(min = 4, message = "Country name must be at least 4 characters long") String country,
			@NotBlank @Size(min = 5, max = 10, message = "Postalcode must be between 5-10 digits") String postalcode) {
		super();
		this.street = street;
		this.buildingName = buildingName;
		this.city = city;
		this.state = state;
		this.country = country;
		this.postalcode = postalcode;
	}


	
	




	
	
	
	
	
}
