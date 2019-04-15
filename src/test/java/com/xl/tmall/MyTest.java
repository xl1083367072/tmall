package com.xl.tmall;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTest {

    @Test
    public void run(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2019,1,20);
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(2019,1,21);
        System.out.println(calendar1.compareTo(calendar2));
    }

    @Test
    public void run2(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,4,28);
        calendar.add(Calendar.DAY_OF_YEAR,7);
        String format = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        System.out.println(format);
    }

}
