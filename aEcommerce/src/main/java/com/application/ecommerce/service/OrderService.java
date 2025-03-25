package com.application.ecommerce.service;

import com.application.ecommerce.payload.OrderDTO;

import jakarta.transaction.Transactional;

public interface OrderService {
	@Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
