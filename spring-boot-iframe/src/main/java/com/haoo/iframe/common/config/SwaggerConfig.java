package com.haoo.iframe.common.config;

import com.haoo.iframe.util.PropertiesUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket all_api() {
        return getDocket("all_interface", "", "全部接口", "本项目中所有接口都在此",
                new String[]{}
        );
    }

    @Bean
    public Docket custom_api() {
        return getDocket("custom_interface", "com.haoo.springbootiframe", "自定义配置接口", "自由定义显示的接口",
                PropertiesUtil.rules
        );
    }


    /**
     * @param groupName   组名
     * @param basePackage 扫描包路径
     * @param apiName     apiName
     * @param apiDesc     apiDesc
     * @param paths       正则路径匹配
     * @return
     */
    private Docket getDocket(String groupName, String basePackage, String apiName, String apiDesc, String[] paths) {
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo(apiName, apiDesc))
                .enable(PropertiesUtil.swaggerEnabled)
                .groupName(groupName)
                .select()
//                .apis(RequestHandlerSelectors.basePackage(basePackage));
                .apis(RequestHandlerSelectors.any());
        if (ArrayUtils.isEmpty(paths)) {
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
