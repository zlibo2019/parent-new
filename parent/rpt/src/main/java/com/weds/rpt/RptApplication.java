package com.weds.rpt;

import com.weds.core.annotation.MyBatisDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.weds.rpt.mapper"}, annotationClass = MyBatisDao.class)
public class RptApplication {

    public static void main(String[] args) {
        SpringApplication.run(RptApplication.class, args);
    }

}
