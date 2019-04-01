package com.packt.webstore.exception;

public class ProductNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -694354952032299587L;
	private String productId;
	
	/*
	 * Constructor class for the exception.
	 * Args:
	 * @productId - ID for the product that has raised an exception.
	 */
	public ProductNotFoundException(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}
}
