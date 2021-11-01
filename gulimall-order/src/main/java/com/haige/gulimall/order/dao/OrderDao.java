package com.haige.gulimall.order.dao;

import com.haige.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:48:05
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
