package com.example.logi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.logi.domain.order.Order;
import com.example.logi.domain.order.OrderStatus;
import com.example.logi.domain.product.Product;
import com.example.logi.repository.OrderRepository;
import com.example.logi.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    public OrderService(OrderRepository orderRepo, ProductRepository productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    public List<Order> findAll() {
        return orderRepo.findAllByOrderByCreatedAtDesc();
    }
    
    // 상태별 조회용 메서드
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepo.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public Page<Order> getPage(String statusCode, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page - 1, 0), 
                size, 
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // statusCode: null 또는 "" 또는 "ALL" 이면 전체
        if (statusCode == null || statusCode.isBlank() || "ALL".equals(statusCode)) {
            return orderRepo.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            OrderStatus status = OrderStatus.valueOf(statusCode);
            return orderRepo.findByStatusOrderByCreatedAtDesc(status, pageable);
        }
    }
    
    /** 현재 주문 수
     *  = 접수완료(RECEIVED) + 출고대기(READY_TO_SHIP) + 출고완료(SHIPPED)
     *    (아직 재고갱신이 안 된 주문들)
     */
    public long countCurrentOrders() {
        return orderRepo.countByStatusIn(
                List.of(
                        OrderStatus.RECEIVED,
                        OrderStatus.READY_TO_SHIP,
                        OrderStatus.SHIPPED
                )
        );
    }

    /** 현재 출하 수
     *  = 재고갱신까지 끝난 주문(STOCK_UPDATED)의 개수
     */
    public long countShippedAndStockUpdated() {
        return orderRepo.countByStatus(OrderStatus.STOCK_UPDATED);
    }

    /**
     6번 페이지에서 SKU + 수량으로 주문 접수
     */
    @Transactional
    public void createOrder(String sku, int qty) {
        // 1) 수량 검증
        if (qty <= 0) {
            throw new IllegalArgumentException("数量は1以上を入力してください。");
        }

        // 2) SKU로 상품 조회
        Product product = productRepo.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException(
                        "該当するSKUの商品が存在しません。"
                ));

        // 3) 재고 부족 체크
        if (product.getQtyOnHand() < qty) {
            throw new IllegalArgumentException(
                    "在庫が不足しています。現在の在庫数：" + product.getQtyOnHand()
            );
        }

        // 4) ✅ 여기서 재고 차감
        product.setQtyOnHand(product.getQtyOnHand() - qty);

        // 5) 주문 생성 (상태 = RECEIVED)
        Order order = new Order();
        order.setProduct(product);
        order.setQty(qty);
        order.setStatus(OrderStatus.RECEIVED);

        orderRepo.save(order);
        // @Transactional + dirty checking 으로 product도 같이 update됨
    }
    
    @Transactional
    public void advanceStatus(Long id) {
        Order order = orderRepo.findById(id).orElseThrow();

        switch (order.getStatus()) {
            case RECEIVED -> {
                // 接収済 → 出荷待ち
                order.setStatus(OrderStatus.READY_TO_SHIP);
            }
            case READY_TO_SHIP -> {
                // 出荷待ち → 出荷完了
                order.setStatus(OrderStatus.SHIPPED);
            }
            case SHIPPED -> {
                // 出荷完了 → 注文件数更新完了 (재고는 이미 접수할 때 차감됨)
                order.setStatus(OrderStatus.STOCK_UPDATED);
            }
            case STOCK_UPDATED -> {
                // 이미 최종 상태 → 아무 것도 안 함
            }
        }
    }
    
}