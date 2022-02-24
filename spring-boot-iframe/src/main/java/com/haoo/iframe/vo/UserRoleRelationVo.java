package com.haoo.iframe.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户角色关系表")
public class UserRoleRelationVo {

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "角色ID")
    private String rid;

}
