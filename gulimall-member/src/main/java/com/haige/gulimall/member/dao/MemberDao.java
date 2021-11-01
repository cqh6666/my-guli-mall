package com.haige.gulimall.member.dao;

import com.haige.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:39:37
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
