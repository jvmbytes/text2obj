/**
 * Created By: Comwave Project Team
 * Created Date: Nov 2, 2011 4:11:06 PM
 */
package shutu.text2obj.dom;

/**
 * @author Geln Yang
 * @version 1.0
 */
public class Converter {

	public static final String SCHEMA_BASE = "http://shutu/schema/text2obj/converter/";
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final int DEFAULT_BATCH_SIZE = 100;

	/** data charset */
	private String charset = DEFAULT_CHARSET;

	private String name;

	/** whether process batched */
	private boolean batched = true;

	/** whether the data content has line flag */
	private boolean hasLineFlag = true;

	/** used to cut line from data just when {@link #hasLineFlag}==false */
	private int lineSize = 0;

	/** the size of lines to batch parse */
	private int batchSize = DEFAULT_BATCH_SIZE;

	protected Component component;

	public String getCharset() {
		return charset != null ? charset : DEFAULT_CHARSET;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public boolean isBatched() {
		return batched;
	}

	public void setBatched(boolean batched) {
		this.batched = batched;
	}

	public boolean isHasLineFlag() {
		return hasLineFlag;
	}

	public void setHasLineFlag(boolean hasLineFlag) {
		this.hasLineFlag = hasLineFlag;
	}

	public int getLineSize() {
		return lineSize;
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
}
