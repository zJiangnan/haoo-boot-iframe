package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("role_permissions_relation")
@ApiModel(value = "角色权限关系表")
public class RolePermissionsRelation {

    @TableId
    @ApiModelProperty(value = "角色权限关系ID")
    private String rpId;

    @ApiModelProperty(value = "角色ID")
    private String rid;

    @ApiModelProperty(value = "权限ID")
    private String pid;

    @ApiModelProperty(value = "权限类型")
    private String permissionsType;

}
