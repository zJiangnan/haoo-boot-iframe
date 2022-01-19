package com.haoo.iframe.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("demo")
public class Demo {
    @TableId
    private String name;
    @TableField("sex")
    private String sex;
    @TableField("test_name")
    private String testName;

    public Demo(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public Demo() {}
}
