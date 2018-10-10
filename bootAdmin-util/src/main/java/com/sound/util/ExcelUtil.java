package com.sound.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;


import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@SuppressWarnings("unused")
public class ExcelUtil {
	private static Logger logger = Logger.getLogger(ExcelUtil.class);

	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	// 显示的导出表的标题
	private String title;
	// 导出表的列名
	private String[] rowName;

	private List<Object[]> dataList = new ArrayList<Object[]>();

	HttpServletResponse response;

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public ExcelUtil() {
	}
	
	public ExcelUtil(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public ExcelUtil(HSSFWorkbook workbook, HSSFSheet sheet) {
		super();
		this.workbook = workbook;
		this.sheet = sheet;
	}

	/**
	 * 创建通用的Excel带标题行信息00000
	 * 
	 * @param workbook
	 *            如果为空 则没有样式
	 * @param sheet
	 *            (创建sheet)
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param rowHeight
	 *            报表的单行行高
	 * @param colNum
	 *            报表的总列数 (合并)
	 * @param fontCaption
	 *            报表行中显示的字符
	 */
	public void createExcelRow(HSSFWorkbook workbook, HSSFSheet sheet,
			int rowNO, int rowHeight, int colNum, String fontCaption) {
		createExcelRow(workbook, sheet, rowNO, -1, colNum, fontCaption, -1,
				null, null);
	}

	/**
	 * 创建通用的Excel行信息000000
	 * 
	 * @param workbook
	 *            如果为空 则没有样式
	 * @param sheet
	 *            (创建sheet)
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param rowHeight
	 *            报表的单行行高
	 * @param colNum
	 *            报表的总列数 (合并)
	 * @param fontCaption
	 *            报表行中显示的字符
	 * @param fontSize
	 *            字体的大小 (字体大小 默认 00)
	 * @param fontWeight
	 *            报表表头显示的字符
	 * @param align
	 *            字体水平位置 (center中间 right右 left左)
	 * @param colNum
	 *            报表的列数
	 */
	public void createExcelRow(HSSFWorkbook workbook, HSSFSheet sheet,
			int rowNO, int rowHeight, int colNum, String fontCaption,
			int fontSize, String fontWeight, String align) {
		if (colNum < 0) {
			logger.debug(this.getClass().getName()
					+ " --> Excel column number is null");
			colNum = 100;
		}

		HSSFRow row = sheet.createRow(rowNO); // 创建第一行
		row.setHeight((short) (rowHeight < 1 ? 300 : rowHeight)); // 设置行高

		HSSFCell cell = row.createCell((short) 0); // 设置第一行
		cell.setCellType(HSSFCell.ENCODING_UTF_16); // 定义单元格为字符串类型
		cell.setCellValue(new HSSFRichTextString(fontCaption));

		sheet.addMergedRegion(new Region(rowNO, (short) 0, rowNO,
				(short) (colNum - 1))); // 指定合并区域

		HSSFCellStyle cellStyle = createCellFontStyle(workbook, fontSize,
				fontWeight, align); // 设定样式
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}

	/**
	 * 设置报表列头0000
	 * 
	 * @param sheet
	 *            (创建sheet)
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param rowHeight
	 *            报表的单行行高
	 * @param columnHeader
	 *            报表行中显示的字符
	 */
	public void createColumnHeader(HSSFSheet sheet, int rowNO, int rowHeight,
			String[] columnHeader) {
		createColumnHeader(sheet, rowNO, rowHeight, columnHeader, -1, null,
				null);
	}

	/**
	 * 设置报表列头
	 * 
	 * @param sheet
	 *            (创建sheet)
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param rowHeight
	 *            报表的单行行高
	 * @param columnHeader
	 *            报表行中显示的字符
	 * @param fontSize
	 *            字体的大小 (字体大小 默认00)
	 */
	public void createColumnHeader(HSSFSheet sheet, int rowNO, int rowHeight,
			String[] columnHeader, int fontSize) {
		createColumnHeader(sheet, rowNO, rowHeight, columnHeader, fontSize,
				null, null);
	}

	/**
	 * 设置报表列头00000
	 * 
	 * @param sheet
	 *            (创建sheet)
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param rowHeight
	 *            报表的单行行高
	 * @param columnHeader
	 *            报表行中显示的字符
	 * @param fontSize
	 *            字体的大小 (字体大小 默认00)
	 * @param fontWeight
	 *            报表表头显示的字符
	 * @param align
	 *            字体水平位置 (center中间 right右 left左)
	 */
	public void createColumnHeader(HSSFSheet sheet, int rowNO, int rowHeight,
			String[] columnHeader, int fontSize, String fontWeight, String align) {
		if (columnHeader == null || columnHeader.length < 1) {
			logger.debug(this.getClass().getName()
					+ " --> Excel columnHeader is null");
			return;
		}
		HSSFRow row = sheet.createRow(rowNO);
		row.setHeight((short) rowHeight);

		HSSFCellStyle cellStyle = createCellFontStyle(workbook, fontSize,
				fontWeight, align);
		if (cellStyle != null) {
			cellStyle.setFillForegroundColor((short) 11);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}

		HSSFCell cell = null;
		for (int i = 0; i < columnHeader.length; i++) {
			sheet.setColumnWidth((short) i, (short) (20 * 256)); // 设置列宽，0个字符宽度。宽度参数为/，故乘以
			cell = row.createCell((short) i);
			cell.setCellType(HSSFCell.ENCODING_UTF_16);
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
			cell.setCellValue(new HSSFRichTextString(columnHeader[i]));
		}
	}

	/**
	 * 创建数据行00000000
	 * 
	 * @param sheet
	 *            (创建sheet)
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param rowHeight
	 *            报表的单行行高
	 * @param columnData
	 *            报表行中显示的数据
	 * @param jsonAttr
	 *            json中key值的string数组
	 * @param maxValue
	 *            Excel显示的最大上限
	 */
/*	public HSSFSheet createColumnData(HSSFSheet sheet, int rowNO,
			JSONArray columnData, String jsonAttr[], int maxValue, int rowHeight) {
		maxValue = (maxValue < 1 || maxValue > 65533) ? 65533 : maxValue;
		int currRowNO = rowNO;
		for (int numNO = currRowNO; numNO < columnData.size() + currRowNO; numNO++) {
			if (numNO % (maxValue + 2) == 0) {
				sheet = workbook.createSheet();
				rowNO = 0;
			}
			createColumnDataDesc(sheet, numNO, rowNO, currRowNO, rowHeight,
					columnData, jsonAttr);
			rowNO++;
		}
		return sheet;
	}*/

	/**
	 * 创建数据行0000000
	 * 
	 * @param sheet
	 *            (创建sheet)
	 * @param numNO
	 *            序列号
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param currRowNO
	 *            初始行号
	 * @param rowHeight
	 *            报表的单行行高
	 * @param columnData
	 *            报表行中显示的数据
	 */
/*	private void createColumnDataDesc(HSSFSheet sheet, int numNO, int rowNO,
			int currRowNO, int rowHeight, JSONArray columnData,
			String jsonAttr[]) {
		createColumnDataDesc(sheet, numNO, rowNO, currRowNO, rowHeight,
				columnData, jsonAttr, -1, null, null);
	}*/

	/**
	 * 创建数据行000000000
	 * 
	 * @param sheet
	 *            (创建sheet)
	 * @param numNO
	 *            序列号
	 * @param rowNO
	 *            报表的单行行号(创建第几行)
	 * @param currRowNO
	 *            初始行号
	 * @param rowHeight
	 *            报表的单行行高
	 * @param columnData
	 *            报表行中显示的数据
	 * @param fontSize
	 *            字体的大小 (字体大小 默认00)
	 * @param fontWeight
	 *            报表表头显示的字符
	 * @param align
	 *            字体水平位置 (center中间 right右 left左)
	 */
/*	private void createColumnDataDesc(HSSFSheet sheet, int numNO, int rowNO,
			int currRowNO, int rowHeight, JSONArray columnData,
			String jsonAttr[], int fontSize, String fontWeight, String align) {
		if (columnData == null || columnData.size() < 1) {
			logger.debug(this.getClass().getName()
					+ " --> Excel columnData is null");
		}
		HSSFRow row = sheet.createRow(rowNO);
		row.setHeight((short) rowHeight);

		HSSFCellStyle cellStyle = null; // createCellFontStyle(workbook,
										// fontSize, fontWeight, align);
		cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 指定单元格居中对齐
		HSSFCell cell = null;
		JSONObject jsonrow = (JSONObject) columnData.get(numNO - currRowNO);
		for (int i = 0; i < jsonAttr.length; i++) {
			sheet.setColumnWidth((short) i, (short) (20 * 256)); // 设置列宽，0个字符宽度。宽度参数为/，故乘以
			cell = row.createCell((short) i);
			cell.setCellType(HSSFCell.ENCODING_UTF_16);
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
			cell.setCellValue(new HSSFRichTextString(jsonrow
					.containsKey(jsonAttr[i]) ? jsonrow.getString(jsonAttr[i])
					: ""));
		}
	}
*/
	/**
	 * 创建通用的Excel最后一行的信息 (创建合计行 (最后一行))
	 * 
	 * @param workbook
	 *            如果为空 则没有样式
	 * @param sheet
	 * @param colNum
	 *            报表的总列数 (合并)
	 * @param fontCaption
	 *            报表行中显示的字符
	 * @param fontSize
	 *            字体的大小 (字体大小 默认00)
	 * @param fontWeight
	 *            报表表头显示的字符
	 * @param align
	 *            字体水平位置 (center中间 right右 left左)
	 * @param colNum
	 *            报表的列数 (需要合并到的列索引)
	 * 
	 */
	public void createSummaryRow(HSSFWorkbook workbook, HSSFSheet sheet,
			int colNum, String fontCaption, int fontSize, String fontWeight,
			String align) {

		HSSFCellStyle cellStyle = createCellFontStyle(workbook, fontSize,
				fontWeight, align);

		HSSFRow lastRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
		HSSFCell sumCell = lastRow.createCell((short) 0);

		sumCell.setCellValue(new HSSFRichTextString(fontCaption));
		if (cellStyle != null) {
			sumCell.setCellStyle(cellStyle);
		}
		sheet.addMergedRegion(new Region(sheet.getLastRowNum(), (short) 0,
				sheet.getLastRowNum(), (short) (colNum - 1))); // 指定合并区域
	}

	/**
	 * 设置字体样式 (字体为宋体 ，上下居中对齐，可设置左右对齐，字体粗细，字体大小 )
	 * 
	 * @param workbook
	 *            如果为空 则没有样式
	 * @param fontSize
	 *            字体大小 默认 00
	 * @param fontWeight
	 *            字体粗细 ( 值为bold 为加粗)
	 * @param align
	 *            字体水平位置 (center中间 right右 left左)
	 */
	public HSSFCellStyle createCellFontStyle(HSSFWorkbook workbook,
			int fontSize, String fontWeight, String align) {
		if (workbook == null) {
			logger.debug(this.getClass().getName()
					+ " --> Excel HSSFWorkbook FontStyle is not set");
			return null;
		}

		HSSFCellStyle cellStyle = workbook.createCellStyle();

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		if (align != null && align.equalsIgnoreCase("left")) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 指定单元格居中对齐
		}
		if (align != null && align.equalsIgnoreCase("right")) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 指定单元格居中对齐
		}

		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 指定单元格垂直居中对齐
		cellStyle.setWrapText(true); // 指定单元格自动换行

