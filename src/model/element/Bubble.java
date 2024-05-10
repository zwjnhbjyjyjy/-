package model.element;

import model.loader.ElementLoader;
import model.manager.ElementManager;
import model.manager.GameMap;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @ClassName: Bubble
 * @Description:ˮ��ը����
 * @author: Jiaxiaoyang
 * @CreateDate: 2022��12��25�� ����6:25:26
 */
public class Bubble extends SuperElement{

	private ImageIcon img;
	private int moveX;
	private int playerNum;//��ʾ��Ӧ��ҵ�ը����0Ϊ���һ��1Ϊ��Ҷ���2ΪnpcA��3ΪnpcB��4ΪnpcC
	private int imgW;
	private int imgH;
	private int power;

	//���캯��

	/**
	 *
	 * @param x ����λ�ú�����
	 * @param y ����λ��������
	 * @param w ����λ�ó���
	 * @param h ����λ�ø߶�
	 * @param img
	 * @param imgW ͼƬ����
	 * @param imgH ͼƬ�߶�
	 * @param playerNum �����ţ�0Ϊ���һ��1Ϊ��Ҷ���2ΪnpcA��3ΪnpcB��4ΪnpcC��
	 * @param power  ���ݵı�ը��Χ
	 */
	public Bubble(int x, int y, int w, int h, ImageIcon img, int imgW, int imgH, int playerNum,int power) {
		super(x, y, w, h);
		this.img = img;
		this.playerNum = playerNum;
		this.imgW = imgW;
		this.imgH = imgH;
		this.power = power;
		moveX = 0;
		//��ͼ��Ӧλ������Ϊ�ϰ������ͨ��
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(x, y);
		gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.BUBBLE);
	}
	/**
	 *
	 * @param x ����λ�ú�����
	 * @param y ����λ��������
	 * @param list list=[Bubble,w,h]
	 * @param playerNum �����ţ�0Ϊ���һ��1Ϊ��Ҷ���2ΪnpcA��3ΪnpcB��4ΪnpcC��
	 * @param power ���ݵı�ը��Χ
	 * @return Bubble ����
	 */
	public static model.element.Bubble createBubble(int x, int y, List<String> list, int playerNum, int power) {
		int imgW = Integer.parseInt(list.get(1));
		int imgH = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap =
				ElementLoader.getElementLoader().getImageMap();//��ȡ��Դ��������ͼƬ�ֵ�
		return new model.element.Bubble(x, y, w, h, imageMap.get(list.get(0)), imgW, imgH ,playerNum, power);
	}

	/**
	 *
	 * @param g Ԫ��ͼƬ
	 */
	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(),
				getX(), getY(), 	//��Ļ���Ͻ�����
				getX()+getW(), getY()+getH(), 	//��Ļ��������
				(moveX/8)*imgW, 0, 				//ͼƬ��������
				(moveX/8+1)*imgW, imgH, 			//ͼƬ��������
				null);
	}
	/**
	 * ��д����ģ��
	 */
	@Override
	public void update() {
		super.update();
		updateImage();
	}

	/**
	 * ����ͼƬ
	 */
	public void updateImage() {
		if(++moveX>=32)
			moveX = 0;
	}
	/**
	 * ʹ�ü�ʱ����2.5��ı�Alive״̬
	 */
	@Override
	public void move() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setAlive(false);
			}
		};
		timer.schedule(task, 2500);
	}



	/**
	 * �ı�ը������Ѿ�����ը����bubbleNum,��ʾ��ըЧ��������ExplodeBubble
	 * ����ͼλ����Ϊfloor
	 */
	@Override
	public void destroy() {
		if(!isAlive()) {
			List<SuperElement> explodeList =
					ElementManager.getManager().getElementList("explode");
			if(playerNum<2) {
				List<SuperElement> list2 = ElementManager.getManager().getElementList("player");
				Player player = (Player) list2.get(playerNum);
				player.setBubbleNum(player.getBubbleNum()-1);
				explodeList.add(BubbleExplode.createExplode(getX(), getY(), power,playerNum));
			}
			else {
				List<SuperElement> list2 = ElementManager.getManager().getElementList("npc");
				Npc npc = (Npc) list2.get(playerNum-2);
				npc.setBubbleNum(npc.getBubbleNum()-1);
				explodeList.add(BubbleExplode.createExplode(getX(), getY(), power,playerNum));
			}
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> maplist = GameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.FLOOR);


		}
	}
	//getters and setters
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getPlayerNum() {
		return this.playerNum;
	}

	public int getPower() {
		return power;
	}
	/**
	 * toSring ������д
	 */
	@Override
	public String toString() {
		return "Bubble{" +
				"img=" + img +
				", moveX=" + moveX +
				", playerNum=" + playerNum +
				", imgW=" + imgW +
				", imgH=" + imgH +
				", power=" + power +
				'}';
	}
}
