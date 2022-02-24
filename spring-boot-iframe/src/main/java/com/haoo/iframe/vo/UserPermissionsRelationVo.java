package com.haoo.iframe.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel(value = "用户权限关系表")
public class UserPermissionsRelationVo {

    @NonNull
    @ApiModelProperty(value = "用户ID")
    private String uid;

    @NonNull
    @ApiModelProperty(value = "权限ID")
    private String pid;

    @ApiModelProperty(value = "权限类型")
    private String permissionsType;

}
