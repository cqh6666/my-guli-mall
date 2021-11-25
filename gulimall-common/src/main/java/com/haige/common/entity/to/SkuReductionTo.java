package com.haige.common.entity.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className: com.haige.common.entity.to-> SkuReductionTo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-25 19:00
 * @version: 1.0
 * @todo:
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;

    private List<MemberPrice> memberPrice;

}
