package com.haige.gulimall.coupon.controller;

import com.haige.common.utils.R;
import com.haige.gulimall.coupon.entity.CouponEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @className: com.haige.gulimall.coupon.controller-> testController
 * @description: 用来测试新加的依赖
 * @author: cqh
 * @createDate: 2021-11-02 12:31
 * @version: 1.0
 * @todo:
 */
@RefreshScope
@RestController
@RequestMapping("coupon/test")
public class testController {

    @Value("${couponUser.name}")
    private String name;
    @Value("${couponUser.age}")
    private Integer age;

    @RequestMapping("/nacos/config")
    public R nacosConfig(){

        return R.ok().put("name",name).put("age",age);
    }

    /**
     * 远程调用接口
     * @return
     */
    @RequestMapping("/member/list")
    public R memberCoupons(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("满10减100");
        return R.ok().put("coupons", Arrays.asList(couponEntity));
    }
}
