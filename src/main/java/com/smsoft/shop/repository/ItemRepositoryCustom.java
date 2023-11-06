package com.smsoft.shop.repository;

import com.smsoft.shop.dto.ItemSearchDto;
import com.smsoft.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
