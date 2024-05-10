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
 * @Description:水泡炸弹类
 * @author: Jiaxiaoyang
 * @CreateDate: 2022年12月25日 下午6:25:26
 */
public class Bubble extends SuperElement{

	private ImageIcon img;
	private int moveX;
	private int playerNum;//表示对应玩家的炸弹，0为玩家一，1为玩家二，2为npcA，3为npcB，4为npcC
	private int imgW;
	private int imgH;
	private int power;

	//构造函数

	/**
	 *
	 * @param x 泡泡位置横坐标
	 * @param y 泡泡位置纵坐标
	 * @param w 泡泡位置长度
	 * @param h 泡泡位置高度
	 * @param img
	 * @param imgW 图片长度
	 * @param imgH 图片高度
	 * @param playerNum 玩家序号（0为玩家一，1为玩家二，2为npcA，3为npcB，4为npcC）
	 * @param power  泡泡的爆炸范围
	 */
	public Bubble(int x, int y, int w, int h, ImageIcon img, int imgW, int imgH, int playerNum,int power) {
		super(x, y, w, h);
		this.img = img;
		this.playerNum = playerNum;
		this.imgW = imgW;
		this.imgH = imgH;
		this.power = power;
		moveX = 0;
		//地图对应位置设置为障碍物，不能通过
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(x, y);
		gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.BUBBLE);
	}
	/**
	 *
	 * @param x 泡泡位置横坐标
	 * @param y 泡泡位置纵坐标
	 * @param list list=[Bubble,w,h]
	 * @param playerNum 玩家序号（0为玩家一，1为玩家二，2为npcA，3为npcB，4为npcC）
	 * @param power 泡泡的爆炸范围
	 * @return Bubble 泡泡
	 */
	public static model.element.Bubble createBubble(int x, int y, List<String> list, int playerNum, int power) {
		int imgW = Integer.parseInt(list.get(1));
		int imgH = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap =
				ElementLoader.getElementLoader().getImageMap();//获取资源加载器的图片字典
		return new model.element.Bubble(x, y, w, h, imageMap.get(list.get(0)), imgW, imgH ,playerNum, power);
	}

	/**
	 *
	 * @param g 元素图片
	 */
	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(),
				getX(), getY(), 	//屏幕左上角坐标
				getX()+getW(), getY()+getH(), 	//屏幕右下坐标
				(moveX/8)*imgW, 0, 				//图片左上坐标
				(moveX/8+1)*imgW, imgH, 			//图片右下坐标
				null);
	}
	/**
	 * 重写父类模板
	 */
	@Override
	public void update() {
		super.update();
		updateImage();
	}

	/**
	 * 更新图片
	 */
	public void updateImage() {
		if(++moveX>=32)
			moveX = 0;
	}
	/**
	 * 使用计时器，2.5秒改变Alive状态
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
	 * 改变炸弹玩家已经放在炸弹数bubbleNum,显示爆炸效果，加入ExplodeBubble
	 * 将地图位置设为floor
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
	 * toSring 方法重写
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
