package com.haige.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.ProductAttrValueDao;
import com.haige.gulimall.product.entity.ProductAttrValueEntity;
import com.haige.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> valueEntities) {
        this.saveBatch(valueEntities);
    }

    @Override
    public List<ProductAttrValueEntity> BaseAttrListForSpu(Long spuId) {
        List<ProductAttrValueEntity> attrValueEntities = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return attrValueEntities;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        // 先删除属性 再添加
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));

        List<ProductAttrValueEntity> collect = entities.stream().map(entity -> {
            entity.setSpuId(spuId);
            return entity;
        }).collect(Collectors.toList());

        this.saveBatch(collect);
    }

}