package com.haige.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.haige.gulimall.product.entity.BrandEntity;
import com.haige.gulimall.product.service.BrandService;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.R;


/**
 * 品牌
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-10-31 21:10:33
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@Validated @RequestBody BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 修改显示状态
     */
    @PostMapping("/update/status")
    public R updateStatus(@RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }

}
