
package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
	  @Autowired
	    private ProductRepository productRepository;

	    @Override
	    public List<Product> getAllProducts() {
	        return productRepository.findAll();
	    }
}