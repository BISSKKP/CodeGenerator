package com.code;

//swagger2的配置文件，在项目的启动类的同级文件建立

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * http://127.0.0.1:8085/code/swagger/index.html  UI界面
 * http://127.0.0.1:8085/code/swagger 文档
 * @author howard
 */
@Configuration
@EnableSwagger2
@PropertySource("classpath:swagger.properties")
public class Swagger2 {
//swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
  @Bean
  public Docket createRestApi() {
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(apiInfo())
              .select()
              //为当前包路径
              .apis(RequestHandlerSelectors.basePackage("com.code"))
              .paths(PathSelectors.any())
              .build();
  }

  //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
  private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
              //页面标题
              .title("Spring Boot Swagger2 构建RESTful API")
              //创建人
              .contact(new Contact("liuhz", "http://www.aiinfor.com", ""))
              //版本号
              .version("1.0")
              //描述
              .description("该项目代码生成器")
              .build();
  }


}