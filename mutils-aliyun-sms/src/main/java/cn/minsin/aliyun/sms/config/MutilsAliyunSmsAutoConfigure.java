package cn.minsin.aliyun.sms.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(MutilsAliyunSmsProperties.class)
@ComponentScan("cn.minsin.aliyun.sms.function")
public class MutilsAliyunSmsAutoConfigure {
    @Getter
    private final MutilsAliyunSmsProperties properties;

    MutilsAliyunSmsAutoConfigure(MutilsAliyunSmsProperties properties) {
        super();
        this.properties = properties;
    }
}

