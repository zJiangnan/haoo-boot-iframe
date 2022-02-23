package com.haoo.iframe.common.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@Data
public class BasePage {

    @ApiModelProperty(value = "当前页")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "显示条数")
    private Integer pageSize = 10;

}
