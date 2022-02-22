package com.haoo.iframe.controller.statements;


import com.alibaba.fastjson.JSON;
import com.haoo.iframe.common.vo.ExcelVO;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.service.DemoService;
import com.haoo.iframe.util.BeanCopyUtils;
import com.haoo.iframe.util.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "报表相关")
@RestController
@RequestMapping("/api/report/")
public class ReportController {

    private final DemoService demoService;

    @Autowired
    public ReportController(DemoService demoService) {
        this.demoService = demoService;
    }

    @ApiOperation("下载报表")
    @GetMapping(value = "/downloadReport")
    public void downloadReport(HttpServletResponse response) {

        List<ExcelVO> list = JSON.parseArray("[{'name':'v1-name','sex':'v1-sex'},{'name':'v2-name','sex':'v2-sex'},{'name':'v3-name','sex':'v3-sex'}]"
                , ExcelVO.class);
        ExcelUtils.writeExcel(response, list, ExcelVO.class, "生成的报表.xlsx");
    }

    @ApiOperation("保存报表")
    @PostMapping(value = "/saveExcel")
    public void saveExcel(@RequestParam("file") MultipartFile multipartFiles) {
        List<ExcelVO> list = ExcelUtils.readExcel(ExcelVO.class, multipartFiles);
        List<Demo> saveList = BeanCopyUtils.copy(list, Demo.class);
        demoService.saveBatch(saveList);
    }
}
