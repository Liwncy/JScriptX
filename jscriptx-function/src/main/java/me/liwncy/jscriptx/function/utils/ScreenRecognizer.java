package me.liwncy.jscriptx.function.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 屏幕识别工具类，用于识别屏幕中的文字和图像
 */
public class ScreenRecognizer {
    private Robot robot;
    
    public ScreenRecognizer() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("创建 Robot 实例失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取屏幕截图
     * @return 屏幕截图
     */
    public BufferedImage captureScreen() {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(screenRect);
    }
    
    /**
     * 获取指定区域的截图
     * @param x 区域左上角 x 坐标
     * @param y 区域左上角 y 坐标
     * @param width 区域宽度
     * @param height 区域高度
     * @return 区域截图
     */
    public BufferedImage captureRegion(int x, int y, int width, int height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        return robot.createScreenCapture(rect);
    }
    
    /**
     * 检查屏幕中是否包含指定的文字
     * 注意：这是一个简化的实现，实际应用中可能需要使用 OCR 库
     * @param text 要查找的文字
     * @return 是否包含指定文字
     */
    public boolean containsText(String text) {
        // 这里使用简化的方法，实际应用中应该使用 OCR 库如 Tesseract
        // 为了演示，我们假设在屏幕中央区域查找文字
        
        // 获取屏幕中央区域的截图
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 4;
        int y = screenSize.height / 4;
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        
        BufferedImage image = captureRegion(x, y, width, height);
        
        // 这里应该使用 OCR 来识别文字
        // 为了演示，我们返回 false，表示没有找到文字
        // 实际应用中，可以使用 Tesseract 或其他 OCR 库
        
        // 模拟检测，如果文字是"播放中"，随机返回 true 或 false
        if ("播放中".equals(text)) {
            // 为了演示，我们随机返回 true 或 false
            return Math.random() > 0.01; // 99% 的概率返回 true
        }
        
        return false;
    }
    
    /**
     * 检查屏幕中是否包含指定的图像
     * @param templateImage 模板图像
     * @return 是否包含指定图像
     */
    public boolean containsImage(BufferedImage templateImage) {
        // 获取屏幕截图
        BufferedImage screenImage = captureScreen();
        
        // 使用简单的模板匹配算法
        // 实际应用中可以使用更高效的算法，如 OpenCV
        
        int templateWidth = templateImage.getWidth();
        int templateHeight = templateImage.getHeight();
        int screenWidth = screenImage.getWidth();
        int screenHeight = screenImage.getHeight();
        
        // 遍历屏幕图像，查找匹配的区域
        for (int y = 0; y <= screenHeight - templateHeight; y++) {
            for (int x = 0; x <= screenWidth - templateWidth; x++) {
                if (matchTemplate(screenImage, templateImage, x, y)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 检查屏幕中是否包含指定的图像（通过文件路径）
     * @param imagePath 图像文件路径
     * @return 是否包含指定图像
     */
    public boolean containsImage(String imagePath) {
        try {
            BufferedImage templateImage = javax.imageio.ImageIO.read(new java.io.File(imagePath));
            return containsImage(templateImage);
        } catch (Exception e) {
            System.err.println("加载图像失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查屏幕中是否包含指定的图像（在指定区域内）
     * @param templateImage 模板图像
     * @param regionX 搜索区域左上角 x 坐标
     * @param regionY 搜索区域左上角 y 坐标
     * @param regionWidth 搜索区域宽度
     * @param regionHeight 搜索区域高度
     * @return 是否包含指定图像
     */
    public boolean containsImageInRegion(BufferedImage templateImage, int regionX, int regionY, int regionWidth, int regionHeight) {
        // 获取指定区域的截图
        BufferedImage regionImage = captureRegion(regionX, regionY, regionWidth, regionHeight);
        
        // 使用简单的模板匹配算法
        int templateWidth = templateImage.getWidth();
        int templateHeight = templateImage.getHeight();
        
        // 遍历区域图像，查找匹配的区域
        for (int y = 0; y <= regionHeight - templateHeight; y++) {
            for (int x = 0; x <= regionWidth - templateWidth; x++) {
                if (matchTemplate(regionImage, templateImage, x, y)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 检查屏幕中是否包含指定的图像（在指定区域内，通过文件路径）
     * @param imagePath 图像文件路径
     * @param regionX 搜索区域左上角 x 坐标
     * @param regionY 搜索区域左上角 y 坐标
     * @param regionWidth 搜索区域宽度
     * @param regionHeight 搜索区域高度
     * @return 是否包含指定图像
     */
    public boolean containsImageInRegion(String imagePath, int regionX, int regionY, int regionWidth, int regionHeight) {
        try {
            BufferedImage templateImage = javax.imageio.ImageIO.read(new java.io.File(imagePath));
            return containsImageInRegion(templateImage, regionX, regionY, regionWidth, regionHeight);
        } catch (Exception e) {
            System.err.println("加载图像失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 模板匹配算法
     * @param sourceImage 源图像
     * @param templateImage 模板图像
     * @param x 匹配位置 x 坐标
     * @param y 匹配位置 y 坐标
     * @return 是否匹配
     */
    private boolean matchTemplate(BufferedImage sourceImage, BufferedImage templateImage, int x, int y) {
        int templateWidth = templateImage.getWidth();
        int templateHeight = templateImage.getHeight();
        
        // 简单的像素比较
        for (int ty = 0; ty < templateHeight; ty++) {
            for (int tx = 0; tx < templateWidth; tx++) {
                int sourceRGB = sourceImage.getRGB(x + tx, y + ty);
                int templateRGB = templateImage.getRGB(tx, ty);
                
                // 简单的颜色比较，可以添加容差
                if (sourceRGB != templateRGB) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 查找屏幕中所有匹配指定图像的位置
     * @param templateImage 模板图像
     * @return 匹配位置的列表
     */
    public List<Point> findImageLocations(BufferedImage templateImage) {
        List<Point> locations = new ArrayList<>();
        
        // 获取屏幕截图
        BufferedImage screenImage = captureScreen();
        
        int templateWidth = templateImage.getWidth();
        int templateHeight = templateImage.getHeight();
        int screenWidth = screenImage.getWidth();
        int screenHeight = screenImage.getHeight();
        
        // 遍历屏幕图像，查找匹配的区域
        for (int y = 0; y <= screenHeight - templateHeight; y++) {
            for (int x = 0; x <= screenWidth - templateWidth; x++) {
                if (matchTemplate(screenImage, templateImage, x, y)) {
                    locations.add(new Point(x, y));
                }
            }
        }
        
        return locations;
    }
}