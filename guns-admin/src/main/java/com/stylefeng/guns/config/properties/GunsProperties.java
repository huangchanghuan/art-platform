package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static com.stylefeng.guns.core.util.ToolUtil.getTempPath;
import static com.stylefeng.guns.core.util.ToolUtil.isEmpty;

/**
 * guns项目配置
 *
 * @author stylefeng
 * @Date 2017/5/23 22:31
 */
@Component
@ConfigurationProperties(prefix = GunsProperties.PREFIX)
public class GunsProperties {

    public static final String PREFIX = "guns";

    private Boolean kaptchaOpen = false;

    private Boolean swaggerOpen = false;

    private String ipPortProjectPath;
    private String fileUploadPath;

    private String videoUploadPath;
    private String videoImagePath;
    private String resourceLocations;

    private Boolean haveCreatePath = false;
    private Boolean haveCreateVideoPath = false;
    private Boolean haveCreateVideoImagePath = false;

    private Boolean springSessionOpen = false;

    /**
     * session 失效时间（默认为30分钟 单位：秒）
     */
    private Integer sessionInvalidateTime = 30 * 60;

    /**
     * session 验证失效时间（默认为15分钟 单位：秒）
     */
    private Integer sessionValidationInterval = 15 * 60;

    public String getFileUploadPath() {
        //如果没有写文件上传路径,保存到临时目录
        if (isEmpty(fileUploadPath)) {
            return getTempPath();
        } else {
            //判断有没有结尾符,没有得加上
            if (!fileUploadPath.endsWith(File.separator)) {
                fileUploadPath = fileUploadPath + File.separator;
            }
            //判断目录存不存在,不存在得加上
            if (!haveCreatePath) {
                File file = new File(fileUploadPath);
                file.mkdirs();
                haveCreatePath = true;
            }
            return fileUploadPath;
        }
    }
    public String getVideoUploadPath() {
        //如果没有写文件上传路径,保存到临时目录
        if (isEmpty(videoUploadPath)) {
            return getTempPath();
        } else {
            //判断有没有结尾符,没有得加上
            if (!videoUploadPath.endsWith(File.separator)) {
                videoUploadPath = videoUploadPath + File.separator;
            }
            //判断目录存不存在,不存在得加上
            if (!haveCreateVideoPath) {
                File file = new File(videoUploadPath);
                file.mkdirs();
                haveCreateVideoPath = true;
            }
            return videoUploadPath;
        }
    }

    public String getVideoImagePath() {
        //如果没有写文件上传路径,保存到临时目录
        if (isEmpty(videoImagePath)) {
            return getTempPath();
        } else {
            //判断有没有结尾符,没有得加上
            if (!videoImagePath.endsWith(File.separator)) {
                videoImagePath = videoImagePath + File.separator;
            }
            //判断目录存不存在,不存在得加上
            if (!haveCreateVideoImagePath) {
                File file = new File(videoImagePath);
                file.mkdirs();
                haveCreateVideoImagePath = true;
            }
            return videoImagePath;
        }
    }

    public String getIpPortProjectPath(HttpServletRequest request) {
        return ipPortProjectPath;
    }

    public String getResourceLocations() {
        return resourceLocations;
    }

    public void setResourceLocations(String resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

    public void setIpPortProjectPath(String ipPortProjectPath) {
        this.ipPortProjectPath = ipPortProjectPath;
    }

    public void setVideoImagePath(String videoImagePath) {
        this.videoImagePath = videoImagePath;
    }

    public void setVideoUploadPath(String videoUploadPath) {
        this.videoUploadPath = videoUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public Boolean getKaptchaOpen() {
        return kaptchaOpen;
    }

    public void setKaptchaOpen(Boolean kaptchaOpen) {
        this.kaptchaOpen = kaptchaOpen;
    }

    public Boolean getSwaggerOpen() {
        return swaggerOpen;
    }

    public void setSwaggerOpen(Boolean swaggerOpen) {
        this.swaggerOpen = swaggerOpen;
    }

    public Boolean getSpringSessionOpen() {
        return springSessionOpen;
    }

    public void setSpringSessionOpen(Boolean springSessionOpen) {
        this.springSessionOpen = springSessionOpen;
    }

    public Integer getSessionInvalidateTime() {
        return sessionInvalidateTime;
    }

    public void setSessionInvalidateTime(Integer sessionInvalidateTime) {
        this.sessionInvalidateTime = sessionInvalidateTime;
    }

    public Integer getSessionValidationInterval() {
        return sessionValidationInterval;
    }

    public void setSessionValidationInterval(Integer sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
    }
}
