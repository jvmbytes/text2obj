/**
 * $Revision: 1.1 $
 * $Author: geln_yang $
 * $Date: 2010/05/31 14:14:35 $
 *
 * Author: Eric Yang
 * Date  : Jul 25, 2009 11:30:14 AM
 *
 */
package shutu.text2obj.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ognl.OgnlException;
import shutu.text2obj.dom.Component;
import shutu.text2obj.dom.Container;
import shutu.text2obj.dom.Line;
import shutu.text2obj.dom.Property;
import shutu.text2obj.exception.BuildException;

/**
 * @author Eric Yang
 * @version 1.0
 */
public class BuildUtil {

	public static final String LEFT = "left";

	public static final String RIGHT = "right";

	public static String obj2xml(Object obj, Component component) throws OgnlException, BuildException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append(renderComponent(obj, component));
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	private static StringBuffer renderComponent(Object obj, Component component) throws OgnlException, BuildException {
		StringBuffer buffer = new StringBuffer();
		if (component.isShowOnce() || component.isShowNoneOnce())
			buffer.append(renderOnceComponent(obj, component));
		else {
			if (!(obj instanceof Collection)) {
				throw new BuildException("Expect a Collection result for component[" + component.getName() + "]!");
			}
			Collection collection = (Collection) obj;
			for (Iterator item = collection.iterator(); item.hasNext();) {
				Object childObject = (Object) item.next();
				buffer.append(renderOnceComponent(childObject, component));
			}
		}
		return buffer;
	}

