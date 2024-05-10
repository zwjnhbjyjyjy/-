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
 * NPC��
 * @ClassName: Npc
 * @Description: �����ˣ����������Ϸ��Ȥ��ʵ���Զ�Ѱ·������ˮ�ݡ����ˮ�ݡ�������ҡ�
 * @author: Jing Yumeng
 * @CreateDate: 2022��12��28�� 20:09:13
 */
public class Npc extends Character{
	final String DANGER_MARKER = "-1";//Σ�ձ��
	private static final int ATTACK_RANGE = 64;//������Χ


	private List<ImageIcon> imgList;//���npc�ĸ������ͼƬ
	private int moveX;//��¼ͼƬ����
	private int imgW;//ͼƬ��
	private int imgH;//ͼƬ��
	private int npcNum;//��¼�ڼ���npc��2ΪnpcA��3ΪnpcB,4ΪnpcC
	private int step; //����npc��������
	private String[][] dangerZone;//����������
	private boolean[][] book;//bfs����

	private Vector<MoveTypeEnum> path;//���npc��������·��

	/**
	 * ��ʼ�����ԵĹ��캯��
	 * @param x ��ʼ������ this.x
	 * @param y ��ʼ������ this.y
	 * @param w ��ʼ������ this.w
	 * @param h ��ʼ������ this.h
	 * @param imgW ��ʼ������ this.imgW
	 * @param imgH ��ʼ������ this.imgH
	 * @param img ��ʼ������ this.imgList
	 * @param npcNum ��ʼ������ this.npcNum
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
	 * �½�npc
	 * @param data npc����
	 * @param i npc��ʼλ���ڵ�ͼ�ĵ�i+1��
	 * @param j npc��ʼλ���ڵ�ͼ�ĵ�j+1��
	 * @param npcNum �ڼ���npc
	 */
	public static Npc createNpc(List<String> data,int i,int j,int npcNum) {
		//data=[��ɫͼƬ��x,y,w,h]
		List<ImageIcon> imageList =
				new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
		int x = j*MapSquare.PIXEL_X+GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y+GameMap.getBiasY();//npc��ʼλ������
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;//����npc��ʾ��һ�������Сһ��
		int imgW = Integer.parseInt(data.get(3));
		int imgH = Integer.parseInt(data.get(4));
		return new Npc(x, y, w, h, imgW, imgH, imageList, npcNum);
	}

	/**
	 * չʾԪ��ͼ��
	 * @param g Ԫ��ͼ��
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
	 * ����״̬
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
	 * ���npc����Ҹ��������Զ��������
	 */
	private void autoAttack() {
		Rectangle npcRect = new Rectangle(getX(), getY(), getW(), getH());
		List<SuperElement> seList = ElementManager.getManager().getElementList("player");//��ȡ����б�
		for(SuperElement se:seList) {
			Rectangle playerRect = new Rectangle(se.getX()-ATTACK_RANGE, se.getY()-ATTACK_RANGE, se.getW()+2*ATTACK_RANGE, se.getH()+2*ATTACK_RANGE);
			//��npc���������Ϊ���ĵľŹ���Χ����npc��ǰ��ˮ��ʱ������ҷ��𹥻�
			if (playerRect.intersects(npcRect)&&getBubbleLargest()-getBubbleNum()>0) {
				addBubble();
			}
		}
	}


	/**
	 *��ȡ��ͼ�е�Σ�����򣬶�����б��
	 * @return ��������Σ�ձ�ǵĶ�ά����
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
		//��ˮ��λ���Լ�ˮ�ݱ�ը��Χ��λ����Σ�ձ��
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
	 *����������������������ɴ�·��
	 * @param di,dj �����ͼ��һ��λ�ã�Ѱ��ͨ�����λ�õ�·��
	 * @return �ҵ�·������true�����򷵻�false
	 */
	private boolean BFS(int di,int dj) {
		Queue<Point> queue = new LinkedList<>();
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> loc = GameMap.getIJ(getX(), getY());
		Point tPoint = new Point(loc.get(0), loc.get(1));//�Ȼ�ȡ��ǰλ��
		int next[][] = {{0,-1},{-1,0},{0,1},{1,0}};
		queue.add(tPoint);
		while(!queue.isEmpty()) {
			Point fPoint = queue.poll();
			book[fPoint.i][fPoint.j] = true;//���������ĵ���б��
			if (di==fPoint.i && dj==fPoint.j) {
				path = fPoint.path;
				return true;
			}
			//�����һ���ѵ��ĵ㲻�Ǵ���ĵ㣬������Ϊ��㣬�������ٽ����ĸ���
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
	 * �����ȡ��ͼ��һ����ȫ��λ�ã�Ѱ�ҿ�ͨ���·��
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
			} while (dangerZone[di][dj].equals(DANGER_MARKER) || gameMap.blockIsObstacle(di, dj));//���λ��Ϊ��ȫ����ʱ����ѭ��
		}while(!BFS(di, dj));//�ҵ�·��ʱ����ѭ��
	}

	/**
	 * Ѱ�Ұ�ȫ·��
	 * @return ����ҵ���ȫ·������true�����򷵻�false
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
			//�����һ���ѵ��ĵ���Σ������������Ϊ��㣬�������ٽ����ĸ���
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
	 *�ж��ܷ��������ƶ������߹�ȥ
	 * @return ������߷���true�����򷵻�false
	 */
	public boolean judgeForward(MoveTypeEnum m) {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		boolean go = false;
		List<Integer> ijList = GameMap.getIJ(getX(), getY());
		//��ʱ�ж�ǰ���ǵذ弴��ǰ��
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
	 *�ж��Ƿ���Ҫֹͣǰ��
	 * @return ���ֹͣ����true�����򷵻�false
	 */
	private boolean judgeStop(List<Integer> loc) {
		GameMap gameMap = ElementManager.getManager().getGameMap();
		dangerZone = getDangerZone();
		int i = loc.get(0);
		int j = loc.get(1);
		//�����ǰλ�õ��ĸ����������ͨ���ҵ�ǰλ�ð�ȫ����ֹͣǰ��
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
	 *npc�ƶ�
	 */
	@Override
	public void move() {
		if(step==MapSquare.PIXEL_X/Character.NPC_INIT_SPEED) {
			step=0;
			autoAttack();
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> loc = GameMap.getIJ(getX(), getY());
			dangerZone = getDangerZone();
			//��ǰλ��ΪΣ������ʱ������Ѱ�Ұ�ȫ·�����Σ�գ���ǰλ�ð�ȫʱ���Զ�Ѱ·
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

			//ǰ������ͨ��ʱ�������ҵ���ȫ·��������·���ˮ��
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
	 * �ж��Ƿ���ˮ����ײ
	 * @param tx,ty λ������
	 * @param list ˮ��Ԫ���б�
	 * @return �����ײ����false�����򷵻�true
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
	 * ���½�ɫͼƬ
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
	 * �ж��Ƿ���������ϷԪ����ײ
	 * @param tx,ty λ������
	 * @param list ��ϷԪ���б�
	 * @return �����ײ����false�����򷵻�true
	 */
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){
		Rectangle npcRect = new Rectangle(tx, ty, getW(), getH());
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX(), se.getY(), se.getW(), se.getH());
			if(npcRect.intersects(elementRect)) {//�����ײ
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
	 * ���ˮ��
	 */
	public void addBubble() {
		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(loc.get(0), loc.get(1));
		if( bubbleNum<bubbleLargest &&  //��ǰ��ˮ����С������ֵ����ǰλ��û��ˮ��
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
