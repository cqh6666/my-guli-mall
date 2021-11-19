package com.haige.gulimall.product.service.impl;

import com.haige.common.constant.ProductConstant;
import com.haige.gulimall.product.dao.AttrDao;
import com.haige.gulimall.product.dao.AttrGroupDao;
import com.haige.gulimall.product.entity.AttrEntity;
import com.haige.gulimall.product.entity.AttrGroupEntity;
import com.haige.gulimall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.haige.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.haige.gulimall.product.service.AttrAttrgroupRelationService;
import org.springframework.util.StringUtils;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrInfo(Long attrGroupId) {

        ArrayList<AttrEntity> attrEntityList = new ArrayList<>();

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id",attrGroupId);

        List<AttrAttrgroupRelationEntity> attrgroupRelationEntities = this.list(queryWrapper);
        for (AttrAttrgroupRelationEntity relationEntity : attrgroupRelationEntities) {
            Long attrId = relationEntity.getAttrId();
            if(attrId!=0){
                attrEntityList.add(attrDao.selectById(attrId));
            }
        }

        return attrEntityList;
    }

    @Override
    public PageUtils getNoAttrInfo(Long attrGroupId, Map<String, Object> params) {

        // 当前分组只能关联自己所属分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        // 当前分组只能关联到别的分组没有引用的属性
        // 当前分类的其他分组
        List<AttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> groupIds = groupEntities.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        // 这些分组关联的属性
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIds));
        List<Long> attrIds = relationEntities.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        // 从当前分类所有属性中移除这些属性
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds!=null && attrIds.size()!=0){
            attrEntityQueryWrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            attrEntityQueryWrapper.and((wrapper) -> {
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> attrEntityIPage = attrDao.selectPage(new Query<AttrEntity>().getPage(params), attrEntityQueryWrapper);
        PageUtils pageUtils = new PageUtils(attrEntityIPage);
        return pageUtils;
    }


    @Override
    public void removeRelationBatch(AttrGroupRelationVo[] relationVos) {

        List<AttrAttrgroupRelationEntity> entityList = Arrays.asList(relationVos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        relationDao.deleteBatchRelation(entityList);
    }


}