	@SuppressWarnings("unchecked")
	private static StringBuffer renderOnceComponent(Object obj, Component component) throws OgnlException,
			BuildException {
		StringBuffer buffer = new StringBuffer();
		String name = component.getName();
		buffer.append("<" + name + " xmlns=\"" + component.getNamespace() + "\">");
		List<Container> children = component.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Container child = children.get(i);
			Object childObj;
			if (obj instanceof List)
				childObj = obj;
			else
				childObj = OgnlUtil.getValue(child.getName(), obj);
			if (childObj == null)
				continue;

			if (child instanceof Line) {
				Line line = (Line) child;
				buffer.append(renderLine(childObj, line));
			} else if (child instanceof Component) {
				Component c = (Component) child;
				buffer.append(renderComponent(childObj, c));
			}
		}
		buffer.append("</" + name + ">");
		return buffer;
	}

	@SuppressWarnings("unchecked")
	private static StringBuffer renderLine(Object obj, Line line) throws OgnlException, BuildException {
		StringBuffer buffer = new StringBuffer();
		if (line.isShowNoneOnce() || line.isShowOnce()) {
			buffer.append(renderSingleLine(obj, line));
		} else {
			List<Object> objects = (List<Object>) obj;
			for (int i = 0; i < objects.size(); i++) {
				Object object = objects.get(i);
				if (object == null)
					System.out.println();
				buffer.append(renderSingleLine(object, line));
			}
		}
		return buffer;
	}

	private static StringBuffer renderSingleLine(Object obj, Line line) throws OgnlException, BuildException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + line.getName() + ">");
		List<Property> properties = line.getProperties();
		for (int i = 0; i < properties.size(); i++) {
			Property property = properties.get(i);
			Object value = OgnlUtil.getValue(property.getName(), obj);
			String v = property.readableBuild(value);
			buffer.append("<" + property.getName() + ">");
			buffer.append(v);
			buffer.append("</" + property.getName() + ">");
		}
		buffer.append("</" + line.getName() + ">");
		return buffer;
	}

	public static String rightTrim(String s) {
		if (s == null || s.trim().length() == 0)
			return "";
		if (s.trim().length() == s.length())
			return s;
		if (!s.startsWith(" ")) {
			return s.trim();
		} else {
			return s.substring(0, s.indexOf(s.trim().substring(0, 1)) + s.trim().length());
		}
	}

	public static String leftTrim(String s) {
		if (s == null || s.trim().length() == 0)
			return "";
		if (s.trim().length() == s.length())
			return s;
		if (!s.startsWith(" ")) {
			return s;
		} else {
			return s.substring(s.indexOf(s.trim().substring(0, 1)));
		}
	}

	/**
	 * Build a string base on the source one.<br>
	 * If the length of the source string is less than the given length ,<br>
	 * fill in the result string with blanks .<br>
	 * If the given length is less than or equal to 0,return the trim result <br>
	 * of source string or a null string when it's null.<br>
	 * 
	 * @param value
	 *            the source string
	 * @param length
	 *            the length of the result string
	 * @param align
	 *            the position of the source string in the result string
	 */
	public static String buildString(String value, int length, String align) {
		if (length <= 0)
			return value == null ? "" : value.trim();

		if (value == null) {
			return blank(length);
		} else if (value.length() == length) {
			return value;
		} else if (value.length() < length) {
			if (align.equalsIgnoreCase(RIGHT)) {
				return blank(length - value.length()) + value;
			} else if (align.equalsIgnoreCase(LEFT)) {
				return value + blank(length - value.length());
			} else {
				throw new RuntimeException("Invalid align value \"" + align + "\"");
			}
		} else {
			throw new RuntimeException("The length of \"" + value + "\" is over " + length);
		}
	}

	/**
	 * Build a number format string. <br>
	 * If result source string has radix point, the given decimal and scale seemed to be 1 if it's 0
	 * 
	 * @param src
	 *            the source number string
	 * @param precision
	 *            the precision of the number
	 * @param scale
	 *            the length of the scale part of result string
	 * @param needRadixPoint
	 *            whether fill radix point in result string
	 * @param needFillZero
	 *            whether fill zero if the length of result string is greater than that of the source.
	 */
	public static String buildNumber(String src, int precision, int scale, boolean needRadixPoint, boolean needFillZero) {
		if (precision < 0 || scale < 0 || (precision - scale) < 0) {
			throw new RuntimeException("Invalid number format!");
		} else {
			int decimal = precision - scale;
			if (needRadixPoint) {
				decimal = decimal == 0 ? 1 : decimal;
			}
			if (src == null || src.trim().length() == 0) {
				String result = "";
				if (needFillZero)
					result += zero(decimal);
				if (needRadixPoint) {
					result = result.equals("") ? zero(decimal) : result;
					result += "." + zero(scale);
				} else if (needFillZero)
					result += zero(scale);
				return result.equals("") ? "0" : result;
			} else {
				src = src.trim();

				int dotPos = src.indexOf(".");
				if (dotPos == -1) {
					if (src.length() > decimal)
						throw new RuntimeException("The length of " + src + " is over " + decimal + "!");
					String result = "";
					if (needFillZero)
						result += zero(decimal - src.length());
					result += src;
					if (needRadixPoint && scale != 0)
						result += "." + zero(scale);
					else if (needFillZero)
						result += zero(scale);
					return result;

				} else {
					String integer = src.substring(0, dotPos);
					String dot = scale == 0 ? "" : src.substring(dotPos + 1, src.length());
					if (integer.length() > decimal)
						throw new RuntimeException("The length of " + integer + " is over " + decimal + "!");
					if (dot.length() > scale)
						dot = dot.substring(0, scale);
					String result = "";
					if (needFillZero)
						result += zero(decimal - integer.length());
					result += integer;
					if (needRadixPoint && scale != 0)
						result += ".";
					result += dot;
					if (needFillZero)
						result += zero(scale - dot.length());
					return result;
				}
			}
		}
	}

	public static String blank(int len) {
		String s = "";
		for (int i = 0; i < len; i++) {
			s += " ";
		}
		return s;
	}

	public static String zero(int len) {
		String s = "";
		for (int i = 0; i < len; i++) {
			s += "0";
		}
		return s;
	}

	public static void main(String[] args) {
		System.out.println(buildNumber("11133", 6, 0, true, true));
		System.out.println(buildNumber("111.5533", 6, 0, true, true));
	}
}
