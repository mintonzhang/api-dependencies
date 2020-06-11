package cn.minsin.aliyun.oss.function;

import cn.minsin.aliyun.oss.config.MutilsAliyunOssProperties;
import cn.minsin.core.exception.MutilsErrorException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;

/**
 * 阿里云OSS 文件下载
 * 官方文档 https://helpcdn.aliyun.com/document_detail/32014.html
 *
 * @author mintonzhang
 * @date 2019年1月29日
 * @since 0.2.9
 */
@Component
public class AliyunOssDownloadFunctions extends AliyunOssBaseFunctions {

    @Autowired
    private MutilsAliyunOssProperties properties;

    @PostConstruct
    public void init() {
        super.properties = properties;
    }

    /**
     * 初始化配置文件
     *
     * @param prefix
     * @throws MutilsErrorException
     */
    public AliyunOssDownloadFunctions init(String prefix) throws MutilsErrorException {
        super.loadConfig(prefix);
        return this;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名 调用{@link AliyunOssUploadFunctions} 获取此fileName
     *                 文件流
     */
    public InputStream downloadFromOss(String fileName) {
        OSS initClient = initClient();
        try {
            OSSObject object = initClient.getObject(childConfig.getBucketName(), fileName);
            return object.getObjectContent();
        } finally {
            initClient.shutdown();
        }

    }

    /**
     * 下载文件
     *
     * @param fileName 文件名 调用{@link AliyunOssUploadFunctions} 获取此fileName
     * @param file     存放oss下载出的文件
     */
    public void downloadFromOss(String fileName, File file) {
        OSS initClient = initClient();
        try {
            initClient.getObject(new GetObjectRequest(childConfig.getBucketName(), fileName), file);
        } finally {
            initClient.shutdown();
        }
    }

}
