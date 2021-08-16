package com.example.shopping.product.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.product.Model.CreateProductRequest;
import com.example.shopping.product.Model.Product;
import com.example.shopping.product.Repository.ProductRepository;

@Service
public class ProductService{

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> getAllProducts() {
		return (List<Product> )productRepository.findAll();
	}
	
	public Product createProducts(CreateProductRequest request) {
		
		Product product = new Product(request.getName(),request.getPrice(),request.getStock(),LocalDate.now());
		
		return (Product) productRepository.save(product);
	}
	

}

