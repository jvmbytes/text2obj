package shutu.text2obj.util;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.transform.TransformerException;

/**
 * @author Geln Yang
 * @createdDate 2014年11月4日
 */
public class TestXMLUtil {

	public static void main(String[] args) throws TransformerException, FileNotFoundException {
		String baseResourcePath = "/" + TestXMLUtil.class.getPackage().getName().replace(".", "/");
		InputStream xslt = TestXMLUtil.class.getResourceAsStream(baseResourcePath + "/demo.xslt");
		InputStream xml = TestXMLUtil.class.getResourceAsStream(baseResourcePath + "/demo.xml");
		String charsetName = "UTF-8";
		String output = XMLUtil.transform(xslt, xml, charsetName);

		System.out.println(output);
	}
}
