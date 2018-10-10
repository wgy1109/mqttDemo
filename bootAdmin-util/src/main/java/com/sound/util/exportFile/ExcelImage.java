package com.sound.util.exportFile;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

public class ExcelImage {
	
	//折线图
	public static File createChartLine(List<Map<String, Object>> list,String title,String xTitle,String yTitle,String num,String url){  
	        //构造数据集合  
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
	        for(int i=0;list!=null && i<list.size();i++){  
	        	Map<String, Object> map = list.get(i);  
	            dataset.addValue(Integer.parseInt(map.get("msg_num").toString()),"数据",map.get("msg_date").toString().substring(8));  
	        }  
	        //核心对象  
	        JFreeChart chart = ChartFactory.createLineChart(title,  //图形的主标题  
	                                            xTitle,                 //X轴外标签的名称  
	                                            yTitle,                         //Y轴外标签的名称  
	                                            dataset,   
	                                            PlotOrientation.VERTICAL,   //图形的显示方式（水平和垂直）   
	                                            true,                       //是否显示子标题                     
	                                            true,                       //是否在图形上显示数值的提示   
	                                            true);                      //是否生成URL地址  
	        //解决主标题的乱码  
	        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 18));  
	        //解决子标题的乱码  
	        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));  
	        chart.getLegend().setPosition(RectangleEdge.RIGHT);//右侧显示子菜单  
	        //获取图表区域对象  
	        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();  
//	      	categoryPlot.setBackgroundPaint(null);  
	        //获取X轴对象  
	        CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();  
	        //获取Y轴对象  
	        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();  
	        //解决X轴上的乱码  
	        categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 15));  
	        //解决X轴外的乱码  
	        categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));  
	        //解决Y轴上的乱码  
	        numberAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 15));  
	        //解决Y轴外的乱码  
	        numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));  
	        //改变Y轴的刻度，默认值从1计算  
	        numberAxis.setAutoTickUnitSelection(false);  
	        NumberTickUnit unit = new NumberTickUnit(Integer.parseInt(num));  
	        numberAxis.setTickUnit(unit);  
	        //获取绘图区域对象  
	        LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();  
	        lineAndShapeRenderer.setBaseShapesVisible(true);//设置转折点  
	        //让数值显示到页面上  
	        lineAndShapeRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
	        lineAndShapeRenderer.setBaseItemLabelsVisible(true);  
	        lineAndShapeRenderer.setBaseItemLabelFont(new Font("宋体", Font.BOLD, 15));  
	        //显示图形  
//	      ChartFrame chartFrame = new ChartFrame("xyz", chart);  
//	      chartFrame.setVisible(true);  
//	      chartFrame.pack();  
	        String filename = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+".png";//用时间作为文件名防止重名的问题发生  
	        File fileurl = new File(url+"excelimg/");
	        if (!fileurl.exists()){
	        	fileurl.mkdirs();
	        }
	        File file = new File(url+"excelimg/"+filename);//保存文件到web容器中  
	        try {  
	            ChartUtilities.saveChartAsPNG(file,chart,600,500);  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return file;  
	    }  
	
	//柱状图
	public static File createChartBar(List<Map<String, Object>> list,String title,String xTitle,String yTitle,String num,String url){  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
        String msg_num  = "msg_num";
        String name = "type_name";
        if(title.indexOf("通道")>0){
        	msg_num  = "allyes";
            name = "message_send_channel";
        }
        for(int i=0;list!=null && i<list.size();i++){  
        	Map<String, Object> map = list.get(i);  
            dataset.addValue(Integer.parseInt(map.get(msg_num).toString()),"数据",map.get(name).toString());  
        } 
  
        JFreeChart  chart = ChartFactory.createBarChart(title, //图表的主标题   
                                            xTitle,//X轴（种类轴）外的标题  
                                            yTitle,//Y轴（值轴）外的标题   
                                            dataset,  //数据的集合  
                                            PlotOrientation.VERTICAL, //图形的显示形式（水平/垂直）  
                                            true, //是否生成子标题   
                                            true, //是否生成提示的工具  
                                            true);  //是否在图像上生成URL路径  
          
        //处理乱码  
        //处理主标题乱码  
        chart.getTitle().setFont(new Font("宋体",Font.BOLD,18));  
        //处理子标题乱码  
        chart.getLegend().setItemFont(new Font("宋体",Font.BOLD,15));  
        chart.getLegend().setPosition(RectangleEdge.RIGHT);//右侧显示子菜单  
        //调出图表区域对象  
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();  
        //获取到X轴  
        CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();  
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45); 
        //获取到Y轴  
        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();  
        //处理X轴外的乱码  
        categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,15));  
        //处理X轴上的乱码  
        categoryAxis.setTickLabelFont(new Font("宋体",Font.BOLD,15));  
        //处理Y轴外的乱码  
        numberAxis.setLabelFont(new Font("宋体",Font.BOLD,15));  
        //处理Y轴上的乱码  
        numberAxis.setTickLabelFont(new Font("宋体",Font.BOLD,15));  
          
        //处理Y轴上的刻度，默认从1开始  
        numberAxis.setAutoTickUnitSelection(false);  
        NumberTickUnit unit = new NumberTickUnit(Integer.parseInt(num));  
        numberAxis.setTickUnit(unit);  
          
        //处理图形，先要获取绘图区域对象  
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();  
        //设置图形的宽度  
//      barRenderer.setMaximumBarWidth(0.1);  
          
        //在图形上显示对应数值  
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
        barRenderer.setBaseItemLabelsVisible(true);  
        barRenderer.setBaseItemLabelFont(new Font("宋体",Font.BOLD,15));  
          
        String filename = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+".png";  
        File fileurl = new File(url+"excelimg/");
        if (!fileurl.exists()){
        	fileurl.mkdirs();
        }
        File file = new File(url+"excelimg/"+filename);  
        try {  
             ChartUtilities.saveChartAsPNG(file,chart,600,500);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return file;  
    }  
	
	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName
	 *            被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
