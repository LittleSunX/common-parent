package com.czxy.bos.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    /**
     * 访问根路径下的内容
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public String home(@PathVariable("page") String page ){
        return page;
    }

    /**
     * 访问pages目录下的html资源
     * @param dir
     * @param page
     * @return
     */
    @RequestMapping("/pages/{dir}/{page}")
    public String pages(@PathVariable("dir") String dir , @PathVariable("page") String page ){
        return "/pages/"+dir+"/" + page;
    }


}
