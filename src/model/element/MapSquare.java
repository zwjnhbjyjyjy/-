package model.element;

import model.manager.GameMap;

import javax.swing.*;
import java.awt.*;

/**
 * ��ͼ������
 * @ClassName: MapSquare
 * @Description: ��ͼ����VO��
 * @author: LiuXingtong
 * @CreateDate: 2022��12��25�� ����23��11
 */
public class MapSquare extends SuperElement {
	public final static int PIXEL_X = 64;//��λ����x
	public final static int PIXEL_Y = 64;//��λ����y
	private ImageIcon img;
	private int sx,sy,dx,dy;

	public MapSquare(int i, int j ,ImageIcon img, int sx, int sy, int dx, int dy, int scaleX,int scaleY) {
		super((j-scaleX+1)*PIXEL_X+ GameMap.getBiasX(),
				(i-scaleY+1)*PIXEL_Y+GameMap.getBiasY(),
				PIXEL_X*scaleX, PIXEL_Y*scaleY);
		this.img = img;
		this.setPictureLoc(sx, sy, dx, dy);
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(),
				getX(), getY(),                  //��Ļ���Ͻ�����
				getX()+getW(), getY()+getH(),    //��Ļ���½�����
				sx, sy,    //ͼƬ���Ͻ�����
				dx, dy,    //ͼƬ���½�����
				null);
	}

	public void setPictureLoc(int sx,int sy,int dx,int dy){
		this.sx = sx;
		this.sy = sy;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void move() {}

	@Override
	public void destroy() {}

}
