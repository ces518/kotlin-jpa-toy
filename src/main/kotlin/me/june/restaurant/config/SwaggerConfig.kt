package me.june.restaurant.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.*
import springfox.documentation.schema.ModelRef
import springfox.documentation.schema.ModelSpecification
import springfox.documentation.schema.ScalarType
import springfox.documentation.service.ParameterType
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun swagger(): Docket {
        return Docket(DocumentationType.OAS_30)
                .globalRequestParameters(
                    listOf(
                        RequestParameterBuilder()
                            .name("dummy-auth-id")
                            .description("Dummy Auth")
                            .`in`(ParameterType.HEADER)
                            .required(false)
                            .build()

                    )
                )
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