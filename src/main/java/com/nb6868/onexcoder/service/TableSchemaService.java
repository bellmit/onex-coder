package com.nb6868.onexcoder.service;

import com.nb6868.onexcoder.utils.GenUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 表结构服务
 *
 * @author Charles (zhanngchaoxu@gmail.com)
 */
@Service
public class TableSchemaService {

    Statement dbStatement;

    /**
     * 初始化db link
     */
    private void initDb() {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/onex");
        hikariConfig.setUsername("rooy");
        hikariConfig.setPassword("root");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        try {
            Connection dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public List<Map<String, Object>> queryList(String keywords) throws Exception {
        initDb();
        StringBuilder sql = new StringBuilder("select table_name, engine, table_comment, create_time from information_schema.tables where table_schema = (select database())");
        if (!ObjectUtils.isEmpty(keywords)) {
            sql.append(" and table_name like concat('%").append(keywords).append("%')");
        }
        sql.append(" order by create_time desc");
        ResultSet resultSet = dbStatement.executeQuery(sql.toString());
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", resultSet.getString("table_name"));
            map.put("engine", resultSet.getString("engine"));
            map.put("tableComment", resultSet.getString("table_comment"));
            map.put("createTime", resultSet.getDate("create_time"));
            resultMapList.add(map);
        }
        dbStatement.close();
        return resultMapList;
    }

    public Map<String, String> queryTable(String tableName) throws Exception {
        initDb();
        StringBuilder sql = new StringBuilder("select table_name, engine, table_comment, create_time from information_schema.tables where" +
                " table_schema = (select database()) and table_name = '").append(tableName).append("'");
        ResultSet resultSet = dbStatement.executeQuery(sql.toString());
        Map<String, String> map = null;
        while (resultSet.next()) {
            map = new HashMap<>();
            map.put("tableName", resultSet.getString("table_name"));
            map.put("engine", resultSet.getString("engine"));
            map.put("tableComment", resultSet.getString("table_comment"));
            map.put("createTime", resultSet.getString("create_time"));
        }
        dbStatement.close();
        return map;
    }

    public List<Map<String, String>> queryColumns(String tableName) throws Exception {
        initDb();
        StringBuilder sql = new StringBuilder("select column_name, data_type, column_comment, column_key, extra from information_schema.columns" +
                " where table_name = '").append(tableName).append("'").append(" and table_schema = (select database()) order by ordinal_position");
        ResultSet resultSet = dbStatement.executeQuery(sql.toString());
        List<Map<String, String>> resultMapList = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, String> map = new HashMap<>();
            map.put("columnName", resultSet.getString("column_name"));
            map.put("dataType", resultSet.getString("data_type"));
            map.put("columnComment", resultSet.getString("column_comment"));
            map.put("columnKey", resultSet.getString("column_key"));
            map.put("extra", resultSet.getString("extra"));
            resultMapList.add(map);
        }
        dbStatement.close();
        return resultMapList;
    }

    public byte[] generatorCode(String[] tableNames) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        try {
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
