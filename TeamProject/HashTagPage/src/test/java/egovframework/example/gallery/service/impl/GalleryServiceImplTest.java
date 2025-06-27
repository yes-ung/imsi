package egovframework.example.gallery.service.impl;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import egovframework.example.common.Criteria;
import egovframework.example.gallery.service.GalleryService;
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
class GalleryServiceImplTest {
	
	@Autowired
	private GalleryService galleryService;

	@Test
	void testSelectGalleryList() {
		//여기서 각 메소드별 테스트진행 : 전체조회
		// 1) 테스트 조건
		Criteria criteria = new Criteria();
		criteria.setSearchKeyword("");
		criteria.setFirstIndex(0);  //1페이지(0)
		criteria.setPageUnit(3);    // 화면에 보일 개수
		// 2) 실제 메소드 실행
		List<?> list = galleryService.selectGalleryList(criteria);
		// 3) 검증(확인) : 로그 , DB 확인 , assert~
		log.info(list);
	}

}
