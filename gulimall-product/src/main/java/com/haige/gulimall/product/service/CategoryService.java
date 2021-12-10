package com.haige.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.product.entity.CategoryEntity;
import com.haige.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 20:24:28
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    /**
     * 找到CatelogId的完整路径
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新
     * @param category
     */
    void updateCascade(CategoryEntity category);

    /**
     * 查出所有一级分类
     * @return
     */
    List<CategoryEntity> getLevelOneCategories();

    Map<String, List<Catelog2Vo>> getCategoryJson();

}

