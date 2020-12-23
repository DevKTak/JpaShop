package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 통합 테이블로 변환하는 단일 테이블 전략
@DiscriminatorColumn(name = "dtype") // 한 테이블에 모든 컬럼을 저장하기 때문에, DTYPE 없이는 테이블을 판단할 수 없다
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 비즈니스 로직 ==//
    // service 쪽이 아닌 데이터를 가지고 있는 쪽에 비즈니스 로직을 넣어줘야
    // 객체지향적으로 응집력도 좋고 관리하기 좋다.
    // 바깥에서 셋터, 겟터로 처리하는게 아니고 핵심 메서드를 통해서 처리한다.

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    /**
     * item 수정
     */
    public void change(String name, int price, int stockQuantity) {
        this.setName(name);
        this.setPrice(price);
        setStockQuantity(stockQuantity);
    }

    ;
}
