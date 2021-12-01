package com.haige.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.ware.entity.PurchaseEntity;
import com.haige.gulimall.ware.entity.vo.MergeVo;
import com.haige.gulimall.ware.entity.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:50:03
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getUnreceiveList(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    /**
     * 采购单的id
     * @param ids
     */
    void receive(List<Long> ids);

    void doneAction(PurchaseDoneVo doneVo);
}

