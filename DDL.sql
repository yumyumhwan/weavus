-- =========================================================
-- 物流運用管理システム (Logistics Operation Management System)
-- Database: logi
-- 작성일: 2025-10-30
-- 설명: Product(상품/재고), Orders(주문/출하) 테이블 생성
-- =========================================================
CREATE DATABASE IF NOT EXISTS logi 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_0900_ai_ci;
USE logi;

-- 🔹 테이블이 이미 존재할 경우 삭제
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;

-- =========================================================
-- 1) 상품 / 재고 테이블
-- =========================================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 상품 고유 ID (PK)
    sku VARCHAR(50) NOT NULL UNIQUE,               -- SKU 코드 (상품 코드)
    name VARCHAR(100) NOT NULL,                    -- 상품명
    qty_on_hand INT NOT NULL DEFAULT 0,            -- 재고 수량
    description VARCHAR(500),                      -- 상품 설명
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP  -- 등록일시
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 2) 주문 / 출하 상태 테이블
-- =========================================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 주문 ID (PK)
    product_id BIGINT NOT NULL,                    -- 상품 ID (FK)
    qty INT NOT NULL,                              -- 주문 수량
    status VARCHAR(30) NOT NULL DEFAULT '受付済',    -- 상태 (접수/출하대기/출하완료)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 주문 생성일시
    CONSTRAINT fk_orders_product
        FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- ✅ 샘플 데이터 (테스트용)
-- =========================================================
INSERT INTO products (sku, name, qty_on_hand, description)
VALUES
('SKU-001', 'USBケーブル', 120, 'Type-C ケーブル 1m'),
('SKU-002', 'Bluetoothマウス', 80, '2.4GHz ワイヤレスマウス'),
('SKU-003', 'ノートパソコンスタンド', 50, '角度調整可能 アルミ製');

INSERT INTO orders (product_id, qty, status)
VALUES
(1, 2, '受付済'),
(2, 1, '出荷待ち'),
(3, 3, '出荷完了');

-- =========================================================
-- 완료 메시지
-- =========================================================
SELECT '✅ DDL 생성 완료: products / orders 테이블 정상 생성됨' AS message;

SELECT * FROM products;
SELECT * FROM orders;