package com.hananoq.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :花のQ
 * @since 2020/11/23 19:45
 **/
@RestController
public class OrderController {

    /**
     * The precondition of accessing this interface is having an authority called p1
     *
     * @return string
     */
    @GetMapping("r1")
    @PreAuthorize("hasAnyAuthority('p1')") // 拥有p1权限才可访问此url
    public String r1() {
        return "fuck you";
    }


    @GetMapping("r2")
    public String r2() {
        return "三天之内撒了你";
    }
}
