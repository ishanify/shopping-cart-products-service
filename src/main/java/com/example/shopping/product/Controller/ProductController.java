package com.example.shopping.product.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping.product.Model.Product;
import com.example.shopping.product.Model.ProductQuantity;
import com.example.shopping.product.Model.UpdateProductRequest;
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
	
	//Get single product by product ID
	@GetMapping("/product/{id}")
	public Product getSinglePproduct(@PathVariable int id) {
		
		return productService.getSingleProduct(id);
		
	}
	
	//create products in DB
	@PostMapping("/products")
	public Product createPproduct( @RequestBody Product request){
		 

		return productService.createProducts(request);
		
	}
	
	@PatchMapping("/product/sold/{id}") // todo: create another patch for adding product to inventory
	public Product editPproduct(@PathVariable int id, @RequestBody UpdateProductRequest updatedProduct) {
		return productService.editProduct(id, updatedProduct);
	}
	
	//validate if requested product and quantities are available in DB, returns product details
	//can be extended to restrict the fields to expose - maybe when images or more metadat is added.
	@PostMapping("/product/validate")
	public List<Product> validatePproduct(@RequestBody List<ProductQuantity> productQuantityRequest) {
		return productService.validateProduct(productQuantityRequest);
	}
	
	@PatchMapping("/product/sold") // todo: create another patch for adding product to inventory
	public void editMultiplePproducts(@RequestBody List<ProductQuantity> productQuantityRequest) {
		productService.editMultipleProducts(productQuantityRequest);
	}

}
