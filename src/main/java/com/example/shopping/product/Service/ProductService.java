package com.example.shopping.product.Service;

import com.example.shopping.product.Exception.ProductNotFoundException;
import com.example.shopping.product.Exception.ProductQuantityLowException;

import java.time.LocalDate;
import java.util.HashMap;
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

	public Product getSingleProduct(Integer id) {
		return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

	}

	// returns non duplicate list of only available products with valid quantities.
	public List<Product> validateProduct(List<ProductQuantity> productQuantityRequest) {

		// already takes care of duplicate product in request
		HashMap<Integer, Integer> requestMap = productQuantityRequest.stream()
				.collect(Collectors.toMap(pq -> pq.getProductId(), pq -> pq.getQuantity(), (p, q) -> p, HashMap::new));

		Iterable<Product> existingProductList = productRepository.findAllById(requestMap.keySet());
		List<Product> filteredProducts = StreamSupport.stream(existingProductList.spliterator(), false)
				.filter(p -> p.getStock() - requestMap.get(p.getId()) >= 0).collect(Collectors.toList());

		return filteredProducts;
	}

}
