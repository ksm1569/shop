package com.smsoft.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter @Setter
@ToString
@Table(name = "cart")
@Entity
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;
}
