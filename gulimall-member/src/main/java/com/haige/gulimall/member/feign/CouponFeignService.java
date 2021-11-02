package com.haige.gulimall.member.feign;

import com.haige.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: com.haige.gulimall.member.feign-> CouponFeignService
 * @description:
 * @author: cqh
 * @createDate: 2021-11-02 12:02
 * @version: 1.0
 * @todo:
 */
// 添加FeignClient注解，绑定服务提供者。
@FeignClient("gulimall-coupon")
@Component
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    R memberCoupons();
}
