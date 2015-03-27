package org.yang.eLearning.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.yang.eLearning.model.User;
import org.yang.eLearning.util.AbstractDbUnitTestCase;
import org.yang.eLearning.util.EntitiesHelper;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

//指定使用的单元测试执行类，这里我们使用SpringJunit4ClassRunner.class类
@RunWith(SpringJUnit4ClassRunner.class)
//指定Spring的配置文件路径，可以指定多个，用逗号隔开；
@ContextConfiguration("/beans.xml")
//指定测试类之前，要执行的操作：DependencyInjectionTestExecutionListener.class可以实现测试类之前的依赖注入
@TestExecutionListeners({DbUnitTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class})

public class TestUserDao2 extends AbstractDbUnitTestCase{

	@Inject
	private SessionFactory sessionFactory;
	@Inject
	private IUserDao userDao;

	@Before
	public void setUp() throws DataSetException,SQLException,IOException{
		Session s =sessionFactory.openSession();
		//把当前线程与事物绑定
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
		//备份数据库的表，建议只备份单元测试用到的表
		bakcupOneTable("tb_user");
	}

	@Test
	public void testList() throws DatabaseUnitException,SQLException,IOException {
		IDataSet ds=createDateSet("tb_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		String hql="from User u where u.id=? and u.name= :name";
		Object[] args=new Object[]{1};
		Map<String, Object> alias=new HashMap();
		alias.put("name", "admin1");
		List<User> list=userDao.list(hql, args, alias);
		Assert.assertEquals(list.get(0).getName(), "admin1");
	}
	
	
	@After
	public void tearDown() throws DatabaseUnitException,SQLException,IOException{
		SessionHolder holder=(SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		Session s=holder.getSession();
		s.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		//恢复数据
		resumesTable();
	}
	
	
}
