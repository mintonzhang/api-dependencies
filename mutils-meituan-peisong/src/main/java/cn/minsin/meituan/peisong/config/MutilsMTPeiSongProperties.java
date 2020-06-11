package cn.minsin.meituan.peisong.config;

import cn.minsin.core.constant.MutilsModelConstant;
import cn.minsin.core.exception.MutilsException;
import cn.minsin.core.init.AbstractConfig;
import cn.minsin.core.tools.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * excel配置文件
 *
 * @author mintonzhang
 * @date 2019年2月14日
 * @since 0.1.0
 */
@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = MutilsModelConstant.MUTILS_PREFIX + ".meituan.peisong")
public class MutilsMTPeiSongProperties extends AbstractConfig {

    /**
     * 服务器地址
     */
    private String serverUrl = "https://peisongopen.meituan.com/api";

    /**
     * 版本信息
     */
    private String version = "1.0";

    /**
     * 需要使用的环境 默认测试环境 所选环境的config必须配置
     */
    private String configPrefix;

    /**
     * 配置文件
     */
    private Map<String, MutilsMTMultiProperties> config = new HashMap<>(2);


    public MutilsMTMultiProperties nowConfig() {
        boolean b = config.containsKey(configPrefix);
        if (!b) {
            log.info("配置文件中Key为'{}',不存在", configPrefix);
            return null;
        }
        return config.get(configPrefix);
    }

    @Override
    protected void checkConfig() {
        if (StringUtil.isBlank(version, serverUrl, configPrefix) || config.isEmpty()) {
            throw new MutilsException("美团配送配置文件初始化失败,请检查version, serverUrl, configPrefix, config.");
        }
        this.nowConfig().checkConfig();
    }


}
