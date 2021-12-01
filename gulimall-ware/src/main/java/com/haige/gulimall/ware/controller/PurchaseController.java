package com.haige.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.haige.gulimall.ware.entity.vo.MergeVo;
import com.haige.gulimall.ware.entity.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.haige.gulimall.ware.entity.PurchaseEntity;
import com.haige.gulimall.ware.service.PurchaseService;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.R;



/**
 * 采购信息
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:50:03
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("done")
    public R doneAction(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.doneAction(doneVo);

        return R.ok();
    }

    /**
     * 领取采购单
     * @param ids 采购单ID
     * @return
     */
    @PostMapping("receive")
    public R receive(@RequestBody List<Long> ids){
        purchaseService.receive(ids);
        return R.ok();
    }
    /**
     * /ware/purchase/unreceive/list
     * 查询未领取的采购单
     */
    @GetMapping("unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.getUnreceiveList(params);

        return R.ok().put("page",page);
    }

    /**
     * 合并采购需求
     */
    @PostMapping("merge")
    public R merge(@RequestBody MergeVo mergeVo){

        purchaseService.mergePurchase(mergeVo);


        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
