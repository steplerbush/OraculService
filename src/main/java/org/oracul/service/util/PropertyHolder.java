package org.oracul.service.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyHolder {

	public static final String core2d = "cores.2d";
	public static final String core3d = "cores.3d";
	public static final String executeOraculDir2D = "oracul.execute.dir.2d";
	public static final String executeOraculCommand2D = "oracul.execute.command.2d";
	public static final String executeOraculDir3D = "oracul.execute.dir.3d";
	public static final String executeOraculCommand3D = "oracul.execute.command.3d";
	public static final String propFileName = "/resources/oracul.properties";

	public String getProperty(String path) {
		Properties properties = new Properties();
		try {
			InputStream in = this.getClass().getResourceAsStream("/oracul.properties");
			properties.load(in);
		} catch (IOException e) {
			return "";
		}
		return properties.getProperty(path);
	}
}
