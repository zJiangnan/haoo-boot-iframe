package com.haoo.iframe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.entity.User;
import com.haoo.iframe.vo.UserVo;

public interface UserService extends IService<User> {

    ReturnEntity findUsers(UserVo userVo);

    void addUser(UserVo userVo);

}
