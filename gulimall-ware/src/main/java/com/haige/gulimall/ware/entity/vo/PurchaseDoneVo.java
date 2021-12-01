package com.haige.gulimall.ware.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @className: com.haige.gulimall.ware.entity.vo-> PurchaseDoneVo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-26 15:13
 * @version: 1.0
 * @todo:
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;

    List<PurchaseItemVo> items;
}
