package com.haige.gulimall.product.web;

import com.haige.gulimall.product.entity.CategoryEntity;
import com.haige.gulimall.product.service.CategoryService;
import com.haige.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @className: com.haige.gulimall.product.web-> IndexController
 * @description:
 * @author: cqh
 * @createDate: 2021-12-03 15:17
 * @version: 1.0
 * @todo:
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){

        // 查出所有一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevelOneCategories();

        // 视图解析器 classpath:/templates/ + 返回值 + .html
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/json/catalog.json")
    public Map<String, List<Catelog2Vo>> getCategoryJson(){
        return categoryService.getCategoryJson();
    }


}
