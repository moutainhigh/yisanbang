package com.vtmer.yisanbang;

//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.vtmer.yisanbang.mapper")
@EnableTransactionManagement
@EnableScheduling
@EnableKnife4j
@EnableConfigurationProperties
public class YisanbangApplication {

    public static void main(String[] args) {
        SpringApplication.run(YisanbangApplication.class, args);
    }

    // /**
    //  * http重定向到https
    //  * @return
    //  */
    // @Bean
    // public TomcatServletWebServerFactory servletContainer() {
    //     TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
    //         @Override
    //         protected void postProcessContext(Context context) {
    //             SecurityConstraint constraint = new SecurityConstraint();
    //             constraint.setUserConstraint("CONFIDENTIAL");
    //             SecurityCollection collection = new SecurityCollection();
    //             collection.addPattern("/*");
    //             constraint.addCollection(collection);
    //             context.addConstraint(constraint);
    //         }
    //     };
    //     tomcat.addAdditionalTomcatConnectors(httpConnector());
    //     return tomcat;
    // }
    //
    // // 注意：https默认端口443 ，然后会跳转到访问80端口
    // @Bean
    // public Connector httpConnector() {
    //     Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    //     connector.setScheme("http");
    //     // Connector监听的http的端口号
    //     connector.setPort(80);
    //     connector.setSecure(false);
    //     // 监听到http的端口号后转向到的https的端口号(yml中设置的port)
    //     connector.setRedirectPort(8089);
    //     return connector;
    // }

}
