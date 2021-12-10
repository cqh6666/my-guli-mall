package com.haige.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.haige.gulimall.product.entity.ProductAttrValueEntity;
import com.haige.gulimall.product.service.ProductAttrValueService;
import com.haige.gulimall.product.vo.AttrRespVo;
import com.haige.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.haige.gulimall.product.service.AttrService;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.R;


/**
 * 商品属性
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 21:10:34
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    /**
     * 查看spuId的信息
     * @param spuId
     * @return
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R BaseAttrListForSpu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> attrValueEntities = attrValueService.BaseAttrListForSpu(spuId);

        return R.ok().put("data",attrValueEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/{attrType}/list/{catelogId}")
    public R BaseAttrPage(@RequestParam Map<String, Object> params,
                          @PathVariable("attrType") String attrType,
                          @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrService.queryBaseAttrPage(params,attrType,catelogId);

        return R.ok().put("page",page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attrRespVo = attrService.getInfoById(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateInfoById(attr);
        return R.ok();
    }

    /**
     * 根据SpuId更新
     * @param spuId
     * @return
     */
    @PostMapping("/update/{spuId}")
    public R update(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> entities){
        attrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));
        return R.ok();
    }

}
