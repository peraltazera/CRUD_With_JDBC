package com.vpereira.service;

import com.vpereira.core.domain.Product;
import com.vpereira.repository.ProductRepository;
import com.vpereira.service.generic.GenericService;

public class ProductService extends GenericService<Product, Long> implements IProductService{

    public ProductService(ProductRepository productRepository) {
        super(productRepository);
    }
}
