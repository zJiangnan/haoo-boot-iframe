package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("role")
@ApiModel(value = "权限表")
public class Permissions {

    @TableId
    @ApiModelProperty(value = "权限ID")
    private String pid;

    @ApiModelProperty(value = "父权限ID")
    private String parentPid;

    @ApiModelProperty(value = "权限名称")
    private String permissionsName;

    @ApiModelProperty(value = "角色描述")
    private String description;

}
