package me.june.restaurant.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun swagger(): Docket {
        return Docket(DocumentationType.OAS_30)
                .apiInfo(swaggerInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(true)
    }


    private fun swaggerInfo() = ApiInfoBuilder()
            .title("API Docs")
            .description("맛집 프로젝트")
            .version("1.0.0")
            .build()
}