package com.example.sharkren.myapplication.praser;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.example.sharkren.myapplication.model.IosStringNullException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by renyuxiang on 16/11/1.
 */

public class IosStringFileParser {
    private String TAG = this.getClass().getSimpleName();
    private Stack<Integer> charIndexStack;

    public IosStringFileParser() {
        charIndexStack = new Stack<>();
    }

    /**
     * ios的k v将反置
     * 文字内容的空格全部清除，全部小写，然后作为K
     * 文字标题不能做任何处理作为V
     *
     * @param instream
     * @return
     */
    public ArrayMap<String, HashSet<String>> parseIosStringFileToReverseMap(InputStream instream) {
        long time = System.currentTimeMillis();
        ArrayMap<String, HashSet<String>> stringMap = new ArrayMap<>();
        try {
            Log.e(TAG, "IOS Parse start...");
            if (instream != null) {
                InputStreamReader inputReader = new InputStreamReader(instream, "unicode");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                //分行读取
                while ((line = buffReader.readLine()) != null) {
                      /*如果不是以"开头的跳过*/
                    if (!line.startsWith("\"")) {
                        continue;
                    }
                    
                    /*如果行尾不是以分号结尾的，那么分号一定是在下一行*/
                    if (line.charAt(line.length() - 1) != ';') {
                        line += buffReader.readLine();
                        Log.e(TAG, "Connect string:" + line);
                    }

                    String[] array = parseLineV3(line);
                    if (array[0] != null && array[1] != null) {
                        HashSet<String> set = stringMap.get(array[1]);
                        if (set == null) {
                            set = new HashSet<>();
                        }
                        set.add(array[0]);
                        stringMap.put(array[1].replace(" ", "").toLowerCase(), set);
                    }
                }
                instream.close();
                Log.e(TAG, "IOS Parse cost:" + (System.currentTimeMillis() - time));
                Log.e(TAG, "IOS Parse finish");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringMap;
    }

    public ArrayMap<String, String> parseIosStringFileToNormalMap(InputStream instream) {
        long time = System.currentTimeMillis();
        ArrayMap<String, String> stringMap = new ArrayMap<>();
        try {
            Log.e(TAG, "IOS Parse start...");
            if (instream != null) {
                InputStreamReader inputReader = new InputStreamReader(instream, "unicode");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                //分行读取
                while ((line = buffReader.readLine()) != null) {
                      /*如果不是以"开头的跳过*/
                    if (!line.startsWith("\"")) {
                        continue;
                    }
                      /*如果行尾不是以分号结尾的，那么分号一定是在下一行*/
                    if (line.charAt(line.length() - 1) != ';') {
                        line += buffReader.readLine();
                        Log.e(TAG, "Connect string:" + line);
                    }

                    String[] array = parseLineV3(line);
                    if (array[0] != null && array[1] != null) {
                        stringMap.put(array[0], array[1]);
                    }
                }
                instream.close();
                Log.e(TAG, "IOS Parse cost:" + (System.currentTimeMillis() - time));
                Log.e(TAG, "IOS Parse finish");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringMap;
    }

    public void checkFile(InputStream inStream) throws IosStringNullException {
        try {
            if (inStream != null) {
                InputStreamReader inputReader = new InputStreamReader(inStream, "unicode");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                //分行读取
                while ((line = buffReader.readLine()) != null) {
                    //                    Log.e(TAG, "Check Line:" + line);

                    /*如果不是以"开头的跳过*/
                    if (!line.startsWith("\"")) {
                        continue;
                    }

                     /*如果行尾不是以分号结尾的，那么分号一定是在下一行*/
                    if (line.charAt(line.length() - 1) != ';') {
                        line += buffReader.readLine();
                        //                        Log.e(TAG, "Connect string:" + line);
                    }

                    String[] array = parseLineV3(line);

                    if (TextUtils.isEmpty(array[0]) || TextUtils.isEmpty(array[1])) {
                        throw new IosStringNullException();
                    }
                }
                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当堆栈中出现"="三个字符的index时，就认为找到中心等号
     *
     * @param line
     * @return
     */
    public String[] parseLineV3(String line) {
        charIndexStack.clear();
        if (line.startsWith("\"")) {
            line.indexOf('c');
            String[] keyContentArray = new String[2];
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c != '=' && c != '"') {
                    continue;
                }

                if (charIndexStack.size() == 2) {
                    if (c == '"') {
                        charIndexStack.push(i);
                        break;
                    } else {
                        charIndexStack.clear();
                    }
                }

                if (charIndexStack.size() == 1) {
                    if (c == '=') {
                        charIndexStack.push(i);
                    } else {
                        charIndexStack.clear();
                    }
                }

                if (charIndexStack.size() == 0) {
                    if (c == '"') {
                        charIndexStack.push(i);
                    }
                }

            }
            /*pop顺序一定不能乱*/
            keyContentArray[1] = line.substring(charIndexStack.pop() + 1, line.lastIndexOf("\""));
            /*将等号出栈*/
            charIndexStack.pop();
            keyContentArray[0] = line.substring(1, charIndexStack.pop());

            //            Log.e(TAG, "K:" + keyContentArray[0]);
            //            Log.e(TAG, "C:" + keyContentArray[1]);
            return keyContentArray;
        }
        return null;
    }

    public String[] parseLineV2(String line) {
        if (line.startsWith("\"")) {
            String key = null;
            String content = null;
            String[] keyContentArray = new String[2];
            String[] array = line.split("\"");
            String stack = "";
            for (int i = 0; i < array.length; i++) {
                if (array[i].trim().equals("=")
                        && i - 1 >= 0
                        && i + 1 < array.length
                        && !array[i - 1].endsWith("\\")
                        && !array[i + 1].startsWith("\\")) {
                    key = stack;
                    stack = "";
                } else {
                    if (TextUtils.isEmpty(stack)) {
                        stack = array[i];
                    } else {
                        if (i == array.length - 1) {
                            content = stack;
                        } else {
                            stack += "\"" + array[i];
                        }
                    }
                }
                if (i == array.length - 1) {
                    content = stack;
                }
            }

            keyContentArray[0] = key;
            Log.e(TAG, "K:" + key);
            keyContentArray[1] = content;
            Log.e(TAG, "C:" + content);
            return keyContentArray;
        }

        return null;
    }

    public String[] parseLineV1(String line) {
        String copy = line;
        try {
            if (line.startsWith("\"")) {
                String[] array = new String[2];
                //例子："XXXXXXXXXX" = "sdfsdfsdfdsfs"
                //去掉第一个引号
                line = line.substring(line.indexOf("\"") + 1);
                //截取Key部分
                String key = line.substring(0, line.indexOf("\""));
                //将key部分及第二个引号去掉
                line = line.substring(line.indexOf("\"") + 1);
                //直接匹配取出Content
                String content = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));

                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                array[0] = key;
                array[1] = content;
                return array;
            }
        } catch (Exception e) {
            Log.e(TAG, "Line:" + copy);
            e.printStackTrace();
        }

        return null;
    }
}
