package com.haige.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @className: com.haige.gulimall.product.config-> ThreadPoolConfigProperties
 * @description:
 * @author: cqh
 * @createDate: 2022-01-07 16:21
 * @version: 1.0
 * @todo:
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;

}
