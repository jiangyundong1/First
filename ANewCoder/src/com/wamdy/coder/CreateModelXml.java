package com.wamdy.coder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.wamdy.entitys.TableColumn;

public class CreateModelXml {
	/**
	 * @author yundongJiang
	 * @param tableName 表名
	 * @param modelName 实体类名(首字母大写)
	 * @param packageNamePrefix (包名统一前缀)
	 * @param url 
	 * @param user
	 * @param password
	 * @throws IOException
	 */
	public void createModelXml(String tableName, String tableChineseName, String modelName, String packageNamePrefix,String url, String user, String password) throws IOException {
		// //1.第一种 创建文档及设置根元素节点的方式
		// 2.第二种 创建文档及设置根元素节点的方式
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("entitys", "http://www.wamdy.com/entitys");

		// 给根节点添加属性
		root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.addAttribute("xsi:schemaLocation", "http://www.wamdy.com/entitys entitys.xsd");

		// 给根节点添加孩子节点
		Element element1 = root.addElement("entity");
		element1.addElement("package").addText(packageNamePrefix); // 包名前缀
		element1.addElement("name").addText(modelName); // 实体类名称
		element1.addElement("note").addText(tableChineseName);
		element1.addElement("tableName").addText(tableName); //表名

		Element element2 = element1.addElement("fields");
		Element element3 = element2.addElement("keyField");
		Element element4 = element3.addElement("field");

		//通过数据库查询得到指定表的所有列,数据类型,注释
		List<TableColumn> list = new ArrayList<TableColumn>();
		List<String> listNote = new ArrayList<String>();
		Connection conn = DBUtil.getConnection(url, user, password);
		list = DBUtil.getColAndDataTypeByTableName(conn, tableName);
		listNote = DBUtil.getNotesByTableName(conn, tableName);
//		TableColumn tableColumn1 = new TableColumn();
//		tableColumn1.setColumnName("ID");
//		tableColumn1.setDataType("VARCHAR2");
//		list.add(tableColumn1);
//
//		tableColumn1 = new TableColumn();
//		tableColumn1.setColumnName("NAME");
//		tableColumn1.setDataType("VARCHAR2");
//		list.add(tableColumn1);
//
//		tableColumn1 = new TableColumn();
//		tableColumn1.setColumnName("TEL_PHONE");
//		tableColumn1.setDataType("NUMBER");
//		list.add(tableColumn1);
//
//		tableColumn1 = new TableColumn();
//		tableColumn1.setColumnName("CREATE_TIME");
//		tableColumn1.setDataType("DATE");
//		list.add(tableColumn1);
//		
//		String note = "主键ID";
//		listNote.add(note);
//
//		note = "姓名";
//		listNote.add(note);
//
//		note = "电话号码";
//		listNote.add(note);
//
//		note = "创建时间";
//		listNote.add(note);
		
		//xml主键构造
		element4.addElement("name").addText(list.get(0).getColumnName().toLowerCase());
		element4.addElement("type").addText(Tool.dataTypeToBaseType(list.get(0).getDataType()));
		element4.addElement("dbType").addText(Tool.lineToHump(list.get(0).getColumnName())); //数据库字段转驼峰
		element4.addElement("note").addText(Tool.nullToEmpty(listNote.get(0)));

		Element element5 = element2.addElement("normalField");
		
		//xml构造其他字段列
		for (int i = 1; i < list.size(); i++) {
			Element element6 = element5.addElement("field");
			element6.addElement("name").addText(list.get(i).getColumnName().toLowerCase());
			element6.addElement("type").addText(Tool.dataTypeToBaseType(list.get(i).getDataType()));
			element6.addElement("dbType").addText(Tool.lineToHump(list.get(i).getColumnName())); //数据库字段转驼峰
			element6.addElement("note").addText(Tool.nullToEmpty(listNote.get(i)));
		}

		// 把生成的xml文档存放在硬盘上 true代表是否换行
		OutputFormat format = new OutputFormat("    ", true);
		format.setEncoding("utf8");// 设置编码格式
		
		//xml存放路径
		File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
		
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(courseFile+"\\conf\\beans\\Demo.xml"), format);

		xmlWriter.write(document);
		xmlWriter.close();
	}
}
