package com.haige.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:50:03
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

