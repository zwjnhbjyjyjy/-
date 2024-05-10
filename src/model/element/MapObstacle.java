package model.element;


import model.loader.ElementLoader;

import javax.swing.*;
import java.util.List;

/**
 * ��ͼ�ϰ�����
 * @ClassName: MapObstacle
 * @Description: ������ͼ�ϵ�
 * @author: LiuXingtong
 * @CreateDate: 2022��12��25�� ����6:31:18
 */
public class MapObstacle extends MapSquare {

	public MapObstacle(int i, int j, ImageIcon img, int sx, int sy, int dx, int dy, int scaleX, int scaleY) {
		super(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
	}

	public static MapObstacle createMapObstacle(List<String> data,int i, int j) {
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get(data.get(0));
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int scaleX = Integer.parseInt(data.get(6));
		int scaleY = Integer.parseInt(data.get(7));
		MapObstacle mapObstacle = new MapObstacle(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
		return mapObstacle;
	}

	@Override
	public void update() {}

}
