package com.haige.gulimall.product.feign;

import com.haige.common.entity.to.SkuReductionTo;
import com.haige.common.entity.to.SpuBoundTo;
import com.haige.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @className: com.haige.gulimall.product.feign-> SpuFeignService
 * @description:
 * @author: cqh
 * @createDate: 2021-11-25 18:41
 * @version: 1.0
 * @todo:
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
