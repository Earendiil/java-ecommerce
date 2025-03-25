package com.application.ecommerece.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.application.ecommerece.entity.Address;
import com.application.ecommerece.entity.User;
import com.application.ecommerece.exceptions.ResourceNotFoundException;
import com.application.ecommerece.payload.AddressDTO;
import com.application.ecommerece.repositories.AddressRepository;
import com.application.ecommerece.repositories.UserRepository;
import com.application.ecommerece.util.AuthUtil;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    

    @Autowired
    private AuthUtil authUtil;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        List<Address> addressesList = user.getAddresses();
        addressesList.add(address);
        user.setAddresses(addressesList);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

	@Override
	public List<AddressDTO> getAdresses() {
		List<Address> addresses = addressRepository.findAll();
		List <AddressDTO> addressDTOs =  addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
				.collect(Collectors.toList());
		return addressDTOs;
	}

	@Override
	public AddressDTO findByAddressId(Long addressId) {
		Address address = addressRepository.findById(addressId)
		.orElseThrow(() -> new ResourceNotFoundException("address", "addres id" ,addressId));
		AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
		return addressDTO;
	}

	@Override
	public List<AddressDTO> getUserAddresses(User user) {
		
		List<Address> addresses = user.getAddresses();
		
		List<AddressDTO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
				.collect(Collectors.toList());
		
		return addressDTOs;
	}

	@Override
	public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "address id", addressId));
		
		address.setBuildingName(addressDTO.getBuildingName());
		address.setCity(addressDTO.getCity());
		address.setCountry(addressDTO.getCountry());
		address.setPostalcode(addressDTO.getPostalcode());
		address.setState(addressDTO.getState());
		address.setStreet(addressDTO.getStreet());
		addressRepository.save(address);
		
		AddressDTO updatedAddress = new AddressDTO();
		updatedAddress = modelMapper.map(address, AddressDTO.class);
		return updatedAddress;
	}

	public void  deleteAddressById(Long addressId) {
		addressRepository.findById(addressId)
		.orElseThrow(() -> new ResourceNotFoundException("Address", "address id", addressId));
		addressRepository.deleteById(addressId);
	}
	
	
	
	
	
	
	
	
	
}
