package com.smsoft.shop.controller;

import com.smsoft.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제");

        return "thymeleafex/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemNm("상품1");
        itemDto.setPrice(2000);
        itemDto.setItemDetail("상품 상세");
        itemDto.setRegTime(LocalDateTime.now());
        model.addAttribute("itemDto", itemDto);

        return "thymeleafex/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemNm("상품" + i);
            itemDto.setPrice(3000 + i);
            itemDto.setItemDetail("상품 상세" + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);

        return "thymeleafex/thymeleafEx03";
    }
}
