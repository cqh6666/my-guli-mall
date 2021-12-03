package com.haige.gulimall.product.feign;

import com.haige.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @className: com.haige.gulimall.product.feign-> WareFeignService
 * @description:
 * @author: cqh
 * @createDate: 2021-12-02 15:59
 * @version: 1.0
 * @todo:
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> ids);
}
