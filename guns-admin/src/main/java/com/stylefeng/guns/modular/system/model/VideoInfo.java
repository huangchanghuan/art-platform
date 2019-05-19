package com.stylefeng.guns.modular.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 平台用户视频表
 * </p>
 *
 * @author huangchanghuan123
 * @since 2019-05-18
 */
@TableName("auth_video_info")
public class VideoInfo extends Model<VideoInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 视频名称
     */
    @TableField("video_name")
    private String videoName;
    /**
     * 视频地址
     */
    @TableField("video_url")
    private String videoUrl;
    /**
     * 图片地址
     */
    @TableField("image_url")
    private String imageUrl;
    /**
     * 创建人
     */
    @TableField("service_creator")
    private String serviceCreator;
    /**
     * 照片拥有者
     */
    private String consumer;
    /**
     * 所属平台用户
     */
    private String account;
    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getServiceCreator() {
        return serviceCreator;
    }

    public void setServiceCreator(String serviceCreator) {
        this.serviceCreator = serviceCreator;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
        "id=" + id +
        ", videoName=" + videoName +
        ", videoUrl=" + videoUrl +
        ", serviceCreator=" + serviceCreator +
        ", consumer=" + consumer +
        ", account=" + account +
        ", createDate=" + createDate +
        "}";
    }
}
