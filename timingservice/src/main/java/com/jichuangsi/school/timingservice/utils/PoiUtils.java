
package com.jichuangsi.school.timingservice.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



/**
 * 生成Excel 并放到指定位置
 *@param filepath 文件路径(要绝对路径)
 *@param filename 文件名称 (如: demo.xls  记得加.xls哦)
 *@param titlelist 标题名称list
 *@param zdlist 字段list
 *@param datalist 数据list (这里也可以改成List<Map<String,String>>  格式的数据)
 *@return 是否正常生成
 *@throws IOException
 *@author:
 *2018年11月24日 上午11:40:39
 * (titlelist  和  zdlist  顺序要一直, 要一一对应)
 */

public class PoiUtils{
public static boolean createExcel(String filepath,String filename,List<String> titlelist,List<String> zdlist,JSONArray datalist) throws IOException{
        boolean success = false;
        try {
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("sheet1");
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row0 = sheet.createRow(0);
        // 添加表头
        for(int i = 0;i<titlelist.size();i++){
        row0.createCell(i).setCellValue(titlelist.get(i));
        }
        //添加表中内容
        for(int row = 0;row<datalist.size();row++){//数据行
        //创建新行
        HSSFRow newrow = sheet.createRow(row+1);//数据从第二行开始
        //获取该行的数据
        @SuppressWarnings("unchecked")
        Map<String,Object> data = (Map<String, Object>) datalist.get(row);

        for(int col = 0;col<zdlist.size();col++){//列
        //数据从第一列开始
        //创建单元格并放入数据
        newrow.createCell(col).setCellValue(data!=null&&data.get(zdlist.get(col))!=null?String.valueOf(data.get(zdlist.get(col))):"");
        }
        }

        //判断是否存在目录. 不存在则创建
        isChartPathExist(filepath);
        //输出Excel文件1
        FileOutputStream output=new FileOutputStream(filepath+filename);
        wb.write(output);//写入磁盘
        output.close();
        success = true;
        } catch (Exception e) {
        success = false;
        e.printStackTrace();
        }
        return success;
        }


/**
 * 判断文件夹是否存在，如果不存在则新建
 *
 * @param dirPath 文件夹路径
 */
private static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
        file.mkdirs();
        }
        }

}