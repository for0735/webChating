package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Product {
	
	private String id;
	private String name;
	private double price;
	private int quantity;
	private Date creationDate;
	private boolean status;
	
}
