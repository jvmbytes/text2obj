package shutu.text2obj;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;
import ognl.OgnlException;
import shutu.text2obj.dom.Component;
import shutu.text2obj.dom.Converter;
import shutu.text2obj.exception.BuildException;
import shutu.text2obj.exception.CfgException;
import shutu.text2obj.exception.ParseException;
import shutu.text2obj.factory.BatchReaderFactory;
import shutu.text2obj.factory.ConvertCfgFactory;
import shutu.text2obj.reader.BatchReader;
import shutu.text2obj.util.BuildUtil;
import shutu.text2obj.util.OgnlUtil;
import shutu.text2obj.util.ParseUtil;
import shutu.text2obj.util.XMLUtil;

/**
 * @Author Eric Yang
 * @version 1.0
 */
public class TestConverter extends TestCase {

	public void testParse() throws IOException, CfgException, ParseException, OgnlException {
		Converter converter = ConvertCfgFactory.load("/config/test.xml");
		InputStream stream = TestConverter.class.getResourceAsStream("/data/test.dat");
		BatchReader reader = BatchReaderFactory.streamReader(stream, converter);
		Object object = ParseUtil.batchParse(reader);
		assertNotNull(object);
		Object value = OgnlUtil.getValue("footer.comment", object);
		assertEquals("the_comment", value);
		value = OgnlUtil.getValue("header.title", object);
		assertEquals("the_title", value);
		value = OgnlUtil.getValue("details[0].name", object);
		assertEquals("name1", value);
		value = OgnlUtil.getValue("details[0].data", object);
		assertEquals("data1", value);
		value = OgnlUtil.getValue("details[1].name", object);
		assertEquals("name2", value);
		value = OgnlUtil.getValue("details[1].data", object);
		assertEquals("data2", value);

		Component component = converter.getComponent();
		try {
			String obj2xml = BuildUtil.obj2xml(object, component);
			System.out.println(obj2xml);
		} catch (BuildException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		StringBuffer schema = XMLUtil.component2schema(component);
		System.out.println(schema);
	}

}
