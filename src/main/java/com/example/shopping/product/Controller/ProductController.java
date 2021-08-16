package com.example.shopping.product.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping.product.Model.CreateProductRequest;
import com.example.shopping.product.Model.Product;
import com.example.shopping.product.Service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	//get all products from DB
	@GetMapping("/products")
	public List<Product> getAallProducts() {
		
		return productService.getAllProducts();
		
	}
	
	//create products in DB
	@PostMapping("/products")
	public Product createPproduct( @RequestBody CreateProductRequest request){
		 

		return productService.createProducts(request);
		
	}

}
