package com.sound.util;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class POIUtil {

	public static void exportExcel(String headers, String columns, List<Map<String, Object>> data, String name,
			HttpServletResponse response) throws Exception{
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-disposition", "attachment;filename=" + new String(name.getBytes(),"iso8859-1") + ".xls");
		ServletOutputStream out = response.getOutputStream();
		HSSFWorkbook workBoox = new HSSFWorkbook();
		HSSFSheet sheet = workBoox.createSheet("excel");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = workBoox.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		// 写入title
		String[] titles = headers.split(",");
		String[] cols = columns.split(",");
		for (int i = 0; i < titles.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			HSSFRichTextString text = new HSSFRichTextString(titles[i]);
			cell.setCellValue(text);
			cell.setCellStyle(style);
		}
		HSSFCellStyle centerstyle = workBoox.createCellStyle();
		centerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		centerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		centerstyle.setWrapText(true);
		centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderLeft((short) 1);
		centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderRight((short) 1);
		centerstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		centerstyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色
		// 写入数据
		for (int i = 1; i < data.size()+1; i++) {
			Map<String, Object> rows = data.get(i-1);
			row = sheet.createRow(i);
			HSSFCell cellData;
			int k = 0 ;
			for (String key : cols){
				String v = rows.get(key) == null? "" : rows.get(key).toString();
				cellData = row.createCell((short) k);
				HSSFRichTextString text = new HSSFRichTextString(v);
				cellData.setCellValue(text);
				cellData.setCellStyle(centerstyle);
				k ++ ;
			}
		}
		workBoox.write(out);
		out.close();
	}
}
