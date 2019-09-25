package al.recipes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    public static final Contact DEFAULT_CONTACT = new Contact(
            "Julian Vasa",
            "https://github.com/julianvasa",
            "vasa.julian@gmail.com");

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaData())
                .select()
                .apis(RequestHandlerSelectors.basePackage("al.recipes"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo metaData() {

        return new ApiInfoBuilder()
                .title("Bootiful blog")
                .description("Simple blog built with a bunch of programming languages")
                .version("2.0")
                .contact(DEFAULT_CONTACT)
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
