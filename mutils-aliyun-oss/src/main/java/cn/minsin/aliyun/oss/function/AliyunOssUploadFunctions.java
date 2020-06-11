package cn.minsin.aliyun.oss.function;

import cn.minsin.aliyun.oss.config.MutilsAliyunOssProperties;
import cn.minsin.core.exception.MutilsErrorException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云OSS 文件上传
 * 官方文档 https://helpcdn.aliyun.com/document_detail/32013.html
 *
 * @author mintonzhang
 * @date 2019年1月29日
 * @since 0.2.9
 */
@Component
public class AliyunOssUploadFunctions extends AliyunOssBaseFunctions {

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
    public AliyunOssUploadFunctions init(String prefix) throws MutilsErrorException {
        super.loadConfig(prefix);
        return this;
    }

    /**
     * 上传文件至OSS
     *
     * @param file    预上传文件
     * @param isCover 如有同名，是否覆盖 true表示覆盖  false表示不覆盖(有可能会导致网络延迟)
     *                存储在oss的文件名 可用此文件名调用其他function进行相关操作 {@link AliyunOssDownloadFunctions AliyunOssManageFunctions}
     * @throws MutilsErrorException
     * @throws IOException
     */
    public String uplodeToOss(MultipartFile file, boolean isCover) throws IOException {
        return uploadToOss(file.getOriginalFilename(), file.getInputStream(), isCover);
    }

    /**
     * 上传文件至OSS
     *
     * @param file    预上传文件
     * @param isCover 如有同名，是否覆盖 true表示覆盖  false表示不覆盖(有可能会导致网络延迟)
     *                存储在oss的文件名 可用此文件名调用其他function进行相关操作 {@link AliyunOssDownloadFunctions AliyunOssManageFunctions}
     * @throws MutilsErrorException
     * @throws FileNotFoundException
     * @throws ClientException
     * @throws OSSException
     * @throws IOException
     */
    public String uplodeToOss(File file, boolean isCover) throws MutilsErrorException, OSSException, ClientException, FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new MutilsErrorException("The file is not exists.");
        }
        return uploadToOss(file.getName(), new FileInputStream(file), isCover);
    }

    /**
     * 上传文件至OSS
     *
     * @param file     预上传文件流
     * @param fileName 文件名
     * @param isCover  如有同名，是否覆盖 true表示覆盖  false表示不覆盖(有可能会导致网络延迟)
     *                 存储在oss的文件名 可用此文件名调用其他function进行相关操作 {@link AliyunOssDownloadFunctions AliyunOssManageFunctions}
     * @throws MutilsErrorException
     * @throws IOException
     */
    public String uploadToOss(InputStream file, String fileName, boolean isCover) {
        return uploadToOss(fileName, file, isCover);
    }

    protected String uploadToOss(String fileName, InputStream in, boolean isCover) throws OSSException, ClientException {
        OSS initClient = initClient();
        try {
            String filename = childConfig.createOssSaveDir(fileName);
            if (!isCover) {
                filename = createNewName(filename);
            }
            initClient.putObject(childConfig.getBucketName(), filename, in);
            return filename;
        } finally {
            initClient.shutdown();
        }
    }

    /**
     * 创建新文件名
     *
     * @param name 原文件名
     * @throws OSSException
     * @throws ClientException
     * @throws MutilsErrorException
     */
    protected String createNewName(String name) throws OSSException, ClientException {

        OSS initClient = initClient();
        try {
            String gName = name;
            int count = 0;
            while (true) {
                boolean exists = initClient.doesObjectExist(childConfig.getBucketName(), gName);
                if (exists) {
                    int index = name.lastIndexOf(".");
                    String extension = "";
                    if (index > 0) {
                        extension = name.substring(index);
                    }
                    count++;
                    gName = name.replace(extension, "") + "-copy(" + count + ")" + extension;
                    continue;
                }
                return gName;
            }
        } finally {
            initClient.shutdown();
        }
    }

}
