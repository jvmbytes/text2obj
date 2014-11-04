/**
 * Created Date: Nov 16, 2011 5:20:02 PM
 */
package shutu.text2obj;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import shutu.text2obj.dom.Converter;
import shutu.text2obj.exception.CfgException;
import shutu.text2obj.exception.ParseException;
import shutu.text2obj.factory.BatchReaderFactory;
import shutu.text2obj.factory.ConvertCfgFactory;
import shutu.text2obj.reader.BatchReader;
import shutu.text2obj.util.ParseUtil;
import junit.framework.TestCase;

/**
 * @author Geln Yang
 * @version 1.0
 */
public class TestStockData extends TestCase {

	@SuppressWarnings("unchecked")
	public void testParse() throws CfgException, ParseException, IOException {
		Converter converter = ConvertCfgFactory.load("/config/stockData.xml");
		InputStream stream = TestConverter.class.getResourceAsStream("/data/601899.csv");
		BatchReader reader = BatchReaderFactory.streamReader(stream, converter);
		HashMap<String, Object> data = (HashMap<String, Object>) ParseUtil.batchParse(reader);
		Object header = data.get("header");
		System.out.println(header);

		List<HashMap<String, Object>> items = (List<HashMap<String, Object>>) data.get("details");
		for (HashMap<String, Object> hashMap : items) {
			System.out.println(hashMap);
		}
	}
}
