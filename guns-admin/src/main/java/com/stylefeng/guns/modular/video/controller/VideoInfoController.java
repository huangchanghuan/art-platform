package com.stylefeng.guns.modular.video.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.VideoInfo;
import com.stylefeng.guns.modular.video.service.IVideoInfoService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 视频管理控制器
 *
 * @author fengshuonan
 * @Date 2019-05-18 21:19:44
 */
@Controller
@RequestMapping("/videoInfo")
public class VideoInfoController extends BaseController {

    private String PREFIX = "/video/videoInfo/";

    @Autowired
    private IVideoInfoService videoInfoService;
    @Autowired
    private GunsProperties gunsProperties;
    /**
     * 跳转到视频管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "videoInfo.html";
    }

    /**
     * 跳转到添加视频管理
     */
    @RequestMapping("/videoInfo_add")
    public String videoInfoAdd() {
        return PREFIX + "videoInfo_add.html";
    }

    /**
     * 上传视频
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        //后缀
        String suffix = picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf("."));
        String videoName = UUID.randomUUID().toString() + suffix;
        try {
            //路径
            File path2 = new File(ResourceUtils.getURL("classpath:static").getPath().replace("%20"," ").replace('/', '\\'));
            if(!path2.exists()) path2 = new File("");
            //如果上传目录为/static/images/upload/，则可以如下获取：
            File upload2 = new File(path2.getAbsolutePath(),"video");
            if(!upload2.exists()) upload2.mkdirs();
            String path=upload2.getAbsolutePath()+"\\";
            //完成上传
//            String fileSavePath = gunsProperties.getVideoUploadPath();
            picture.transferTo(new File(path + videoName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
        //开始插入数据
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setAccount(ShiroKit.getUser().getAccount());
        videoInfo.setConsumer("被拍待定");
        videoInfo.setVideoName(picture.getOriginalFilename());        videoInfo.setVideoUrl("/static/video/"+videoName);
        videoInfo.setServiceCreator(ShiroKit.getUser().getName());
        videoInfoService.insert(videoInfo);
        return videoName;
    }

    /**
     * 跳转到上传视频管理
     */
    @RequestMapping("/videoInfo_upload")
    public String videoInfoUpload() {
        return PREFIX + "videoInfo_upload.html";
    }

    /**
     * 跳转到修改视频管理
     */
    @RequestMapping("/videoInfo_update/{videoInfoId}")
    public String videoInfoUpdate(@PathVariable Integer videoInfoId, Model model) {
        VideoInfo videoInfo = videoInfoService.selectById(videoInfoId);
        model.addAttribute("item",videoInfo);
        LogObjectHolder.me().set(videoInfo);
        return PREFIX + "videoInfo_edit.html";
    }

    /**
     * 获取视频管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<VideoInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("account", ShiroKit.getUser().getAccount());
        return videoInfoService.selectList(wrapper);
    }

    /**
     * 新增视频管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(VideoInfo videoInfo) {
        videoInfoService.insert(videoInfo);
        return SUCCESS_TIP;
    }

    /**
     * 删除视频管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer videoInfoId) {
        videoInfoService.deleteById(videoInfoId);
        return SUCCESS_TIP;
    }

    /**
     * 修改视频管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(VideoInfo videoInfo) {
        videoInfoService.updateById(videoInfo);
        return SUCCESS_TIP;
    }

    /**
     * 视频管理详情
     */
    @RequestMapping(value = "/detail/{videoInfoId}")
    @ResponseBody
    public Object detail(@PathVariable("videoInfoId") Integer videoInfoId) {
        return videoInfoService.selectById(videoInfoId);
    }
}
