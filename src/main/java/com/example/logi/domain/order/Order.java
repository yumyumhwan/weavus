package com.example.logi.domain.order;

import com.example.logi.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Order {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                         // 화면의 '주문번호'로 노출

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;                 // 단일 상품 주문 (MVP 단순화)

    @Min(1)
    @Column(nullable = false)
    private Integer qty;                     // 주문 수량

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.RECEIVED;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}