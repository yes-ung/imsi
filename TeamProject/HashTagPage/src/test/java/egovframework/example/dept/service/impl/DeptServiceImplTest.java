package egovframework.example.dept.service.impl;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import egovframework.example.common.Criteria;
import egovframework.example.dept.service.DeptService;
import egovframework.example.dept.service.DeptVO;
import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
      "classpath:/egovframework/spring/context-aspect.xml",
       "classpath:/egovframework/spring/context-common.xml",
       "classpath:/egovframework/spring/context-datasource.xml",
       "classpath:/egovframework/spring/context-idgen.xml",
       "classpath:/egovframework/spring/context-mapper.xml",
       "classpath:/egovframework/spring/context-sqlMap.xml",
       "classpath:/egovframework/spring/context-transaction.xml"
   })
@Log4j2
class DeptServiceImplTest {
	
    @Autowired
	DeptService deptService;
    
	@Test
	void testSelectDeptList() {
		//여기서 각 메소드별 테스트진행 : 전체조회
		// 1) 테스트 조건
		Criteria criteria = new Criteria();
		criteria.setSearchKeyword("");
		criteria.setFirstIndex(0);  //1페이지(0)
		criteria.setPageUnit(3);    // 화면에 보일 개수
		// 2) 실제 메소드 실행
		List<?> list = deptService.selectDeptList(criteria);
		// 3) 검증(확인) : 로그 , DB 확인 , assert~
		log.info(list);
	}
	@Test
	void testSelectDeptListTotCnt() {
		//여기서 각 메소드별 테스트진행 : 행 개수 구하기
				// 1) 테스트 조건
				Criteria criteria = new Criteria();
				criteria.setSearchKeyword("");
				
				// 2) 실제 메소드 실행
				int count = deptService.selectDeptListTotCnt(criteria);
				// 3) 검증(확인) : 로그 , DB 확인 , assert~
				log.info(count);
	}
	@Test
	void testInsert() {
//		1) 테스트 조건 : DeptVO(dno,dname,loc)
		DeptVO deptVO = new DeptVO(0,"개발부","부산");
//		2) 실제 메소드실행
		deptService.insert(deptVO);
//		3) 검증(확인) : 로그 , DB 확인 ,assert~
	}
	@Test
	void testSelectDept() {
//		1) 테스트 조건(given) : 
		int dno=10;
//		2) 실제 메소드실행(when)
		DeptVO deptVO = deptService.selectDept(dno);
//		3) 검증(확인)(then) : 로그 , DB 확인 ,assert~
		log.info(deptVO);
	}
	@Test
	void testUpdate() {
//		1) 테스트 조건 : DeptVO(dno,dname,loc)
		DeptVO deptVO = new DeptVO(90,"개발부","울산");
//		2) 실제 메소드실행
		deptService.update(deptVO);
//		3) 검증(확인) : 로그 , DB 확인 ,assert~
	}
	@Test
	void testDelete() {
//		1) 테스트 조건 : DeptVO(dno,dname,loc)
		DeptVO deptVO = new DeptVO();
		deptVO.setDno(220);
//		2) 실제 메소드실행
		deptService.delete(deptVO);
//		3) 검증(확인) : 로그 , DB 확인 ,assert~
	}

}
