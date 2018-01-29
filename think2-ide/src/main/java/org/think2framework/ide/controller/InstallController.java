package org.think2framework.ide.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统安装控制器
 */
@RestController
@RequestMapping("/ide")
public class InstallController {

    @RequestMapping(value = "/install.do")
    public String list(HttpServletRequest request) {

        return "";
    }

}