		// 单元格字体
		HSSFFont font = workbook.createFont();
		if (fontWeight != null && fontWeight.equalsIgnoreCase("normal")) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		} else {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}

		font.setFontName("宋体");
		font.setFontHeight((short) (fontSize < 1 ? 200 : fontSize));
		cellStyle.setFont(font);

		return cellStyle;
	}

	public void exportExcel(String fileName) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(fileName));
			workbook.write(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportExcelToweb(String fileName, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="
				+ java.net.URLEncoder.encode(fileName, "utf-8"));
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 利用模板导出Excel
	 * 
	 * @param inputFile
	 *            输入模板文件路径
	 * @param outputFile
	 *            输入文件存放于服务器路径
	 * @param dataList
	 *            待导出数据
	 * @throws Exception
	 * @roseuid:
	 */
	@SuppressWarnings("deprecation")
	public void exportExcelFile(String inputFileName, String outputFileName,
			List<?> dataList) throws Exception {
		// 用模板文件构造poi
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
				inputFileName));
		// 创建模板工作表
		HSSFWorkbook templatewb = new HSSFWorkbook(fs);
		// 直接取模板第一个sheet对象
		HSSFSheet templateSheet = templatewb.getSheetAt(1);
		if (dataList.size() % 65535 == 0) {
			templateSheet = templatewb.createSheet();
		}
		// 得到模板的第一个sheet的第一行对象 为了得到模板样式
		HSSFRow templateRow = templateSheet.getRow(0);

		// 取得Excel文件的总列数
		int columns = templateSheet.getRow((short) 0)
				.getPhysicalNumberOfCells();
		logger.debug("columns   is   :   " + columns);
		// 创建样式数组
		HSSFCellStyle styleArray[] = new HSSFCellStyle[columns];

		// 一次性创建所有列的样式放在数组里
		for (int s = 0; s < columns; s++) {
			// 得到数组实例
			styleArray[s] = templatewb.createCellStyle();
		}
		// 循环对每一个单元格进行赋值
		// 定位行
		for (int rowId = 1; rowId < dataList.size(); rowId++) {
			// 依次取第rowId行数据 每一个数据是valueList
			List<?> valueList = (List<?>) dataList.get(rowId - 1);
			// 定位列
			for (int columnId = 0; columnId < columns; columnId++) {
				// 依次取出对应与colunmId列的值
				// 每一个单元格的值
				String dataValue = (String) valueList.get(columnId);
				// 取出colunmId列的的style
				// 模板每一列的样式
				HSSFCellStyle style = styleArray[columnId];
				// 取模板第colunmId列的单元格对象
				// 模板单元格对象
				HSSFCell templateCell = templateRow.getCell((short) columnId);
				// 创建一个新的rowId行 行对象
				// 新建的行对象
				HSSFRow hssfRow = templateSheet.createRow(rowId);
				// 创建新的rowId行 columnId列 单元格对象
				// 新建的单元格对象
				HSSFCell cell = hssfRow.createCell((short) columnId);
				// 如果对应的模板单元格 样式为非锁定
				if (templateCell.getCellStyle().getLocked() == false) {
					// 设置此列style为非锁定
					style.setLocked(false);
					// 设置到新的单元格上
					cell.setCellStyle(style);
				}
				// 否则样式为锁定
				else {
					// 设置此列style为锁定
					style.setLocked(true);
					// 设置到新单元格上
					cell.setCellStyle(style);
				}
				// 设置编码
				// cell.setEncoding(HSSFCell.ENCODING_UTF_);
				// Debug.println( "dataValue   :   " + dataValue);
				// 设置值 统一为String
				cell.setCellValue(dataValue);
			}
		}
		// 设置输入流
		FileOutputStream fOut = new FileOutputStream(outputFileName);
		// 将模板的内容写到输出文件上
		templatewb.write(fOut);
		fOut.flush();

		// 操作结束，关闭文件
		fOut.close();
	}

	public List<List<String>> getBlacklists() {
		List<List<String>> result = new ArrayList<List<String>>();
		int rowNum = sheet.getLastRowNum();

		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			List<String> item = new ArrayList<String>();
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(0);
			for (int j = 0; j < 4; j++) {
				item.add(row.getCell(j).getStringCellValue());
			}
		}
		return result;
	}

	public void export(HttpServletResponse httpResponse) throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
			HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

			// 产生表格标题行
			HSSFRow rowm = sheet.createRow(0);
			HSSFCell cellTiltle = rowm.createCell((short) 0);

			// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
			HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
			HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象

			// sheet.addMergedRegion(new CellRangeAddress(0, 1, 0,
			// (rowName.length - 1)));
			cellTiltle.setCellStyle(columnTopStyle);
			cellTiltle.setCellValue(title);

			// 定义所需列数
			int columnNum = rowName.length;
			HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				HSSFCell cellRowName = rowRowName.createCell((short) n); // 创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}

			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < dataList.size(); i++) {

				Object[] obj = dataList.get(i);// 遍历每个对象
				HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数

				for (int j = 0; j < obj.length; j++) {
					HSSFCell cell = null; // 设置单元格的数据类型
					if (j == 0) {
						cell = row.createCell((short) j,
								HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(i + 1);
					} else {
						cell = row.createCell((short) j,
								HSSFCell.CELL_TYPE_STRING);
						if (!"".equals(obj[j]) && obj[j] != null) {
							cell.setCellValue(obj[j].toString()); // 设置单元格的值
						}
					}
					cell.setCellStyle(style); // 设置单元格样式
				}
			}
			// 让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth((short) colNum) / 256;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
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
			}

			if (workbook != null) {
				try {
					String fileName = "Excel-"
							+ String.valueOf(System.currentTimeMillis())
									.substring(4, 13) + ".xls";
					String headStr = "attachment; filename=\"" + fileName
							+ "\"";
					response = httpResponse;
					response.setContentType("APPLICATION/OCTET-STREAM");
					response.setHeader("Content-Disposition",
							java.net.URLEncoder.encode(headStr, "UTF-8"));
					OutputStream out = response.getOutputStream();
					workbook.write(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 列头单元格样式
	 */
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}

	/*
	 * 列数据信息单元格样式
	 */
	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}
	
	public void exportBySXSSFWorkbook(String title, String[] rowsName, List<Map<String, Object>> dataList, String[] key, String fileName, String filetitle){
		logger.info("execute job begin");
		List<String> headerList = Lists.newArrayList(rowsName);
		long begin = System.currentTimeMillis();
		FileOutputStream out = null;
		Workbook wb = null;
		try{
		   wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
												// exceeding rows will be
												// flushed to disk
		Sheet sh = wb.createSheet(title);
		Row headerRow = sh.createRow(0);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
//			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = sh.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sh.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) { 
			int colWidth = sh.getColumnWidth(i)*2; 
			colWidth = colWidth < 3000 ? 3000 : colWidth;
			colWidth = colWidth > 25*256 ? 25*256 : colWidth ;
			sh.setColumnWidth(i, colWidth);  
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int rownum = 0; rownum < dataList.size(); rownum++) {
			Map<String, Object> map  = dataList.get(rownum);
			Row row = sh.createRow(rownum+1);
			Cell cell = null;
			for(int k = 0 ; k<key.length ; k++){
				cell = row.createCell(k);
				  if(key[k].indexOf("time")>0 && map.get(key[k]) != null){
					  cell.setCellValue(df.format(map.get(key[k])));
				  }else if(key[k].indexOf("time")>0 && map.get(key[k]) == null){
					  cell.setCellValue("");
				  }else{
					  cell.setCellValue(map.get(key[k])+"");
				  }
			  }
		}
		out = new FileOutputStream(Const.URL+fileName+"/"+filetitle);
		wb.write(out);
		dataList.clear();
		dataList = null;
		wb = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		try {
			if(out != null){
				out.close();
				out = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		long end = System.currentTimeMillis();
		logger.info((end - begin) + "ms");
		logger.info("execute job finished");
	}
}
