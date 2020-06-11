package cn.minsin.aliyun.oss.config;

import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: minton.zhang
 * @since: 2019/10/9 15:57
 */
@Configuration
@EnableConfigurationProperties(MutilsAliyunOssProperties.class)
@ComponentScan("cn.minsin.aliyun.oss.function")
public class MutilsAliyunOssAutoConfigure {
    @Getter
    private final MutilsAliyunOssProperties properties;

    MutilsAliyunOssAutoConfigure(MutilsAliyunOssProperties properties) {
        super();
        this.properties = properties;
    }
}

