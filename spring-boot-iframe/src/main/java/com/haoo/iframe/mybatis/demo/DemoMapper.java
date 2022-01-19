package com.haoo.iframe.mybatis.demo;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoo.iframe.entity.Demo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemoMapper extends BaseMapper<Demo> {

    List<Demo> findPage();

}
