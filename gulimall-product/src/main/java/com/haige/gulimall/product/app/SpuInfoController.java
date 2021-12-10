package com.haige.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import com.haige.gulimall.product.vo.spuinfo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.haige.gulimall.product.entity.SpuInfoEntity;
import com.haige.gulimall.product.service.SpuInfoService;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.R;



/**
 * spu信息
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 21:10:33
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {

    @Autowired
    private SpuInfoService spuInfoService;


    @PostMapping("{spuId}/up")
    public R spuUp(@PathVariable("spuId")Long spuId){
        spuInfoService.up(spuId);

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 新增商品
     * /product/spuinfo/save
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo spuInfoVo){
		spuInfoService.saveSpuInfo(spuInfoVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
