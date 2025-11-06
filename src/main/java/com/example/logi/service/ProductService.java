package com.example.logi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.logi.domain.product.Product;
import com.example.logi.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

 // 1) 전체 목록
    public List<Product> findAll() {
        return repo.findAll();
    }

    // 2) 페이징용
    public Page<Product> getPage(int page, int size) {
        // page는 0부터 시작하니까 -1 해줌 (화면에서는 1부터 보여줄 거라서)
        Pageable pageable = PageRequest.of(
                page, 
                size, 
                Sort.by(Sort.Direction.DESC, "id")   // id 역순
        );
        return repo.findAll(pageable);
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