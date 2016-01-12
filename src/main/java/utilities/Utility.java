
package utilities;

import static org.junit.Assert.fail;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a useful methods group.
 * 
 * @author zhangyutao
 * 
 */
public class Utility {

	/**
	 * Return a string about current time. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @return Return a string about current time yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		return getTime(0);
	}

	public static String readFileByLines(File file) {
		String fileTxt = "";
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;

			while ((tempString = reader.readLine()) != null) {
				if (line == 1) {
					fileTxt = tempString;
				} else {
					fileTxt = fileTxt + "\n" + tempString;
				}

				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return fileTxt;
	}

	/**
	 * Return a string about a time = current time + days. author: zhangyutao
	 * create on 2014-03-04
	 * 
	 * @param daysOffset
	 *            how many day after current day yyyy-MM-dd HH:mm:ss
	 * @return Return a string about a time = current time + days
	 */
	public static String getTime(int daysOffset) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE) + daysOffset);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
	}

	/**
	 * Return a string with new format according to the date and the format you
	 * provide. author: zhangyutao create on 2014-03-04
	 * 
	 * @param date
	 *            date
	 * @param format
	 *            format like "yyyy-MM-dd HH:mm:ss". More format refer to
	 *            {@link java.text.SimpleDateFormat#SimpleDateFormat}
	 * @return Return a string with new format according to the date and the
	 *         format you provide
	 */
	public static String getTime(Date date, String format) {
		return new SimpleDateFormat(format).format(date.getTime());
	}

	/**
	 * Return a data with new format and timezome. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param sourceDate
	 *            the data you want convert, it should be in standard data
	 *            format.More format refer to
	 *            {@link java.text.SimpleDateFormat#SimpleDateFormat(String)}
	 * @param strDateFormat
	 *            format like "yyyy-MM-dd HH:mm:ss". More format refer to
	 *            {@link java.text.SimpleDateFormat#SimpleDateFormat}
	 * @param timezone
	 *            like "America/Los_Angeles".More format refer to
	 *            {@link TimeZone java.util.TimeZone#getTimeZone(String)}
	 * @return Return a data with new format and timezome
	 */
	public static Date convertTimeFromStringToDate(String sourceDate, String strDateFormat, String timezone) {
		DateFormat format = new SimpleDateFormat(strDateFormat);
		try {
			format.setTimeZone(TimeZone.getTimeZone(timezone));
			return format.parse(sourceDate);
		} catch (ParseException e) {
			throw new RuntimeException("Error while parsing " + sourceDate, e);
		}
	}

	/**
	 * Return a data with new format and timezome. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param sourceDate
	 *            the data you want convert, it should be in standard data
	 *            format.More format refer to
	 *            {@link java.text.SimpleDateFormat#SimpleDateFormat(String)}
	 * @param strDateFormat
	 *            format like "yyyy-MM-dd HH:mm:ss". More format refer to
	 *            {@link java.text.SimpleDateFormat#SimpleDateFormat}
	 * @param timezone
	 *            like "America/Los_Angeles".More format refer to
	 *            {@link TimeZone java.util.TimeZone#getTimeZone(String)}
	 * @return Return a data with new format and timezome
	 */
	public static Date convertTimeFromStringToDate(String sourceDate, String strDateFormat) {
		DateFormat format = new SimpleDateFormat(strDateFormat);
		try {
			return format.parse(sourceDate);
		} catch (ParseException e) {
			throw new RuntimeException("Error while parsing " + sourceDate, e);
		}
	}

	public static long dateDiff(Date dstartTime, Date dendTime, String format, String unit) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(dstartTime);
		c2.setTime(dendTime);
		long diff;
		long res = -999999;
		diff = c2.getTimeInMillis() - c1.getTimeInMillis();
		switch (unit) {
		case "d":
			res = diff / (24 * 60 * 60 * 1000);
			break;
		case "h":
			res = diff / (60 * 60 * 1000);
			break;
		case "m":
			res = diff / (60 * 1000);
			break;
		case "s":
			res = diff / 1000;
			break;
		}

		return res;
	}

	public static long dateDiff(String startTime, String endTime, String format, String unit) {

		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		long diff;
		try {

			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			switch (unit) {
			case "d":
				return diff / nd;
			case "h":
				return diff / nh;
			case "m":
				return diff / nm;
			case "s":
				return diff / ns;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 99999;
	}

	/**
	 * uncompress a GIP byte[] data and then return a string about the content.
	 * author: zhangyutao create on 2014-03-04
	 * 
	 * @param compressed
	 *            like a body of http response
	 * @return uncompress a GIP byte[] data and then return a string about the
	 *         content
	 */
	public static String unGZIP(byte[] compressed) {
		/*
		 * if(compressedStr==null){ return null; }
		 */

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		// byte[] compressed = null;
		String decompressed = null;
		try {
			/*
			 * compressed = new
			 * sun.misc.BASE64Decoder().decodeBuffer(compressedStr); when string
			 * input the function, use compressedStr
			 */
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException: " + e.getMessage());
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
					fail("IOException: " + e.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					fail("IOException: " + e.getMessage());
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					fail("IOException: " + e.getMessage());
				}
			}
		}
		return decompressed;
	}

	/**
	 * Query Xml according to standard Xpath Query. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param respXml
	 *            a file path
	 * @param xpathQuery
	 *            xpath
	 * @throws ParserConfigurationException
	 *             Parser Configuration Exception
	 * @return NodeList
	 */
	public static NodeList queryXml(String respXml, String xpathQuery) throws ParserConfigurationException {
		XPathFactory xfactory = XPathFactory.newInstance();
		XPath xpath = xfactory.newXPath();
		NodeList result = null;
		try {
			Document doc = stringToDoc(respXml);
			XPathExpression path = xpath.compile(xpathQuery);
			result = (NodeList) path.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			fail("XPathExpressionException: " + e.getMessage());
		}
		return result;
	}

	/**
	 * Query Xml according to standard Xpath Query. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param respXmlByte
	 *            like a body of http format response
	 * @param xpathQuery
	 *            xpath
	 * @throws ParserConfigurationException
	 *             Parser Configuration Exception
	 * @return NodeList
	 */
	public static NodeList queryXml(byte[] respXmlByte, String xpathQuery) throws ParserConfigurationException {
		XPathFactory xfactory = XPathFactory.newInstance();
		XPath xpath = xfactory.newXPath();
		NodeList result = null;
		try {
			Document doc = stringToDoc(respXmlByte);
			XPathExpression path = xpath.compile(xpathQuery);
			result = (NodeList) path.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			fail("XPathExpressionException: " + e.getMessage());
		}
		return result;
	}

	/**
	 * String to XML org.w3c.dom.Document. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param xmlStr
	 *            xmlStr
	 * @return doc
	 */
	private static Document stringToDoc(String xmlStr) {
		// string to XML
		Document doc = null;
		try {
			xmlStr = new String(xmlStr.getBytes(), "gb2312");
			StringReader sr = new StringReader(xmlStr);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);

		} catch (ParserConfigurationException e) {
			System.err.println(xmlStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("ParserConfigurationException: " + e.getMessage());
		} catch (SAXException e) {
			System.err.println(xmlStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("SAXException: " + e.getMessage());
		} catch (IOException e) {
			System.err.println(xmlStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("IOException: " + e.getMessage());
		}
		return doc;
	}

	/**
	 * String to XML org.w3c.dom.Document. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param xmlByte
	 *            xmlByte
	 * @return doc
	 */
	private static Document stringToDoc(byte[] xmlByte) {
		// string to XML
		Document doc = null;
		String xmlStr = "";
		try {
			xmlStr = new String(xmlByte, "gb2312");
			StringReader sr = new StringReader(xmlStr);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);

		} catch (ParserConfigurationException e) {
			System.err.println(xmlStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("ParserConfigurationException: " + e.getMessage());
		} catch (SAXException e) {
			System.err.println(xmlStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("SAXException: " + e.getMessage());
		} catch (IOException e) {
			System.err.println(xmlStr);
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("IOException: " + e.getMessage());
		}
		return doc;
	}

	/**
	 * kill the window process by the name you provided. author: zhangyutao
	 * create on 2014-03-04
	 * 
	 * @param programName
	 *            program Name
	 */
	public static void closeProcess(String programName) {
		Process listprocess;

		try {
			listprocess = Runtime.getRuntime().exec("cmd.exe /c tasklist");
			InputStream is = listprocess.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			// StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = r.readLine()) != null) {
				String id = null;
				Matcher matcher = Pattern.compile(programName + "[ ]*([0-9]*)").matcher(str);
				while (matcher.find()) {
					if (matcher.groupCount() >= 1) {
						id = matcher.group(1);
						if (id != null) {
							Integer pid = null;
							try {
								pid = Integer.parseInt(id);
							} catch (NumberFormatException e) {
								e.printStackTrace();
								fail("NumberFormatException: " + e.getMessage());
							}
							if (pid != null) {
								Runtime.getRuntime().exec("cmd.exe /c taskkill /f /pid " + pid);
								System.out.println("kill progress " + programName);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException: " + e.getMessage());
		}
	}

	/**
	 * copy all file from source to destination. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @param source
	 *            source
	 * @param dest
	 *            dest
	 */
	public static void copyAllFolderAndFiles(File source, File dest) {
		File file2 = null;
		File[] file = null;

		file = source.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				file2 = new File(dest.getPath() + File.separator + file[i].getName());
				file2.mkdirs();

				copyAllFolderAndFiles(file[i], file2);
			} else {
				try {

					FileInputStream fis = new FileInputStream(source.getPath() + File.separator + file[i].getName());
					FileOutputStream fos = new FileOutputStream(dest.getPath() + File.separator + file[i].getName());

					int a;
					while ((a = fis.read()) != -1) {
						fos.write((char) a);
					}

					fis.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("IOException: " + e.getMessage());
				}
			}
		}

	}

	/**
	 * check if the string is a number. author: zhangyutao Created on Mar 12,
	 * 2014
	 * 
	 * @param str
	 *            str
	 * @return true or false
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i <= (str.length() - 1); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String jsontoXml(byte[] josonSource) {
		String source = "";
		try {
			source = new String(josonSource, "gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = JSONObject.fromObject(source);
		;
		return xmlSerializer.write(json).toString();
	}

	public static String jsontoXml(String jsonString) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSONObject jsonobj = JSONObject.fromObject(jsonString);
		return xmlSerializer.write(jsonobj).toString();

	}

	public static String RegGetString(String source, String find) {
		String str = "";
		Pattern pattern = Pattern.compile(find);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			str = matcher.group();
			break;
		}

		return str;
	}

	/**
	 * Use regular expression to replace the specified strings.
	 * 
	 * @author wei.sun13@hp.com create on 2015-02-01
	 * @param source
	 *            source string
	 * @param pattern
	 *            pattern string
	 * @param replacement
	 *            replacement
	 * @return the dest string
	 */
	public static String RegExpReplace(String source, String pattern, String replacement) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		String str = m.replaceAll(replacement);

		return str;
	}

	/**
	 * Use regular expression to check if the specified string can be matched.
	 * 
	 * @author wei.sun13@hp.com create on 2015-02-01
	 * @param matchStr
	 *            match string
	 * @param pattern
	 *            pattern string
	 * @return the boolean result
	 */
	public static boolean RegExpMatch(String matchStr, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(matchStr);
		boolean ifMatch = m.matches();

		return ifMatch;
	}

	public static void copyFileToClipBoard(final ArrayList<File> fileList) {
		FileListSelection fileListSel = new FileListSelection(fileList);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fileListSel, null);

	}

	public static class FileListSelection implements Transferable {
		private ArrayList<File> fileList;

		public FileListSelection(ArrayList<File> fileList) {
			this.fileList = fileList;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.javaFileListFlavor.equals(flavor);
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (!DataFlavor.javaFileListFlavor.equals(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return fileList;
		}
	}

	public static String getSysClipboardText() {
		String ret = "";
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {

			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (ret.length() > 1) {
			if (ret.substring(ret.length() - 1).equals("\n") || ret.substring(ret.length() - 1).equals("\r")) {
				ret = ret.substring(0, ret.length() - 1);
			}
		}

		return ret;
	}

	public static String[] runProcess(String cmd, int timeOutInSeconds) {
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec(cmd);
		} catch (Throwable e) {
			throw new IllegalArgumentException("Failed to start process:" + e.getMessage());
		}
		String[] result = waitProcesEnd(p, timeOutInSeconds);
		p.destroy(); // release the ram
		return result;
	}

	public static String[] waitProcesEnd(Process p, int timeOutInSeconds) {
		int exitCode = -1;
		int timeout = 120;
		if (timeout < timeOutInSeconds) {
			timeout = timeOutInSeconds;
		}

		Calendar calendar = Calendar.getInstance();
		Date current = new Date();
		calendar.setTime(current);
		calendar.add(Calendar.SECOND, timeout);
		Date conditionTimeOut = calendar.getTime();
		while (true) {
			try {
				exitCode = p.exitValue();
				break;
			} catch (IllegalThreadStateException e) {
				Date now = new Date();
				if (now.after(conditionTimeOut)) {
					e.printStackTrace();
					throw new IllegalArgumentException("process may be die, the timeout for wait the process is "
							+ String.valueOf(timeout) + " seconds");
				}
			}

		}
		String output = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
		} catch (UnsupportedEncodingException e1) {
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			e1.printStackTrace();
		}
		try {
			// System.getProperty("line.separator");
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				} else {
					output = output + line + "\n";
				}
			}
			if (!output.equals("")) {
				output = output.substring(0, output.length() - "\n".length());
			}

		} catch (IOException e) {
			e.printStackTrace();

			throw new IllegalArgumentException("Failed to read output of process:" + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Failed to read output of process:" + e.getMessage());

			}

		}
		String[] result = { output, String.valueOf(exitCode) };
		return result;
	}

	public static List<File> getFileListClipboard() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if ((!(t == null)) && (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))) {

				@SuppressWarnings("unchecked")
				List<File> fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
				return fileList;
			}
		} catch (UnsupportedFlavorException e) {
			// System.out.println("Error tip: "+e.getMessage());
		} catch (IOException e) {
			// System.out.println("Error tip: "+e.getMessage());
		}
		return null;
	}

	public static void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}

	public static void createFile(File f) {
		String filepath = f.getAbsolutePath();
		String[] arrfilepath = filepath.split("\\\\");
		String folder = arrfilepath[0];
		for (int i = 1; i < arrfilepath.length - 1; i++) {

			folder = folder + "\\" + arrfilepath[i];
			File b = new File(folder);
			if (b.exists()) {
				if (!b.isDirectory()) {
					b.mkdir();
				}
			} else {
				b.mkdir();
			}
		}
		if (arrfilepath.length > 1) {
			if (!f.exists()) {
				if (f.isDirectory()) {
					f.mkdir();
				} else {
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

	public static Object[] resizeArray(Object[] oldArray, int newSize) {
		Object[] newArray = new Object[newSize];
		for (int i = 0; i < newSize; i++) {
			newArray[i] = oldArray[i];
		}

		return newArray;
	}

	public static String readFileToString(File file) {
		String str = "";
		try {
			FileInputStream in = new FileInputStream(file);
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			str = new String(buffer, "GB2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * Return the stack trace of an error or exception you provided. author:
	 * yu-tao.zhang create on 2014-03-04
	 * 
	 * @param t
	 *            the error or exception
	 * @return the stack trace of an error or exception
	 */
	public static String stackTraceInfo(Throwable t) {
		/*
		 * System.out.println(stackElements[i].getClassName());
		 * System.out.println(stackElements[i].getFileName());
		 * System.out.println(stackElements[i].getLineNumber());
		 * System.out.println(stackElements[i].getMethodName());
		 */
		String info = "";
		StackTraceElement[] stackElements = t.getStackTrace();
		int stackElementsLen = stackElements.length;
		for (int i = 0; i < stackElementsLen; i++) {
			String className = stackElements[i].getClassName();
			String methodName = stackElements[i].getMethodName();
			String lineName = String.valueOf(stackElements[i].getLineNumber());
			info = info + "[className]: " + className + ", [methodName]:" + methodName + ", [lineName]:" + lineName
					+ "\n";
		}
		return "Stack Trace Info: " + info;
	}

	public static boolean ipIsInRange(String ipLowerRange, String ipUpperRange, String ip) {
		ipLowerRange = ipLowerRange.trim();
		ipUpperRange = ipUpperRange.trim();
		ip = ip.trim();

		if (ipLowerRange == null || ipUpperRange == null || ipLowerRange.equals("") || ipUpperRange.equals(""))
			throw new NullPointerException("IP Range cannot be null");
		if (ip == null || ip.equals(""))
			throw new NullPointerException("IP cannot be null");

		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		if (!ipLowerRange.matches(REGX_IP) || !ipUpperRange.matches(REGX_IP) || !ip.matches(REGX_IP))
			return false;

		String[] sipLower = ipLowerRange.split("\\.");
		String[] sipUpper = ipUpperRange.split("\\.");
		String[] sipCompared = ip.split("\\.");
		long iplower = 0L, ipUpper = 0L, ipCompared = 0L;
		for (int i = 0; i < 4; ++i) {
			iplower = iplower << 8 | Integer.parseInt(sipLower[i]);
			ipUpper = ipUpper << 8 | Integer.parseInt(sipUpper[i]);
			ipCompared = ipCompared << 8 | Integer.parseInt(sipCompared[i]);
		}
		if (iplower > ipUpper) {
			long temp = iplower;
			iplower = ipUpper;
			ipUpper = temp;
		}
		return iplower <= ipCompared && ipCompared <= ipUpper;
	}

	/**
	 * uncompress jar file
	 * 
	 * @param jarFile
	 * @param tarDir
	 * @throws IOException
	 */
	public static void uncompressJAR(File jarFile, String tarName, File tarDir) throws IOException {
		@SuppressWarnings("resource")
		JarFile jfInst = new JarFile(jarFile);
		Enumeration<JarEntry> enumEntry = jfInst.entries();
		while (enumEntry.hasMoreElements()) {
			JarEntry jarEntry = enumEntry.nextElement();
			if (jarEntry.getName().startsWith(tarName + "/")) {
				File tarFile = new File(tarDir, jarEntry.getName());
				makeFileForJarEntry(jarEntry, tarFile);
				if (jarEntry.isDirectory()) {
					continue;
				}

				@SuppressWarnings("resource")
				FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();

				InputStream ins = jfInst.getInputStream(jarEntry);
				transferStream(ins, fileChannel);

			}
		}
	}

	/**
	 * transfer Stream
	 * 
	 * @param ins
	 * @param targetChannel
	 */
	private static void transferStream(InputStream ins, FileChannel targetChannel) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
		ReadableByteChannel rbcInst = Channels.newChannel(ins);
		try {
			while (-1 != (rbcInst.read(byteBuffer))) {
				byteBuffer.flip();
				targetChannel.write(byteBuffer);
				byteBuffer.clear();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (null != rbcInst) {
				try {
					rbcInst.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != targetChannel) {
				try {
					targetChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * print jar information
	 * 
	 * @param fileInst
	 *            - jar file
	 */
	public static void printJarEntry(File fileInst) {
		JarFile jfInst = null;
		;
		try {
			jfInst = new JarFile(fileInst);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<JarEntry> enumEntry = jfInst.entries();
		while (enumEntry.hasMoreElements()) {
			System.out.println((enumEntry.nextElement()));
		}
	}

	/**
	 * Create file
	 * 
	 * @param jarEntry
	 * @param fileInst
	 * @throws IOException
	 */
	public static void makeFileForJarEntry(JarEntry jarEntry, File fileInst) {
		if (!fileInst.exists()) {
			if (jarEntry.isDirectory()) {
				fileInst.mkdirs();
			} else {
				try {
					fileInst.createNewFile();
				} catch (IOException e) {
					System.out.println("fail to create file>>".concat(fileInst.getPath()));
				}
			}
		}
	}

}
