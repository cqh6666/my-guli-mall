package com.haige.gulimall.ware.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @className: com.haige.gulimall.ware.entity.vo-> MergeVo
 * @description: 合并采购单
 * @author: cqh
 * @createDate: 2021-11-26 9:59
 * @version: 1.0
 * @todo:
 */
@Data
public class MergeVo {

    private Long purchaseId;
    private List<Long> items;
}
