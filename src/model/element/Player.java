package model.element;

import model.loader.ElementLoader;
import model.manager.ElementManager;
import model.manager.GameMap;
import model.manager.MoveTypeEnum;
import util.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * Player类
 * @ClassName: Player
 * @Description: 玩家。玩家操作的体现，实现玩家移动，放置水泡等。
 * @author: Zhao Wenjie
 * @CreateDate: 2022年12月30日
 */
public class Player extends Character{

	//玩家图片
	private ImageIcon img;

	private int moveX;

	private int moveY;

	//记录攻击状态，默认为false
	private boolean attack;

	//记录是否为一直按着攻击键，实现一次按键只放一个水泡
	private boolean keepAttack;

	//记录第几个玩家，0为玩家一，1为玩家二
	private int playerNum;

	/**
	 * 初始化玩家属性的构造方法
	 *
	 * @param initialX 左上角坐标x
	 * @param initialY 左上角坐标y
	 * @param initialW 宽度w
	 * @param initialH 高度h
	 * @param initialImg 玩家图片img
	 * @param initialPlayerNum 玩家序号playerNum
	 */
	public Player(int initialX, int initialY, int initialW, int initialH, ImageIcon initialImg, int initialPlayerNum) {

		super(initialX, initialY, initialW, initialH,"player");
		this.img = initialImg;
		this.playerNum = initialPlayerNum;
		moveX = 0;
		moveY = 0;
		attack = false;
		keepAttack = false;
	}

	/**
	 * 在地图左上角创建玩家
	 * @param list 存储玩家信息的列表，list = [PlayerA,x,y,w,h]
	 * @param playerNum 玩家序号
	 * @return 玩家
	 */
	public static Player createPlayer(List<String> list,int playerNum) {

		int x = Integer.parseInt(list.get(1));
		int y = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		//获取资源加载器的图片字典
		Map<String, ImageIcon> imageMap =
				ElementLoader.getElementLoader().getImageMap();
		return new Player(x, y, w, h, imageMap.get(list.get(0)),playerNum);
	}

	/**
	 * 在地图指定位置创建玩家
	 * @param data 地图数据
	 * @param i 在地图中的横向相对位置
	 * @param j 在地图重点纵向相对位置
	 * @param playerNum 玩家序号
	 * @return 玩家
	 */
	public static Player createPlayer(List<String> data,int i,int j,int playerNum) {

		int x = j*MapSquare.PIXEL_X + GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y + GameMap.getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		//获取资源加载器的图片字典
		Map<String, ImageIcon> imageMap =
				ElementLoader.getElementLoader().getImageMap();
		return new Player(x, y, w, h, imageMap.get(data.get(0)),playerNum);
	}

	/**
	 * 展示元素图片
	 * @param g 画笔
	 */
	@Override
	public void showElement(Graphics g) {

		//判断该元素是否需要展示
		if(!isShowing)
			return;

		g.drawImage(img.getImage(),
				getX(), getY(), 	//屏幕左上角坐标
				getX()+getW(), getY()+getH(), 	//屏幕右下坐标
				(moveX/6)*100+27, moveY*100+43, 			//图片左上坐标
				(moveX/6)*100+72, moveY*100+99, 			//图片右下坐标
				null);

	}

	/**
	 * 移动
	 */
	@Override
	public void move() {

		//获取移动前坐标
		int tX = getX();
		int tY = getY();
		//设置移动后坐标
		switch(moveType) {
			case TOP:
				tY-=speed;
				break;
			case LEFT:
				tX-=speed;
				break;
			case RIGHT:
				tX+=speed;
				break;
			case DOWN:
				tY+=speed;
				break;
			case STOP:
			default:
				break;
		}

		//判断移动是否与炸弹、可破坏方块、不可破坏方块相遇
		boolean det1 = crashDetection(tX, tY, ElementManager.getManager().getElementList("obstacle"));
		boolean det2 = crashDetection(tX, tY, ElementManager.getManager().getElementList("fragility"));
		boolean det3 = bubbleCrashDetection(tX, tY, ElementManager.getManager().getElementList("bubble"));

		//更新坐标
		if(det1&&det2&&det3) {
			setX(tX);
			setY(tY);
		}
	}

