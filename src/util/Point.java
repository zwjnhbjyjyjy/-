package util;

import model.manager.MoveTypeEnum;
import java.util.Vector;

/**
 *@ClassName: Point
 * @Description:Point类,用于NPC实现自动化
 * @author:He Xiaofei
 * @Time:2022-12-26
 */
public class Point {
	public int i;
	public int j;
	public Vector<MoveTypeEnum> path;
	public Point(int i,int j) {
		this.i = i;
		this.j = j;
		path = new Vector<>();
	}
}
