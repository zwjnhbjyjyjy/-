package model.element;


import java.awt.Graphics;
import java.awt.Rectangle;

/***
 * ��ϷԪ����
 * @ClassName: SuperElement
 * @Description: ������ϷԪ�صĸ���
 * @author: Jing Yumeng
 * @CreateDate: 2022��12��27�� 10:29:26
 */

public abstract class SuperElement {

	//Ԫ������
	private int x;
	private int y;
	private int w;
	private int h;
	//��¼�Ƿ���
	private boolean alive;

	//�չ��캯��
	private SuperElement() {}
	/**
	 * ��ʼ�����ԵĹ��캯��
	 * @param x ��ʼ������ this.x
	 * @param y ��ʼ������ this.y
	 * @param w ��ʼ������ this.w
	 * @param h ��ʼ������ this.h
	 * ��ʼ����״̬Ϊ"true"
	 */
	public SuperElement(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		alive = true;
	}

	/**
	 * ����״̬
	 */
	public void update() {
		move();
		destroy();
	}


	/**
	 * Ԫ��չʾ
	 * @param g Ԫ��ͼƬ
	 */
	public abstract void showElement(Graphics g);


	/**
	 * Ԫ���ƶ�
	 */
	public abstract void move();

	/**
	 * Ԫ������
	 */
	public abstract void destroy();

	/**
	 * �жϴ���Ԫ���뱾Ԫ���Ƿ�λ���غ�
	 * @param se ��Ҫ�����жϵ�Ԫ��
	 * @return true or false
	 */
	public boolean crash(SuperElement se) {
		Rectangle r1 = new Rectangle(x, y, w, h);
		Rectangle r2 = new Rectangle(se.x, se.y, se.w, se.h);
		return r1.intersects(r2);//�н�����Χtrue
	}



	//getters and setters
	public void setGeometry(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public int getTopBound() {
		return y;
	}

	public int getLeftBound() {
		return x;
	}

	public int getRightBound() {
		return x+w;
	}

	public int getBottomBound() {
		return y+h;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}

