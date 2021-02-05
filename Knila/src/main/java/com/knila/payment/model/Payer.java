package com.knila.payment.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bill_file_details")
@Entity
public class Payer implements Serializable {

	private static final long serialVersionUID = -4705134652216226949L;

	@Column(name = "pk_payer_id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int pkPayerId;

	@Column(name = "payer_name")
	private String payerName;

	@Column(name = "payer_address")
	private String address;

	@Column(name = "due_date")
	private String dueDate;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "amount")
	private String amount;

	public int getPkPayerId() {
		return pkPayerId;
	}

	public void setPkPayerId(int pkPayerId) {
		this.pkPayerId = pkPayerId;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
