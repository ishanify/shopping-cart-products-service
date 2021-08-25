package com.example.shopping.product.Service;

import com.example.shopping.product.Exception.ProductNotFoundException;
import com.example.shopping.product.Exception.ProductQuantityLowException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.product.Model.Product;
import com.example.shopping.product.Model.ProductQuantity;
import com.example.shopping.product.Model.UpdateProductRequest;
import com.example.shopping.product.Repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return (List<Product>) productRepository.findAll();
	}

	public Product createProducts(Product request) {

		Product product = Product.builder().name(request.getName()).price(request.getPrice()).stock(request.getStock())
				.createdDate(LocalDate.now()).build();

		return (Product) productRepository.save(product);
	}

	public Product editProduct(Integer id, UpdateProductRequest updatedProduct) {
		Product existingProduct = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

		Integer stock = existingProduct.getStock();

		if (stock - updatedProduct.getStockChange() <= 0)
			throw new ProductQuantityLowException();

		existingProduct.setStock(stock - updatedProduct.getStockChange());

		return productRepository.save(existingProduct);
	}
	
	public void editMultipleProducts(List<ProductQuantity> productQuantityRequest) {
		
		
		HashMap<Integer, Integer> requestMap=convertProductQuantityListToMap(productQuantityRequest);
		Iterator<Product> existingProductItr = productRepository.findAllById(requestMap.keySet()).iterator();

		List<Product> updatedProductList = new ArrayList<Product>();
		while( existingProductItr.hasNext())
		{
			Product product=existingProductItr.next();
			Integer stockChange = requestMap.get(product.getId());
			
			if(product.getStock()<stockChange) {
				throw new ProductQuantityLowException();
			}
			Integer updatedStock=product.getStock()-stockChange;
			updatedProductList.add(Product.builder()
					.id(product.getId())
					.name(product.getName())
					.stock(updatedStock)
					.price(product.getPrice())
					.createdDate(product.getCreatedDate())
					.build());
		}
		
		

		 productRepository.saveAll(updatedProductList);
		 
	}
	public Product getSingleProduct(Integer id) {
		return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

	}

	// returns non duplicate list of only available products with valid quantities.
	public List<Product> validateProduct(List<ProductQuantity> productQuantityRequest) {

		HashMap<Integer, Integer> requestMap =convertProductQuantityListToMap(productQuantityRequest);
		
		Iterable<Product> existingProductList = productRepository.findAllById(requestMap.keySet());
		List<Product> filteredProducts = StreamSupport.stream(existingProductList.spliterator(), false)
				.filter(p -> !(p.getStock().compareTo(requestMap.get(p.getId())) < 0)).collect(Collectors.toList());

		return filteredProducts;
	}

	private HashMap<Integer, Integer> convertProductQuantityListToMap(List<ProductQuantity> productQuantityRequest) {
				return  productQuantityRequest.stream()
						.collect(Collectors.toMap(pq -> pq.getProductId(), pq -> pq.getQuantity(), (p, q) -> p, HashMap::new));
	}

}
