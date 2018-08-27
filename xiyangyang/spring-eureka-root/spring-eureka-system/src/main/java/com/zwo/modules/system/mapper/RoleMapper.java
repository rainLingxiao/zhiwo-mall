package com.zwo.modules.system.mapper;

import com.zwo.modules.system.domain.Role;
import com.zwo.modules.system.domain.RoleCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    long countByExample(RoleCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int deleteByExample(RoleCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int insert(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int insertSelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    List<Role> selectByExample(RoleCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    Role selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int updateByExample(@Param("record") Role record, @Param("example") RoleCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated Mon Aug 27 16:43:03 CST 2018
     */
    int updateByPrimaryKey(Role record);
}