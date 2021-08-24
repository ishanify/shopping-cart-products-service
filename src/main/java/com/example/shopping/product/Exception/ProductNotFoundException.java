package com.example.shopping.product.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product Not Found")
public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException() {
		super("VALIDATION_ERROR");
	}
}
