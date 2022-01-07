package com.haige.gulimall.product.vo;


import com.haige.gulimall.product.entity.SkuImagesEntity;
import com.haige.gulimall.product.entity.SkuInfoEntity;
import com.haige.gulimall.product.entity.SpuInfoDescEntity;
import com.haige.gulimall.product.vo.skuinfo.SeckillSkuVo;
import com.haige.gulimall.product.vo.skuinfo.SkuItemSaleAttrVo;
import com.haige.gulimall.product.vo.skuinfo.SpuItemAttrGroupVo;
import lombok.Data;

import java.util.List;

/**
 * @author cqh
 */
@Data
public class SkuItemVo {

    private SkuInfoEntity info;

    private boolean hasStock = true;

    private List<SkuImagesEntity> images;

    /**
     * 销售属性
     */
    private List<SkuItemSaleAttrVo> saleAttr;

    private SpuInfoDescEntity desc;

    /**
     * spu属性分组
     */
    private List<SpuItemAttrGroupVo> groupAttrs;

    private SeckillSkuVo seckillSkuVo;

}
