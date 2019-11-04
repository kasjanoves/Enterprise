package enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication(scanBasePackages = "enterprise")
public class SpringBootWebApplication
{
    @Bean
    public javax.validation.Validator localValidatorFactoryBean()
    {
        return new LocalValidatorFactoryBean();
    }

    public static void main(String[] args)
    {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
