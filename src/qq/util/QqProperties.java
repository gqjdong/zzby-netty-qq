package qq.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class QqProperties {
	
	private static Map<String, Properties> propMap = new HashMap<String, Properties>();

	private static final String SERVER = "server.properties";
	private static final String CLIENT = "client.properties";
	private static final String USER = "user.properties";

	public static String getServer() {
		return SERVER;
	}

	public static String getClient() {
		return CLIENT;
	}

	public static String getUser() {
		return USER;
	}

	private static Properties getProperties(String propName) {
		Properties p = new Properties();
		InputStream in = getClassLoader().getResourceAsStream(propName);
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * 根据properties文件名得到properties
	 * 
	 * @param fileName
	 * @return
	 */
	public static Properties getInstanceByFileName(String fileName) {
		if (!propMap.containsKey(fileName)) {
			propMap.put(fileName, getProperties(fileName));
		}
		return propMap.get(fileName);
	}

	/**
	 * 根据properties文件名和属性名得到属性值
	 * 
	 * @param fileName
	 * @param propKey
	 * @return
	 */
	public static String getPropertyByFileName(String fileName, String propKey) {
		if (!propMap.containsKey(fileName)) {
			propMap.put(fileName, getProperties(fileName));
		}
		Properties properties = propMap.get(fileName);
		return properties.getProperty(propKey);
	}

	private static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		if (classLoader == null) {
			classLoader = PropertiesHelper.class.getClassLoader();
		}
		return classLoader;
	}

}
