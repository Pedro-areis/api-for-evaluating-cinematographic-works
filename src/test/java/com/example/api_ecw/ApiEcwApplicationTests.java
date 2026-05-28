package com.example.api_ecw;

import com.example.api_ecw.tmdb_api.GenreCacheService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ApiEcwApplicationTests {
	@MockitoBean
	private GenreCacheService genreCacheService;

	@Test
	void contextLoads() {
	}

}
