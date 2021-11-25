package com.haige.gulimall.product.service.impl;

import com.haige.gulimall.product.dao.BrandDao;
import com.haige.gulimall.product.dao.CategoryDao;
import com.haige.gulimall.product.entity.BrandEntity;
import com.haige.gulimall.product.entity.CategoryEntity;
import com.haige.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.CategoryBrandRelationDao;
import com.haige.gulimall.product.entity.CategoryBrandRelationEntity;
import com.haige.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAllInfo(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        BrandEntity brand = brandDao.selectById(brandId);
        CategoryEntity category = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());

        this.save(categoryBrandRelation);


    }

    @Override
    public void updateBrandName(Long brandId, String name) {
        CategoryBrandRelationEntity brandRelationEntity = new CategoryBrandRelationEntity();
        brandRelationEntity.setBrandId(brandId);
        brandRelationEntity.setBrandName(name);
        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id",brandId);
        this.update(brandRelationEntity,queryWrapper);
    }

    @Override
    public void updateCategoryName(Long catId, String name) {
        // 方法一
//        CategoryBrandRelationEntity categoryRelationEntity = new CategoryBrandRelationEntity();
//        categoryRelationEntity.setCatelogId(catId);
//        categoryRelationEntity.setCatelogName(name);
//        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("catelog_id",catId);
//        this.update(categoryRelationEntity,queryWrapper);

        //方法二 用mapper
        this.baseMapper.updateCategoryName(catId,name);
    }

    @Override
    public List<BrandEntity> getBrandByCatId(Long catId) {

        List<CategoryBrandRelationEntity> brandRelationEntityList = this.baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> brandEntityList = brandRelationEntityList.stream().map(item -> {
            Long brandId = item.getBrandId();
            BrandEntity brandEntity = brandService.getById(brandId);
            return brandEntity;
        }).collect(Collectors.toList());

        return brandEntityList;
    }

}