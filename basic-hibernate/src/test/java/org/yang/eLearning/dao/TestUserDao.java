package org.yang.eLearning.dao;

import java.io.IOException;
import java.sql.SQLException;

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

public class TestUserDao extends AbstractDbUnitTestCase{

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
	public void testSelect() throws DatabaseUnitException,SQLException,IOException{
		IDataSet ds=createDateSet("tb_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		User u=userDao.select(1);
		//Assert.assertEquals(new User(1, "admin1"), u);
		EntitiesHelper.assertUser(u);
	}
	
	/*@Test(expected=ObjectNotFoundException.class)
	public void testDelete() throws DatabaseUnitException,SQLException,IOException{
		IDataSet ds = createDateSet("tb_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		userDao.delete(1);
		User tu=userDao.select(1);
		System.out.println(tu.getName());
	}*/
	@Test
	public void testInsert() throws DatabaseUnitException,SQLException,IOException{
		IDataSet ds=createDateSet("tb_user");
		DatabaseOperation.TRUNCATE_TABLE.execute(dbunitCon, ds);
		User t =new User();
		t.setName("zs");
		User u=userDao.add(t);
		//User sUser=userDao.select(100);
		System.out.println(u);
		//Assert.assertEquals(sUser, t);
		//EntitiesHelper.assertUser(u);
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
