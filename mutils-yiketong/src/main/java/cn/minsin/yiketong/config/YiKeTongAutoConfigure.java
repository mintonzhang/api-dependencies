package cn.minsin.yiketong.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(YiKeTongProperties.class)
@ComponentScan("cn.minsin.yiketong.function")
public class YiKeTongAutoConfigure {
    @Getter
    private final YiKeTongProperties properties;

    YiKeTongAutoConfigure(YiKeTongProperties properties) {
        super();
        this.properties = properties;
    }
}

