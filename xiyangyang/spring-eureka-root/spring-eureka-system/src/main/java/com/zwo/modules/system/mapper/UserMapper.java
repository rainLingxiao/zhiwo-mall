package com.zwo.modules.system.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zwo.modules.core.mapper.BaseMapper;
import com.zwo.modules.system.domain.User;
import com.zwo.modules.system.vo.UserVo;

public interface UserMapper extends BaseMapper<User> {
	public Set<String> findRoles(@Param("username") String username);

	public Set<String> findPermissions(@Param("username") String username);

	public UserVo findByUsername(@Param("username") String username);
}