package model.element;

import model.loader.ElementLoader;
import model.manager.ElementManager;
import model.manager.GameMap;
import model.manager.MoveTypeEnum;
import util.Point;
import util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

/***
 * NPC类
 * @ClassName: Npc
 * @Description: 机器人，用于添加游戏乐趣。实现自动寻路、放置水泡、躲避水泡、攻击玩家。
 * @author: Jing Yumeng
 * @CreateDate: 2022年12月28日 20:09:13
 */
public class Npc extends Character{
	final String DANGER_MARKER = "-1";//危险标记
	private static final int ATTACK_RANGE = 64;//攻击范围


	private List<ImageIcon> imgList;//存放npc四个方向的图片
	private int moveX;//记录图片索引
	private int imgW;//图片宽
	private int imgH;//图片高
	private int npcNum;//记录第几个npc，2为npcA，3为npcB,4为npcC
	private int step; //控制npc步伐节奏
	private String[][] dangerZone;//不可走区域
	private boolean[][] book;//bfs数组

	private Vector<MoveTypeEnum> path;//存放npc接下来的路径

	/**
	 * 初始化属性的构造函数
	 * @param x 初始化属性 this.x
	 * @param y 初始化属性 this.y
	 * @param w 初始化属性 this.w
	 * @param h 初始化属性 this.h
	 * @param imgW 初始化属性 this.imgW
	 * @param imgH 初始化属性 this.imgH
	 * @param img 初始化属性 this.imgList
	 * @param npcNum 初始化属性 this.npcNum
	 */
	public Npc(int x, int y, int w, int h, int imgW, int imgH,List<ImageIcon> img, int npcNum) {
		super(x, y, w, h,"npc");
		this.imgW = imgW;
		this.imgH = imgH;
		this.imgList = new ArrayList<>(img);
		this.path = new Vector<>();
		this.npcNum = npcNum;
		moveX = 0;
		step = 0;
		moveType=MoveTypeEnum.STOP;
	}

	/**
	 * 新建npc
	 * @param data npc数据
	 * @param i npc初始位置在地图的第i+1行
	 * @param j npc初始位置在地图的第j+1列
	 * @param npcNum 第几个npc
	 */
	public static Npc createNpc(List<String> data,int i,int j,int npcNum) {
		//data=[角色图片，x,y,w,h]
		List<ImageIcon> imageList =
				new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
		int x = j*MapSquare.PIXEL_X+GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y+GameMap.getBiasY();//npc初始位置坐标
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;//控制npc显示与一个方格大小一致
		int imgW = Integer.parseInt(data.get(3));
		int imgH = Integer.parseInt(data.get(4));
		return new Npc(x, y, w, h, imgW, imgH, imageList, npcNum);
	}

	/**
	 * 展示元素图形
	 * @param g 元素图形
	 */
	@Override
	public void showElement(Graphics g) {
		if(isShowing==false) return;
		g.drawImage(imgList.get(moveX).getImage(),
				getX(), getY(),
				getX()+getW(), getY()+getH(),
				0, 0,
				getImgW(), getImgH(),
				null);
	}

	/**
	 * 更新状态
	 */
	@Override
	public void update() {
		if(!dead) {
			move();
			updateImage();
			destroy();
		}
	}

	/**
	 * 如果npc在玩家附近，则自动攻击玩家
	 */
	private void autoAttack() {
		Rectangle npcRect = new Rectangle(getX(), getY(), getW(), getH());
		List<SuperElement> seList = ElementManager.getManager().getElementList("player");//获取玩家列表
		for(SuperElement se:seList) {
			Rectangle playerRect = new Rectangle(se.getX()-ATTACK_RANGE, se.getY()-ATTACK_RANGE, se.getW()+2*ATTACK_RANGE, se.getH()+2*ATTACK_RANGE);
			//当npc处于以玩家为中心的九宫格范围内且npc当前有水泡时，对玩家发起攻击
			if (playerRect.intersects(npcRect)&&getBubbleLargest()-getBubbleNum()>0) {
				addBubble();
			}
		}
	}


