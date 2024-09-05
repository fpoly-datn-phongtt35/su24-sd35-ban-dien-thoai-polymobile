package com.poly.polystore.utils;

import org.springframework.util.ObjectUtils;

import java.util.Date;

public class FunctionUtil {
    public static String compareDate(Date warranty, Date startDate){
        if(!ObjectUtils.isEmpty(warranty) && !ObjectUtils.isEmpty(startDate)){
            if(warranty.getYear() - startDate.getYear() > 0){
                return warranty.getYear() - startDate.getYear() + " Năm";
            }else if(warranty.getMonth() - startDate.getMonth() > 0){
                return warranty.getYear() - startDate.getYear() + " Tháng";
            }if(warranty.getDay() - startDate.getYear() > 0){
                return warranty.getDate() - startDate.getDate() + " Ngày";
            }
        }
        return "";
    }
}
