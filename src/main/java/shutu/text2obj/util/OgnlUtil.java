package shutu.text2obj.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

public class OgnlUtil {
	private static Map<String, Object> expressions = new HashMap<String, Object>();

	public static Object getValue(String expression, Object root) throws OgnlException {
		return Ognl.getValue(compile(expression), root);
	}

	@SuppressWarnings("unchecked")
	public static Object getValue(String expression, Map context, Object root) throws OgnlException {
		return Ognl.getValue(compile(expression), context, root);
	}

	@SuppressWarnings("unchecked")
	public static Object getValue(String name, Map context, Object root, Class resultType) throws OgnlException {
		return Ognl.getValue(compile(name), context, root, resultType);
	}

	public static void setValue(String name, Object root, Object value) throws OgnlException {
		Ognl.setValue(compile(name), root, value);
	}

	@SuppressWarnings("unchecked")
	public static void setValue(String name, Map context, Object root, Object value) throws OgnlException {
		Ognl.setValue(compile(name), context, root, value);
	}

	public static Object compile(String expression) throws OgnlException {
		synchronized (expressions) {
			Object o = expressions.get(expression);
			if (o == null) {
				o = Ognl.parseExpression(expression);
				expressions.put(expression, o);
			}
			return o;
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", "Geln");
		List<String> list = new ArrayList<String>();
		map.put("details", list);
		list.add("B1");
		list.add("B2");
		System.out.println(OgnlUtil.getValue("user", map));
		System.out.println(OgnlUtil.getValue("details[1]", map));
		OgnlUtil.setValue("details[1]", map, "Modify Data");
		System.out.println(list.get(1));
	}
}
