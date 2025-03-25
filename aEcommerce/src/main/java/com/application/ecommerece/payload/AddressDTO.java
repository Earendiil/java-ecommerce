package com.application.ecommerece.payload;

import java.util.ArrayList;
import java.util.List;

import com.application.ecommerece.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
	
	private Long addressId;
	private String street;
	private String buildingName;
	private String city;
	private String state;
	private String country;
	private String postalcode;
	
	private List<User> users = new ArrayList<>();

}
