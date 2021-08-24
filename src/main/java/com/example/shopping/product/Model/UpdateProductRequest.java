package com.example.shopping.product.Model;


public class UpdateProductRequest {

	// stockChange refers to number of products sold/ has to be deducted from inventory
	private Integer stockChange;

	public Integer getStockChange() {
		return stockChange;
	}

	public void setStockChange(Integer stockChange) {
		this.stockChange = stockChange;
	}
}
