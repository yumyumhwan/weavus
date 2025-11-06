package com.example.logi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.logi.domain.order.Order;
import com.example.logi.domain.order.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주문 생성일 기준 내림차순 (최신 주문이 위로)
    List<Order> findAllByOrderByCreatedAtDesc();
    
    // 상태별로 정렬해서 조회
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    // ✅ 현재 미완료 주문수(접수완료 / 출고대기 / 출고완료)
    long countByStatusIn(List<OrderStatus> statuses);

    // ✅ 특정 상태 개수 (재고갱신완료용)
    long countByStatus(OrderStatus status);
    
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);
}