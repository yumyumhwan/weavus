package com.example.logi.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    // 상태(일본어), ボタンのラベル(日本語), 최종 상태 여부
    RECEIVED("受付済",       "出荷準備",  false),  // 접수완료
    READY_TO_SHIP("出荷待ち", "出荷処理",  false),  // 출고대기
    SHIPPED("出荷完了",      "注文件数更新",  false),  // 출고완료
    STOCK_UPDATED("注文件数更新完了", null,  true);   // 재고갱신완료 (버튼 없음)

    private final String labelJa;      // 화면의 상태 표시(日本語)
    private final String actionLabel;  // 商品処理 버튼의 텍스트
    private final boolean finalStatus; // true면 버튼 표시 X
}