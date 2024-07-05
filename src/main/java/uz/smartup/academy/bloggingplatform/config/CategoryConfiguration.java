package uz.smartup.academy.bloggingplatform.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CategoryConfiguration {
    @Value("${categories}")
    private String categories;

    public List<String> getCategories() {
        return Arrays.asList(categories.split(","));
    }
}
