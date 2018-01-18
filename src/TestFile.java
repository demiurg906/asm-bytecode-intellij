import java.util.function.BiFunction;

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
		
		Object[] objArray = new Object[]{ "hai", 12.3, 4.4F, null, (Runnable)()->{ int i = 0; }, (BiFunction<String,String,Integer>)(a,b)->12 };
		return new int[]{1,2,3,4};
	}
	
	public void dualParam(int i, int j){
		switch (j){
			case 1:
				System.out.print("is one");
				break;
			case 2:
			case 3:
				System.out.print("I haz fallthrough");
			default:
				System.out.print("awwww :(");
		}
		try {
			if (i == 1) {
				int pop = 0;
			} else if (i< 10){
				j++;
			} else if (i>200){
				j--;
			} else if (i<=12){
				j += 10;
			} else if (i >= 14){
				j -= 10;
				if (j<2) {
					Object blah = null;
					char c = 'A';
					System.out.print(c);
				}
			} else if (j != 15){
				System.out.print(j);
			}
		} catch (Exception e){
			System.out.println("OH NOES");
		}
	}

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
