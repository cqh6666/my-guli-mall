package com.haige.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haige.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.haige.gulimall.product.entity.AttrEntity;
import com.haige.gulimall.product.service.AttrAttrgroupRelationService;
import com.haige.gulimall.product.service.CategoryService;
import com.haige.gulimall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.haige.gulimall.product.entity.AttrGroupEntity;
import com.haige.gulimall.product.service.AttrGroupService;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.R;



/**
 * 属性分组
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 21:10:33
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationService relationService;
    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){

//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPageByCatelogId(params,catelogId);

        return R.ok().put("page", page);
    }

    /**
     * 根据分组ID获取详情信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long[] path = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 获取属性分组的关联的所有属性
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R getAttrInfo(@PathVariable("attrgroupId")Long attrGroupId){
        List<AttrEntity> data = relationService.getAttrInfo(attrGroupId);

        return R.ok().put("data",data);
    }

    /**
     * 获取属性分组没有关联的其他属性
     * /product/attrgroup/{attrgroupId}/noattr/relation
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoAttrRelation(@RequestParam Map<String, Object> params,
                               @PathVariable("attrgroupId")Long attrGroupId){
        PageUtils pageUtils = relationService.getNoAttrInfo(attrGroupId,params);
        return R.ok().put("page",pageUtils);
    }

    /**
     * 添加属性与分组关联关系
     */
    @PostMapping("/attr/relation")
    public R addAttrAndAttrGroupRelation(@RequestBody AttrAttrgroupRelationEntity[] relationEntities){
        relationService.saveBatch(Arrays.asList(relationEntities));
        return R.ok();
    }

    /**
     * 删除属性与分组的关联关系
     */
    @PostMapping("/attr/relation/delete")
    public R deleteAttrAndAttrGroupRelation(@RequestBody AttrGroupRelationVo[] relationVos){
        relationService.removeRelationBatch(relationVos);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

}
