package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
@ApiModel(value = "用户表")
public class User {

    @TableId
    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "所属组织")
    private String orgId;

    @ApiModelProperty(value = "登录帐号")
    private String loginName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "创建时间")
    private String createdTime;

    @ApiModelProperty(value = "修改时间")
    private String modifiedTime;

    @ApiModelProperty(value = "登录次数")
    private long loginTimes;

}