	/**
	 *获取地图中的危险区域，对其进行标记
	 * @return 返回做好危险标记的二维数组
	 */
	private String[][] getDangerZone(){
		String[][] dangerZone = new String[GameMap.getMapRows()][GameMap.getMapCols()];
		List<List<String>> mapList = GameMap.getMapList();
		for(int i=0;i<mapList.size();i++) {
			Object[] tarr = mapList.get(i).toArray();
			for(int j=0;j<GameMap.getMapCols();j++) {
				dangerZone[i][j] = tarr[j].toString();
			}
		}
		//将水泡位置以及水泡爆炸范围的位置做危险标记
		List<SuperElement> bubbleList = ElementManager.getManager().getElementList("bubble");
		for(SuperElement se:bubbleList) {
			Bubble bubble = (Bubble) se;
			List<Integer> loc = GameMap.getIJ(se.getX(), se.getY());
			dangerZone[loc.get(0)][loc.get(1)] = DANGER_MARKER;
			for(int i=Math.max(loc.get(0)-bubble.getPower(),0);i<=Math.min(loc.get(0)+bubble.getPower(),GameMap.getMapRows()-1);i++)
				dangerZone[i][loc.get(1)] = DANGER_MARKER;
			for(int i=Math.max(loc.get(1)-bubble.getPower(),0);i<=Math.min(loc.get(1)+bubble.getPower(),GameMap.getMapCols()-1);i++)
				dangerZone[loc.get(0)][i] = DANGER_MARKER;
		}
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(SuperElement se:explodeList) {
			BubbleExplode explode = (BubbleExplode) se;
			List<Integer> loc = GameMap.getIJ(se.getX(), se.getY());
			dangerZone[loc.get(0)][loc.get(1)] = DANGER_MARKER;
			int up = explode.getUp();
			int down = explode.getDown();
			int left = explode.getLeft();
			int right = explode.getRight();
			for(int i=Math.max(loc.get(0)-up,0);i<=Math.min(loc.get(0)+down,GameMap.getMapRows()-1);i++)
				dangerZone[i][loc.get(1)] = DANGER_MARKER;
			for(int i=Math.max(loc.get(1)-left,0);i<=Math.min(loc.get(1)+right,GameMap.getMapCols()-1);i++)
				dangerZone[loc.get(0)][i] = DANGER_MARKER;
		}
		for(int i=0;i<GameMap.getMapRows();i++) {
			for(int j=0;j<GameMap.getMapCols();j++) {
				if(dangerZone[i][j].charAt(0)==GameMap.SquareType.BUBBLE.value()) {
					for(int k=Math.max(i-bubblePower,0);k<=Math.min(i+bubblePower,GameMap.getMapRows()-1);k++)
						dangerZone[k][j] = DANGER_MARKER;
					for(int k=Math.max(j-bubblePower,0);k<=Math.min(j+bubblePower,GameMap.getMapRows()-1);k++)
						dangerZone[i][k] = DANGER_MARKER;
				}
			}
		}
		return dangerZone;
	}

	/**
	 *广度优先搜索遍历，搜索可达路径
	 * @param di,dj 传入地图上一个位置，寻找通往这个位置的路径
	 * @return 找到路径返回true，否则返回false
	 */
	private boolean BFS(int di,int dj) {
		Queue<Point> queue = new LinkedList<>();
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> loc = GameMap.getIJ(getX(), getY());
		Point tPoint = new Point(loc.get(0), loc.get(1));//先获取当前位置
		int next[][] = {{0,-1},{-1,0},{0,1},{1,0}};
		queue.add(tPoint);
		while(!queue.isEmpty()) {
			Point fPoint = queue.poll();
			book[fPoint.i][fPoint.j] = true;//对搜索过的点进行标记
			if (di==fPoint.i && dj==fPoint.j) {
				path = fPoint.path;
				return true;
			}
			//如果上一个搜到的点不是传入的点，则以其为起点，搜索其临近的四个点
			int ti,tj;
			for(int i=0;i<4;i++) {
				ti = fPoint.i+next[i][0];
				tj = fPoint.j+next[i][1];
				List<Integer> tloc = new ArrayList<>();
				tloc.add(ti);tloc.add(tj);
				if(!book[ti][tj]&&!dangerZone[ti][tj].equals(DANGER_MARKER)&&!gameMap.blockIsObstacle(ti, tj)) {
					tPoint = new Point(ti, tj);
					tPoint.path.addAll(fPoint.path);
					tPoint.path.addElement(MoveTypeEnum.values()[i]);
					queue.add(tPoint);
				}
			}
		}
		return false;
	}

