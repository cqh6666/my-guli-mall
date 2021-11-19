package com.haige.gulimall.product.service.impl;

import com.haige.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.CategoryDao;
import com.haige.gulimall.product.entity.CategoryEntity;
import com.haige.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1. 查出所有分类
        List<CategoryEntity> categoryList = baseMapper.selectList(null);

        // 2. 组装成父子的树形结构
        // 2.1 找到所有一级分类
        List<CategoryEntity> levelOne = categoryList.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == 0;
        }).map(menu -> {
            menu.setChildren(getChildrens(menu,categoryList));
            return menu;
        }).sorted((menu1,menu2) -> {
            return (menu1.getSort()==null ? 0: menu1.getSort()) - (menu2.getSort()==null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return levelOne;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        // todo: 检查当前删除的菜单，是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        LinkedList<Long> paths = new LinkedList<>();
        findParentPath(paths,catelogId);
        return paths.toArray(new Long[0]);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);

        if(!StringUtils.isEmpty(category.getName())){
            // categoryBrandRelation这个表也要跟着更新
            categoryBrandRelationService.updateCategoryName(category.getCatId(),category.getName());
        }
    }

    private void findParentPath(LinkedList<Long> paths,Long catelogId){
        paths.addFirst(catelogId);
        CategoryEntity parentCategory = this.getById(catelogId);
        if(parentCategory.getParentCid()!=0){
            findParentPath(paths,parentCategory.getParentCid());
        }
    }
    /**
     * 递归查找所有的子菜单
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> categoryChildren = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2) -> {
            return (menu1.getSort()==null ? 0: menu1.getSort()) - (menu2.getSort()==null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return categoryChildren;
    }

}