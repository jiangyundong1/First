package com.wamdy.util;

import java.util.Date;

public class TypeTransform
{
	public static Class<?> transform(String cl)
	{
		if("String".toLowerCase().equals(cl.toLowerCase()))
		{
			return String.class;
		}else if("Integer".toLowerCase().equals(cl.toLowerCase()))
		{
			return Integer.class;
		}else if("Boolean".toLowerCase().equals(cl.toLowerCase()))
		{
			return Boolean.class;
		}else if("Date".toLowerCase().equals(cl.toLowerCase()))
		{
			return Date.class;
		}else if("Short".toLowerCase().equals(cl.toLowerCase()))
		{
			return Short.class;
		}else if("Long".toLowerCase().equals(cl.toLowerCase()))
		{
			return Long.class;
		}else if("Text".toLowerCase().equals(cl.toLowerCase()))
		{
			return String.class;
		}else if("Float".toLowerCase().equals(cl.toLowerCase()))
		{
			return Float.class;
		}else if("Double".toLowerCase().equals(cl.toLowerCase()))
		{
			return Double.class;
		}
		return null;
	}
}
