package com.example.shopping.product.Service;
import com.example.shopping.product.Exception.ProductNotFoundException;
import com.example.shopping.product.Exception.ProductQuantityLowException;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.product.Model.Product;
import com.example.shopping.product.Model.UpdateProductRequest;
import com.example.shopping.product.Repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductService{

	@Autowired
	ProductRepository productRepository;
	


	public List<Product> getAllProducts() {
		return (List<Product> )productRepository.findAll();
	}
	
	public Product createProducts(Product request) {
		
		Product product = Product.builder()
						.name(request.getName())
						.price(request.getPrice())
						.stock(request.getStock())
						.createdDate(LocalDate.now())
						.build();
		
		return (Product) productRepository.save(product);
	}
	public Product editProduct(Integer id, UpdateProductRequest updatedProduct) {
		Product existingProduct = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
		
		Integer stock = existingProduct.getStock();

		if(stock - updatedProduct.getStockChange()<= 0)
			throw new ProductQuantityLowException();
		
		existingProduct.setStock(stock-updatedProduct.getStockChange());
		
		return productRepository.save(existingProduct);
	}

	public Product getSingleProduct(Integer id) {
		return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

		
	}
	

}

