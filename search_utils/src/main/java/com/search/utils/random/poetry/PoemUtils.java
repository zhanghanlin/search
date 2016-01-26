package com.search.utils.random.poetry;

import java.util.*;

public class PoemUtils {

    private static final String word = "到如今,君知否,谁知道,功名事,须信道,最好是,人间世,从今去,"
            + "凝伫,归去,不如归去,知否,谁信道,倚阑干,到而今,又还是,归去来兮,人不见,当此际,记当年,东风里,"
            + "怎奈向,春去也,须知道,争知道,更那堪,留不住,谩赢得,那堪更,一觞一咏,休休,君不见,家山好,"
            + "归来也,思往事,悠悠,无绪,还知否,追往事,人间天上,最苦是,疏影横斜,空怅望,空惆怅,记年时,"
            + "人间事,又只恐,回首处,夜沈沈,断人肠,早归来,有多少,空凝伫,向尊前,微雨过,情脉脉,斜阳外,无语,"
            + "月明中,朱颜绿鬓,绿鬓朱颜,谁念我,还知么,问何如,不堪回首,东风恶,人何处,人正在,今老矣,从别后,"
            + "倚东风,又何须,多少事,天长地久,安阳好,对东风,对西风,广寒宫殿,归去也,归来晚,愿年年,江南岸,"
            + "空回首,终不似,肠断,肠断处,落花飞絮,西源好,阑干外,风流,飕飕,与谁同,五云深处,人间何处难忘酒,"
            + "人静,从此去,倚西风,分明是,功名富贵,南徐好,岁岁年年,思晴好,想当年,无限事,朝朝暮暮,歌窈窕,"
            + "独自个,竹篱茅舍,纶巾羽扇,良辰美景,记当时,诗曰,醉归来,七十古来稀,人如玉,人尽道,何处,凝望处,"
            + "千古恨,千秋岁,去年今日,向此际,坐中客,天赋与,好天良夜,年年今日,待归来,愁绝,故人何处,明月清风,"
            + "暗香浮动,曲水流觞,浑不似,清绝,盈盈,空肠断,空赢得,算人间,算只有,缘底事,记当日,还又是,道骨仙风,"
            + "都付与,都休问,酒醒时,问人间,问何时,风不定,一声声,二十年,人散后,人易老,从今后,休去,休辞醉,"
            + "依然是,几时休,凭阑久,去天尺五,又谁知,君且住,吾老矣,堪羡,多少恨,夜来风雨,天下事,天如水,如何得,"
            + "嫣然一笑,寂寞,山居好,归去来,心下事,怎知道,思悠悠,恁时节,悄无人,愿岁岁,文章太守,无个事,最关情,"
            + "最好处,有谁知,浮世事,满城风雨,玉骨冰肌,画堂深,登临处,看不足,真个是,知何处,知音少,称寿处,空相忆,"
            + "笑人间,纱窗外,落花流水,长安道,问当年,雨初晴,频回首,风又雨,风流云散,一杯酒,一蓑烟雨,三千岁,"
            + "东风外,人去后,人未老,人道是,今夜里,但怅望,佳人何处,再相逢,冰肌玉骨,净几明窗,凌波微步,凝望久,"
            + "千山万水,卷珠帘,又何妨,又过了,叹人生,君看取,吴头楚尾,地久天长,堪恨处,堪爱处,多应是,夜将阑,"
            + "天付与,天寒日暮,如今憔悴,山无数,帘栊静,广寒宫里,待明朝,忆当年,急管繁弦,恨悠悠,憔悴,携手处,"
            + "无一事,暗香疏影,最难忘,月明风细,有个人人,水悠悠,江南春早,深院宇,深院静,清风明月,画图中,留恋,"
            + "留春不住,相逢处,看明年,算惟有,经行处,绮罗丛里,缓带轻裘,肠欲断,自别后,莫匆匆,行乐处,许多愁,"
            + "试与问,试屈指,谈笑里,谩回首,还知道,送君南浦,都不管,都莫问,酒巡未止,采菱拾翠,长亭路,问谁是,"
            + "难忘处,非烟非雾,风前月下,黯销魂,一叶扁舟,一年一度,一杯相属,一枝枝,一轮明月,下缺,不知今夕何夕,"
            + "东风起,举杯相属,之句,人似玉,人别后,人生行乐,人都道,今夕何夕,仙风道骨,似当年,但回首,但赢得,"
            + "佳丽地,依前是,依然,便从今,便直饶,凝眸,几番风雨,凭谁说,凭阑处,凭阑干,分付与,分携处,别离情绪东风,"
            + "人间,春风,西风,归来,江南,相思,梅花,千里,回首,明月,多少,如今,阑干,年年,万里,一笑,黄昏,当年,"
            + "天涯,相逢,芳草,尊前,一枝,风雨,流水,依旧,风吹,风月,多情,故人,当时,无人,斜阳,不知,不见,深处,"
            + "时节,平生,凄凉,春色,匆匆,功名,一点,无限";
    private static final String[] words = word.split(",");

    /**
     * 获得诗词题目
     */
    public static String getPoemTitle() {
        Random random = new Random();
        int randomNum = random.nextInt(words.length);
        return words[randomNum];
    }

    /**
     * 获得诗句
     */
    public static String getPoemSentence(int type) {
        List<String> twoWordList = new ArrayList<String>();
        List<String> threeWordList = new ArrayList<String>();
        Map<Integer, String> wordMap = new HashMap<Integer, String>();
        for (int i = 0; i < words.length; i++) {
            wordMap.put(i, words[i]);
            switch (wordMap.get(i).length()) {
                case 2:
                    twoWordList.add(wordMap.get(i));
                    break;
                case 3:
                    threeWordList.add(wordMap.get(i));
                    break;
            }
        }
        if (type == 5) {
            return fiveWords(twoWordList, threeWordList);
        } else {
            return sevenWords(twoWordList, threeWordList);
        }
    }

    /**
     * 五言诗
     */
    public static String fiveWords(List<String> list1, List<String> list2) {
        Random random = new Random();
        int randomNum1 = random.nextInt(list1.size());
        int randomNum2 = random.nextInt(list2.size());
        String randomWord = list1.get(randomNum1) + list2.get(randomNum2);
        return randomWord;
    }

    /**
     * 七言诗
     */
    public static String sevenWords(List<String> list1, List<String> list2) {
        Random random = new Random();
        int randomNum1 = random.nextInt(list1.size());
        int randomNum2 = random.nextInt(list2.size());
        String randomWord = list1.get(randomNum1) + list2.get(randomNum2);
        list1.remove(randomNum1);
        int randomNum = random.nextInt(list1.size());
        return list1.get(randomNum) + randomWord;
    }

    public static void main(String[] args) {
        System.out.println(getPoemSentence(5));
        System.out.println(getPoemSentence(7));
    }
}