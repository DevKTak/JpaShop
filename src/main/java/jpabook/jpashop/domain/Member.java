package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 1(Member) : N(Order) 관계 명시 (연관관계의 주인이 아니다,
    // Order 테이블에 있는 member 필드에 의해 매핑이 되었다.
    // 거울일 뿐, 읽기전용, 여기에 값을 넣는다해서 FK값이 변경되지 않음.
    private List<Order> orders = new ArrayList<>();
}
