package com.haige.gulimall.search.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @className: com.haige.gulimall.search.web-> IndexController
 * @description:
 * @author: cqh
 * @createDate: 2021-12-17 17:59
 * @version: 1.0
 * @todo:
 */
@Controller
public class IndexController {

    @GetMapping({"/","/index.html"})
    public String indexPage(){
        return "list";
    }
}
