package com._4paradigm.sage.tablesync;

import com._4paradigm.sage.tablesync.config.Constants;
import com._4paradigm.sage.tablesync.entity.UserDMO;
import com._4paradigm.sage.tablesync.service.BinlogService;
import com._4paradigm.sage.tablesync.service.UserService;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.List;

@SpringBootApplication
@Slf4j
public class TableSyncApplication {

	@Autowired
	private UserService userService;

	public static TableSyncApplication tableSyncApplication;

	@PostConstruct  //在初始化的时候初始化静态对象和它的静态成员变量bean对象，静态存储下来，防止被释放
	public void init() {
		tableSyncApplication = this;
		tableSyncApplication.userService = this.userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TableSyncApplication.class, args);

		CanalConnector connector = CanalConnectors.newSingleConnector(
				new InetSocketAddress(Constants.canalUrl, Constants.canalPort),
				"example",
				"",
				""
		);

		int batchSize = 1000;

		try {
			connector.connect();
			connector.subscribe(".*\\..*");
			connector.rollback();

			while (true) {
				try {
					Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
					long batchId = message.getId();
					int size = message.getEntries().size();

					if (batchId == -1 || size == 0) {
						Thread.sleep(1000);
					} else {
						// log.info(String.format("message [ batchId=%s, size=%s ] \n", batchId, size));
						HandleData(message.getEntries());
					}

					connector.ack(batchId); // 提交确认
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		} finally {
			connector.disconnect();
		}
	}


	private static void HandleData(List<CanalEntry.Entry> entrys) throws Exception {
		for (CanalEntry.Entry entry : entrys) {
			if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
				continue;
			}

			CanalEntry.RowChange rowChage = null;
			try {
				rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
			} catch (Exception e) {
				throw new RuntimeException(
						"ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e
				);
			}

			CanalEntry.EventType eventType = rowChage.getEventType();

			// 同步指定的表
			if(Constants.srcDBName.equals(entry.getHeader().getSchemaName())
					&& Constants.srcDBTable.equals(entry.getHeader().getTableName())) {

				String binlogInfo = String.format(
						"binlog:[%s], offset:[%s], eventLength:[%s], SchemaName:[%s], TableName:[%s], eventType:[%s]",
						entry.getHeader().getLogfileName(),
						entry.getHeader().getLogfileOffset(),
						entry.getHeader().getEventLength(),
						entry.getHeader().getSchemaName(),
						entry.getHeader().getTableName(),
						eventType);
				log.info(binlogInfo);

				log.info(String.format("开始同步数据库表: %s", entry.getHeader().getTableName()));
				for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
					if (eventType == CanalEntry.EventType.DELETE) {
						executerDeleteSql(rowData);
					} else if (eventType == CanalEntry.EventType.INSERT) {
						executerInsertSql(rowData);
					} else if (eventType == CanalEntry.EventType.UPDATE){
						executerUpdateSql(rowData);
					}
				}
			}
		}
	}


	private static void executerDeleteSql(CanalEntry.RowData rowData) throws Exception {
		BinlogService.printColumn(rowData.getBeforeColumnsList());
		List<CanalEntry.Column> columns = rowData.getBeforeColumnsList();
		UserDMO userDMO = BinlogService.composeUserDmo(columns);
		log.info(String.format("###### 删除用户: %s, 用户ID: %s", userDMO.getName(), userDMO.getId()));

		tableSyncApplication.userService.delete(userDMO.getId());
	}

	private static void executerInsertSql(CanalEntry.RowData rowData) throws Exception {
		BinlogService.printColumn(rowData.getAfterColumnsList());
		List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
		UserDMO userDMO = BinlogService.composeUserDmo(columns);
		log.info(String.format("###### 新增用户: %s, 用户ID: %s", userDMO.getName(), userDMO.getId()));

		tableSyncApplication.userService.insert(userDMO);
	}

	private static void executerUpdateSql(CanalEntry.RowData rowData) throws Exception {
		log.info("---------------- 更新前 --------------");
		BinlogService.printColumn(rowData.getBeforeColumnsList());
		log.info("---------------- 更新后 --------------");
		BinlogService.printColumn(rowData.getAfterColumnsList());

		List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
		UserDMO userDMO = BinlogService.composeUserDmo(columns);
		log.info(String.format("###### 更新用户信息: %s, 用户ID: %s", userDMO.getName(), userDMO.getId()));

		tableSyncApplication.userService.update(userDMO);
	}
}
