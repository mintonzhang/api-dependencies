package cn.minsin.aliyun.oss.function;

import cn.minsin.aliyun.oss.config.MutilsAliyunOssMultiProperties;
import cn.minsin.aliyun.oss.config.MutilsAliyunOssProperties;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.rule.AbstractFunctionRule;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

class AliyunOssBaseFunctions extends AbstractFunctionRule {

    protected MutilsAliyunOssProperties properties;

    protected MutilsAliyunOssMultiProperties childConfig;


    /**
     * 初始化阿里云 OSS functions
     *
     * @param prefix 配置文件中的前缀
     * @throws MutilsErrorException
     */
    protected MutilsAliyunOssMultiProperties loadConfig(String prefix) throws MutilsErrorException {
        MutilsAliyunOssMultiProperties aliyunOssMultiConfig = properties.getBucketnameAndSavedir().get(prefix);
        if (aliyunOssMultiConfig == null) {
            throw new MutilsErrorException("The config key named '" + prefix + "' not found, Please check config.");
        }
        return aliyunOssMultiConfig;
    }

    /**
     * 初始化oss客户端
     */
    protected OSS initClient() {
        return new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKeyId(), properties.getAccessKeySecret());
    }
}
