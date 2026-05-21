package in.thehealingpresence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Environment environment;

    public WebConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // In dev we want CSS/JS/image edits to show on next refresh; in prod we cache aggressively.
        // noStore() guarantees the browser refetches every CSS/JS/image — no stale cache during dev.
        boolean isDev = environment.getActiveProfiles().length == 0
                || Arrays.asList(environment.getActiveProfiles()).contains("dev");
        CacheControl cache = isDev
                ? CacheControl.noStore()
                : CacheControl.maxAge(365, TimeUnit.DAYS);

        registry.addResourceHandler("/images/**", "/css/**", "/js/**", "/videos/**")
                .addResourceLocations("classpath:/static/images/", "classpath:/static/css/", "classpath:/static/js/", "classpath:/static/videos/")
                .setCacheControl(cache);
    }
}
