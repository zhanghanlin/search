package com.search.utils.string;

import java.util.Random;

public class StringUtils extends org.apache.commons.lang.StringUtils {
    /**
     * 生成随机汉字
     * <p/>
     * 原理是从汉字区位码找到汉字。在汉字区位码中分高位与底位， 且其中简体又有繁体。位数越前生成的汉字繁体的机率越大。
     * 所以在本例中高位从171取，底位从161取， 去掉大部分的繁体和生僻字。但仍然会有
     *
     * @return
     * @throws Exception
     */
    public static String randomChinese() throws Exception {
        String str = null;
        int hightPos, lowPos; // 定义高低位
        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));// 获取高位值
        lowPos = (161 + Math.abs(random.nextInt(93)));// 获取低位值
        byte[] b = new byte[2];
        b[0] = (new Integer(hightPos).byteValue());
        b[1] = (new Integer(lowPos).byteValue());
        str = new String(b, "GBk");// 转成中文
        return str;
    }

    /**
     * 生成count个随即汉字
     *
     * @param count
     * @return
     */
    public static String randomChinese(int count) {
        String str = "";
        for (int i = 0; i < count; i++) {
            try {
                str += randomChinese();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * 获得随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        int result = min + new Double(Math.random() * (max - min)).intValue();
        return result;
    }
}
