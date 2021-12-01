package com.haige.common.constant;

/**
 * @className: com.haige.common.constant-> WareConstant
 * @description: 采购单状态枚举
 * @author: cqh
 * @createDate: 2021-11-26 10:18
 * @version: 1.0
 * @todo:
 */

public class WareConstant {

    public enum PurchaseStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),RECEIVED(2,"已领取"),
        FINISHED(3,"已完成"),HASERED(4,"有异常");

        private int code;
        private String mag;

        PurchaseStatusEnum(int code, String mag) {
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

    public enum PurchaseDetailStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),BUYING(2,"正在采购"),
        FINISHED(3,"已完成"),HASERED(4,"采购失败");

        private int code;
        private String mag;

        PurchaseDetailStatusEnum(int code, String mag) {
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
