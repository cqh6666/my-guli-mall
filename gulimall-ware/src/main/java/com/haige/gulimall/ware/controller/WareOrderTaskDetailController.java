package com.haige.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haige.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.haige.gulimall.ware.service.WareOrderTaskDetailService;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.R;



/**
 * 库存工作单
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:50:03
 */
@RestController
@RequestMapping("ware/wareordertaskdetail")
public class WareOrderTaskDetailController {
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareOrderTaskDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareOrderTaskDetailEntity wareOrderTaskDetail = wareOrderTaskDetailService.getById(id);

        return R.ok().put("wareOrderTaskDetail", wareOrderTaskDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail){
		wareOrderTaskDetailService.save(wareOrderTaskDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareOrderTaskDetailEntity wareOrderTaskDetail){
		wareOrderTaskDetailService.updateById(wareOrderTaskDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareOrderTaskDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
