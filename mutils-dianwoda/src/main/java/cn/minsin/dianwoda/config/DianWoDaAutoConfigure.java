package cn.minsin.dianwoda.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(DianWoDaProperties.class)
@ComponentScan("cn.minsin.dianwoda.function")
public class DianWoDaAutoConfigure {
    @Getter
    private final DianWoDaProperties properties;

    DianWoDaAutoConfigure(DianWoDaProperties properties) {
        super();
        this.properties = properties;
    }
}

