package com.haige.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 20:24:29
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveProductAttr(List<ProductAttrValueEntity> valueEntities);

    List<ProductAttrValueEntity> BaseAttrListForSpu(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);

}

