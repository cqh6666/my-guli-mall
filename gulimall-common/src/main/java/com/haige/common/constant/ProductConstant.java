package com.haige.common.constant;

/**
 * @className: io.renren.common.constant-> ProductConstant
 * @description:
 * @author: cqh
 * @createDate: 2021-11-18 21:44
 * @version: 1.0
 * @todo:
 */
public class ProductConstant {

    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String mag;

        AttrEnum(int code, String mag) {
            this.code = code;
            this.mag = mag;
        }

        public int getCode() {
            return code;
        }

        public String getMag() {
            return mag;
        }
    }

    public enum StatusEnum{
        NEW_PRODUCT(0,"新建"),SPU_UP(1,"上架"),SPU_DOWN(2,"下架");

        private int code;
        private String mag;

        StatusEnum(int code, String mag) {
            this.code = code;
            this.mag = mag;
        }

        public int getCode() {
            return code;
        }

        public String getMag() {
            return mag;
        }
    }
}
