package com.example.logi.service;

import com.example.logi.domain.product.Product;
import com.example.logi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Product create(Product p) {
        return repo.save(p);
    }

    public Product findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public Product update(Long id, Product req) {
        Product p = repo.findById(id).orElseThrow();
        p.setSku(req.getSku());
        p.setName(req.getName());
        p.setQtyOnHand(req.getQtyOnHand());
        p.setDescription(req.getDescription());
        return p; // dirty checking
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public boolean existsSku(String sku) {
        return repo.existsBySku(sku);
    }
}