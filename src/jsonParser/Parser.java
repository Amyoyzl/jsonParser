package jsonParser;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


public class Parser {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String toJson(Object o) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");

		// 反射获取对象类型
		Class clazz = o.getClass();
		if (clazz.getSimpleName().equals("Map") || clazz.getSimpleName().equals("HashMap")) {
			Map<String, Object> map = (Map<String, Object>) o;
			Set<String> keys = map.keySet();
			for (String key : keys) {
				builder.append(String.format("%s:%s,", key, map.get(key)));
			}
		} else {

			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				String name = field.getName();
				String Name = upperCase(name);
				try {
					// 该字段的getter
					Method m = clazz.getDeclaredMethod("get" + Name);
					Object value = m.invoke(o);
					builder.append(String.format("%s:%s,",name, value));
					
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		builder.deleteCharAt(builder.length()-1);
		builder.append("}");
		return builder.toString();
	}

	public String upperCase(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object toObject(String json, Class clazz) {
		if(json.length()==0) {
			return null;
		}
		Object o = null;
		try {
			Constructor c = clazz.getConstructor();
			o = c.newInstance();
			json = json.substring(1, json.length()-1);
			String[] s = json.split(",");
			for(int i=0; i < s.length; i++) {
				String[] str = s[i].split(":");
				String key = str[0];
				String value = str[1];
				String name = upperCase(key);
				Field field;
				try {
					field = clazz.getDeclaredField(key);
					Class type = field.getType();
					Method m = clazz.getMethod("set"+name, type);
					m.invoke(o, (Object)value);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				
			}
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return o;
	}
	
	@SuppressWarnings("rawtypes")
	public Object toObject(InputStream in, Class clazz) {
		return null;
	}
}
