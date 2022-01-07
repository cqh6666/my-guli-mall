package com.haige.gulimall.product.service.impl;

import com.haige.gulimall.product.entity.AttrEntity;
import com.haige.gulimall.product.service.AttrService;
import com.haige.gulimall.product.vo.AttrGroupWithAttrVo;
import com.haige.gulimall.product.vo.skuinfo.SpuItemAttrGroupVo;
import org.springframework.beans.BeanUtils;
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

import com.haige.gulimall.product.dao.AttrGroupDao;
import com.haige.gulimall.product.entity.AttrGroupEntity;
import com.haige.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationServiceImpl relationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCatelogId(Map<String, Object> params, Long catelogId) {
        if(catelogId == 0){
            return queryPage(params);
        }

        String key = (String) params.get("key");
        // select * from pms_attr_group where catelog_id = ? and (attr_group_id = key or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id",catelogId);
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        IPage<AttrGroupEntity> searchPage = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);

        return new PageUtils(searchPage);
    }

    @Override
    public List<AttrGroupWithAttrVo> getAttrGroupWithAttrByCatelogId(Long catelogId) {

        // 根据分类ID得到所有对应的分组
        List<AttrGroupEntity> groupEntityList = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        List<AttrGroupWithAttrVo> attrVos = groupEntityList.stream().map(groupEntity -> {
            AttrGroupWithAttrVo attrVo = new AttrGroupWithAttrVo();
            BeanUtils.copyProperties(groupEntity, attrVo);
            List<AttrEntity> attrs = relationService.getAttrInfo(groupEntity.getAttrGroupId());
            attrVo.setAttrs(attrs);
            return attrVo;
        }).collect(Collectors.toList());

        return attrVos;


    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuIdAndCatelogId(Long spuId, Long catalogId) {
        return this.baseMapper.getAttrGroupWithAttrsBySpuIdAndCatelogId(spuId,catalogId);
    }

}