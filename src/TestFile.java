/**
 * Created by Thiakil on 16/01/2018.
 */
public class TestFile {

	private int testInt = 123;

	private static final int maxInt = Integer.MAX_VALUE;

	private Integer test2 = 2;

	byte unmarked;

	private double testDbl = 122.0D;

	public static String testString = "Hello";

	public static final String testString2 = "Hello";

	private Object obj = new Object();

	private static Class<?> clazz = Object.class;

	public int[] doArray(){
		Object[] objArray = new Object[]{ "hai", 12.3, 4.4F, null, (Runnable)()->{ int i = 0; } };
		return new int[]{1,2,3,4};
	}
	
	public void dualParam(int i, int j){}

	private static class staticClass{
		int innerInt;
	}

	protected class nonStaticClass{
		String somevalue = "hellooooo";
	}

	class unmarked {
		byte b;
	}

}
