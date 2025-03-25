package com.application.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.ecommerce.entity.User;
import com.application.ecommerce.payload.AddressDTO;
import com.application.ecommerce.service.AddressService;
import com.application.ecommerce.util.AuthUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }
    
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
       
       List <AddressDTO> addresses = addressService.getAdresses();
       
        return new ResponseEntity<List<AddressDTO>>(addresses,HttpStatus.OK);
    }
    
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddress(@PathVariable Long addressId){
		AddressDTO addressDTO = addressService.findByAddressId(addressId);
    	return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    	
    }
    
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
	User user = authUtil.loggedInUser();
    List<AddressDTO> addressDTOs =addressService.getUserAddresses(user);
    	return new ResponseEntity<List<AddressDTO>>(addressDTOs, HttpStatus.OK);
    	
    }
    
    @PutMapping("/addresses/{addressId}")
    private ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
    												@RequestBody AddressDTO addressDTO){
		AddressDTO address = addressService.updateAddressById(addressId, addressDTO);											
    	
    	
    	return new ResponseEntity<AddressDTO>(address, HttpStatus.OK);
    	
    }
    @DeleteMapping("/addresses/{addressId}")
	private ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
		 addressService.deleteAddressById(addressId);
		return new  ResponseEntity<String>("Address deleted!", HttpStatus.OK);
	}
    
    
    
    
    
    
    
    
}
