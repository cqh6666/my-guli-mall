package com.haige.gulimall.search.feign;

import com.haige.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @className: com.haige.gulimall.search-> ProductFeignService
 * @description:
 * @author: cqh
 * @createDate: 2021-12-25 17:55
 * @version: 1.0
 * @todo:
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);
}
