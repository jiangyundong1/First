package com.wamdy.entitys;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.wamdy.entitys.Entitys.Entity;

public class MyTest
{
	@Test
	public void testJaxbToXml()
	{
		try
		{
			Entitys entitys = new Entitys();
			ArrayList<Entity> entityss = new ArrayList<Entitys.Entity>();
			entitys.entity = entityss;
			Entity entity = new Entity();
			entity.setAsiaName("用户");
			entity.setAuthor("hzw");
			entity.setName("User");
			entity.setPackage("com.wamdy");
			entity.setTableName("user");
			entityss.add(entity);
			JAXBContext ct = JAXBContext.newInstance(Entitys.class);
			Marshaller marshaller = ct.createMarshaller();
			marshaller.marshal(entitys, System.out);
//			FileOutputStream fio = new FileOutputStream(Entitys.class.getClassLoader().toString()+"/conf/create.xml");
//			marshaller.marshal(entitys, fio);
//			fio.close();
		}catch(JAXBException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestjaxbToObject()
	{
		try
		{
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><entitys xmlns=\"http://www.wamdy.com/entitys\"><entity><package>com.wamdy</package><name>User</name><asiaName>用户</asiaName><author>hzw</author><tableName>user</tableName></entity></entitys>";
			JAXBContext jaxbContext = JAXBContext.newInstance(Entitys.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Entitys entitys = (Entitys) unmarshaller.unmarshal(new StringReader(xml));
			System.out.println();
			System.out.println(entitys.toString());
			System.out.println(entitys.getEntity().get(0).getAsiaName());
			System.out.println(entitys.getEntity().get(0).getAuthor());
		}catch(JAXBException e)
		{
			e.printStackTrace();
		}
	}
}
