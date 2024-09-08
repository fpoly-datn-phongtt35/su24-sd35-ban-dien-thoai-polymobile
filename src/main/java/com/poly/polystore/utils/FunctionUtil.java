package com.poly.polystore.utils;

import com.poly.polystore.core.client.models.request.WarrantyDTO;
import org.springframework.util.ObjectUtils;

import java.util.Date;

public class FunctionUtil {
    public static String compareDate(Date endDate, Date startDate){
        if(!ObjectUtils.isEmpty(endDate) && !ObjectUtils.isEmpty(startDate)){
            if(endDate.getYear() - startDate.getYear() > 0){
                return endDate.getYear() - startDate.getYear() + " Năm";
            }else if(endDate.getMonth() - startDate.getMonth() > 0){
                return endDate.getYear() - startDate.getYear() + " Tháng";
            }if(endDate.getDay() - startDate.getYear() > 0){
                return endDate.getDate() - startDate.getDate() + " Ngày";
            }
        }
        return "";
    }
    public static String getStatus(WarrantyDTO warranty){
        Date endDate = warranty.getThoiGianBH();
        if(!ObjectUtils.isEmpty(endDate)){
            if(endDate.before(new Date()))
                return "Hết hạn bảo hành";
            else return warranty.getTrangThai();
        }
        return "";
    }
}
