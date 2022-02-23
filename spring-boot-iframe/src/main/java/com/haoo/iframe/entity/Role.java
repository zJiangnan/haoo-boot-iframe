package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("role")
@ApiModel(value = "角色表")
public class Role {

    @TableId
    @ApiModelProperty(value = "角色ID")
    private String rid;

    @ApiModelProperty(value = "父级角色ID")
    private String parentRid;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "修改时间")
    private Date modifiedTime;

    @ApiModelProperty(value = "角色描述")
    private String description;

}
