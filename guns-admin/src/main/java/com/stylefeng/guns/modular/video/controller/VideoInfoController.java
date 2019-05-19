package com.stylefeng.guns.modular.video.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.VideoUtil;
import com.stylefeng.guns.modular.system.model.VideoInfo;
import com.stylefeng.guns.modular.video.service.IVideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
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
     * 视频路径:
     * 图片路径:
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        //后缀
        String suffix = picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf("."));
        String videoName = UUID.randomUUID().toString() + suffix;
        try {
            //生成视频路径
            String videoPath = gunsProperties.getVideoUploadPath();
            //完成视频上传
            File file = new File(videoPath + videoName);
            picture.transferTo(file);
            //生成图片路径
            String imgPath = gunsProperties.getFileUploadPath()+"videoImage/";
            //调用截取视频第一帧图片
            List<File> files = VideoUtil.fetchPicByCount(file, imgPath, 1);
            //开始插入数据
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setAccount(ShiroKit.getUser().getAccount());
            videoInfo.setConsumer("被拍待定");
            videoInfo.setVideoName(picture.getOriginalFilename());
            videoInfo.setVideoUrl("videoPath/"+videoName);
            videoInfo.setImageUrl("videoImage/"+files.get(0).getName());
            videoInfo.setServiceCreator(ShiroKit.getUser().getName());
            videoInfoService.insert(videoInfo);
            return videoName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
    }

//    /**
//     * 图片下载
//     * @param imageName
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/show",method = RequestMethod.GET)
//    public ResponseEntity downloadImage(String imageName, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            return ResponseEntity.ok(resourceLoader.getResource("file:"+gunsProperties.getFileUploadPath()+imageName ));
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

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
