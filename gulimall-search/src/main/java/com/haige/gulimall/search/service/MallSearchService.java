package com.haige.gulimall.search.service;

import com.haige.gulimall.search.vo.SearchParam;
import com.haige.gulimall.search.vo.SearchResult;

/**
 * @className: com.haige.gulimall.search.service-> MallSearchService
 * @description:
 * @author: cqh
 * @createDate: 2021-12-17 18:34
 * @version: 1.0
 * @todo:
 */

public interface MallSearchService {
    SearchResult search(SearchParam searchParam);
}
