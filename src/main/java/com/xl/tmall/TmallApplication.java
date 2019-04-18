package com.xl.tmall;

import com.xl.tmall.utils.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.xl.tmall.pojo","com.xl.tmall.dao"})     //区分redis相关dao和es的dao
@EnableElasticsearchRepositories(basePackages = "com.xl.tmall.es")
public class TmallApplication {

    static {
        PortUtil.checkPort(6379,"redis服务器",true);
        PortUtil.checkPort(9300,"es服务器",true);
        PortUtil.checkPort(5601,"kibana服务器",true);
    }

    public static void main(String[] args) {
        SpringApplication.run(TmallApplication.class, args);
    }

}
