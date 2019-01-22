package com.king.test;

import java.math.BigDecimal;

import org.joda.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        //
        System.out.println(new LocalDateTime("2018-12-31").getWeekOfWeekyear());
        System.out.println(new LocalDateTime("2018-12-31").getWeekyear());
        System.out.println(new LocalDateTime("2018-12-31").toString("yyyy-ww"));
        if(true) {
            return;
        }
        BigDecimal price = new BigDecimal(0);
        price = price.add(new BigDecimal(10));
        //System.out.println(price);
        String str = "2018-2019“美好生活”大调查";
        System.out.println(str.length());
        System.out.println(str.getBytes().length);
        
    }
}
