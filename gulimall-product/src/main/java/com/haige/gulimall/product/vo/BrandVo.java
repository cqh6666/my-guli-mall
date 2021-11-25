package com.haige.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @className: com.haige.gulimall.product.vo-> BrandVo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-19 17:04
 * @version: 1.0
 * @todo:
 */
@Data
public class BrandVo {

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名
     */
    private String brandName;
}
