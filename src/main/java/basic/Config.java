/*
 * Copyright Notice ====================================================
 * This file contains proprietary information of Hewlett-Packard Co.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2014 All rights reserved. =============================
 */

package basic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import utilities.Utility;

/**
 * This class is used to read config.properties and generate global variables
 * for whole project.
 * 
 * @author yu-tao.zhang@hp.com create on 2014-03-04
 */
public class Config {
	public Config() {

	}

	public static String RSCPath = "";
	/**
	 * it is configuration properties.
	 * 
	 * @author: yu-tao.zhang@hp.com create on 2014-03-04
	 * @param CONFIG
	 */
	private static final Properties CONFIG = new Properties();
	public static String BINFOLDER = "bin";
	public static String MYCONFIGProper = "vautotest.properties";
	public static String RUNCONFIGPATH = BINFOLDER + "\\" + MYCONFIGProper;

	/** the path of external resource will be called by this system. */
	public static String RESOURCEPATH = BINFOLDER;
	/** the directory of SikuliX Path. */
	public static String SIKULIX_PATH = RESOURCEPATH + "\\sikuliX";

	/** ask for run as admin when executing cmd */
	public static String ASKRUNADMIN = "";
	/** the new password for reseting when firstly logon windows server */
	public static String NEWPASSWORD = "";

	/**
	 * get the maven info whever maven build
	 */
	public static String artifactId = "";
	public static String version = "";
	public static String MAVEN_BUILD_TS = "";
	public static String mavensurefirereports = "";
	public static String surefiredetails = "";
	public static String surefireoverall = "";

	static {
		try {

			Config me = new Config();
			@SuppressWarnings("rawtypes")
			Class cls = me.getClass();
			ClassLoader loader = cls.getClassLoader();
			String clsName = cls.getName().replaceAll("\\.", "/") + ".class";
			java.net.URL url = loader.getResource(clsName);
			String realPath = url.getPath();
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");
			// realPath = realPath.substring(1).replaceAll("/", "\\\\");
			// System.out.println("realPath: " + realPath);
			if (realPath.contains("file:")) {
				// file:/C:/Users/zhanyuta/.m2/repository/com/hp/at/vautotest/0.0.1-SNAPSHOT/vautotest-0.0.1-SNAPSHOT.jar!/basic/Config.class
				String jarPath = realPath.replaceAll(clsName, "").substring(6).replaceAll("/", "\\\\").split(".jar!")[0]
						+ ".jar";
				// System.out.println("jarPath: " + jarPath);
				String tmp = System.getProperty("java.io.tmpdir");
				RSCPath = tmp + "vautaotest";
				// new File(TEMP).getAbsolutePath();
				System.out.println("Temp folder: " + RSCPath);
				File jarFile = new File(jarPath);
				File tarDir = new File(RSCPath);
				Utility.uncompressJAR(jarFile, BINFOLDER, tarDir);
				BINFOLDER = RSCPath + "\\" + BINFOLDER;
				RUNCONFIGPATH = BINFOLDER + "\\" + MYCONFIGProper;
				RESOURCEPATH = BINFOLDER;
				SIKULIX_PATH = RESOURCEPATH + "\\sikuliX";
			} else {

				/*
				 * File jarFile = new File(
				 * "C:\\Users\\zhanyuta\\.m2\\repository\\com\\hp\\at\\vautotest\\0.0.1-SNAPSHOT\\vautotest-0.0.1-SNAPSHOT.jar"
				 * ); File tarDir = new File(
				 * "C:\\Users\\zhanyuta\\.m2\\repository\\com\\hp\\at\\vautotest\\0.0.1-SNAPSHOT"
				 * ); Utility.uncompress(jarFile, BINFOLDER, tarDir);
				 */
				/// C:/Users/zhanyuta/.m2/repository/com/hp/at/vautotest/target/classes
				RSCPath = realPath.replaceAll(clsName, "").substring(1).replaceAll("/", "\\\\");
				BINFOLDER = RSCPath + BINFOLDER;
				RUNCONFIGPATH = BINFOLDER + "\\" + MYCONFIGProper;
				RESOURCEPATH = BINFOLDER;
				SIKULIX_PATH = RESOURCEPATH + "\\sikuliX";
			}

			FileReader reader = new FileReader(RUNCONFIGPATH);
			CONFIG.load(reader);
			ASKRUNADMIN = CONFIG.getProperty("askRunAsAdmin");
			NEWPASSWORD = CONFIG.getProperty("newpassword");
			artifactId = CONFIG.getProperty("ArtifactId");
			version = CONFIG.getProperty("Version");
			MAVEN_BUILD_TS = CONFIG.getProperty("MavenBuildTS");
			mavensurefirereports = CONFIG.getProperty("mavensurefirereports");
			surefiredetails = CONFIG.getProperty("surefiredetails");
			surefireoverall = CONFIG.getProperty("surefireoverall");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
