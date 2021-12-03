package com.haige.common.entity.vo;

import lombok.Data;

/**
 * @className: com.haige.gulimall.ware.entity.vo-> HasStockVo
 * @description: SKU是否有库存
 * @author: cqh
 * @createDate: 2021-12-02 15:49
 * @version: 1.0
 * @todo:
 */
@Data
public class HasStockVo {

    private Long skuId;
    private Boolean hasStock;
}
