package cn.minsin.core.init;

import javax.annotation.PostConstruct;

/**
 * 初始化基类
 *
 * @author mintonzhang
 * @date 2019年1月1日
 * @since 0.1.0
 */
public abstract class AbstractConfig {
    /**
     * 子类配置项检查
     */
    @PostConstruct
    protected abstract void checkConfig();

}
