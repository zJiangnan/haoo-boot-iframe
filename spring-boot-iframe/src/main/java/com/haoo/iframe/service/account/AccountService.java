package com.haoo.iframe.service.account;

import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.vo.UserPermissionsRelationVo;
import com.haoo.iframe.vo.UserRoleRelationVo;
import com.haoo.iframe.vo.UserVo;

public interface AccountService {

    ReturnEntity findUsers(UserVo userVo);

    void addUser(UserVo userVo);

    UserVo loginUser(String loginUser);

    void bindUserRights(UserPermissionsRelationVo uprVo);

    void bindUserRole(UserRoleRelationVo urrVo);

}
