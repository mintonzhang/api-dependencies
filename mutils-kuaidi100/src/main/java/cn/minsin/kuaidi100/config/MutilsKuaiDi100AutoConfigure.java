package cn.minsin.kuaidi100.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(MutilsKuaiDi100Properties.class)
@ComponentScan("cn.minsin.kuaidi100.function")
public class MutilsKuaiDi100AutoConfigure {
    @Getter
    private final MutilsKuaiDi100Properties properties;

    MutilsKuaiDi100AutoConfigure(MutilsKuaiDi100Properties properties) {
        super();
        this.properties = properties;
    }
}

