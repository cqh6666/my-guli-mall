package com.haige.common.entity.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @className: com.haige.common.entity.to-> SpuBoundTo
 * @description:
 * @author: cqh
 * @createDate: 2021-11-25 18:48
 * @version: 1.0
 * @todo:
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
