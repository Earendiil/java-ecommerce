package com.application.ecommerce.service;

import java.util.List;

import com.application.ecommerce.entity.User;
import com.application.ecommerce.payload.AddressDTO;

public interface AddressService {

	AddressDTO createAddress(AddressDTO addressDTO, User user);

	List<AddressDTO> getAdresses();

	AddressDTO findByAddressId(Long addressId);


	public List<AddressDTO> getUserAddresses(User user);

	AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO);

	public void deleteAddressById(Long addressId);

}
