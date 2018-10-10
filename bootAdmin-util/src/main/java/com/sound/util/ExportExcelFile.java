package com.sound.util;

import java.awt.image.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import javax.imageio.*;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.sound.util.exportFile.*;


public class ExportExcelFile {
	
	private static Logger log = LoggerFactory.getLogger(ExportExcelFile.class);
			
	/**
	 * 工作薄对象
	 */
	private XSSFWorkbook wb;
	
	/**
	 * 工作表对象
	 */
	private XSSFSheet sheet;
	
	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;
	
	/**
	 * 当前行号
	 */
	private int rownum;
	
	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 */
	public ExportExcelFile(String title, Class<?> cls){
		this(title, cls, 1);
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public ExportExcelFile(String title, Class<?> cls, int type, int... groups){
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==type)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, f});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, f});
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==type)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, m});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, m});
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String t = ((ExcelField)os[0]).title();
			// 如果是导出，则去掉注释
			if (type==1){
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length==2){
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		initialize(title, "sheet", headerList, null);
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headers 表头数组
	 */
	public ExportExcelFile(String title, String[] headers) {
		initialize(title, "sheet", Lists.newArrayList(headers), null);
	}
	
	public ExportExcelFile() {
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	public ExportExcelFile(String title, String sheetname, List<String> headerList, File file) {
		initialize(title, sheetname, headerList, file);
	}
	
	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	private void initialize(String title, String sheetname, List<String> headerList, File file) {
		this.wb = new XSSFWorkbook();
		this.sheet = wb.createSheet(sheetname);
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
		}
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) { 
			int colWidth = sheet.getColumnWidth(i)*2; 
			log.info("colwidth : "+ colWidth);
			colWidth = colWidth < 3000 ? 3000 : colWidth;
			colWidth = colWidth > 30*256 ? 30*256 : colWidth ;
			log.info("colwidth update 30 end : "+ colWidth);
	        sheet.setColumnWidth(i, colWidth );  
		}
		if(file != null){
	        try {
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
				BufferedImage bufferImg = ImageIO.read(file);     
				ImageIO.write(bufferImg, "png", byteArrayOut);  
				//画图的顶级管理器，
				XSSFDrawing patriarch = sheet.createDrawingPatriarch();     
				//anchor主要用于设置图片的属性  
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,(short) 2, 8, (short) 12, 30);     
				anchor.setAnchorType(3);     
				//插入图片    
				patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), 6));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		log.debug("Initialize success.");
	}
	
	/**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
//		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		return styles;
	}

	/**
	 * 添加一行
	 * @return 行对象
	 */
	public Row addRow(){
		return sheet.createRow(rownum++);
	}
	

	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val){
		return this.addCell(row, column, val, 1, Class.class);
	}
	
	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
		Cell cell = row.createCell(column);
		String cellFormatString = "@";
		try {
			if(val == null){
				cell.setCellValue("");
			}else if(fieldType != Class.class){
				cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
			}else{
				if(val instanceof String) {
					cell.setCellValue((String) val);
				}else if(val instanceof Integer) {
					cell.setCellValue((Integer) val);
					cellFormatString = "0";
				}else if(val instanceof Long) {
					cell.setCellValue((Long) val);
					cellFormatString = "0";
				}else if(val instanceof Double) {
					cell.setCellValue((Double) val);
					cellFormatString = "0.00";
				}else if(val instanceof Float) {
					cell.setCellValue((Float) val);
					cellFormatString = "0.00";
				}else if(val instanceof Date) {
					cell.setCellValue((Date) val);
					cellFormatString = "yyyy-MM-dd HH:mm";
				}else {
					cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
						"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
				}
			}
			if (val != null){
				CellStyle style = styles.get("data_column_"+column);
				if (style == null){
					style = wb.createCellStyle();
					style.cloneStyleFrom(styles.get("data"+(align>=1&&align<=3?align:"")));
			        style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
					styles.put("data_column_" + column, style);
				}
				cell.setCellStyle(style);
			}
		} catch (Exception ex) {
			log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
			cell.setCellValue(val.toString());
		}
		return cell;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	/*public <E> ExportExcelFile setDataList(List<E> list){
		for (E e : list){
			int colunm = 0;
			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
				Object val = null;
				// Get entity value
				try{
					if (StringUtils.isNotBlank(ef.value())){
						val = Reflections.invokeGetter(e, ef.value());
					}else{
						if (os[1] instanceof Field){
							val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
						}else if (os[1] instanceof Method){
							val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
						}
					}
					// If is dict, get dict label
					if (StringUtils.isNotBlank(ef.dictType())){
						val = DictUtils.getDictLabel(val==null?"":val.toString(), ef.dictType(), "");
					}
				}catch(Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					val = "";
				}
				this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
				sb.append(val + ", ");
			}
			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}
		return this;
	}*/
	
	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportExcelFile write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcelFile write(HttpServletResponse response, String fileName) throws IOException{
		response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(fileName));
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param fileName 输出文件名
	 */
	public ExportExcelFile writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}
	
	/**
	 * 清理临时文件
	 */
	public ExportExcelFile dispose(){
//		wb.
		return this;
	}
	
	public void exportlinelist(String fileName, String[] rowName, List<Map<String, Object>> dataLists, String url) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(); // 创建工作簿对象
			List<Object[]> dataList1 = new ArrayList<Object[]>();
			List<Object[]> dataList2 = new ArrayList<Object[]>();
			List<Object[]> dataList3 = new ArrayList<Object[]>();
			List<Map<String, Object>> dataListsimage1 = new ArrayList<>();
			List<Map<String, Object>> dataListsimage2 = new ArrayList<>();
			List<Map<String, Object>> dataListsimage3 = new ArrayList<>();
			for(Map<String, Object> map : dataLists){
				if("1".equals(map.get("msg_type").toString())){
					Object[] objs = new Object[rowName.length];
					int i = 0 ;
					objs[i++] = map.get("msg_date").toString();
					objs[i++] = map.get("msg_num").toString();
					dataList1.add(objs);
					dataListsimage1.add(map);
				}else if("2".equals(map.get("msg_type").toString())){
					Object[] objs = new Object[rowName.length];
					int i = 0 ;
					objs[i++] = map.get("msg_date").toString();
					objs[i++] = map.get("msg_num").toString();
					dataList2.add(objs);
					dataListsimage2.add(map);
				}else if("3".equals(map.get("msg_type").toString())){
					Object[] objs = new Object[rowName.length];
					int i = 0 ;
					objs[i++] = map.get("msg_date").toString();
					objs[i++] = map.get("msg_num").toString();
					dataList3.add(objs);
					dataListsimage3.add(map);
				}
			}
			
			for(int k = 1 ; k < 4; k++){
				List<Object[]> dataList = new ArrayList<Object[]>();
				List<Map<String, Object>> dataListsimage = new ArrayList<>();
				XSSFSheet sheet = null;     			// 创建工作表
				switch(k){
				case 1:
					sheet = workbook.createSheet("行业"); 
					dataList = dataList1;
					dataListsimage = dataListsimage1;
					break;
				case 2:
					sheet = workbook.createSheet("营销"); 
					dataList = dataList2;
					dataListsimage = dataListsimage2;
					break;
				case 3:
					sheet = workbook.createSheet("批发"); 
					dataList = dataList3;
					dataListsimage = dataListsimage3;
					break;
				}
				File file = ExcelImage.createChartLine(dataListsimage, "发送量统计折线图","日期","","100000",url);
				// 产生表格标题行
				XSSFRow rowm = sheet.createRow(0);
				XSSFCell cellTiltle = rowm.createCell((short) 0);
	
				// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
				XSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
				XSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象
	
				// 定义所需列数
				int columnNum = rowName.length;
				XSSFRow rowRowName = sheet.createRow(0); // 在索引2的位置创建行(最顶端的行开始的第二行)
	
				for (int i = 0; i < columnNum; i++) { 		//设定列宽
					int colWidth = sheet.getColumnWidth(i)*2; 
					log.info("colwidth : "+ colWidth);
					colWidth = colWidth < 3000 ? 3000 : colWidth;
					colWidth = colWidth > 35*256 ? 35*256 : colWidth ;
					log.info("colwidth update 35 end : "+ colWidth);
				    sheet.setColumnWidth(i, colWidth);  
				}
				
				// 将列头设置到sheet的单元格中
				for (int n = 0; n < columnNum; n++) {
					XSSFCell cellRowName = rowRowName.createCell((short) n); // 创建列头对应个数的单元格
					cellRowName.setCellType(XSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
					XSSFRichTextString text = new XSSFRichTextString(rowName[n]);
					cellRowName.setCellValue(text); // 设置列头单元格的值
					cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
				}
				
				// 将查询出的数据设置到sheet对应的单元格中
				for (int i =0; i < dataList.size(); i++) {
					Object[] obj = dataList.get(i);// 遍历每个对象
					XSSFRow row = sheet.createRow(i + 1);// 创建所需的行数
					for (int j = 0; j < obj.length; j++) {
						XSSFCell cell = null; // 设置单元格的数据类型
						cell = row.createCell((short) j,
								(short) XSSFCell.CELL_TYPE_STRING);
						if (!"".equals(obj[j]) && obj[j] != null) {
							cell.setCellValue(obj[j].toString()); // 设置单元格的值
						} else {
							cell.setCellValue("");
						}
						cell.setCellStyle(style); // 设置单元格样式
					}
				}
				
				// 让列宽随着导出的列长自动适应
				/*for (int colNum = 0; colNum < columnNum; colNum++) {
					int columnWidth = sheet.getColumnWidth((short) colNum) / 256;
					for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
						XSSFRow currentRow;
						// 当前行未被使用过
						if (sheet.getRow(rowNum) == null) {
							currentRow = sheet.createRow(rowNum);
						} else {
							currentRow = sheet.getRow(rowNum);
						}
						if (currentRow.getCell(colNum) != null) {
							XSSFCell currentCell = currentRow.getCell(colNum);
							if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
								if (currentCell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
									continue;
								}
								int length = currentCell.getStringCellValue()
										.getBytes().length;
								if (columnWidth < length) {
									columnWidth = length;
								}
							}
						}
					}
					if (colNum == 0) {
						sheet.setColumnWidth((short) colNum,
								(short) ((short) (columnWidth - 2) * 256));
					} else {
						sheet.setColumnWidth((short) colNum,
								(short) ((short) (columnWidth + 4) * 256));
					}
				}*/
				
				if(file != null){
			        try {
						ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
						BufferedImage bufferImg = ImageIO.read(file);     
						ImageIO.write(bufferImg, "png", byteArrayOut);  
						//画图的顶级管理器，
						XSSFDrawing patriarch = sheet.createDrawingPatriarch();     
						//anchor主要用于设置图片的属性  
						XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,(short) 4, 4, (short) 15, 26);     
						anchor.setAnchorType(3);     
						//插入图片    
						patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), 6));
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			}
			if (workbook != null) {
				FileOutputStream os = new FileOutputStream(url+fileName);
				workbook.write(os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 列头单元格样式
	 */
	public XSSFCellStyle getColumnTopStyle(XSSFWorkbook workbook) {
		// 设置字体
		XSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

	/*
	 * 列数据信息单元格样式
	 */
	public XSSFCellStyle getStyle(XSSFWorkbook workbook) {
		// 设置字体
		XSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	
	public void textImage(){
		FileOutputStream fileOut = null;     
        BufferedImage bufferImg = null;     
        try {  
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
            bufferImg = ImageIO.read(new File("E:/user/target/20161027162258.png"));     
            ImageIO.write(bufferImg, "png", byteArrayOut);  
              
            HSSFWorkbook wb = new HSSFWorkbook();     
            HSSFSheet sheet1 = wb.createSheet("test picture");    
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
            HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();     
            //anchor主要用于设置图片的属性  
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 2, 10, (short) 12, 30);     
            anchor.setAnchorType(3);     
            //插入图片    
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));   
            fileOut = new FileOutputStream("E:/user/target/export2.xlsx");     
            // 写入excel文件     
             wb.write(fileOut);     
             System.out.println("----Excle文件已生成------");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            if(fileOut != null){  
                 try {  
                    fileOut.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
	}
	
	public void addSheet(String oldfile, String title, String sheetname, List<Map<String, Object>> listAnalysis, List<String> headerList, File file){
		String msg_num  = "msg_num";
        String name = "type_name";
        if(oldfile.indexOf("通道")>0){
        	msg_num  = "allyes";
            name = "message_send_channel";
        }
		try {
			int rownum = 0;
			File excel = new File(oldfile);  // 读取文件
			FileInputStream in = new FileInputStream(excel); // 转换为流
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheetx = workbook.createSheet(sheetname);
			Map<String, CellStyle> styles = createStyles(workbook);
			// Create title
			if (StringUtils.isNotBlank(title)){
				Row titleRow = sheetx.createRow(rownum++);
				titleRow.setHeightInPoints(30);
				Cell titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(styles.get("title"));
				titleCell.setCellValue(title);
				sheetx.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
						titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
			}
			if (headerList == null){
				throw new RuntimeException("headerList not null!");
			}
			Row headerRow = sheetx.createRow(rownum++);
			headerRow.setHeightInPoints(16);
			for (int i = 0; i < headerList.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellStyle(styles.get("header"));
				String[] ss = StringUtils.split(headerList.get(i), "**", 2);
				if (ss.length==2){
					cell.setCellValue(ss[0]);
					Comment comment = sheetx.createDrawingPatriarch().createCellComment(
							new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
					comment.setString(new XSSFRichTextString(ss[1]));
					cell.setCellComment(comment);
				}else{
					cell.setCellValue(headerList.get(i));
				}
				sheetx.autoSizeColumn(i);
			}
			for (int i = 0; i < headerList.size(); i++) { 
				int colWidth = sheetx.getColumnWidth(i)*2; 
				log.info("colwidth : "+ colWidth);
				colWidth = colWidth < 3000 ? 3000 : colWidth;
				colWidth = colWidth > 25*256 ? 25*256 : colWidth ;
				log.info("colwidth update 25 end : "+ colWidth);
			    sheetx.setColumnWidth(i, colWidth);  
			}
			// 将查询出的数据设置到sheet对应的单元格中
			for (int i =0; i < listAnalysis.size(); i++) {
				Map<String, Object> map  = listAnalysis.get(i);// 遍历每个对象
				XSSFRow row = sheetx.createRow(rownum++);// 创建所需的行数
				XSSFCell cell = row.createCell((short) 0, (short) XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(map.get(name).toString()); // 设置单元格的值
				cell = row.createCell((short) 1, (short) XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(map.get(msg_num).toString()); // 设置单元格的值
			}
			
			if((file.exists())){
			    try {
					ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
					BufferedImage bufferImg = ImageIO.read(file);     
					ImageIO.write(bufferImg, "png", byteArrayOut);  
					//画图的顶级管理器，
					XSSFDrawing patriarch = sheetx.createDrawingPatriarch();     
					//anchor主要用于设置图片的属性  
					XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,(short) 2, 8, (short) 12, 30);     
					anchor.setAnchorType(3);     
					//插入图片    
					patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), 6));
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			FileOutputStream os = new FileOutputStream(oldfile);
			workbook.write(os);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
