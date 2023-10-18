package com.smsoft.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smsoft.shop.constant.ItemSellStatus;
import com.smsoft.shop.entity.Item;
import com.smsoft.shop.entity.QItem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;
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
        for (int i = 0; i < 10; i++) {
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

        for (Item item : itemList) {
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

        for (Item item : itemList) {
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

    @Test
    @Order(8)
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem).where(qItem.itemSellStatus.eq(ItemSellStatus.SELL)).where(qItem.itemDetail.like("%상품 상세설명%")).orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("상품" + i);
            item.setPrice(10000 + i);
            item.setStockNumber(100);
            item.setItemDetail("상품 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("상품" + i);
            item.setPrice(10000 + i);
            item.setStockNumber(100);
            item.setItemDetail("상품 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }
    }

    @Test
    @Order(9)
    @DisplayName("Querydsl 조회 테스트2")
    public void queryDslTest2() {
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = QItem.item;

        String itemDetail = "상품 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(qItem.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(qItem.price.gt(price));
        System.out.println(ItemSellStatus.SELL);

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item item : resultItemList) {
            System.out.println(item.toString());
        }
    }
}