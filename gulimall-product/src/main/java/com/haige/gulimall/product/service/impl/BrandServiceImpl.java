package com.haige.gulimall.product.service.impl;

import com.haige.gulimall.product.dao.CategoryBrandRelationDao;
import com.haige.gulimall.product.entity.AttrGroupEntity;
import com.haige.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.BrandDao;
import com.haige.gulimall.product.entity.BrandEntity;
import com.haige.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // params 为 相关参数，比如搜索key值
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            wrapper.eq("brand_id",key).or().like("name",key);
        }
        IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        // 保证冗余字段的数据一致
        this.updateById(brand);

        // 如果有名字字段发生更新
        if(!StringUtils.isEmpty(brand.getName())){
            // 此时需要同步其他表的数据
            categoryBrandRelationService.updateBrandName(brand.getBrandId(),brand.getName());
            // TODO 更新其他联表数据
        }

    }

}