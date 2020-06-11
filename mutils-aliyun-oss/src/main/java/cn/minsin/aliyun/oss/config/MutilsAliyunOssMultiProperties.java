package cn.minsin.aliyun.oss.config;

import cn.minsin.core.exception.MutilsException;
import cn.minsin.core.init.AbstractChildrenConfig;
import cn.minsin.core.tools.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 阿里云oss子配置项
 *
 * @author mintonzhang
 * @date 2019年2月13日
 * @since 0.2.8
 */
@Getter
@Setter
public class MutilsAliyunOssMultiProperties extends AbstractChildrenConfig {
    /**
     * 默认储存空间
     */
    private String bucketName;

    /**
     * 默认保存目录
     */
    private String saveDir;


    public String createOssSaveDir(String fileName) {
        if (saveDir == null) {
            return fileName;
        }
        return saveDir + "/" + fileName;
    }

    @Override
    public void checkConfig() {
        if (StringUtil.isBlank(bucketName, saveDir)) {
            throw new MutilsException("阿里云oss配置文件初始化失败");
        }
    }
}