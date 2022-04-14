package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("user_role_relation")
@ApiModel(value = "用户角色关系表")
public class UserRoleRelation {

    @TableId
    @ApiModelProperty(value = "用户角色ID")
    private String urId;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "角色ID")
    private String rid;

}
