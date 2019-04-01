package com.packt.webstore.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packt.webstore.domain.Product;
import com.packt.webstore.domain.repository.ProductRepository;
import com.packt.webstore.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public void updateAllStock() {
		List<Product> allProducts = productRepository.getAllProducts();
		
		for(Product product : allProducts) {
			if(product.getUnitsInStock() < 500) {
				productRepository.updateStock(
						product.getProductId(),
						product.getUnitsInStock() + 1000);
			}
		}
	}

	public List<Product> getAllProducts() {
		List<Product> allProducts = productRepository.getAllProducts();
		return allProducts;
	}

	public List<Product> getProductsByCategory(String category) {
		List<Product> selectedProductsByCategory = productRepository.getProductsByCategory(category);
		return selectedProductsByCategory;
	}

	public List<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
		List<Product> selectedProductsByFilter = productRepository.getProductsByFilter(filterParams);
		return selectedProductsByFilter;
	}

	public Product getProductById(String productId) {
		Product selectedProduct = productRepository.getProductById(productId);
		return selectedProduct;
	}

	public void addProduct(Product product) {
		productRepository.addProduct(product);
	}

}
