package com.haige.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.product.entity.BrandEntity;
import com.haige.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 20:24:28
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAllInfo(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrandName(Long brandId, String name);

    void updateCategoryName(Long catId, String name);

    List<BrandEntity> getBrandByCatId(Long catId);
}

