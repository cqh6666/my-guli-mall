package com.haige.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.haige.gulimall.product.entity.AttrEntity;
import com.haige.gulimall.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 20:24:28
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AttrEntity> getAttrInfo(Long attrGroupId);

    void removeRelationBatch(AttrGroupRelationVo[] relationVos);

    PageUtils getNoAttrInfo(Long attrGroupId, Map<String, Object> params);
}

