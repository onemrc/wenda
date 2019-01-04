package com.demo.wenda.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * create by: one
 * create time:2019/1/3 18:41
 * 描述：文件工具
 */
public class FileUtil {

    public static void saveFile(String filename, byte[] data) {
        if (data != null) {
            String filepath = filename;
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data, 0, data.length);
                fileOutputStream.flush();
                fileOutputStream.close();

//                Cleaning(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去除图片中干扰像素
     *
     * @param file
     */
    private static void Cleaning(File file) {
        try {
            BufferedImage image = ImageIO.read(file);

            //获取图片的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();

            //循环执行除去干扰像素
            for (int i = 1; i < width; i++) {
                Color colorFirst = new Color(image.getRGB(i, 1));
                int numFirstGet = colorFirst.getRed() + colorFirst.getGreen() + colorFirst.getBlue();
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color color = new Color(image.getRGB(x, y));
                        int num = color.getRed() + color.getGreen() + color.getBlue();
                        if (num >= numFirstGet) {
                            image.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }

            //图片背景变黑，验证码变白色
            for (int i = 1; i < width; i++) {
                Color color = new Color(image.getRGB(i, 1));
                int num = color.getRed() + color.getBlue() + color.getGreen();
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color color1 = new Color(image.getRGB(x, y));
                        int num1 = color1.getRed() + color1.getBlue() + color1.getGreen();
                        if (num == num1) {
                            image.setRGB(x, y, Color.BLACK.getRGB());
                        } else {
                            image.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }


            ImageIO.write(image, "JPG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        Cleaning(new File("E:\\验证码图片\\test\\tmp\\2.png"));
//    }


}
