package com.xl.tmall;


import org.junit.Test;

import java.util.Calendar;

public class MyTest {

    @Test
    public void run(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2019,1,20);
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(2019,1,21);
        System.out.println(calendar1.compareTo(calendar2));
    }

}
