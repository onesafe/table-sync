package com._4paradigm.sage.tablesync;

import com._4paradigm.sage.tablesync.manager.CanalManager;
import com._4paradigm.sage.tablesync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TableSyncApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(TableSyncApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CanalManager.processData(userService);
	}
}
