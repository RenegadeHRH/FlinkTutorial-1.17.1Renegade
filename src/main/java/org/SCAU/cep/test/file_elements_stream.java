package org.SCAU.cep.test;


import org.SCAU.model.socialMediaStocks2;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.io.FileUtils;

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mst.StdOut.println;

public class file_elements_stream {


    public static void main(String[] args) throws IOException {
//        File file = new File("input\\social media stocks 2012-2022.json");
//        readerMethod(file);
        parseJonFile();
    }
    private static List<socialMediaStocks2> parseJonFile() throws IOException {
        List<socialMediaStocks2> stocks= new ArrayList<>();
        try {
            //读取json文件并转化为list<Map>格式
            File file = new File("input\\social media stocks 2012-2022.json");
            String jsonString = FileUtils.readFileToString(file, "UTF-8");

            //jsonString结果不是json数组格式的字符串这里需要拼接成json数组格式的字符串
//            String jsonArrayString = String.format("%s%s%s", "[", jsonString, "]");
            List<Map> list = JSON.parseArray(jsonString, Map.class);

            for(Map j:list){
                socialMediaStocks2 stock = new socialMediaStocks2(j.get("date").toString(), j.get("symbol").toString(), j.get("adjClose").toString(), j.get("close").toString(), j.get("high").toString(), j.get("low").toString(), j.get("open").toString(), j.get("volume").toString());
                println(stock.toString());
                stocks.add(stock);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stocks;
    }
    private static void readerMethod(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        Reader reader = new InputStreamReader(new FileInputStream(file), "Utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        String jsonStr = sb.toString();
        System.out.println(JSON.parseObject(jsonStr));
    }

}
