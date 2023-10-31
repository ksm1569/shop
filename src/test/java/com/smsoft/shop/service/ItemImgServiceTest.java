package com.smsoft.shop.service;

import com.smsoft.shop.constant.ItemSellStatus;
import com.smsoft.shop.dto.ItemFormDto;
import com.smsoft.shop.entity.Item;
import com.smsoft.shop.entity.ItemImg;
import com.smsoft.shop.repository.ItemImgRepository;
import com.smsoft.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@SpringBootTest
class ItemImgServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFile() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            String path = "D:/shop/item";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setPrice(10000);
        itemFormDto.setItemDetail("상품 상세 테스트");
        itemFormDto.setStockNumber(100);
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);

        List<MultipartFile> multipartFileList = createMultipartFile();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
    }
}