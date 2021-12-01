package com.haige.gulimall.ware.entity.vo;

import lombok.Data;

/**
 * @className: com.haige.gulimall.ware.entity.vo-> PurchaseItemVo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-26 15:14
 * @version: 1.0
 * @todo:
 */
@Data
public class PurchaseItemVo {

    private Long itemId;
    private Integer status;
    private String reason;

}
