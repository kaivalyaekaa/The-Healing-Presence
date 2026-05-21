package in.thehealingpresence.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // In dev we want CSS/JS/image edits to show on next refresh; in prod we cache aggressively.
        boolean isDev = "dev".equalsIgnoreCase(activeProfile) || "default".equalsIgnoreCase(activeProfile);
        // In dev, noStore() guarantees the browser refetches every CSS/JS/image — no stale cache.
        CacheControl cache = isDev
                ? CacheControl.noStore()
                : CacheControl.maxAge(365, TimeUnit.DAYS);

        registry.addResourceHandler("/images/**", "/css/**", "/js/**", "/videos/**")
                .addResourceLocations("classpath:/static/images/", "classpath:/static/css/", "classpath:/static/js/", "classpath:/static/videos/")
                .setCacheControl(cache);
    }
}
