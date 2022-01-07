package com.haige.gulimall.product.web;

import com.haige.gulimall.product.service.SkuInfoService;
import com.haige.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @className: com.haige.gulimall.product.web-> ItemController
 * @description:
 * @author: cqh
 * @createDate: 2022-01-05 15:57
 * @version: 1.0
 * @todo:
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model){

        SkuItemVo itemVo = null;
        try {
            itemVo = skuInfoService.item(skuId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        model.addAttribute("item",itemVo);

        return "item";
    }

}
