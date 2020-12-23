package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookForm {

    private Long id; // 상품은 수정이 있기때문에 form에 id를 받을 것임

    //    @NotEmpty(message = "상품 이름은 필수 입니다")
    private String name;

    private int price;
    private int stockQuantity;

    // 책과 관련 된 속성
    @NotEmpty(message = "isbn 테스트")
    private String isbn;

    private String author;

}
