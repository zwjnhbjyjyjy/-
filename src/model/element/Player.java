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
 * Player��
 * @ClassName: Player
 * @Description: ��ҡ���Ҳ��������֣�ʵ������ƶ�������ˮ�ݵȡ�
 * @author: Zhao Wenjie
 * @CreateDate: 2022��12��30��
 */
public class Player extends Character{

	//���ͼƬ
	private ImageIcon img;

	private int moveX;

	private int moveY;

	//��¼����״̬��Ĭ��Ϊfalse
	private boolean attack;

	//��¼�Ƿ�Ϊһֱ���Ź�������ʵ��һ�ΰ���ֻ��һ��ˮ��
	private boolean keepAttack;

	//��¼�ڼ�����ң�0Ϊ���һ��1Ϊ��Ҷ�
	private int playerNum;

	/**
	 * ��ʼ��������ԵĹ��췽��
	 *
	 * @param initialX ���Ͻ�����x
	 * @param initialY ���Ͻ�����y
	 * @param initialW ���w
	 * @param initialH �߶�h
	 * @param initialImg ���ͼƬimg
	 * @param initialPlayerNum ������playerNum
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
	 * �ڵ�ͼ���ϽǴ������
	 * @param list �洢�����Ϣ���б�list = [PlayerA,x,y,w,h]
	 * @param playerNum ������
	 * @return ���
	 */
	public static Player createPlayer(List<String> list,int playerNum) {

		int x = Integer.parseInt(list.get(1));
		int y = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		//��ȡ��Դ��������ͼƬ�ֵ�
		Map<String, ImageIcon> imageMap =
				ElementLoader.getElementLoader().getImageMap();
		return new Player(x, y, w, h, imageMap.get(list.get(0)),playerNum);
	}

	/**
	 * �ڵ�ͼָ��λ�ô������
	 * @param data ��ͼ����
	 * @param i �ڵ�ͼ�еĺ������λ��
	 * @param j �ڵ�ͼ�ص��������λ��
	 * @param playerNum ������
	 * @return ���
	 */
	public static Player createPlayer(List<String> data,int i,int j,int playerNum) {

		int x = j*MapSquare.PIXEL_X + GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y + GameMap.getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		//��ȡ��Դ��������ͼƬ�ֵ�
		Map<String, ImageIcon> imageMap =
				ElementLoader.getElementLoader().getImageMap();
		return new Player(x, y, w, h, imageMap.get(data.get(0)),playerNum);
	}

	/**
	 * չʾԪ��ͼƬ
	 * @param g ����
	 */
	@Override
	public void showElement(Graphics g) {

		//�жϸ�Ԫ���Ƿ���Ҫչʾ
		if(!isShowing)
			return;

		g.drawImage(img.getImage(),
				getX(), getY(), 	//��Ļ���Ͻ�����
				getX()+getW(), getY()+getH(), 	//��Ļ��������
				(moveX/6)*100+27, moveY*100+43, 			//ͼƬ��������
				(moveX/6)*100+72, moveY*100+99, 			//ͼƬ��������
				null);

	}

	/**
	 * �ƶ�
	 */
	@Override
	public void move() {

		//��ȡ�ƶ�ǰ����
		int tX = getX();
		int tY = getY();
		//�����ƶ�������
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

		//�ж��ƶ��Ƿ���ը�������ƻ����顢�����ƻ���������
		boolean det1 = crashDetection(tX, tY, ElementManager.getManager().getElementList("obstacle"));
		boolean det2 = crashDetection(tX, tY, ElementManager.getManager().getElementList("fragility"));
		boolean det3 = bubbleCrashDetection(tX, tY, ElementManager.getManager().getElementList("bubble"));

		//��������
		if(det1&&det2&&det3) {
			setX(tX);
			setY(tY);
		}
	}

	/**
	 * �����δ��ըը������ײ
	 *
	 * @param tx ��ʱx
	 * @param ty ��ʱy
	 * @param list ը��list
	 * @return û����ײ
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
	 * ��ײ���+ƽ���ƶ�
	 *
	 * @param tx ��ʱx
	 * @param ty ��ʱy
	 * @param list ������ײ���list
	 * @return boolean,false��ʾ��ײ��true��ʾδ��ײ
	 */
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){

		//�ж���ײƫ��ֵ
		int bias = 1;
		//ƽ���ƶ���ֵ
		int THRESHOLD = 25;
		Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
		Random random = new Random();
		GameMap gameMap = ElementManager.getManager().getGameMap();

		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
			//�����ײ��������rectangle�Ľ���Ϊ�ǿ�
			if(playerRect.intersects(elementRect)) {
				//�жϷ���
				switch(moveType) {
					case TOP:
					case DOWN:
						//����rectangle���㴦�Ŀ��
						int width=Math.min(getX()+getW(),se.getX()+se.getW())-Math.max(getX(), se.getX());
						//������ֵ����ƽ������
						if(width>THRESHOLD)
							break;
						//��������
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
						////����rectangle���㴦�ĸ߶�
						int height=Math.min(getY()+getH(),se.getY()+se.getH())-Math.max(getY(), se.getY());
						//������ֵ����ƽ������
						if(height>THRESHOLD)
							break;
						//���������
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
	 * ����player״̬
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
	 * ����ͼƬ
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
	 * �������
	 */
	public void addBubble() {

		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> mapList = GameMap.getIJ(loc.get(0), loc.get(1));
		//�ж��Ƿ�Ϊ����״̬����ǰ��ը����С������ֵ����ǰλ��û��ը��
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
	 * �ƻ�
	 */
	@Override
	public void destroy() {}


	/**
	 * ��ȡ���ͼƬ
	 *
	 * @return ���ͼƬ
	 */
	public ImageIcon getImg() {
		return img;
	}

	/**
	 * �޸����ͼƬ
	 *
	 * @param img �µ����ͼƬ
	 */
	public void setImg(ImageIcon img) {
		this.img = img;
	}

	/**
	 * �жϹ���״̬
	 *
	 * @return ����״̬
	 */
	public boolean isAttack() {
		return attack;
	}

	/**
	 * �޸Ĺ���״̬
	 *
	 * @param attack ����״̬
	 */
	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	/**
	 * �ж��Ƿ�һֱ���Ź�����
	 *
	 * @return �Ƿ�һֱ���Ź�����
	 */
	public boolean isKeepAttack() {
		return keepAttack;
	}

	/**
	 * �޸İ���������״̬
	 *
	 * @param keepAttack ����������״̬
	 */
	public void setKeepAttack(boolean keepAttack) {
		this.keepAttack = keepAttack;
	}

	/**
	 * ��ȡ������
	 *
	 * @return ������
	 */
	public int getPlayerNum() {
		return this.playerNum;
	}

	/**
	 * ��ȡ���״̬
	 *
	 * @return ���״̬
	 */
	@Override
	public boolean isDead() {
		return dead;
	}

	/**
	 * ���ô��״̬
	 * @param dead ���״̬
	 */
	@Override
	public void setDead(boolean dead) {

		this.dead = dead;
	}
}

