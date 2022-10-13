package org.zerock.mapper;

import static org.junit.Assert.fail;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class DataSourceTests {
	// Setter로 DataSource를 주입 받는다.
	@Setter(onMethod_=@Autowired)
	private DataSource dataSource;
	
	// setter로 SqlSessiobFactory를 주입 받는다.
	@Setter(onMethod_=@Autowired)
	private SqlSessionFactory sqlSessionFactory;
	
	@Test	// 테스를 실행하지 않겠다는 설정 -< 코멘트로 막으면
	public void testConnection() {
		try (Connection conn = dataSource.getConnection()){
			log.info(conn);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	// MyBatis를 사용해서 Connection 객체를 가져옴
	@Test
	public void testMyBatis() {
		try (SqlSession session = sqlSessionFactory.openSession();
				Connection conn = session.getConnection()) {
			log.info(session);
			log.info(conn);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
}
