package com.haige.gulimall.product.service.impl;

import com.haige.common.constant.ProductConstant;
import com.haige.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.haige.gulimall.product.dao.AttrGroupDao;
import com.haige.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.haige.gulimall.product.entity.AttrGroupEntity;
import com.haige.gulimall.product.entity.CategoryEntity;
import com.haige.gulimall.product.service.AttrAttrgroupRelationService;
import com.haige.gulimall.product.service.CategoryService;
import com.haige.gulimall.product.vo.AttrRespVo;
import com.haige.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.product.dao.AttrDao;
import com.haige.gulimall.product.entity.AttrEntity;
import com.haige.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrAttrgroupRelationService attrgroupRelationService;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAttr(AttrVo attrVo) {

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);

        this.save(attrEntity);

        Long attrGroupId = attrVo.getAttrGroupId();
        if(attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attrGroupId!=0){
            // 保存关联关系
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, String attrType, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();

        // attr类型
        queryWrapper.eq("attr_type","base".equalsIgnoreCase(attrType)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catelogId != 0){
            queryWrapper.eq("catelog_id",catelogId);
        }

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
               wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),queryWrapper
        );

        // 查询到的所有记录
        List<AttrEntity> records = page.getRecords();

        // 转化为api响应数据格式
        List<AttrRespVo> attrRespVoList = new ArrayList<>();
        for (AttrEntity record : records) {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(record,attrRespVo);

            // 根据关系表查询AttrGroupName,只有在type为 规格参数 才需要查询 (销售属性没有分组)
            if("base".equalsIgnoreCase(attrType)){
                AttrAttrgroupRelationEntity attrGroupRelation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
                if(attrGroupRelation!=null && attrGroupRelation.getAttrGroupId()!=null){
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupRelation.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            // 查询Category表获得所属分类
            Long respVoCatelogId = attrRespVo.getCatelogId();
            if(respVoCatelogId!=0){
                CategoryEntity categoryEntity = categoryService.getById(respVoCatelogId);
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            attrRespVoList.add(attrRespVo);
        }

        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrRespVoList);

        return pageUtils;
    }

    @Override
    public AttrRespVo getInfoById(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);

        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            // 加上分组的ID和名称
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if(relationEntity!=null){
                Long attrGroupId = relationEntity.getAttrGroupId();
                attrRespVo.setAttrGroupId(attrGroupId);

                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                if(attrGroupEntity!=null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        // 加上分类名称和路径
        Long catelogId = attrEntity.getCatelogId();
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if(categoryEntity!=null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrRespVo.setCatelogPath(catelogPath);

        return attrRespVo;

    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInfoById(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        // 修改分组关联, 只有为 基本属性 才需要保存关联关系
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            // 这是修改还是新增，需要判断以下
            attrgroupRelationService.saveOrUpdate(relationEntity,new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));

        }

    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {
        return this.baseMapper.selectSearchAttrs(attrIds);
    }

}