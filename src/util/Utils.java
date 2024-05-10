package util;
import java.util.Random;
/**
 *@ClassName: Utils
 * @Description:Utils类
 * @author:He Xiaofei
 * @Time:2022-12-27
 */
public class Utils {
	//获得一个随机数字
	public static Random random = new Random();
	//判断x是否在数字a、b之间
	public static boolean between(int x, int a, int b) {
		if(a>b) return between(x, b, a);
		return x>a&&x<b;
	}
}
