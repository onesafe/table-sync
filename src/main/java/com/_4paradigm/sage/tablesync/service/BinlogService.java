package com._4paradigm.sage.tablesync.service;

import com._4paradigm.sage.tablesync.entity.UserDMO;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangyiping on 2020/3/11 6:24 PM.
 */
@Slf4j
public class BinlogService {

    public static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            log.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

    public static UserDMO composeUserDmo(List<CanalEntry.Column> columns) throws Exception {
        UserDMO userDMO = new UserDMO();
        log.info("根据Column组装UserDMO");

        for (CanalEntry.Column column : columns) {

            String key = column.getName();
            String value = column.getValue();

            switch (key) {
                case "id":
                    userDMO.setId(value);
                    break;
                case "username":
                    userDMO.setUsername(value);
                    break;
                case "password":
                    userDMO.setPassword(value);
                    break;
                case "types":
                    userDMO.setTypes(Integer.parseInt(value));
                    break;
                case "phone":
                    userDMO.setPhone(value);
                    break;
                case "email":
                    userDMO.setEmail(value);
                    break;
                case "partner_id":
                    userDMO.setPartner_id(value);
                    break;
                case "status":
                    userDMO.setStatus(value);
                    break;
                case "last_login_at":
                    userDMO.setLast_login_at(StringToDate(value));
                    break;
                case "updated_by":
                    userDMO.setUpdated_by(value);
                    break;
                case "created_at":
                    userDMO.setCreated_at(StringToDate(value));
                    break;
                case "updated_at":
                    userDMO.setUpdated_at(StringToDate(value));
                    break;
                case "remark":
                    userDMO.setRemark(value);
                    break;
                case "portrait":
                    userDMO.setPortrait(value);
                    break;
                case "editedByAdmin":
                    userDMO.setEditedByAdmin(Integer.parseInt(value));
                    break;
                case "post":
                    userDMO.setPost(Integer.parseInt(value));
                    break;
                case "description":
                    userDMO.setDescription(value);
                    break;
                case "name":
                    userDMO.setName(value);
                    break;
                case "department":
                    userDMO.setDepartment(Integer.parseInt(value));
                    break;
            }

        }
        return userDMO;
    }

    private static Date StringToDate(String date) throws Exception {
        if(null == date) return null;

        String _date1 = "2019-08-02 03:30:22";
        String _date2 = "2018-09-27 19:06:44.654";

        SimpleDateFormat _formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat _formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        if(date.length() == _date1.length()) {
            return _formatter1.parse(date);
        } else if(date.length() == _date2.length()) {
            return _formatter2.parse(date);
        }

        return null;
    }
}
