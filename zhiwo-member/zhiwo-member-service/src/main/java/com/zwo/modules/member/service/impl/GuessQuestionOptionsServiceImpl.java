/**
* 
*/
package com.zwo.modules.member.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.zwo.modules.member.dao.GuessQuestionOptionsMapper;
import com.zwo.modules.member.domain.GuessQuestionOptions;
import com.zwo.modules.member.domain.GuessQuestionOptionsCriteria;
import com.zwo.modules.member.service.IGuessQuestionOptionsService;
import com.zwotech.modules.core.service.impl.BaseService;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author hjx
 *
 */
@Service
@Lazy(true)
@Transactional(readOnly = false)
public class GuessQuestionOptionsServiceImpl extends BaseService<GuessQuestionOptions> implements IGuessQuestionOptionsService {
	private static Logger logger = LoggerFactory.getLogger(GuessQuestionOptionsServiceImpl.class);

	private static final String BASE_MESSAGE = "【GuessQuestionOptionsServiceImpl服务类提供的基础操作增删改查等】";

	@Autowired
	@Lazy(true)
	private GuessQuestionOptionsMapper guessQuestionOptionsMapper;

	@Override
	public Mapper<GuessQuestionOptions> getBaseMapper() {
		return guessQuestionOptionsMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#insertBatch(java.util.List)
	 */
	/*
	 * @Override public int insertBatch(List<GuessQuestionOptions> list) { // TODO
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
	@CacheEvict(value = "GuessQuestionOptions", allEntries = true)
	public int deleteByExample(Object example) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByExample批量删除开始");

		// 逻辑操作
		int result = guessQuestionOptionsMapper.deleteByExample(example);

		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteByExample批量删除结束");
		return result;
	}

	@CacheEvict(value = "GuessQuestionOptions", allEntries = true)
	// @Override
	public int deleteBatch(List<String> list) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteBatch批量删除开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "deleteBatch批量删除ID为：" + list.toString());

		// 逻辑操作
		GuessQuestionOptionsCriteria guessQuestionOptionsCriteria = new GuessQuestionOptionsCriteria();
		guessQuestionOptionsCriteria.createCriteria().andIdIn(list);
		int result = guessQuestionOptionsMapper.deleteByExample(guessQuestionOptionsCriteria);

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
	@CacheEvict(value = "GuessQuestionOptions", key = "#id+''")
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
//	@CachePut(value = "GuessQuestionOptions", key = "#record.id")
	public int insert(GuessQuestionOptions record) {
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
//	@CachePut(value = "GuessQuestionOptions", key = "#record.id")
	public int insertSelective(GuessQuestionOptions record) {
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
	public List<GuessQuestionOptions> selectByExample(Object example) {
		return guessQuestionOptionsMapper.selectByExample(example);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#selectByPrimaryKey(java.
	 * lang.String)
	 */
	@Override
	@Cacheable(key = "#id+''", value = "GuessQuestionOptions")
	@Transactional(readOnly = true)
	public GuessQuestionOptions selectByPrimaryKey(String id) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "selectByPrimaryKey查询开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "selectByPrimaryKey查询参数为：" + id);

		// 逻辑操作
		GuessQuestionOptions guessQuestionOptions = super.selectByPrimaryKey(id);
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "selectByPrimaryKey查询结束");
		return guessQuestionOptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zwotech.modules.core.service.IBaseService#updateByExampleSelective(
	 * java.lang.Object, java.lang.Object)
	 */
	@CacheEvict(value = "GuessQuestionOptions", allEntries = true)
	@Override
	public int updateByExampleSelective(GuessQuestionOptions record, Object example) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExampleSelective更新开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExampleSelective更新条件对象为：" + record.toString());

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
	@CacheEvict(value = "GuessQuestionOptions", allEntries = true)
	public int updateByExample(GuessQuestionOptions record, Object example) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExample更新开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExample更新对象为：" + record.toString());

		// 逻辑操作
		int result = super.updateByExample(record, example);
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByExample更新结束");
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
	@CacheEvict(value = "GuessQuestionOptions", key = "#record.id")
	public int updateByPrimaryKeySelective(GuessQuestionOptions record) {
		// 日志记录
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKeySelective更新开始");
		if (logger.isInfoEnabled())
			logger.info(BASE_MESSAGE + "updateByPrimaryKeySelective更新对象为：" + record.toString());

		// 逻辑操作
		int result = this.guessQuestionOptionsMapper.updateByPrimaryKeySelective(record);
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
	@CacheEvict(value = "GuessQuestionOptions", key = "#record.id")
	public int updateByPrimaryKey(GuessQuestionOptions record) {
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
	public PageInfo<GuessQuestionOptions> selectByPageInfo(Object example, PageInfo<GuessQuestionOptions> pageInfo) {
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
	public List<GuessQuestionOptions> selectByQuestionId(String questionId) {
		GuessQuestionOptionsCriteria optionsCriteria = new GuessQuestionOptionsCriteria();
		optionsCriteria.createCriteria().andGuessQuestionIdEqualTo(questionId);
		List<GuessQuestionOptions> list = guessQuestionOptionsMapper.selectByExample(optionsCriteria);
		return list;
	}

}
