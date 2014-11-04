/**
 * $Revision: 1.4 $
 * $Author: geln_yang $
 * $Date: 2011/09/01 15:33:21 $
 *
 * Author: Eric Yang
 * Date  : Jun 2, 2010 5:17:35 PM
 *
 */
package shutu.text2obj.factory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import shutu.text2obj.dom.Component;
import shutu.text2obj.dom.Converter;
import shutu.text2obj.reader.BatchReader;
import shutu.text2obj.reader.BatchStreamReader;

/**
 * @Author Eric Yang
 * @version 1.0
 */
public class BatchReaderFactory {

	public static BatchReader contentReader(String fileContent, String charset, long batchSize) throws IOException {
		StringReader sReader = new StringReader(fileContent);
		BufferedReader bReader = new BufferedReader(sReader);
		BatchReader batchReader = new BatchReader();
		batchReader.setReader(bReader);
		batchReader.setBatchSize(batchSize);
		batchReader.setCharset(charset);
		return batchReader;
	}

	private static BatchReader contentReader(String fileContent, String charset, long batchSize, boolean hasLineFlag,
			int lineSize) throws IOException {
		BatchReader batchReader = contentReader(fileContent, charset, batchSize);
		batchReader.setHasLineFlag(hasLineFlag);
		batchReader.setLineSize(lineSize);
		return batchReader;
	}

	public static BatchReader contentReader(String fileContent, String charset, long batchSize, Component component)
			throws IOException {
		return contentReader(fileContent, charset, batchSize, component.getConverter().isHasLineFlag(), component
				.getConverter().getLineSize());
	}

	/*---------------streamReader------------------------------------------------------------------*/
	public static BatchReader fileReader(String filePath, String charset, long batchSize) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		return streamReader(inputStream, charset, batchSize);
	}

	private static BatchReader fileReader(String filePath, String charset, long batchSize, boolean hasLineFlag,
			int lineSize) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		return streamReader(inputStream, charset, batchSize, hasLineFlag, lineSize);
	}

	public static BatchReader fileReader(String filePath, long batchSize, Component component) throws IOException {
		return fileReader(filePath, component.getConverter().getCharset(), batchSize, component.getConverter()
				.isHasLineFlag(), component.getConverter().getLineSize());
	}

	/*---------------streamReader------------------------------------------------------------------*/
	public static BatchReader streamReader(InputStream inputStream, String charset, long batchSize) throws IOException {
		BatchReader batchReader = new BatchReader();
		batchReader.setBatchSize(batchSize);
		batchReader.setCharset(charset);
		/* last */
		batchReader.setReader(inputStream, charset);
		return batchReader;
	}

	private static BatchReader streamReader(InputStream inputStream, String charset, long batchSize,
			boolean hasLineFlag, int lineSize) throws IOException {
		BatchReader batchReader;
		if (!hasLineFlag) {
			batchReader = new BatchStreamReader();
		} else {
			batchReader = new BatchReader();
		}
		batchReader.setBatchSize(batchSize);
		batchReader.setHasLineFlag(hasLineFlag);
		batchReader.setLineSize(lineSize);
		batchReader.setCharset(charset);
		/* last */
		batchReader.setReader(inputStream, charset);
		return batchReader;
	}

	public static BatchReader streamReader(InputStream inputStream, Converter converter) throws IOException {
		BatchReader streamReader = streamReader(inputStream, converter.getCharset(), converter.getBatchSize(),
				converter.isHasLineFlag(), converter.getLineSize());
		streamReader.setConverter(converter);
		return streamReader;
	}
}
