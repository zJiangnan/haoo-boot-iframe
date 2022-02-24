package com.haoo.iframe.controller.account;

import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.service.account.AccountService;
import com.haoo.iframe.vo.UserPermissionsRelationVo;
import com.haoo.iframe.vo.UserRoleRelationVo;
import com.haoo.iframe.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "账户相关")
@RestController
@RequestMapping("/api/account/")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation("用户列表查询")
    @PostMapping(value = "/users")
    public ReturnEntity users(@RequestBody @Validated UserVo user) {
        return accountService.findUsers(user);
    }

    @ApiOperation("添加用户")
    @PostMapping(value = "/addUser")
    public void addUser(@RequestBody UserVo user) {
        accountService.addUser(user);
    }

    @ApiOperation("用户权限绑定")
    @PostMapping(value = "/bindUserRights")
    public void bindUserRights(@RequestBody @Validated UserPermissionsRelationVo upr) {
        accountService.bindUserRights(upr);
    }

    @ApiOperation("用户角色绑定")
    @PostMapping(value = "/bindUserRole")
    public void bindUserRole(@RequestBody @Validated UserRoleRelationVo urrVo) {
        accountService.bindUserRole(urrVo);
    }

}
