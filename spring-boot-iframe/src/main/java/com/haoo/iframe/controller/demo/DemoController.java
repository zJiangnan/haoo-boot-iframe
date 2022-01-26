package com.haoo.iframe.controller.demo;

import com.haoo.iframe.util.RestTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 * <p></p>
 *
 * @author pluto
 * @version 1.0
 * @see DemoController
 **/
@RestController
@RequestMapping("/t")
@Api(tags = "测试接口")
public class DemoController {

    @ApiOperation("测试")
    @GetMapping("/test")
    public String test() {
        try {
            ResponseEntity<String> post = RestTemplateUtils.post("http://echo:8080/t/test1", "", new HttpHeaders());
            System.out.println(post);
            System.out.println(post.getBody());
            return "完成";
        } catch (Exception e) {
            e.printStackTrace();
            return "失败";
        }
    }

    @ApiOperation("测试1")
    @PostMapping("/test1")
    public String test1() {
        return "你好！";
    }

}
