package model.element;


import java.awt.Graphics;
import java.awt.Rectangle;

/***
 * 游戏元素类
 * @ClassName: SuperElement
 * @Description: 所有游戏元素的父类
 * @author: Jing Yumeng
 * @CreateDate: 2022年12月27日 10:29:26
 */

public abstract class SuperElement {

	//元素坐标
	private int x;
	private int y;
	private int w;
	private int h;
	//记录是否存活
	private boolean alive;

	//空构造函数
	private SuperElement() {}
	/**
	 * 初始化属性的构造函数
	 * @param x 初始化属性 this.x
	 * @param y 初始化属性 this.y
	 * @param w 初始化属性 this.w
	 * @param h 初始化属性 this.h
	 * 初始存在状态为"true"
	 */
	public SuperElement(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		alive = true;
	}

	/**
	 * 更新状态
	 */
	public void update() {
		move();
		destroy();
	}


	/**
	 * 元素展示
	 * @param g 元素图片
	 */
	public abstract void showElement(Graphics g);


	/**
	 * 元素移动
	 */
	public abstract void move();

	/**
	 * 元素销毁
	 */
	public abstract void destroy();

	/**
	 * 判断传入元素与本元素是否位置重合
	 * @param se 需要进行判断的元素
	 * @return true or false
	 */
	public boolean crash(SuperElement se) {
		Rectangle r1 = new Rectangle(x, y, w, h);
		Rectangle r2 = new Rectangle(se.x, se.y, se.w, se.h);
		return r1.intersects(r2);//有交集范围true
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

