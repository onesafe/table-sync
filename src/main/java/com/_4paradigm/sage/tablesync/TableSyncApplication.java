package com._4paradigm.sage.tablesync;

import com._4paradigm.sage.tablesync.manager.CanalManager;
import com._4paradigm.sage.tablesync.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootApplication
@Slf4j
public class TableSyncApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(TableSyncApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// showData();
		CanalManager.processData(jdbcTemplate);
	}

//	private void showData() {
//		jdbcTemplate.queryForList("SELECT * FROM user")
//				.forEach(row -> log.info(row.toString()));
//	}
}
