package com.example.logi.domain.product;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_sku", columnList = "sku", unique = true)
})
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SKU를 입력하세요")
    @Size(max = 50, message = "SKU는 50자 이내")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @NotBlank(message = "상품명을 입력하세요")
    @Size(max = 100, message = "상품명은 100자 이내")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "수량을 입력하세요")
    @Min(value = 0, message = "수량은 0 이상")
    @Column(name = "qty_on_hand", nullable = false)
    private Integer qtyOnHand;

    @Size(max = 500, message = "설명은 500자 이내")
    @Column(length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}