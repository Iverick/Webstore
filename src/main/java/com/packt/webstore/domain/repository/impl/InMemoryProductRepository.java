package com.packt.webstore.domain.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.packt.webstore.domain.Product;
import com.packt.webstore.domain.repository.ProductRepository;
import com.packt.webstore.exception.ProductNotFoundException;

@Repository
public class InMemoryProductRepository implements ProductRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	
	public List<Product> getAllProducts() {
		String SQL = "SELECT * FROM products";
		Map<String, Object> params = new HashMap<String, Object>();
		List<Product> result = jdbcTemplate.query(SQL, params, new ProductMapper());
		return result;
	}
	
	
	private static final class ProductMapper implements RowMapper<Product> {
		
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			// This method maps database entries into Product class instances
			Product product = new Product();
			product.setProductId(rs.getString("ID"));
			product.setName(rs.getString("NAME"));
			product.setDescription(rs.getString("DESCRIPTION"));
			product.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
			product.setManufacturer(rs.getString("MANUFACTURER"));
			product.setCategory(rs.getString("CATEGORY"));
			product.setCondition(rs.getString("CONDITION"));
			product.setUnitsInStock(rs.getLong("UNITS_IN_STOCK"));
            product.setUnitsInOrder(rs.getLong("UNITS_IN_ORDER"));
            product.setDiscontinued(rs.getBoolean("DISCONTINUED"));
			return product;
		}
		
	}

	
	public void updateStock(String productId, long noOfUnits) {
		String SQL = "UPDATE PRODUCTS SET UNITS_IN_STOCK = :unitsInStock WHERE ID = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("unitsInStock", noOfUnits);
		params.put("id", productId);
		
		jdbcTemplate.update(SQL, params);
		
	}


	public List<Product> getProductsByCategory(String category) {
		String SQL = "SELECT * FROM PRODUCTS WHERE CATEGORY = :category";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", category);
		
		List<Product> result = jdbcTemplate.query(SQL, params, new ProductMapper());
		return result;
	}


	public List<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
		String SQL = "SELECT * FROM PRODUCTS WHERE CATEGORY IN (:categories) AND MANUFACTURER"
				+ " IN (:brands)";
		List<Product> result = jdbcTemplate.query(SQL, filterParams, new ProductMapper());
		return result;
	}


	public Product getProductById(String productId) {
		String SQL = "SELECT * FROM PRODUCTS WHERE ID = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", productId);
		
		try {
			return jdbcTemplate.queryForObject(SQL, params, new ProductMapper());
		} catch (DataAccessException e) {
			throw new ProductNotFoundException(productId);
		}
	}


	public void addProduct(Product product) {
		String SQL = "INSERT INTO PRODUCTS (ID, "
				+ "NAME,"
				+ "DESCRIPTION,"
				+ "UNIT_PRICE,"
				+ "MANUFACTURER,"
				+ "CATEGORY,"
				+ "CONDITION,"
				+ "UNITS_IN_STOCK,"
				+ "UNITS_IN_ORDER,"
				+ "DISCONTINUED)"
				+ "VALUES (:id, :name, :desc, :price, :manufacturer,"
				+ ":category, :condition, :inStock, :inOrder, :discontinued)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", product.getProductId());
		params.put("name", product.getName());
		params.put("desc", product.getDescription());
		params.put("price", product.getUnitPrice());
		params.put("manufacturer", product.getManufacturer());
		params.put("category", product.getCategory());
		params.put("condition", product.getCondition());
		params.put("inStock", product.getUnitsInStock());
		params.put("inOrder", product.getUnitsInOrder());
		params.put("discontinued", product.isDiscontinued());
		
		jdbcTemplate.update(SQL, params);
	}
}
