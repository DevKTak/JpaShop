package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 외래키 이름 설정
    private Item item; // 주문 상품

    @ManyToOne(fetch = FetchType.LAZY) // N(OrderItem) : 1(Order) 관계 명시
    @JoinColumn(name = "order_id") // 매핑을 뭘로 할지, FK(order_id) 이름 설정
    private Order order; // 주문

    private int orderPrice; // 주문가격
    private int count; // 주문 수량

    //== 생성 메서드 ==//
    public static OrderItem CreateOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 재고 수량 줄이기
        return orderItem;
    }

    //== 비즈니스 로직 ==//
    public void cancel() { // 재고 수량 원복
        getItem().addStock(count); // item.addStock(count)로 해도 될듯
    }

    //== 조회 로직 ==//

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
