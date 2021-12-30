package com.haige.gulimall.search.web;

import com.haige.gulimall.search.service.MallSearchService;
import com.haige.gulimall.search.vo.SearchParam;
import com.haige.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * @className: com.haige.gulimall.search.web-> SearchController
 * @description:
 * @author: cqh
 * @createDate: 2021-12-17 18:12
 * @version: 1.0
 * @todo:
 */
@Controller
public class SearchController {

    @Autowired
    private MallSearchService mallSearchService;

    /**
     * 自动将前端传来的参数封装成searchParam
     * @param searchParam
     * @return
     */
    @GetMapping({"/list","list.html"})
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest  request){
        // 请求参数
        searchParam.set_queryString(request.getQueryString());
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result",result);
        return "list";
    }
}
