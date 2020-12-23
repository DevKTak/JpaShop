package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 자동
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // N(Order) : 1(Member) 관계 명시
    @JoinColumn(name = "member_id") // 매핑을 뭘로 할지, FK(member_id) 이름 설정
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 1(Order) : N(OrderItem) 관계 명시
    // Order 테이블에 order 필드에 의해 매핑이 된다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송정보

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        this.orderItems.add(orderItem);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 ==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료 된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel(); // 여러개의 상품을 주문했다면 상품마다 카운트를 원복 하는 기능
        }
    }

    //== 조회 로직 ==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        // return this.orderItems.stream()
        //              .mapToInt(OrderItem::getTotalPrice)
        //              .sum();
        int totalPrice = 0;
        for (OrderItem orderItem : this.orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public String toStringDefault() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
