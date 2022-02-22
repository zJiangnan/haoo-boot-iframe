package com.haoo.iframe.common.vo;

import com.haoo.iframe.common.annotation.ExcelColumn;
import lombok.Data;

@Data
public class ExcelVO {
    @ExcelColumn(value = "name",col = 1)
    private String name;
    @ExcelColumn(value = "sex",col = 2)
    private String sex;
}
