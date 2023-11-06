package com.smsoft.shop.dto;

import com.smsoft.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String SearchBy;
    private String searchQuery = "";

}
