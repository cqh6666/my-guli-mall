package com.haige.gulimall.ware.feign;

import com.haige.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: com.haige.gulimall.ware.feign-> ProductFeignService
 * @description:
 * @author: cqh
 * @createDate: 2021-11-28 17:02
 * @version: 1.0
 * @todo:
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

}
