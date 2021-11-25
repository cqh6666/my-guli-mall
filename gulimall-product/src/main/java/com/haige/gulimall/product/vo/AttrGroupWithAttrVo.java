package com.haige.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.haige.gulimall.product.entity.AttrEntity;
import com.haige.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @className: com.haige.gulimall.product.vo-> AttrGroupWithAttrVo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-19 18:42
 * @version: 1.0
 * @todo:
 */
@Data
public class AttrGroupWithAttrVo {

    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 每个分组对应的关联属性
     */
    private List<AttrEntity> attrs;
}
