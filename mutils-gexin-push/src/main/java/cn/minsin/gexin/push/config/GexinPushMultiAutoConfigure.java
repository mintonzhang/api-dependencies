package cn.minsin.gexin.push.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(GexinPushProperties.class)
@ComponentScan("cn.minsin.gexin.push.function")
public class GexinPushMultiAutoConfigure {
    @Getter
    private final GexinPushProperties properties;

    GexinPushMultiAutoConfigure(GexinPushProperties properties) {
        super();
        this.properties = properties;
    }
}

