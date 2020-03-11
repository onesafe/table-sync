package com._4paradigm.sage.tablesync.manager;

import com._4paradigm.sage.tablesync.config.Constants;
import com._4paradigm.sage.tablesync.entity.UserDMO;
import com._4paradigm.sage.tablesync.service.BinlogService;
import com._4paradigm.sage.tablesync.service.UserService;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangyiping on 2020/3/12 12:00 AM.
 */
@Slf4j
@Service
public class DataManager {

    public static void HandleData(List<CanalEntry.Entry> entrys, UserService userService) throws Exception {
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
                        executerDeleteSql(rowData, userService);
                    } else if (eventType == CanalEntry.EventType.INSERT) {
                        executerInsertSql(rowData, userService);
                    } else if (eventType == CanalEntry.EventType.UPDATE){
                        executerUpdateSql(rowData, userService);
                    }
                }
            }
        }
    }

    private static void executerDeleteSql(CanalEntry.RowData rowData, UserService userService) throws Exception {
        BinlogService.printColumn(rowData.getBeforeColumnsList());
        List<CanalEntry.Column> columns = rowData.getBeforeColumnsList();
        UserDMO userDMO = BinlogService.composeUserDmo(columns);
        log.info(String.format("###### 删除用户: %s, 用户ID: %s", userDMO.getName(), userDMO.getId()));

        userService.delete(userDMO.getId());
    }

    private static void executerInsertSql(CanalEntry.RowData rowData, UserService userService) throws Exception {
        BinlogService.printColumn(rowData.getAfterColumnsList());
        List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
        UserDMO userDMO = BinlogService.composeUserDmo(columns);
        log.info(String.format("###### 新增用户: %s, 用户ID: %s", userDMO.getName(), userDMO.getId()));

        userService.insert(userDMO);
    }

    private static void executerUpdateSql(CanalEntry.RowData rowData, UserService userService) throws Exception {
        log.info("---------------- 更新前 --------------");
        BinlogService.printColumn(rowData.getBeforeColumnsList());
        log.info("---------------- 更新后 --------------");
        BinlogService.printColumn(rowData.getAfterColumnsList());

        List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
        UserDMO userDMO = BinlogService.composeUserDmo(columns);
        log.info(String.format("###### 更新用户信息: %s, 用户ID: %s", userDMO.getName(), userDMO.getId()));

        userService.update(userDMO);
    }
}
