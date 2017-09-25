/**
 * 
 */
package com.zwo.modules.system.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.common.Mapper;

import com.github.pagehelper.PageInfo;
import com.zwo.modules.system.dao.TbResourcesMapper;
import com.zwo.modules.system.dao.TbRoleMapper;
import com.zwo.modules.system.dao.TbRoleResourcesMapper;
import com.zwo.modules.system.domain.TbResources;
import com.zwo.modules.system.domain.TbRole;
import com.zwo.modules.system.domain.TbRoleCriteria;
import com.zwo.modules.system.domain.TbRoleResources;
import com.zwo.modules.system.domain.TbRoleResourcesCriteria;
import com.zwo.modules.system.domain.TbUserGroup;
import com.zwo.modules.system.service.ITbRoleService;
import com.zwotech.common.utils.RedisUtil;
import com.zwotech.common.utils.SpringContextHolder;
import com.zwotech.modules.core.service.impl.BaseService;

/**
 * @author hjx
 *
 */
@Service
@Lazy(true)
@Transactional(readOnly = false)
public class RoleServiceImpl extends BaseService<TbRole> implements ITbRoleService {
	private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	private static final String BASE_MESSAGE = "【TbRoleServiceImpl服务类提供的基础操作增删改查等】";

	public static final String KEY_TBROLE = "_key_tbRole";
	
	@Autowired
	@Lazy(true)
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;
	
	@Autowired
	@Lazy(true)
	private TbRoleMapper roleMapper;
	
	@Autowired
	@Lazy(true)
	private TbResourcesMapper resourcesMapper;
	
	@Autowired
	@Lazy(true)
	private TbRoleResourcesMapper roleResourcesMapper;

	@Override
	public Mapper<TbRole> getBaseMapper() {
		return roleMapper;
	}

	public RoleServiceImpl() {
		super();
		if (SpringContextHolder.getApplicationContext().containsBean(
				"redisTemplate")) {
			redisTemplate = SpringContextHolder.getBean("redisTemplate");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#insertBatch(java.util.List)
	 */
	/*
	 * @Override public int insertBatch(List<TbRole> list) { // TODO
	 * Auto-generated method stub return 0; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#countByExample(java.lang.
	 * Object)
	 */
	/*
	 * @Override public int countByExample(Object example) { // TODO
	 * Auto-generated method stub return 0; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#deleteByExample(java.lang.
	 * Object)
	 */
	@Override
	public int deleteByExample(Object example) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByExample批量删除开始");
		List<TbRole> roles = this.selectByExample(example);
		for (TbRole role : roles) {
			RedisUtil.removeRedisKey(redisTemplate, role.getId()+KEY_TBROLE);
		}
		// 逻辑操作
		int result = roleMapper.deleteByExample(example);

		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByExample批量删除结束");
		return result;
	}

	@Override
	public int deleteBatch(List<String> list) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteBatch批量删除开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteBatch批量删除ID为：" + list.toString());

		// 逻辑操作
		TbRoleCriteria roleCriteria = new TbRoleCriteria();
		roleCriteria.createCriteria().andIdIn(list);
		List<TbRole> roles = this.selectByExample(roleCriteria);
		for (TbRole role : roles) {
			RedisUtil.removeRedisKey(redisTemplate, role.getId()+KEY_TBROLE);
		}
		
		int result = roleMapper.deleteByExample(roleCriteria);

		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteBatch批量删除结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#deleteByPrimaryKey(java.
	 * lang.String)
	 */
	@Override
	@CacheEvict(value = "TbRole", key="#id+'_key_tbRole'")
	public int deleteByPrimaryKey(String id) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByPrimaryKey删除开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByPrimaryKey删除ID为：" + id.toString());

		// 逻辑操作
		int result = super.deleteByPrimaryKey(id);

		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByPrimaryKey删除结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#insert(java.lang.Object)
	 */
	@Override
//	@CachePut(value = "TbRole", key = "#record.id+'_key_tbRole'")
	public int insert(TbRole record) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "insert插入开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "insert插入对象为：" + record.toString());

		// 如果数据没有设置id,默认使用时间戳
		if (null == record.getId() || "".equals(record.getId())) {
			record.setId(System.currentTimeMillis() + "" + Math.round(Math.random() * 99));
		}
		int result = super.insert(record);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "insert插入结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#insertSelective(java.lang.
	 * Object)
	 */

	@Override
//	@CachePut(value = "TbRole", key = "#record.id+'_key_tbRole'")
	public int insertSelective(TbRole record) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "insert插入开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "insert插入对象为：" + record.toString());

		// 如果数据没有设置id,默认使用时间戳
		if (null == record.getId() || "".equals(record.getId())) {
			record.setId(System.currentTimeMillis() + "" + Math.round(Math.random() * 99));
		}
		int result = super.insertSelective(record);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "insert插入结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#selectByExample(java.lang.
	 * Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TbRole> selectByExample(Object example) {
		return super.selectByExample(example);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#selectByPrimaryKey(java.
	 * lang.String)
	 */
	@Override
	@Cacheable(key = "#id+'_key_tbRole'", value = "TbRole")
	@Transactional(readOnly = true)
	public TbRole selectByPrimaryKey(String id) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "selectByPrimaryKey查询开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "selectByPrimaryKey查询参数为：" + id);

		// 逻辑操作
		TbRole role = super.selectByPrimaryKey(id);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "selectByPrimaryKey查询结束");
		return role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#updateByExampleSelective(
	 * java.lang.Object, java.lang.Object)
	 */
