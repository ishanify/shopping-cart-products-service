package com.example.shopping.product.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product quantity available is too low")

public class ProductQuantityLowException extends RuntimeException {

}
