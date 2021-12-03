package com.haige.gulimall.search.controller;

import com.haige.common.entity.es.SkuEsModel;
import com.haige.common.exception.BizCodeEnum;
import com.haige.common.utils.R;
import com.haige.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @className: com.haige.gulimall.search.controller-> ElasticSearchController
 * @description:
 * @author: cqh
 * @createDate: 2021-12-02 16:28
 * @version: 1.0
 * @todo:
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSearchController {

    @Autowired
    private ProductSaveService saveService;

    @PostMapping("product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){
        boolean statusUp;
        try{
            statusUp = saveService.productStatusUp(skuEsModelList);
        }catch (Exception e){
            log.error("ES controller上架错误...{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }

        if(statusUp){
            return R.ok();
        }else{
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }

    }

}
