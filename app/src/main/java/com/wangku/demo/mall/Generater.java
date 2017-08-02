package com.wangku.demo.mall;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class Generater {
    public static ArrayList<Integer> GeneratePicResId(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.mipmap.goods_pic);
        list.add(R.mipmap.goods_pic1);
        list.add(R.mipmap.goods_pic2);
        list.add(R.mipmap.goods_pic3);
        return list;
    }

    public static Param GenerateParam() {
        String paramStr="{\"spec\":[{\"specName\":\"颜色\",\"specValue\":[\"黑色\",\"红色\",\"粉色\",\"白色\",\"蓝色\"]},{\"specName\":\"克重\",\"specValue\":[\"80g\",\"40g\",\"70g\",\"60g\",\"50g\"]}],\"sku\":[{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"黑色\",\"80g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"黑色\",\"40g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"黑色\",\"70g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"黑色\",\"60g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"黑色\",\"50g\"]}\n" +
                ",{ \"inventoryCount\" : 1000\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"红色\",\"80g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"红色\",\"40g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"红色\",\"70g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"红色\",\"60g\"]}\n" +
                ",{ \"inventoryCount\" : 1000\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"红色\",\"50g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"粉色\",\"80g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"粉色\",\"40g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"粉色\",\"70g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"粉色\",\"60g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"粉色\",\"50g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"白色\",\"80g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"白色\",\"40g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"白色\",\"70g\"]}\n" +
                ",{ \"inventoryCount\" : 1000\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"白色\",\"60g\"]}\n" +
                ",{ \"inventoryCount\" : 1000\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"白色\",\"50g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"蓝色\",\"80g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"蓝色\",\"40g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"蓝色\",\"70g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"蓝色\",\"60g\"]}\n" +
                ",{ \"inventoryCount\" : 0\n" +
                "     , \"id\" : 355\n" +
                "     , \"spec\" : [\"蓝色\",\"50g\"]}\n" +
                "\n" +
                "]}";
        Param param = new Gson().fromJson(paramStr, Param.class);
        return param;
    }
}
