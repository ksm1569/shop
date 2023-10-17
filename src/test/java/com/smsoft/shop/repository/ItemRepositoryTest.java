package com.smsoft.shop.repository;

import com.smsoft.shop.constant.ItemSellStatus;
import com.smsoft.shop.entity.Item;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    @Order(1)
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(2000);
        item.setStockNumber(5);
        item.setItemDetail("상품 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);
        System.out.println(saveItem.toString());
    }

    @Test
    @Order(2)
    @DisplayName("상품 10개 저장 테스트")
    public void createItemListTest() {
        for(int i=0; i<10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(2000 + i);
            item.setStockNumber(5);
            item.setItemDetail("상품 상세설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @Order(3)
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품2");

        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @Order(4)
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        List<Item> itemList = itemRepository.findByPriceLessThan(2003);

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @Order(5)
    @DisplayName("가격 lessThan 내림차순 테스트")
    public void findByPriceLessThan() {
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(2003);

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @Order(6)
    @DisplayName("@Query로 상품 상세설명 조회 테스트")
    public void findByItemDetail() {
        List<Item> itemList = itemRepository.findByItemDetail("상품 상세설명");

        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @Order(7)
    @DisplayName("native query 속성으로 상세설명 조회 테스트")
    public void findByItemDetailNative() {
        List<Item> itemList = itemRepository.findByItemDetailByNative("상품 상세설명");

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }
}