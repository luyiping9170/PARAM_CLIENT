package com.dt.param.util;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

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
	 * ��ȡstr��ƴ������ĸ��������ĸ���Ǻ��֣���ֱ�ӷ��أ���������ú��ֵĺ���ƴ������ȡ����ĸ
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
	 * �ж�һ���ַ��Ƿ��������ִ���ͷ
	 * @param word
	 * @return
	 */
	public static boolean startWithChinese(String word){
		return word.matches("^[\u4e00-\u9fa5]");
	}
	
	/**
	 * �ж�һ���ַ��Ƿ�Ϊ�����ַ�
	 * @param c
	 * @return
	 */
	public static boolean isChinese(String c){
		return c.matches("[\u4e00-\u9fa5]+");
	}
	
	/**
	 * �ж�һ���ַ��Ƿ������
	 * @param c
	 * @return
	 */
	public static boolean contacinsChinese(String c){
		return c.matches(".[\u4e00-\u9fa5]+.");
	}
	
	/**
	 * ʹ��pinyin4j������ת��Ϊ����ƴ��
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
	 * Parse a Chinese string into pinyin representation
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
	
	public static String BASE64Encode(String str) throws UnsupportedEncodingException{
		return Base64.encodeBase64String(str.getBytes("UTF-8"));
	}
	
	public static String BASE64Decode(String str) throws UnsupportedEncodingException{
		return new String(Base64.decodeBase64(str),"UTF-8");
	}
	
}
