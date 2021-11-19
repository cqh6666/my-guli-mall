package com.haige.gulimall.product.vo;

import lombok.Data;

/**
 * @className: com.haige.gulimall.product.vo-> AttrRespVo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-18 19:05
 * @version: 1.0
 * @todo:
 */
@Data
public class AttrRespVo extends AttrVo{

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分类名称
     */
    private String catelogName;

    /**
     * 分类路径
     */
    private Long[] catelogPath;
}