	/**
	 * 检测与未爆炸炸弹的碰撞
	 *
	 * @param tx 临时x
	 * @param ty 临时y
	 * @param list 炸弹list
	 * @return 没有碰撞
	 */
	private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {

		for(SuperElement se:list) {
			switch(moveType) {
				case TOP:
				case DOWN:
					if(Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound())
							|| Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound())
							||(getBottomBound()==se.getBottomBound()&&getTopBound()==se.getTopBound())) {
						return true;
					}
					break;
				case LEFT:
				case RIGHT:
					if(Utils.between(getLeftBound(), se.getLeftBound(), se.getRightBound())
							||Utils.between(getRightBound(), se.getLeftBound(), se.getRightBound())
							||(getLeftBound()==se.getLeftBound()&&getRightBound()==se.getRightBound())) {
						return true;
					}
					break;
				default:
					break;
			}
		}
		return crashDetection(tx, ty, list);
	}

	/**
	 * 碰撞检测+平滑移动
	 *
	 * @param tx 临时x
	 * @param ty 临时y
	 * @param list 可能碰撞物的list
	 * @return boolean,false表示相撞，true表示未相撞
	 */
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){

		//判断碰撞偏差值
		int bias = 1;
		//平滑移动阈值
		int THRESHOLD = 25;
		Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
		Random random = new Random();
		GameMap gameMap = ElementManager.getManager().getGameMap();

		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
			//如果碰撞，即两个rectangle的交点为非空
			if(playerRect.intersects(elementRect)) {
				//判断方向
				switch(moveType) {
					case TOP:
					case DOWN:
						//两个rectangle交汇处的宽度
						int width=Math.min(getX()+getW(),se.getX()+se.getW())-Math.max(getX(), se.getX());
						//超过阈值不做平滑处理
						if(width>THRESHOLD)
							break;
						//玩家在左边
						if(getX()<se.getX()) {
							if(moveType==MoveTypeEnum.TOP&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getTopBound()-10)))
								break;
							else if(moveType==MoveTypeEnum.DOWN&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getBottomBound()+10)))
								break;
							for(int i=0;i<width;i++) {
								if(random.nextBoolean())
									setX(getX()-1);
							}
						} else {
							if(moveType==MoveTypeEnum.TOP&&!gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getTopBound()-10)))
								break;
							else if(moveType== MoveTypeEnum.DOWN&&!gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getBottomBound()+10)))
								break;
							for(int i=0;i<width;i++) {
								if(random.nextBoolean())
									setX(getX()+1);
							}
						}
						break;
					case LEFT:
					case RIGHT:
						////两个rectangle交汇处的高度
						int height=Math.min(getY()+getH(),se.getY()+se.getH())-Math.max(getY(), se.getY());
						//超过阈值不做平滑处理
						if(height>THRESHOLD)
							break;
						//玩家在上面
						if(getY()<se.getY()) {
							if(moveType==MoveTypeEnum.LEFT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()-10, getTopBound())))
								break;
							else if(moveType==MoveTypeEnum.RIGHT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()+10, getTopBound())))
								break;
							for(int i=0;i<height;i++) {
								if(random.nextBoolean())
									setY(getY()-1);
							}
						} else {
							if(moveType==MoveTypeEnum.LEFT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()-10, getBottomBound())))
								break;
							else if(moveType==MoveTypeEnum.RIGHT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()+10, getBottomBound())))
								break;
							for(int i=0;i<height;i++) {
								if(random.nextBoolean())
									setY(getY()+1);
							}
						}
						break;
					default:
						break;
				}
				return false;
			}
		}
		return true;
	}


	/**
	 * 更新player状态
	 */
	@Override
	public void update() {
		if(!dead) {
			move();
			addBubble();
			updateImage();
			destroy();
		}
	}

	/**
	 * 更新图片
	 */
	public void updateImage() {

		if(moveType==MoveTypeEnum.STOP){
			return;
		}

		if(++moveX>=24)
			moveX = 0;

		switch (moveType) {
			case TOP:
				moveY = 3;
				break;
			case LEFT:
				moveY = 1;
				break;
			case RIGHT:
				moveY = 2;
				break;
			case DOWN:
				moveY = 0;
				break;
			default:
				break;
		}
	}

	/**
	 * 添加气泡
	 */
	public void addBubble() {

		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> mapList = GameMap.getIJ(loc.get(0), loc.get(1));
		//判断是否为攻击状态，当前的炸弹数小于上限值，当前位置没有炸弹
		if(attack && !dead && bubbleNum<bubbleLargest &&
				gameMap.getBlockSquareType(mapList.get(0), mapList.get(1))!=GameMap.SquareType.BUBBLE) {

			List<SuperElement> list =
					ElementManager.getManager().getElementList("bubble");
			list.add(Bubble.createBubble(loc.get(0), loc.get(1), ElementLoader.getElementLoader().getGameInfoMap().get("bubble"),playerNum,getBubblePower()));
			attack = false;
			bubbleNum++;
		}
	}

	/**
	 * 破坏
	 */
	@Override
	public void destroy() {}


	/**
	 * 获取玩家图片
	 *
	 * @return 玩家图片
	 */
	public ImageIcon getImg() {
		return img;
	}

	/**
	 * 修改玩家图片
	 *
	 * @param img 新的玩家图片
	 */
	public void setImg(ImageIcon img) {
		this.img = img;
	}

	/**
	 * 判断攻击状态
	 *
	 * @return 攻击状态
	 */
	public boolean isAttack() {
		return attack;
	}

	/**
	 * 修改攻击状态
	 *
	 * @param attack 攻击状态
	 */
	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	/**
	 * 判断是否一直按着攻击键
	 *
	 * @return 是否一直按着攻击键
	 */
	public boolean isKeepAttack() {
		return keepAttack;
	}

	/**
	 * 修改按攻击键的状态
	 *
	 * @param keepAttack 按攻击键的状态
	 */
	public void setKeepAttack(boolean keepAttack) {
		this.keepAttack = keepAttack;
	}

	/**
	 * 获取玩家序号
	 *
	 * @return 玩家序号
	 */
	public int getPlayerNum() {
		return this.playerNum;
	}

	/**
	 * 获取存活状态
	 *
	 * @return 存活状态
	 */
	@Override
	public boolean isDead() {
		return dead;
	}

	/**
	 * 设置存活状态
	 * @param dead 存活状态
	 */
	@Override
	public void setDead(boolean dead) {

		this.dead = dead;
	}
}

