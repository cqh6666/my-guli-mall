package com.haige.gulimall.product.vo;

import lombok.Data;

/**
 * @className: com.haige.gulimall.product.vo-> AttrGroupRelationVo
 * @description: 收集前端传来的属性ID和分组ID
 * @author: cqh
 * @createDate: 2021-11-19 12:58
 * @version: 1.0
 * @todo:
 */
@Data
public class AttrGroupRelationVo {

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性分组id
     */
    private Long attrGroupId;
}
