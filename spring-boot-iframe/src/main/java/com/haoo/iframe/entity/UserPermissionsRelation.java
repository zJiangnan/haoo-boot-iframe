package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("user_permissions_relation")
@ApiModel(value = "用户权限关系表")
public class UserPermissionsRelation {

    @TableId
    @ApiModelProperty(value = "用户权限关系ID")
    private String upId;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "权限ID")
    private String pid;

    @ApiModelProperty(value = "权限类型")
    private String permissionsType;

}
