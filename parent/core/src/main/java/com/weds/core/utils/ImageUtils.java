package com.weds.core.utils;

import com.weds.core.utils.coder.Coder;
import org.apache.commons.io.IOUtils;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    /**
     * @param bytes
     * @param type
     * @param path
     * @param root
     * @return
     * @throws Exception
     */
    public static String byteToFile(byte[] bytes, String fileName, String type, String path, String root) throws Exception {
        String fullPath;

        if (StringUtils.isBlank(fileName)) {
            fileName = Coder.md5Hex(bytes).toUpperCase();
        }

        if (StringUtils.isBlank(root)) {
            root = ".";
        }

        if (!StringUtils.isBlank(path)) {
            fullPath = root + File.separator + path + File.separator;
        } else {
            fullPath = root + File.separator;
        }

        File dir = new File(fullPath);
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }

        String filePath = fullPath + fileName + "." + type;

        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            file.deleteOnExit();
        }

        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.write(bytes, fos);
        fos.close();
        return filePath;
    }

    public static String byteToFile(byte[] bytes, String fileName, String type, String path) throws Exception {
        return byteToFile(bytes, fileName, type, path, null);
    }

    public static String byteToFile(byte[] bytes, String type, String path) throws Exception {
        return byteToFile(bytes, null, type, path, null);
    }

    /**
     * Base64生成文件
     *
     * @param img
     * @param type
     * @param path
     * @param root
     * @return
     * @throws Exception
     */
    public static String base64ToFile(String img, String fileName, String type, String path, String root) throws Exception {
        if (img.startsWith("data:image")) {
            img = img.substring(img.indexOf("base64") + 7);
        }
        byte[] bytes = Coder.decryptBASE64(img);
        return byteToFile(bytes, fileName, type, path, root);
    }

    public static String base64ToFile(String img, String fileName, String type, String path) throws Exception {
        return base64ToFile(img, fileName, type, path, null);
    }

    public static String base64ToFile(String img, String type, String path) throws Exception {
        return base64ToFile(img, null, type, path, null);
    }

    /**
     * 文件生成Base64
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String fileToBase64(String path) throws Exception {
        File file = new File(path);
        if (file.isDirectory() || !file.exists()) {
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(fis);
        fis.close();
        return Coder.encryptBASE64(bytes);
    }

    /**
     * 图片生成Base64
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String imgToBase64(String path) throws Exception {
        String fmt = "data:image/%s;base64,%s";
        File file = new File(path);
        if (file.isDirectory() || !file.exists()) {
            return null;
        }
        String type = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
        String base64 = fileToBase64(path);
        return String.format(fmt, type, base64);
    }

    /**
     * 图片缩放
     *
     * @param bytes  原图片
     * @param minLen 比例缩放宽度
     * @throws Exception
     */
    public static String byteToImageScale(byte[] bytes, String fileName, String type, String path, String root, int minLen) throws Exception {
        String fullPath;

        if (StringUtils.isBlank(fileName)) {
            fileName = Coder.md5Hex(bytes).toUpperCase();
        }

        if (StringUtils.isBlank(root)) {
            root = ".";
        }

        if (!StringUtils.isBlank(path)) {
            fullPath = root + File.separator + path + File.separator;
        } else {
            fullPath = root + File.separator;
        }

        File dir = new File(fullPath);
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }

        String filePath = fullPath + fileName + "." + type;

        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            file.deleteOnExit();
        }

        if (minLen <= 0) {
            throw new IllegalArgumentException();
        }

        Image resizedImage = null;
        ImageIcon ii = new ImageIcon(bytes);

        Image i = ii.getImage();

        int iWidth = i.getWidth(null);
        int iHeight = i.getHeight(null);

        int maxTemp = Math.max(iHeight, iWidth);

        if (maxTemp > minLen) {
            if (iWidth > iHeight) {
                resizedImage = i.getScaledInstance(minLen, (minLen * iHeight) / iWidth, Image.SCALE_SMOOTH);
            } else {
                resizedImage = i.getScaledInstance((minLen * iWidth) / iHeight, minLen, Image.SCALE_SMOOTH);
            }
        } else {
            resizedImage = i.getScaledInstance(iWidth, iHeight, Image.SCALE_SMOOTH);
        }

        // 1这个代码确保图像中的所有像素的加载。
        Image temp = new ImageIcon(resizedImage).getImage();

        // 2创建缓冲的图像。
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // 3将图像复制到缓冲的图像。
        Graphics g = bufferedImage.createGraphics();

        // 4背景清晰的图像和画。
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        // 4软化。
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        OutputStream out = new FileOutputStream(file);
        ImageIO.write(bufferedImage, type, out);
        out.close();

        return filePath;
    }

    public static String byteToImageScale(byte[] bytes, String type, String path, String root, int minLen) throws Exception {
        return byteToImageScale(bytes, null, type, path, root, minLen);
    }

    public static void makeFct(String inFile) throws IOException {
        int[] head = {0x01, 0x27, 0x51, 0x74, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        File file = new File(inFile);
        String path = file.getParent();
        String name = file.getName();
        String fctName;
        if (name.contains("_")) {
            fctName = name.substring(0, name.indexOf("_")) + ".fct";
        } else {
            fctName = name.substring(0, name.indexOf(".")) + ".fct";
        }
        String outFile = path + File.separator + fctName;
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new FileOutputStream(outFile);

        long len = file.length();
        head[8] = (int) (len % 256);
        head[9] = (int) ((len / 256) % 256);
        head[10] = (int) ((len / 256 / 256) % 256);
        head[11] = (int) ((len / 256 / 256 / 256) % 256);

        for (int i : head) {
            outputStream.write(i);
        }

        byte[] buffer = new byte[1024];
        int byteRead;
        while ((byteRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    /**
     * 为图片添加图片水印
     *
     * @param watermarkUrl 原图
     * @param source       水印图片
     * @param output       制作完成的图片
     * @return
     * @throws IOException
     */
    public static void markImgMark(String watermarkUrl, String source, String output) throws IOException {
        File file = new File(source);
        Image img = ImageIO.read(file);
        int width = img.getWidth(null);//水印宽度
        int height = img.getHeight(null);//水印高度
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIcon imgIcon = new ImageIcon(watermarkUrl);
        Image con = imgIcon.getImage();
        float clarity = 0.6f;//透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, clarity));
        g.drawImage(con, 10, 10, null);//水印的位置
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.dispose();
        File sf = new File(output);
        ImageIO.write(bi, "jpg", sf); // 保存图片
        System.out.println("添加图片水印成功");
    }

    /**
     * 设置文字水印
     *
     * @param sourceImg 源图片路径
     * @param targetImg 保存的图片路径
     * @param watermark 水印内容
     * @param fontColor 水印颜色
     * @param font      水印字体
     * @throws IOException
     */
    public static void addWatermark(String sourceImg, String targetImg, String watermark,
                                    Color fontColor, Font font, Color bgColor) throws IOException {
        File srcImgFile = new File(sourceImg);
        Image srcImg = ImageIO.read(srcImgFile);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.6f));
        g.setFont(font);
        //设置水印的坐标
        // int fontWidth = g.getFontMetrics(g.getFont()).charsWidth(watermark.toCharArray(), 0, watermark.length());
        int fontHeight = g.getFontMetrics(g.getFont()).getHeight();
        int ascent = g.getFontMetrics(g.getFont()).getAscent();
        g.setColor(bgColor);
        int maxLine = watermark.split("\n").length;
        g.fillRect(0, srcImgHeight - maxLine * fontHeight + 2, srcImgWidth, maxLine * fontHeight);
        g.setColor(fontColor);

        // g.drawString("添加水印完成", 0, 0);
        drawString(g, watermark, fontHeight, srcImgWidth, maxLine, 0, srcImgHeight - maxLine * fontHeight + ascent);
        g.dispose();
        // 输出图片
        FileOutputStream outImgStream = new FileOutputStream(targetImg);
        ImageIO.write(bufImg, "jpg", outImgStream);
        System.out.println("添加水印完成");
        outImgStream.flush();
        outImgStream.close();
    }

    public static void addWatermark(String sourceImg, String targetImg, String watermark) throws IOException {
        Font font = new Font(null, Font.BOLD, 20);
        addWatermark(sourceImg, targetImg, watermark, Color.BLACK, font, Color.WHITE);
    }

    /**
     * @param g
     * @param text       文本
     * @param lineHeight 行高
     * @param maxWidth   行宽
     * @param maxLine    最大行数
     * @param left       左边距
     * @param top        上边距
     * @param trim       是否修剪文本（1、去除首尾空格，2、将多个换行符替换为一个）
     * @param lineIndent 是否首行缩进
     */
    private static void drawString(Graphics2D g, String text, float lineHeight, float maxWidth, int maxLine, float left,
                                   float top, boolean trim, boolean lineIndent) {
        if (text == null || text.length() == 0) return;
        if (trim) {
            text = text.replaceAll("\\n+", "\n").trim();
        }
        if (lineIndent) {
            text = "　　" + text.replaceAll("\\n", "\n　　");
        }
        drawString(g, text, lineHeight, maxWidth, maxLine, left, top);
    }

    /**
     * @param g
     * @param text       文本
     * @param lineHeight 行高
     * @param maxWidth   行宽
     * @param maxLine    最大行数
     * @param left       左边距
     * @param top        上边距
     */
    private static void drawString(Graphics2D g, String text, float lineHeight, float maxWidth, int maxLine, float left,
                                   float top) {
        if (text == null || text.length() == 0) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(c);
            int stringWidth = fm.stringWidth(sb.toString());
            if (c == '\n' || stringWidth > maxWidth) {
                if (c == '\n') {
                    i += 1;
                }
                if (maxLine > 1) {
                    g.drawString(text.substring(0, i), left, top);
                    drawString(g, text.substring(i), lineHeight, maxWidth, maxLine - 1, left, top + lineHeight);
                } else {
                    g.drawString(text.substring(0, i - 1) + "…", left, top);
                }
                return;
            }
        }
        g.drawString(text, left, top);
    }
}