	/**
	 * 随机获取地图上一个安全的位置，寻找可通达的路径
	 */
	private void findPath() {
		dangerZone = getDangerZone();
		book = new boolean[GameMap.getMapRows()][GameMap.getMapCols()];
		GameMap gameMap = ElementManager.getManager().getGameMap();
		int di=0,dj=0;
		do {
			do {
				di = (int) (Math.random() * GameMap.getMapRows());
				dj = (int) (Math.random() * GameMap.getMapCols());
			} while (dangerZone[di][dj].equals(DANGER_MARKER) || gameMap.blockIsObstacle(di, dj));//随机位置为安全区域时结束循环
		}while(!BFS(di, dj));//找到路径时结束循环
	}

	/**
	 * 寻找安全路径
	 * @return 如果找到安全路径返回true，否则返回false
	 */
	private boolean findSafePath() {
		book = new boolean[GameMap.getMapRows()][GameMap.getMapCols()];
		dangerZone = getDangerZone();
		Queue<Point> queue = new LinkedList<>();
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> loc = GameMap.getIJ(getX(), getY());
		Point tPoint = new Point(loc.get(0), loc.get(1));
		int next[][] = {{0,-1},{-1,0},{0,1},{1,0}};
		queue.add(tPoint);
		while(!queue.isEmpty()) {
			Point fPoint = queue.poll();
			book[fPoint.i][fPoint.j] = true;
			if (!dangerZone[fPoint.i][fPoint.j].equals(DANGER_MARKER)
					&& gameMap.getBlockSquareType(fPoint.i, fPoint.j)!=GameMap.SquareType.BUBBLE) {
				path.clear();
				path.addAll(fPoint.path);
				return true;
			}
			//如果上一个搜到的点是危险区域，则以其为起点，搜索其临近的四个点
			int ti,tj;
			for(int i=0;i<4;i++) {
				ti = fPoint.i+next[i][0];
				tj = fPoint.j+next[i][1];
				List<Integer> tloc = new ArrayList<>();
				tloc.add(ti);tloc.add(tj);
				if(!book[ti][tj]&&gameMap.blockIsWalkable(tloc)) {
					tPoint = new Point(ti, tj);
					tPoint.path.addAll(fPoint.path);
					tPoint.path.addElement(MoveTypeEnum.values()[i]);
					queue.add(tPoint);
				}
			}
		}
		return false;
	}

	/**
	 *判断能否按照所给移动类型走过去
	 * @return 如果能走返回true，否则返回false
	 */
	public boolean judgeForward(MoveTypeEnum m) {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		boolean go = false;
		List<Integer> ijList = GameMap.getIJ(getX(), getY());
		//暂时判断前面是地板即可前进
		switch(m) {
			case LEFT: if(gameMap.blockIsWalkable(ijList.get(0), ijList.get(1)-1)) go = true;
				break;
			case RIGHT: if(gameMap.blockIsWalkable(ijList.get(0), ijList.get(1)+1)) go = true;
				break;
			case TOP: if(gameMap.blockIsWalkable(ijList.get(0)-1, ijList.get(1))) go = true;
				break;
			case DOWN: if(gameMap.blockIsWalkable(ijList.get(0)+1, ijList.get(1))) go = true;
				break;
			case STOP: go=true;
				break;
		}
		return go;
	}

