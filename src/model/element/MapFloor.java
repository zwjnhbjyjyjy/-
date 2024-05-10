package model.element;


import model.loader.ElementLoader;

import javax.swing.*;
import java.util.List;

/**
 * ��ͼ�ذ���
 * @ClassName: MapFloor
 * @Description: ������ͼ�ذ�
 * @author: LiuXingtong
 * @CreateDate: 2022��12��25�� ����6:31:34
 */
public class MapFloor extends MapSquare {
	public MapFloor(int i, int j, ImageIcon img, int sx, int sy, int dx, int dy, int scaleX, int scaleY) {
		super(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
	}

	public static MapFloor createMapFloor(List<String> data, int i,int j) {
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get("mapObstacle");
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int scaleX = Integer.parseInt(data.get(6));
		int scaleY = Integer.parseInt(data.get(7));
		return new MapFloor(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
	}

	@Override
	public void update() {}

}
