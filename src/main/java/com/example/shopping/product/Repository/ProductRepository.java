package com.example.shopping.product.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.shopping.product.Model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{

}
