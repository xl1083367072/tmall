package com.xl.tmall.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

//图片处理工具类
public class ImageUtil {

//    将图片转成jpg类型
    public static BufferedImage change2Jpg(File file){
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(file.getAbsolutePath());
            PixelGrabber  pg = new PixelGrabber(image,0,0,-1,-1,true);
            pg.grabPixels();
            int width = pg.getWidth();
            int height = pg.getHeight();
            final int[] RGB_MASKS = {0xFF0000,0xFF00,0xFF};
            final ColorModel RGB_OPAQUE =  new DirectColorModel(32,RGB_MASKS[0],RGB_MASKS[1],RGB_MASKS[2]);
            DataBuffer buffer = new DataBufferInt((int[])pg.getPixels(),pg.getWidth()*pg.getHeight());
            WritableRaster raster = Raster.createPackedRaster(buffer,width,height,width,RGB_MASKS,null);
            BufferedImage bufferedImage = new BufferedImage(RGB_OPAQUE,raster,false,null);
            return bufferedImage;
        }catch (InterruptedException e){
            e.printStackTrace();;
            return null;
        }
    }

//    修改图片尺寸
    public static Image resizeImage(Image image,int width,int height){
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(image.getScaledInstance(width,height,Image.SCALE_SMOOTH),0,0,null);
        return bufferedImage;
    }

    /**
     *
     * @param src       源目标路径
     * @param width     调整的宽度
     * @param height    调整的高度
     * @param dest      调整后存放的路径
     */
    public static void resizeImage(File src,int width,int height,File dest){
        if(!dest.getParentFile().exists())
            dest.getParentFile().mkdirs();
        try {
            Image image = ImageIO.read(src);
            Image resizeImage = resizeImage(image, width, height);
            ImageIO.write((RenderedImage)resizeImage,"jpg",dest);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
