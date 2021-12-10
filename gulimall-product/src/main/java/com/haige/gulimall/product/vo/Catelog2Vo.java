package com.haige.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @className: com.haige.gulimall.product.vo-> Catelog2Vo
 * @description:
 * @author: cqh
 * @createDate: 2021-12-03 15:59
 * @version: 1.0
 * @todo:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 一级父分类的id
     */
    private String catalog1Id;

    /**
     * 三级子分类
     */
    private List<Category3Vo> catalog3List;

    private String id;

    private String name;


    /**
     * 三级分类vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category3Vo {

        /**
         * 父分类、二级分类id
         */
        private String catalog2Id;

        private String id;

        private String name;
    }

}
