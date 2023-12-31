package com.smsoft.shop.entity;

import com.smsoft.shop.constant.ItemSellStatus;
import com.smsoft.shop.repository.ItemRepository;
import com.smsoft.shop.repository.MemberRepository;
import com.smsoft.shop.repository.OrderItemRepository;
import com.smsoft.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("test item");
        item.setPrice(2000);
        item.setStockNumber(50);
        item.setItemDetail("item detail");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for(int i=0; i<5; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setOrderPrice(5000);
            orderItem.setCount(12);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(5, savedOrder.getOrderItems().size());
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i=0; i<5; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setOrderPrice(3000);
            orderItem.setCount(2);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();

        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class" + orderItem.getOrder().getClass());
        System.out.println("=================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=================");
    }
}