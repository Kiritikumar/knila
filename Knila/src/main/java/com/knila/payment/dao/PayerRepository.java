package com.knila.payment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knila.payment.model.Payer;

public interface PayerRepository extends JpaRepository<Payer, String> {
	List<Payer> findDistinctPayerByAccountNumberOrPayerNameOrAddress( String accountNumber, String payerName,
			String address);
 
	 List<Payer> findByPkPayerIdIn(List<Long> ids);

}