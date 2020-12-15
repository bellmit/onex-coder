package com.nb6868.onexcoder.config;

import com.nb6868.onexcoder.dao.*;
import com.nb6868.onexcoder.utils.OnexException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据库配置
 *
 * @author Charles (zhanngchaoxu@gmail.com)
 */
@Configuration
public class DbConfig {

    @Value("${database: mysql}")
    private String database;

    @Autowired
    private MySqlGeneratorDao mySqlGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao() {
        if ("mysql".equalsIgnoreCase(database)) {
            return mySqlGeneratorDao;
        } else {
            throw new OnexException("不支持当前数据库：" + database);
        }
    }
}
