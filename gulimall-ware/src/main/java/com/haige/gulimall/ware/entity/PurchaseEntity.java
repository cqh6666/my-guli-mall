package com.haige.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 采购信息
 * 
 * @author chenqinhai
 * @email 2018ch@m.scnu.edu.cn
 * @date 2021-11-01 22:50:03
 */
@Data
@TableName("wms_purchase")
public class PurchaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购单id
	 */
	@TableId
	private Long id;
	/**
	 * 采购人id
	 */
	private Long assigneeId;
	/**
	 * 采购人名
	 */
	private String assigneeName;
	/**
	 * 联系方式
	 */
	private String phone;
	/**
	 * 优先级
	 */
	private Integer priority;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 仓库id
	 */
	private Long wareId;
	/**
	 * 总金额
	 */
	private BigDecimal amount;
	/**
	 * 创建日期
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 更新日期
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
