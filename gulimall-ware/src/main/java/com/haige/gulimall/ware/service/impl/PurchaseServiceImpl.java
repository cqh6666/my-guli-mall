package com.haige.gulimall.ware.service.impl;

import com.haige.common.constant.WareConstant;
import com.haige.gulimall.ware.controller.PurchaseDetailController;
import com.haige.gulimall.ware.entity.PurchaseDetailEntity;
import com.haige.gulimall.ware.entity.vo.MergeVo;
import com.haige.gulimall.ware.entity.vo.PurchaseDoneVo;
import com.haige.gulimall.ware.entity.vo.PurchaseItemVo;
import com.haige.gulimall.ware.service.PurchaseDetailService;
import com.haige.gulimall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haige.common.utils.PageUtils;
import com.haige.common.utils.Query;

import com.haige.gulimall.ware.dao.PurchaseDao;
import com.haige.gulimall.ware.entity.PurchaseEntity;
import com.haige.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService detailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getUnreceiveList(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0).or().eq("status",1);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(MergeVo mergeVo) {

        Long purchaseId = mergeVo.getPurchaseId();
        // 若没有选择采购单，则插入一个采购单作为使用
        if(purchaseId == null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // TODO: 确认采购单状态是0或1才合并

        // 根据采购单ID，更新采购单详情信息
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = items.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        detailService.updateBatchById(purchaseDetailEntities);

    }

    @Override
    public void receive(List<Long> ids) {

        //1 确认当前采购单的状态（新建或已分配）
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity purchaseEntity = this.getById(id);
            return purchaseEntity;
        }).filter(item -> {
            return item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
        }).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
            return item;
        }).collect(Collectors.toList());

        //2 改变采购单的状态
        this.updateBatchById(collect);

        //3 改变采购项的状态
        collect.forEach((item)->{
            // 根据采购单ID得到所有采购项
            List<PurchaseDetailEntity> entities = detailService.listDetailByPurchaseId(item.getId());
            // 对采购项所有对象进行更新状态
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entity.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            detailService.updateBatchById(detailEntities);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doneAction(PurchaseDoneVo doneVo) {
        // flag代表是否需要改变采购单的状态，这取决于采购项是否全部完成
        Boolean flag = true;

        // 2. 改变采购项状态
        List<PurchaseItemVo> purchaseItemVos = doneVo.getItems();
        // 存入数据库的List
        List<PurchaseDetailEntity> purchaseDetailEntities = new ArrayList<>();

        for (PurchaseItemVo itemVo : purchaseItemVos) {
            // 遍历所有成功的采购项和不成功的采购项
            PurchaseDetailEntity entity = new PurchaseDetailEntity();

            // 有异常
            if(itemVo.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERED.getCode()){
                flag = false;
                entity.setStatus(itemVo.getStatus());
            }else{
                // 成功操作
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISHED.getCode());
                // 采购项成功后需要入库
                // 入库之前需要先查出采购项相关信息
                PurchaseDetailEntity detailEntity = detailService.getById(itemVo.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(),detailEntity.getWareId(),detailEntity.getSkuNum());
            }
            entity.setId(itemVo.getItemId());
            purchaseDetailEntities.add(entity);
        }
        detailService.updateBatchById(purchaseDetailEntities);

        // 1. 改变采购单状态
        Long purchaseId = doneVo.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISHED.getCode():WareConstant.PurchaseStatusEnum.HASERED.getCode());
        this.updateById(purchaseEntity);

    }

}