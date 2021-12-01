package com.haige.gulimall.ware.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @className: com.haige.gulimall.product.handler-> MyMetaObjectHandler
 * @description: 创建时间和更新时间自动添加
 * @author: cqh
 * @createDate: 2021-11-19 20:06
 * @version: 1.0
 * @todo:
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时的填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 更新时的填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}