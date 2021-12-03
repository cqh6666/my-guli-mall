package com.haige.gulimall.product.feign;

import com.haige.common.entity.es.SkuEsModel;
import com.haige.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @className: com.haige.gulimall.product.feign-> SearchFeignService
 * @description:
 * @author: cqh
 * @createDate: 2021-12-02 20:55
 * @version: 1.0
 * @todo:
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}
