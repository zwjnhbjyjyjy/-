package util;
import java.util.Random;
/**
 *@ClassName: Utils
 * @Description:Utils��
 * @author:He Xiaofei
 * @Time:2022-12-27
 */
public class Utils {
	//���һ���������
	public static Random random = new Random();
	//�ж�x�Ƿ�������a��b֮��
	public static boolean between(int x, int a, int b) {
		if(a>b) return between(x, b, a);
		return x>a&&x<b;
	}
}
