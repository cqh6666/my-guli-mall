package com.haige.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @className: com.haige.gulimall.search.vo-> SearchParam
 * @description:
 * @author: cqh
 * @createDate: 2021-12-17 18:32
 * @version: 1.0
 * @todo:
 */
@Data
public class SearchParam {

    /**
     * 页面传递过来的全文匹配关键字
     */
    private String keyword;

    /**
     * 品牌id,可以多选
     * brandId = 1,2,3,4..
     */
    private List<Long> brandId;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    /**
     * 排序条件：sort=price/salecount/hotscore  +  _desc/asc
     */
    private String sort;

    /**
     * 是否显示有货
     * hasStock 0 / 1
     */
    private Integer hasStock;

    /**
     * 价格区间查询
     * skuPrice = 1_500 / 500_1000 / 1000_
     */
    private String skuPrice;

    /**
     * 按照属性进行筛选
     * attrs=1_其他:安卓 & attrs=2_5寸:6寸
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;

}
