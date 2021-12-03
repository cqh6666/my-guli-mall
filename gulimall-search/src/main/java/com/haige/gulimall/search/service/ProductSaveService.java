package com.haige.gulimall.search.service;

import com.haige.common.entity.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @className: com.haige.gulimall.search.service-> ProductSaveService
 * @description:
 * @author: cqh
 * @createDate: 2021-12-02 16:32
 * @version: 1.0
 * @todo:
 */
public interface ProductSaveService {
    /**
     * 保存上架数据
     * @param skuEsModelList
     * @return
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModelList) throws IOException;
}
