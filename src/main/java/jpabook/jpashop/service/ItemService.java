package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // default: false
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * - 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고,
     * 엔티티의 데이터를 직접 변경하세요.
     * - 트랜잭션 커밋 시점에 변경감지가 실행됩니다.
     * - 영속성 컨텍스트가 자동 변경됨.
     */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(id); // 다시 조회

        // setter를 지양하고 명확한 메소드를 만들고 이 안에서 값을 변경하자.
        findItem.change(name, price, stockQuantity);
//        findItem.setName(name);
//        findItem.setPrice(price);
//        findItem.setStockQuantity(stockQuantity);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItem() {
        return itemRepository.findAll();
    }


}
