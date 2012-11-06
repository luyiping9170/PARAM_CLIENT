package com.dt.param.util;

import java.text.Collator;
import java.util.Arrays;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class StringUtil {

	private static net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat t1 = new HanyuPinyinOutputFormat(); 
	static{
        t1.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        t1.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        t1.setVCharType(HanyuPinyinVCharType.WITH_V);  
	}
	
	
	
	public static int strCompare(String str1,String str2,Collator cmp){
		if(str1.equals(str2))
			return 0;
		
		String[] arr = new String[]{str1,str2};
		Arrays.sort(arr,cmp);
		if(str1.equals(arr[0]))
			return -1;
		return 1;
	}
	
	/**
	 * 获取str的拼音首字母，若首字母不是汉字，则直接返回，否则解析该汉字的汉语拼音并获取首字母
	 * @param str
	 * @return
	 */
	public static String getPinyin(String str){
		String py = "";
		for(char c:str.toCharArray()){
			String string = String.valueOf(c);
			if(isChinese(string)){
				py += toPinYin(c) + " ";
			}
			else{
				py += string + " ";
			}
		}
		return py;
	}

	/***
	 * 判断一个字符串是否已中文字串开头
	 * @param word
	 * @return
	 */
	public static boolean startWithChinese(String word){
		return word.matches("^[\u4e00-\u9fa5]");
	}
	
	/**
	 * 判断一个字符是否为中文字符
	 * @param c
	 * @return
	 */
	public static boolean isChinese(String c){
		return c.matches("[\u4e00-\u9fa5]+");
	}
	
	/**
	 * 判断一个字符串是否包含中文
	 * @param c
	 * @return
	 */
	public static boolean contacinsChinese(String c){
		return c.matches(".[\u4e00-\u9fa5]+.");
	}
	
	/**
	 * 使用pinyin4j将汉字转化为汉语拼音
	 * @param str
	 * @return
	 */
	protected static String toPinYin(String str) {  
        String py = "";  
        String[] t = new String[str.length()];  
          
        char [] hanzi=new char[str.length()];  
        for(int i=0;i<str.length();i++){  
            hanzi[i]=str.charAt(i);
        }  
          
        try {  
            for (int i = 0; i < str.length(); i++) {  
                if ((str.charAt(i) >= 'a' && str.charAt(i) < 'z')  
                        || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')  
                        || (str.charAt(i) >= '0' && str.charAt(i) <= '9')) {  
                    py += str.charAt(i);  
                } else {  
                        t = PinyinHelper.toHanyuPinyinStringArray(hanzi[i], t1);  
                        py=py+t[0];  
                    }  
                }
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
  
        return py.trim().toString();  
    }  
	
	/**
	 * 将一个中文字符转为拼音字符串
	 * @param c
	 * @return
	 * @throws BadHanyuPinyinOutputFormatCombination 
	 */
	protected static String toPinYin(char c){
		
		try {
			return PinyinHelper.toHanyuPinyinStringArray(c, t1)[0].trim();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "z";
	}
}
