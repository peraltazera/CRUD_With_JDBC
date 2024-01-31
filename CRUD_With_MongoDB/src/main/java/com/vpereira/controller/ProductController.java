package com.vpereira.controller;

import com.vpereira.controller.generic.GenericController;
import com.vpereira.core.domain.Product;
import com.vpereira.repository.ProductRepository;
import com.vpereira.service.ProductService;
import jakarta.ws.rs.Path;

@Path("product")
public class ProductController extends GenericController<Product, String> {
    public ProductController() {
        super(new ProductService(new ProductRepository()));
    }
}
