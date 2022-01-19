package com.haoo.iframe.template.swagger;

//import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2
//@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())  //显示所有类
                //.apis(RequestHandlerSelectors.basePackage("com.huawei.sharedrive.app")) /**此处配置包含接口的包**/
                //.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))  //只显示添加@Api注解的类
                .build()
                .apiInfo(apiInfo("", ""))
//                .enable(PropertyUtils.swaggerEnabled);//是否启用文档
                .enable(true);//是否启用文档
    }


//    @Bean
//    public Docket custom_api() {
//        return getDocket("custom_interface", "com.haoo.springbootiframe", "自定义配置接口", "自定义配置接口",
//                //PropertyUtils.rules
//                new String[]{}
//        );
//    }


    /**
     * @param groupName   组名
     * @param basePackage 扫描包路径
     * @param apiName     apiName
     * @param apiDesc     apiDesc
     * @param paths       正则路径匹配
     * @return
     */
    private Docket getDocket(String groupName, String basePackage, String apiName, String apiDesc, String[] paths) {
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(apiName, apiDesc))
                .groupName(groupName)
                //.securitySchemes(Arrays.asList(new BasicAuth("test")))
                .select()
//                .apis(RequestHandlerSelectors.basePackage(basePackage));
                .apis(RequestHandlerSelectors.any());
        if (paths == null) {
            apiSelectorBuilder.paths(PathSelectors.any());
        } else {
            StringBuilder pathRegex = new StringBuilder();
            for (String path : paths) {
                pathRegex.append("(").append(path).append(")|");
            }
            apiSelectorBuilder.paths(PathSelectors.regex(pathRegex.substring(0, pathRegex.length() - 1)));
        }

        return apiSelectorBuilder.build();
    }

    private ApiInfo apiInfo(String title, String description) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version("1.0").build();
    }


}
