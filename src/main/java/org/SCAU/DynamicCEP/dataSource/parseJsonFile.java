package org.SCAU.DynamicCEP.dataSource;

import com.alibaba.fastjson2.JSON;
import org.SCAU.model.socialMediaStocks2;
import org.SCAU.model.stockSerializable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mst.StdOut.println;

public class parseJsonFile {
    public static List<stockSerializable> parseJonFile() throws IOException {
        List<stockSerializable> stocks= new ArrayList<>();
        try {
            //读取json文件并转化为list<Map>格式
            File file = new File("input\\social media stocks 2012-2022.json");
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            //jsonString结果不是json数组格式的字符串这里需要拼接成json数组格式的字符串
//            String jsonArrayString = String.format("%s%s%s", "[", jsonString, "]");
            List<Map> list = JSON.parseArray(jsonString, Map.class);

            for(Map j:list){
                stockSerializable stock = new stockSerializable(j.get("date").toString(), j.get("symbol").toString(), j.get("adjClose").toString(), j.get("close").toString(), j.get("high").toString(), j.get("low").toString(), j.get("open").toString(), j.get("volume").toString());
//                println(stock.toString());
                stocks.add(stock);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stocks;
    }
}