//	@CacheEvict(value = "TbRole", allEntries = true)
	@Override
	public int updateByExampleSelective(TbRole record, Object example) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExampleSelective更新开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExampleSelective更新条件对象为：" + record.toString());
		List<TbRole> roles = this.selectByExample(example);
		for (TbRole role : roles) {
			RedisUtil.removeRedisKey(redisTemplate, role.getId()+KEY_TBROLE);
		}
		// 逻辑操作
		int result = super.updateByExampleSelective(record, example);
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExampleSelective更新结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#updateByExample(java.lang.
	 * Object, java.lang.Object)
	 */
	@Override
//	@CacheEvict(value = "TbRole", allEntries = true)
	public int updateByExample(TbRole record, Object example) {
		//日志记录
		if(logger.isInfoEnabled())
			logger.info(BASE_MESSAGE+"updateByExample更新开始");
		if(logger.isInfoEnabled())
			logger.info(BASE_MESSAGE+"updateByExample更新对象为：" + record.toString());
		List<TbRole> roles = this.selectByExample(example);
		for (TbRole role : roles) {
			RedisUtil.removeRedisKey(redisTemplate, role.getId()+KEY_TBROLE);
		}								
		//逻辑操作		
		int result = super.updateByExample(record, example);
		//日志记录
		if(logger.isInfoEnabled())
			logger.info(BASE_MESSAGE+"updateByExample更新结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#updateByPrimaryKeySelective
	 * (java.lang.Object)
	 */
	@Override
	@CacheEvict(value = "TbRole", key="#record.id+'_key_tbRole'")
	public int updateByPrimaryKeySelective(TbRole record) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKeySelective更新开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKeySelective更新对象为：" + record.toString());

		// 逻辑操作
		int result = super.updateByPrimaryKeySelective(record);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKeySelective更新结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#updateByPrimaryKey(java.
	 * lang.Object)
	 */
	@Override
	@CacheEvict(value = "TbRole", key="#record.id+'_key_tbRole'")
	public int updateByPrimaryKey(TbRole record) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKey更新开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKey更新对象为：" + record.toString());

		// 逻辑操作
		int result = super.updateByPrimaryKey(record);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKey更新结束");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#selectByPageInfo(java.lang.
	 * Object, com.github.pagehelper.PageInfo)
	 */
	@Transactional(readOnly = true)
	@Override
	public PageInfo<TbRole> selectByPageInfo(Object example, PageInfo<TbRole> pageInfo) {
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "分页开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "分页参数：" + pageInfo.toString());
		pageInfo = super.selectByPageInfo(example, pageInfo);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "分页结束");
		return pageInfo;
	}

	@Override
	public void connectRoleResources(String resourcesId, String roleId) {
		TbRoleResources record = new TbRoleResources();
		record.setResourcesId(resourcesId);
		record.setRoleId(roleId);
		this.roleResourcesMapper.insert(record);
	}

	@Override
	public void unconnectRoleResources(String resourcesId, String roleId) {
		TbRoleResourcesCriteria example = new TbRoleResourcesCriteria();
		example.createCriteria().andResourcesIdEqualTo(resourcesId)
				.andRoleIdEqualTo(roleId);
		roleResourcesMapper.deleteByExample(example);
	}

	@Override
	public void batchConnectRoleResources(List<TbRoleResources> roleResources, String roleId) {
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "批量关联角色资源开始");
		for (TbRoleResources tbRoleResources : roleResources) {
			if(tbRoleResources.getId()==null){
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				tbRoleResources.setId(uuid);
			}
		}
		String sql = " INSERT INTO tb_role_resources (id,resources_id,role_id) VALUES (?,?,?)";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, roleResources.get(i).getId());
				ps.setString(2, roleResources.get(i).getResourcesId());
				ps.setString(3, roleId);
			}
			@Override
			public int getBatchSize() {
				return roleResources.size();
			}
		});
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "批量关联角色资源结束");
	}

	@Override
	public void batchUnconnectRoleResources(String roleId) {
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "批量解除角色资源开始");
		TbRoleResourcesCriteria roleResourcesCriteria = new TbRoleResourcesCriteria();
		roleResourcesCriteria.createCriteria().andRoleIdEqualTo(roleId);
		roleResourcesMapper.deleteByExample(roleResourcesCriteria);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "批量解除角色资源结束");
	}

	@Override
	@Transactional(readOnly = true)
	public List<TbResources> selectByRolename(String rolename) {
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "根据角色名进行查询资源开始");
		List<TbResources> list = resourcesMapper.selectByRolename(rolename);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "根据角色名进行查询资源结束,結果条目数为："+list.size());
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TbResources> selectByRoleId(String roleId) {
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "根据角色ID进行查询资源开始");
		List<TbResources> list = resourcesMapper.selectByRoleId(roleId);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "根据角色ID进行查询资源结束,結果条目数为："+list.size());
		return list;
	}

}
