package com.example.logi.repository;

import com.example.logi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);          // 이미 있을 수 있음 (중복체크용)

    Optional<Product> findBySku(String sku);  // 주문 접수용 (SKU로 상품 찾기)
}