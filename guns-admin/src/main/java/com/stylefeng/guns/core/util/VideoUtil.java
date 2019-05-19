package com.stylefeng.guns.core.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoUtil {
    private static final int SECOND = 50;

    private static final Logger logger = LoggerFactory.getLogger(VideoUtil.class);

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videoFile 源视频文件
     * @param saveFile  截取帧的图片存放路径
     * @throws Exception
     */
    public static List<File> fetchPic(File videoFile, String saveFile, int second) throws Exception {

        java.util.List<File> files = new ArrayList<>();

        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        ff.start();
        int lenght = ff.getLengthInAudioFrames();
        System.out.println(ff.getFrameRate());

        int i = 0;
        Frame frame = null;

        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            frame = ff.grabImage();
            if (i >= (int) (ff.getFrameRate() * second) && frame.image != null) {
                System.out.print(i + ",");
                if (frame != null && frame.image != null) {
                    System.out.println(i);
                    files.add(writeToFile(frame, saveFile, i));
                }
                second++;
            }
            i += second;
        }
        ff.stop();
        return files;
    }

    public static List<Integer> getList(int count, int length) {
        if (count > length) {
            count = length;
        }
        System.out.println(length);
        System.out.println(count);
        int total = (int) (length / count);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i * total);
            System.out.println(i * total);
        }
        return list;
    }

    public static List<File> fetchPicByCount(File videoFile, String saveFile, int count) throws Exception {

        java.util.List<File> files = new ArrayList<>();

        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        ff.start();

        int frameLength = ff.getLengthInFrames();


        System.out.println("length:" + frameLength);

        List<Integer> list = getList(count, frameLength);

        System.out.println(ff.getFrameRate());

        int i = 0;
        Frame frame = null;

        while (i < frameLength) {
            frame = ff.grabImage();
            if (list.contains(i)) {
                if (frame != null && frame.image != null) {
                    System.out.println(i);
                    files.add(writeToFile(frame, saveFile, i));
                }
            }
            i++;
        }
        ff.stop();
        return files;
    }


    public static File writeToFile(Frame frame, String saveFile, int second) throws IOException {
        String fileName = String.valueOf(System.currentTimeMillis()) + second;
        File targetFile = new File(saveFile + File.separator + fileName + ".jpg");
        String imgSuffix = "jpg";

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        ImageIO.write(bi, imgSuffix, targetFile);

        return targetFile;
    }


    /**
     * 获取视频时长，单位为秒
     *
     * @param file
     * @return 时长（s）
     */
    public static Long getVideoTime(File file) {
        Long times = 0L;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
            ff.start();
            times = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

//    public static void getBySecond(String filePath, String directory) {
//        opencv_videoio.CvCapture capture = cvCaptureFromFile(filePath);
//        //帧率
//        double fps = cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
//        System.out.println("帧率:" + fps);
//        opencv_core.IplImage frame = null;
//        double pos1 = 0;
//
//        double rootCount = 0;
//        while (true) {
//
//            //读取关键帧
//            frame = cvQueryFrame(capture);
//
//            rootCount = fps;
//            while (rootCount > 0) {
//                //这一段的目的是跳过每一秒钟的帧数,也就是说fps是帧率(一秒钟有多少帧),在读取一帧后,跳过fps数量的帧就相当于跳过了1秒钟。
//                frame = cvQueryFrame(capture);
//                rootCount--;
//            }
//
//            //获取当前帧的位置
//            pos1 = cvGetCaptureProperty(capture, CV_CAP_PROP_POS_FRAMES);
//            System.out.println(pos1);
//
//            if (null == frame)
//                break;
//
//            cvSaveImage("E:/223/" + pos1 + ".jpg", frame);
//
//        }
//
//        cvReleaseCapture(capture);
//    }

    /*public void getBySecond() {
        opencv_videoio.CvCapture capture = opencv_highgui.cvC("D:/085402.crf");



        //帧率
        int fps = (int) opencv_highgui.cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FPS);
        System.out.println("帧率:"+fps);

        opencv_core.IplImage frame = null;
        double pos1 = 0;

        int rootCount = 0;

        while (true) {

            //读取关键帧
            frame = opencv_highgui.cvQueryFrame(capture);

            rootCount = fps;
            while(rootCount > 0 ){
                //这一段的目的是跳过每一秒钟的帧数,也就是说fps是帧率(一秒钟有多少帧),在读取一帧后,跳过fps数量的帧就相当于跳过了1秒钟。
                frame = opencv_highgui.cvQueryFrame(capture);
                rootCount--;
            }

            //获取当前帧的位置
            pos1 = opencv_highgui.cvGetCaptureProperty(capture,opencv_highgui.CV_CAP_PROP_POS_FRAMES);
            System.out.println(pos1);

            if (null == frame)
                break;

            opencv_highgui.cvSaveImage("d:/img/" + pos1 + ".jpg",frame);

        }

        opencv_highgui.cvReleaseCapture(capture);
    }*/

    public static void main(String[] args) {
        try {
            //getList(10,113);

            File file = new File("C:\\Users\\Administrator\\Desktop\\F.mp4");
            List<File> files = VideoUtil.fetchPicByCount(file, "C:\\Users\\Administrator\\Desktop\\233", 100);
            System.out.println(files.get(0).getName());
            System.out.println(VideoUtil.getVideoTime(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
