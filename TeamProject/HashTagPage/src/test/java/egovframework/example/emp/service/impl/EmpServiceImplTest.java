package egovframework.example.emp.service.impl;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import egovframework.example.common.Criteria;
import egovframework.example.dept.service.DeptVO;
import egovframework.example.emp.service.EmpService;
import egovframework.example.emp.service.EmpVO;
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
class EmpServiceImplTest {
	
	@Autowired
	EmpService empService;

	@Test
	void testSelectEmpList() {
		//여기서 각 메소드별 테스트진행 : 전체조회
				// 1) 테스트 조건
				Criteria criteria = new Criteria();
				criteria.setSearchKeyword("");
				criteria.setFirstIndex(0);
				criteria.setPageUnit(3);
				// 2) 실제 메소드 실행
				List<?> list = empService.selectEmpList(criteria);
				// 3) 검증(확인) : 로그 , DB 확인 , assert~
				log.info(list);
	}
	@Test
	void testSelectEmpListTotCnt() {
		//여기서 각 메소드별 테스트진행 : 총 행 갯수 구하기
		// 1) 테스트 조건
		Criteria criteria = new Criteria();
		criteria.setSearchKeyword("");
		
		// 2) 실제 메소드 실행
		int count = empService.selectEmpListTotCnt(criteria);
		// 3) 검증(확인) : 로그 , DB 확인 , assert~
		log.info(count);
	}
	@Test
	void testInsert() {
//		1) 테스트 조건 : 
		EmpVO empVO = new EmpVO();
		empVO.setEname("고길동");
		empVO.setJob("부장");
		empVO.setManager(8000);
		empVO.setHiredate("2025-06-24");
		empVO.setSalary(5000);
		empVO.setCommission(1000);
		empVO.setDno(10);
//		2) 실제 메소드 실행
		empService.insert(empVO);
//		3) 검증(확인) : 로그, DB 확인, assert~ (DB확인)
		
		EmpVO empVO2 = new EmpVO(0,"김철수","대장",9999,"2025-06-24",9999,3333,20);
		empService.insert(empVO2);
		
	}
	@Test
	void testSelectEmp() {
//		1) 테스트 조건(given) : 
		int eno=8000;
//		2) 실제 메소드실행(when)
		EmpVO empVO = empService.selectEmp(eno);
//		3) 검증(확인)(then) : 로그 , DB 확인 ,assert~
		log.info(empVO);
	}
	@Test
	void testUpdate() {
//		1) 테스트 조건 : 
		EmpVO empVO = new EmpVO();
		empVO.setEno(8021);
		empVO.setEname("곰곰곰");
		empVO.setJob("부장");
		empVO.setManager(8000);
		empVO.setHiredate("2025-06-24");
		empVO.setSalary(5000);
		empVO.setCommission(1000);
		empVO.setDno(10);
//		2) 실제 메소드 실행
		empService.update(empVO);
//		3) 검증(확인) : 로그, DB 확인, assert~ (DB확인)
	}
	@Test
	void testDelete() {
//		1) 테스트 조건(given) : 
		EmpVO empVO = new EmpVO();
		empVO.setEno(8024);
//		2) 실제 메소드실행(when)
		empService.delete(empVO);                  
//		3) 검증(확인)(then) : 로그 , DB 확인 ,assert~
		log.info(empVO);	}

}
