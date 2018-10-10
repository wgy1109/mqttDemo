package com.sound.plugin;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.sound.util.*;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MyBatisPagePlugin implements Interceptor {
	private static String dialect = ""; //
	private static String pageSqlId = ""; //

	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
					"mappedStatement");
			if (mappedStatement.getId().matches(".*Page$")) {
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject 未初始化");
				} else {
					Connection connection = (Connection) invocation.getArgs()[0];
					// String sql = boundSql.getSql();
					String sql = modifyLikeSql(boundSql.getSql(), parameterObject);
					String countSql = "select count(1) from (" + sql + ") temp_count";
					PreparedStatement countStatement = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
							boundSql.getParameterMappings(), parameterObject);
					setParameters(countStatement, mappedStatement, countBS, parameterObject);
					ResultSet rs = countStatement.executeQuery();
					int count = 0;
					while (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStatement.close();
					PageInfo page = null;
					if (parameterObject instanceof PageInfo) {
						page = (PageInfo) parameterObject;
						page.setTotalSize(count);
					} else {
						Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "page");
						if (pageField != null) {
							page = (PageInfo) ReflectHelper.getValueByFieldName(parameterObject, "page");
							if (page == null)
								page = new PageInfo();
							page.setTotalSize(count);
							ReflectHelper.setValueByFieldName(parameterObject, "page", page);
						} else {
							throw new NoSuchFieldException(parameterObject.getClass().getName() + "������ page ���ԣ�");
						}
					}
					String pageSql = generatePageSql(sql, page);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // ����ҳsql��䷴���BoundSql.
				}
			} else {
				BoundSql boundSql = delegate.getBoundSql();
				if (boundSql.getSql().indexOf("like") > 0) {
					Object parameterObject = boundSql.getParameterObject();
					String sql = modifyLikeSql(boundSql.getSql(), parameterObject);
					System.out.println(sql);
					ReflectHelper.setValueByFieldName(boundSql, "sql", sql); // ����ҳsql��䷴���BoundSql.
				}
			}
		}
		return invocation.proceed();
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (Tools.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		pageSqlId = p.getProperty("pageSqlId");
		if (Tools.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql, PageInfo page) {
		if (page != null && Tools.notEmpty(dialect)) {
			if (page.getCurrentPage() == 0) {
				page.setCurrentPage(1);
			} else {
				page.setCurrentPage(page.getCurrentPage());
			}
			int currentPage = page.getCurrentPage();
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + (currentPage - 1) * page.getPageSize() + "," + page.getPageSize());
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") tmp_tb where ROWNUM<=");
				pageSql.append(currentPage * page.getPageSize() + page.getPageSize());
				pageSql.append(") where row_id>");
				pageSql.append(currentPage * page.getPageSize());
			} else if ("postgre".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + page.getPageSize() + " offset " + (currentPage - 1) * page.getPageSize());
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	/**
	 * ��SQL����(?)��ֵ,�ο�org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String modifyLikeSql(String sql, Object parameterObject) {
		if (!sql.toLowerCase().contains("like"))
			return sql;
		// 修改参数
		Map<String, Object> valMap = new HashMap<String, Object>();
		if (parameterObject instanceof Map) {
			valMap = (Map<String, Object>) parameterObject;
		} else if (parameterObject instanceof String) {
			return sql;
		} else {
			PageInfo paramMap = (PageInfo) parameterObject;
			valMap = paramMap.getFormData();
		}
		for (Object str : valMap.keySet()) {
			Object val = valMap.get(str);
			if (val != null && val instanceof String
					&& (val.toString().contains("%") || val.toString().contains("_"))) {
				Pattern pattern = Pattern.compile("%");
				String valueStr = val.toString();
				Matcher findMatcher = pattern.matcher(valueStr);
				int index = 0;
				while (findMatcher.find()) {
					int i = findMatcher.start();
					if (i == 0) {
						valueStr = "/%" + valueStr.substring(1);
						continue;
					}
					if (i == (valueStr.length() - 1)) {
						valueStr = valueStr.substring(0, valueStr.length() - 1) + "/%";
						continue;
					}
					if (!valueStr.substring(i - 1, i).equals("/")) {
						valueStr = valueStr.substring(0, i + index + 1) + "/%" + valueStr.substring(i + index + 2);
						index++;
					}
				}
				valMap.put(str.toString(), valueStr);
			}
		}
		return sql;
	}

}
