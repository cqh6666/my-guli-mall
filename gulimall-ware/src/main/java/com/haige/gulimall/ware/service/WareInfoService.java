package com.haige.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haige.common.utils.PageUtils;
import com.haige.gulimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:50:03
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils querPageByCondition(Map<String, Object> params);
}