	/**
	 *判断是否需要停止前进
	 * @return 如果停止返回true，否则返回false
	 */
	private boolean judgeStop(List<Integer> loc) {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		dangerZone = getDangerZone();
		int i = loc.get(0);
		int j = loc.get(1);
		//如果当前位置的四个方向均不可通行且当前位置安全，则停止前进
		if((dangerZone[i+1][j].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i+1, j))
				&&(dangerZone[i-1][j].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i-1, j))
				&&(dangerZone[i][j+1].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i, j+1))
				&&(dangerZone[i][j-1].equals(DANGER_MARKER)||!gameMap.blockIsWalkable(i, j-1))
				&&!dangerZone[i][j].equals(DANGER_MARKER)) {
			return true;
		}

		return false;
	}

	/**
	 *npc移动
	 */
	@Override
	public void move() {
		if(step==MapSquare.PIXEL_X/Character.NPC_INIT_SPEED) {
			step=0;
			autoAttack();
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> loc = GameMap.getIJ(getX(), getY());
			dangerZone = getDangerZone();
			//当前位置为危险区域时，首先寻找安全路径躲避危险，当前位置安全时，自动寻路
			if(dangerZone[loc.get(0)][loc.get(1)].equals(DANGER_MARKER)) {
				findSafePath();
			} else if(path.isEmpty()) {
				if(judgeStop(loc)) {
					path.add(MoveTypeEnum.STOP);
				} else {
					while(path.isEmpty()) {
						findPath();
					}
				}
			}
			if(path.size()>0) {
				moveType = path.firstElement();
				path.removeElementAt(0);
			}

			//前方不可通行时，在能找到安全路径的情况下放置水泡
			if(!judgeForward(moveType)) {
				gameMap.setBlockSquareType(loc, GameMap.SquareType.BUBBLE);
				boolean find = findSafePath();
				gameMap.setBlockSquareType(loc, GameMap.SquareType.FLOOR);
				if(find) {
					addBubble();
					moveType = path.firstElement();
					path.removeElementAt(0);
				}
				else {
					findPath();
				}
			}
		}
		int tx = getX();
		int ty = getY();
		if(speed!=0) {
			switch (moveType) {
				case LEFT:
					tx -= speed;
					break;
				case RIGHT:
					tx += speed;
					break;
				case TOP:
					ty -= speed;
					break;
				case DOWN:
					ty += speed;
					break;
				default:
					break;
			}
			boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
			boolean det2 = bubbleCrashDetection(tx, ty, ElementManager.getManager().getElementList("bubble"));

			if (det1 && det2) {
				setX(tx);
				setY(ty);
				step++;
			}
		}

	}

	/**
	 * 判断是否与水泡碰撞
	 * @param tx,ty 位置坐标
	 * @param list 水泡元素列表
	 * @return 如果碰撞返回false，否则返回true
	 */
	private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {
		for(SuperElement se:list) {
			switch(moveType) {
				case TOP:
				case DOWN:
					if(Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound())
							||Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound())
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
	 * 更新角色图片
	 */
	private void updateImage() {
		switch(moveType) {
			case STOP: moveX = 0;break;
			case LEFT: moveX = 1;break;
			case RIGHT: moveX = 2;break;
			case TOP: moveX = 3;break;
			case DOWN: moveX = 0;break;
		}
	}

	/**
	 * 判断是否与其他游戏元素碰撞
	 * @param tx,ty 位置坐标
	 * @param list 游戏元素列表
	 * @return 如果碰撞返回false，否则返回true
	 */
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){
		Rectangle npcRect = new Rectangle(tx, ty, getW(), getH());
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX(), se.getY(), se.getW(), se.getH());
			if(npcRect.intersects(elementRect)) {//如果碰撞
				return false;
			}
		}
		return true;
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}


	/**
	 * 添加水泡
	 */
	public void addBubble() {
		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(loc.get(0), loc.get(1));
		if( bubbleNum<bubbleLargest &&  //当前的水泡数小于上限值，当前位置没有水泡
				gameMap.getBlockSquareType(maplist.get(0), maplist.get(1))!=GameMap.SquareType.BUBBLE) {
			bubbleNum++;
			List<SuperElement> list =
					ElementManager.getManager().getElementList("bubble");
			list.add(Bubble.createBubble(loc.get(0), loc.get(1), ElementLoader.getElementLoader().getGameInfoMap().get("bubble"),npcNum+2,getBubblePower()));

		}
	}

	//getters and setters
	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getNpcNum() {
		return npcNum;
	}

	public void setNpcNum(int npcNum) {
		this.npcNum = npcNum;
	}

	public int getImgW() {
		return imgW;
	}

	public void setImgW(int imgW) {
		this.imgW = imgW;
	}

	public int getImgH() {
		return imgH;
	}

	public void setImgH(int imgH) {
		this.imgH = imgH;
	}



}
