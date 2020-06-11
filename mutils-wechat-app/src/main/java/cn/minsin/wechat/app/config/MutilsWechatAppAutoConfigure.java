package cn.minsin.wechat.app.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(MutilsWechatAppProperties.class)
@ComponentScan("cn.minsin.wechat.app.function")
public class MutilsWechatAppAutoConfigure {
    @Getter
    private final MutilsWechatAppProperties properties;

    MutilsWechatAppAutoConfigure(MutilsWechatAppProperties properties) {
        super();
        this.properties = properties;
    }
}

