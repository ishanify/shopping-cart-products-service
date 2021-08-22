package com.example.shopping;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.shopping.product.Exception.ProductNotFoundException;
import com.example.shopping.product.Exception.ProductQuantityLowException;
import com.example.shopping.product.Model.Product;
import com.example.shopping.product.Model.UpdateProductRequest;
import com.example.shopping.product.Repository.ProductRepository;
import com.example.shopping.product.Service.ProductService;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

	ProductRepository productRepository=mock(ProductRepository.class);
	
	ProductService productService = new ProductService(productRepository);
; 
	
	Product testProduct;
	
	@Before
	public void setUp() {
		testProduct = Product.builder()
				.id(2)
				.name("Tie")
				.price(BigDecimal.valueOf(25))
				.stock(5)
				.createdDate(LocalDate.now())
				.build();
		productRepository.save(testProduct);
	}
	
	@Test(expected = ProductNotFoundException.class)
	public void getProduct_throws_ProductNotFoundException() {

		//when(productRepository.findById(3)).thenThrow(ProductNotFoundException.class);
		productService.getSingleProduct(3);
	}
	

	@Test
	public void testGetSingleProduct() {

		when(productRepository.findById(2)).thenReturn(Optional.of(testProduct));
		Product actualResponse = productService.getSingleProduct(2);
		
		assertEquals(actualResponse,testProduct);
	}
	
	@Test
	public void testCreateProduct()
	{

		when(productRepository.save(any())).thenReturn(testProduct);
		Product createdProduct = productService.createProducts(new Product());
		
		assertEquals(testProduct, createdProduct);
		
	}

	@Test
	public void testEditProduct() {
		
		Product updatedExpectedProduct = new Product(testProduct.getId(),testProduct.getName(), testProduct.getStock()-1, testProduct.getPrice(), testProduct.getCreatedDate());
		
		when(productRepository.save(any())).thenReturn(updatedExpectedProduct);
		when(productRepository.findById(2)).thenReturn(Optional.of(testProduct));
//		UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
//				.stockChange(1)
//				.build();
		UpdateProductRequest updateProductRequest = new UpdateProductRequest();
		updateProductRequest.setStockChange(1);
		Product updatedActualProduct = productService.editProduct(2, updateProductRequest);
		assertEquals(updatedActualProduct, updatedExpectedProduct);
	}

	@Test (expected = ProductQuantityLowException.class)
	public void testEditProduct_throws_ProductQuantityLowException() {
		
		when(productRepository.findById(2)).thenReturn(Optional.of(testProduct));
		UpdateProductRequest updateProductRequest = new UpdateProductRequest();
		updateProductRequest.setStockChange(10);
		productService.editProduct(2, updateProductRequest);
	}
}
