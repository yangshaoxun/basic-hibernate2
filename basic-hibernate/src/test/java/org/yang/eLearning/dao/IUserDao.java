package org.yang.eLearning.dao;

import java.util.List;
import java.util.Map;

import org.yang.eLearning.model.Pager;
import org.yang.eLearning.model.User;

public interface IUserDao extends IBaseDao<User>{
	
	/*
	 * 根据hql的查询，使用参数对象，别名查询
	 */
	public List<User> list(String hql, Object[] args, Map<String, Object> alias);
	
	/*
	 * 使用参数查询
	 */
	public List<User> listByParameter(String hql, Object[] args);
	
	/*
	 * 基于别名的查询
	 */
	public List<User> listByAlias(String hql,Map<String, Object> alias);
	
	/*
	 * 基于单个参数对象的查询
	 */
	public List<User> list(String hql,Object obj);
	
	/*
	 * 实现分页查找的方法
	 */
	public Pager<User> find(String hql,Object[] args,Map<String, Object> alias);
	
}
