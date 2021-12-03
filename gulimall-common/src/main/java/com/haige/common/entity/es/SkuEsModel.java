package com.haige.common.entity.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className: com.haige.common.entity.es-> SkuEsModel
 * @description: 存入ES的格式
 * @author: cqh
 * @createDate: 2021-12-01 22:36
 * @version: 1.0
 * @todo:
 */
@Data
public class SkuEsModel {

    /**
     * 具体商品ID
     */
    private Long skuId;
    /**
     * 一类产品ID
     */
    private Long spuId;
    /**
     * 商品标题，可用全文检索
     */
    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;
    /**
     * 是否有库存
     */
    private Boolean hasStock;
    /**
     * 热度评分
     */
    private Long hotScore;
    /**
     * 品牌ID
     */
    private Long brandId;
    /**
     * 分类ID
     */
    private Long catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    /**
     * 属性
     */
    private List<Attrs> attrs;

    @Data
    public static class Attrs {

        private Long attrId;

        private String attrName;

        private String attrValue;

    }


}
