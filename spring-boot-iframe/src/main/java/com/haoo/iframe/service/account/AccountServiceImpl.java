package com.haoo.iframe.service.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.common.enums.ApiCode;
import com.haoo.iframe.common.system.MD5;
import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.entity.User;
import com.haoo.iframe.entity.UserPermissionsRelation;
import com.haoo.iframe.entity.UserRoleRelation;
import com.haoo.iframe.mybatis.mapper.UserMapper;
import com.haoo.iframe.mybatis.mapper.UserPermissionsRelationMapper;
import com.haoo.iframe.mybatis.mapper.UserRoleRelationMapper;
import com.haoo.iframe.service.entity.UserService;
import com.haoo.iframe.util.BeanCopyUtils;
import com.haoo.iframe.util.RedisUtils;
import com.haoo.iframe.vo.UserPermissionsRelationVo;
import com.haoo.iframe.vo.UserRoleRelationVo;
import com.haoo.iframe.vo.UserVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final UserService userService;

    private final RedisUtils redisUtils;

    private final UserMapper userMapper;

    private final UserPermissionsRelationMapper userPermissionsRelationMapper;
    private final UserRoleRelationMapper userRoleRelationMapper;

    public AccountServiceImpl(UserService userService, RedisUtils redisUtils,
                              UserMapper userMapper, UserPermissionsRelationMapper userPermissionsRelationMapper,
                              UserRoleRelationMapper userRoleRelationMapper) {
        this.userService = userService;
        this.redisUtils = redisUtils;
        this.userMapper = userMapper;
        this.userPermissionsRelationMapper = userPermissionsRelationMapper;
        this.userRoleRelationMapper = userRoleRelationMapper;
    }

    @Override
    public ReturnEntity findUsers(UserVo userVo) {
        //分页
        PageHelper.startPage(userVo.getPageNum(), userVo.getPageSize());
        //条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userVo.getOrgId()), "org_id", userVo.getOrgId());
        wrapper.like(StringUtils.isNotBlank(userVo.getLoginName()), "login_name", userVo.getLoginName());
        wrapper.like(StringUtils.isNotBlank(userVo.getMobile()), "mobile", userVo.getMobile());
        wrapper.like(StringUtils.isNotBlank(userVo.getEmail()), "email", userVo.getEmail());
        //User 转 UserVO
        List<UserVo> list = BeanCopyUtils.copy(userMapper.selectList(wrapper), UserVo.class);
        return new ReturnEntity(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), new PageInfo<>(list));
    }

    @Override
    public void addUser(UserVo userVo) {
        User user = BeanCopyUtils.copy(userVo, User.class);
        //password通过md5加密存储
        userVo.setPassword(MD5.getMD5String(userVo.getPassword()));
        userMapper.insert(user);
        redisUtils.set(user.getLoginName(), user);
    }

    @Override
    public UserVo loginUser(String loginUser) {
        //先查redis用户信息
        User user = (User) redisUtils.get(loginUser);
        //没有则到数据库
        if (ObjectUtils.isEmpty(user)) {
            user = userMapper.selectOne(new QueryWrapper<User>().eq("login_name", loginUser));
            return ObjectUtils.isEmpty(user) ? null : BeanCopyUtils.copy(user, UserVo.class);
        }
        return BeanCopyUtils.copy(user, UserVo.class);
    }

    @Override
    public void bindUserRights(UserPermissionsRelationVo uprVo) {
        UserPermissionsRelation upr = BeanCopyUtils.copy(uprVo, UserPermissionsRelation.class);
        userPermissionsRelationMapper.insert(upr);
    }

    @Override
    public void bindUserRole(UserRoleRelationVo urrVo) {
        UserRoleRelation uur = BeanCopyUtils.copy(urrVo,UserRoleRelation.class);
        userRoleRelationMapper.insert(uur);
    }

}
