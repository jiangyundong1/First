package com.wamdy.coder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.wamdy.entitys.Entitys;
import com.wamdy.entitys.Entitys.Entity;
import com.wamdy.entitys.Field;
import com.wamdy.util.TypeTransform;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CodeMaker
{
	/**
	 * @author yundongJiang
	 * 
	 * 根据生成的xml去创建对应的model,dao,service
	 * 
	 * @param args
	 */
	public void createFreeMarkerModel()
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try
		{
			process();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void process() throws Exception
	{
		Properties p = loadCfg();
		List<HashMap<String, Object>> entitysCfg = loadEntitysCfg(p);

		for(HashMap<String,Object> entityCfg : entitysCfg)
		{
			makeCode(entityCfg,p);
		}
	}
	
	public static Properties loadCfg() throws FileNotFoundException, IOException
	{
		Properties p = new Properties();
		p.load(new FileReader(new File("conf/path.properties")));
		return p;
	}
	
	public static ArrayList<HashMap<String, Object>> loadEntitysCfg(Properties p) throws JAXBException
	{
		ArrayList<HashMap<String,Object>> entitysCfg = new ArrayList<HashMap<String,Object>>();
		JAXBContext jaxbContext = JAXBContext.newInstance(Entitys.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Entitys entitys = (Entitys) unmarshaller.unmarshal(new File(p.getProperty("entityConfiLocation")));
		List<Entity> entityList = entitys.getEntity();
		for(Entity entity : entityList)
		{
			HashMap<String,Object> data = new HashMap<String,Object>();
			data.put("package", entity.getPackage());
			data.put("name", entity.getName());
			data.put("note", entity.getNote());
			data.put("author", entity.getAuthor());
			data.put("asiaName", entity.getAsiaName());
			data.put("tableName", entity.getTableName());
			Field keyField = entity.getFields().getKeyField().getField();
			Map<String,Object> keyFieldInfo = new HashMap<String,Object>();
			keyFieldInfo.put("name", keyField.getName());// 属性名
			keyFieldInfo.put("type", keyField.getType());// 属性类型
			keyFieldInfo.put("dbType", keyField.getDbType());//对应的数据库类型
			keyFieldInfo.put("asiaName", keyField.getAsiaName());//属性别名
			keyFieldInfo.put("note", keyField.getNote());//属性注释
			keyFieldInfo.put("sortable",keyField.getSortable());
			Set<String> importData = new HashSet<String>();
			Class<?> filedType = TypeTransform.transform(keyField.getType());
			if(filedType == Date.class)
			{// 有时间类型要添加,此时应导入data类
				importData.add("java.util.Date");
			}
			data.put("keyField", keyFieldInfo);
			List<Field> normalFields = entity.getFields().getNormalField().getField();
			Map<String,Object> normalFieldInfo = null;
			List<Map<String,Object>> normalFieldsInfo = new ArrayList<Map<String,Object>>();
			
			int curIndex = 1;
			for(Field field : normalFields)
			{
				normalFieldInfo = new HashMap<String,Object>();
				normalFieldInfo.put("name", field.getName());// 属性名
				normalFieldInfo.put("type", field.getType());// 属性类型
				normalFieldInfo.put("dbType", field.getDbType());//对应的数据库类型
				normalFieldInfo.put("asiaName", field.getAsiaName());//属性别名
				normalFieldInfo.put("note", field.getNote());//属性注释
				normalFieldInfo.put("sortable",field.getSortable());
				normalFieldInfo.put("index",curIndex++);
//				temp.put("index", index++);// 顺序号
//				temp.put("isText", false);// 是否是Text类型
				filedType = TypeTransform.transform(field.getType());
				if(filedType == Date.class)
				{// 有时间类型要添加,此时应导入data类
					importData.add("java.util.Date");
				}
				normalFieldsInfo.add(normalFieldInfo);
			}
			
			data.put("normalField", normalFieldsInfo);
			data.put("importData",importData);
			entitysCfg.add(data);
		}
		return entitysCfg;
	}
	
	public static void makeCode(Map<String, Object> data, Properties p) throws IOException, TemplateException
	{
		String basePath = p.getProperty("outputLocation");
		String srcFolder = p.getProperty("srcFolderName");
		String webRootFolder = p.getProperty("webRootFolderName");
		String jsFolder = p.getProperty("jsFolderName");
		String jspFolder= p.getProperty("jspFolderName");
		String basePackage = (String) data.get("package");
		String className = (String) data.get("name");
		String lowerClassName = className.toLowerCase();
		String javaCodeLocation = basePath + srcFolder + basePackage.replace(".", "/");
		String pageCodeLocation = basePath + webRootFolder;
		
		String entityFolderLocation = javaCodeLocation + "/entity/";
		String entitySettingFolderLocation = javaCodeLocation + "/entity/entitySetting/";
		String daoFolderLocation = javaCodeLocation + "/dao/" + lowerClassName + "/";
		String servicesFolderLocation = javaCodeLocation + "/service/" + lowerClassName + "/";
		
		String jsCodeLocation = pageCodeLocation + jsFolder + lowerClassName + "/";
		String jspCodeLocation = pageCodeLocation + jspFolder + lowerClassName + "/";
		
//		String [] folders = new String[ ]{entityFolderLocation, entitySettingFolderLocation, daoFolderLocation, servicesFolderLocation, pageCodeLocation, pageCodeLocation + "js/",
//				jsCodeLocation, jspCodeLocation };
		String[] folders = new String[] {jsCodeLocation,jspCodeLocation,entitySettingFolderLocation,daoFolderLocation,servicesFolderLocation};
		makeFolders(folders);
		FileOutputStream [] foss = new FileOutputStream[ ]{new FileOutputStream(entityFolderLocation + className + ".java"),
//				new FileOutputStream(entitySettingFolderLocation + className + ".xml"),
//				new FileOutputStream(servicesFolderLocation + "I" + className + "Service.java"),
				new FileOutputStream(servicesFolderLocation + className + "Service.java"),
				
				new FileOutputStream(daoFolderLocation + className + "Dao.java")/*,
				
				new FileOutputStream(daoFolderLocation + className + "EntityDao.java"),
				new FileOutputStream(daoFolderLocation + "I" + className + "MapDao.java"),
				new FileOutputStream(daoFolderLocation + className + "MapDao.java"),
				new FileOutputStream(jsCodeLocation + className + "Manage.js"),
				new FileOutputStream(jsCodeLocation + className + "Select.js"),
				new FileOutputStream(jspCodeLocation + className + "Manage.jsp"), 
				new FileOutputStream(jspCodeLocation + className + "Select.jsp")*/};
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(CodeMaker.class, p.getProperty("templatesLocation")); // 指定模板所在的classpath目录
		Template [] templates = new Template[ ]{cfg.getTemplate("Entity.jtl"), // 指定实体模板
//				cfg.getTemplate("Foreign.jtl"), // 指定外键配置模板
//				cfg.getTemplate("IService.jtl"), // 指定服务接口模板
				cfg.getTemplate("Service.jtl"), // 指定服务模板
				cfg.getTemplate("IEntityDao.jtl")/*, // 指定实体DAO接口模板
				cfg.getTemplate("EntityDao.jtl"), // 指定实体DAO模板
				cfg.getTemplate("IMapDao.jtl"), // 指定MapDAO接口模板
				cfg.getTemplate("MapDao.jtl"), // 指定MapDAO模板
				cfg.getTemplate("entityManage.js.jtl"),// 指定前端表格
				cfg.getTemplate("entitySelect.js.jtl"),// 指定前端表格
				cfg.getTemplate("entityManage.jsp.jtl"), // 指定前端管理页面
				cfg.getTemplate("entitySelect.jsp.jtl")*/// 指定前端表格
		};
		for(int i = 0; i < templates.length; i++)
		{
			templates[i].process(data, new OutputStreamWriter(foss[i], "utf-8"));
			foss[i].flush();
			foss[i].close();
		}
		return;
	}
	
	private static void makeFolders(String [] folders)
	{
		for(int i = 0; i < folders.length; i++)
		{
			createFile(new File(folders[i]));
//			System.out.println(folders[i]);
		}
	}
	
	public static void createFile(File file)
	{
		if(file == null)
		{
			return;
		}
		File parentFile = file.getParentFile();
		createFile(parentFile);
		if(!file.exists())
		{
			file.mkdir();
			System.out.println("创建不存在的文件夹：" + file);
		}
	}
}
