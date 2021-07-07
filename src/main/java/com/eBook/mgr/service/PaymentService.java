package com.eBook.mgr.service;

import java.util.List;

import com.eBook.mgr.dto.PaymentDto;

public interface PaymentService {
	public List<PaymentDto> listPayment(String setDate) throws Exception;
	
	public void insertPayment(PaymentDto dto) throws Exception;
	
	public String[] writerIdList() throws Exception;
	
	public void removeList(String setDate) throws Exception;
	
	public void updateCarryAmount(String carryAmount, String writerId) throws Exception;
	
	public void updateVirtuousTax(String writerId) throws Exception;
	
	// 합계로직
	public PaymentDto allPayment(String writerId, String setDate) throws Exception;
}
