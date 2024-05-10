package model.manager;

import controller.GameController;
import model.element.*;
import model.element.Character;
import model.loader.ElementLoader;
import view.BeginJPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@ClassName:GameMap
 * @Description:ʵ����Ϸ��ͼ
 * @author:He Xiaofei
 * @CreateDate:2022-12-30
 */
public class GameMap {
	//��ͼ���
	private int windowW;
	//��ͼ�߶�
	private int windowH;
	//��ͼ����
	private static int mapRows;
	//��ͼ����
	private static int mapCols;
	//λ������x
	private static int biasX;
	//λ������Y
	private static int biasY;
	//��ͼ
	private static List<List<String>> mapList;

	//�Զ��巽�����Ͷ�Ӧö����
	public enum SquareType{
		//0�����ϰ��1����ذ壻2����ɴݻ��3������ߣ�6�������1��7�������2��8����npc��9����ˮ��
		OBSTACLE('0'),FLOOR('1'),FRAGILITY('2'),ITEM('3'),PLAYER_1('6'),PLAYER_2('7'),NPC('8'),BUBBLE('9');
		//valueĬ��Ϊ 0
		private char value = 0;
		//�޸�value
		private SquareType(char value) {
			this.value = value;
		}
		//��charת��Ϊ��Ӧ��enum
		public static SquareType valueOf(char c) {
			switch (c) {
				case '0':  return OBSTACLE;	//�ϰ���
				case '1':  return FLOOR;	//�ذ�
				case '2':  return FRAGILITY;//�ɴݻ���
				case '3':  return ITEM;  	//����
				case '6':  return PLAYER_1;	//���1
				case '7':  return PLAYER_2;	//���2
				case '8':  return NPC;		//NPC
				case '9':  return BUBBLE;  	//ˮ��
				default:
					return null;
			}
		}
		//��ȡvalue
		public char value() {
			return this.value;
		}
	}

	//���캯��,�����ͼ�Ŀ�Ⱥ͸߶�
	public GameMap(int windowW, int windowH) {
		this.windowW = windowW;
		this.windowH = windowH;
	}

	//�����ذ�
	private void createFloor() {
		//��ȡSquare.pro�����ļ�����Ϣ
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		//floorԪ���б�
		List<SuperElement> floorList = ElementManager.getManager().getElementList("floor");
		String type = null;
		//�ӵ�ͼ�����ļ��еõ�����һ�ֵذ�
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				//11������ɫ����
				if(mapList.get(i).get(j).equals("11")){
					type = "11";
					break;
				}
				//12�����ɫ�ذ�
				if(mapList.get(i).get(j).equals("12")){
					type = "12";
					break;
				}
			}
		}
		//����ڵذ幹�캯��������һ������
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				//��i��j�е�λ���ϴ����ذ����
				floorList.add(MapFloor.createMapFloor(typeMap.get(type),i, j));
			}
		}


	}

	//������ͼԪ��
	private void createSquare() {
		//��ȡSqu.pro�е�������Ϣ
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		//Ԫ���ֵ�
		Map<String, List<SuperElement>>elmenteMap = ElementManager.getManager().getMap();
		//��Ϸ��Ϣ�ֵ�
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
		//npc����
		int npcNum = 0;
		for (int i = 0; i < mapRows; i++) {
			for (int j = 0; j < mapCols; j++) {
				//���i��j��Ԫ�ص�����
				String type = mapList.get(i).get(j);
				//����Ԫ��������ӵ���Ӧ��Ԫ���б���
				switch (type.charAt(0)) {
					case '0':
						if(type.equals("00")) break;//����ǽ
						elmenteMap.get("obstacle").add(MapObstacle.createMapObstacle(typeMap.get(type), i, j));
						break;
					case '2':
						elmenteMap.get("fragility").add(MapFragility.createMapFragility(typeMap.get(type), i, j));
						break;
					case '3':
						elmenteMap.get("magicBox").add(MagicBox.createMagicBox(i, j));
						break;
					case '6':
						//��ʼ�����1
						initPlayer(i, j, 0);
						break;
					case '7':
						//�ж��ǲ���˫��ģʽ
						if(GameController.isTwoPlayer())
							//��ʼ�����2
							initPlayer(i, j, 1);
						else {
							//�����ʾΪnpc
							switch (type.charAt(1)) {
								case '1':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcA"), i, j, npcNum++));break;
								case '2':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcB"), i, j, npcNum++));break;
								case '3':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcC"), i, j, npcNum++));break;
								default:break;
							}
						}
						break;
					case '8':
						switch (type.charAt(1)) {
							case '1':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcA"), i, j, npcNum++));break;
							case '2':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcB"), i, j, npcNum++));break;
							case '3':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcC"), i, j, npcNum++));break;
							default:break;
						}

						break;

					default:
						break;
				}
			}
		}
		//ȷ���˵�ͼ��npc������
		GameController.setNpcNum(npcNum);
	}

	public void createMap(String pro){
		try {
			mapList = ElementLoader.getElementLoader().readMapPro(pro);
			//��ȡ��ͼ���С�����
			List<String> size = ElementLoader.getElementLoader().getGameInfoMap().get("mapSize");
			mapRows = Integer.parseInt(size.get(0));
			mapCols = Integer.parseInt(size.get(1));
			//��������ʹ��ͼλ�ھ���λ��
			biasX = (windowW- MapSquare.PIXEL_X*mapCols)/2;
			biasY = (windowH- MapSquare.PIXEL_Y*mapRows)/2;
			//��ذ�Ԫ��
			createFloor();
			//�����Ԫ��
			createSquare();
			//�޸�npc������ֵ
			setNpcHealthByMode();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ͼ���ؽ�ɫ
	 * @param i
	 * @param j
	 * @param num ��ţ����1��0�����2��1
	 */
	private void initPlayer(int i, int j, int num) {
		//����б�
		List<SuperElement> playerList = ElementManager.getManager().getMap().get("player");
		//�����������ģʽƥ��������������
		if(playerList.size()==(GameController.isTwoPlayer()?2:1)) {
			List<Integer> locList = GameMap.getXY(i,j);
			playerList.get(num).setX(locList.get(0));
			playerList.get(num).setY(locList.get(1));
		} else {//��ƥ����½�Player
			Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
			for(SuperElement se:playerList) {
				Player player = (Player) se;
				if(player.getPlayerNum()==num) {
					return;
				}
			}
			Player player = null;
			if(num==0) {//��������һ
				//ʵ�����һѡ�Ľ�ɫ
				switch (BeginJPanel.getOneRoleNum()){
					case 1:
						player = Player.createPlayer(gameInfoMap.get("playerOne"), i, j, num);
						break;
					case 2:
						player = Player.createPlayer(gameInfoMap.get("playerTwo"), i, j, num);
						break;
					case 3:
						player = Player.createPlayer(gameInfoMap.get("playerThree"), i, j, num);
						break;
					case 4:
						player = Player.createPlayer(gameInfoMap.get("playerFour"), i, j, num);
						break;
				}

			} else if(num==1) {//�������Ҷ�
				//ʵ����Ҷ�ѡ�Ľ�ɫ
				switch (BeginJPanel.getTwoRoleNum()){
					case 1:
						player = Player.createPlayer(gameInfoMap.get("playerOne"), i, j, num);
						break;
					case 2:
						player = Player.createPlayer(gameInfoMap.get("playerTwo"), i, j, num);
						break;
					case 3:
						player = Player.createPlayer(gameInfoMap.get("playerThree"), i, j, num);
						break;
					case 4:
						player = Player.createPlayer(gameInfoMap.get("playerFour"), i, j, num);
						break;
				}
			} else {
				return;
			}
			//���½���ɫ��ӵ�����б�
			playerList.add(num, player);
		}
	}
	/**
	 *����ģʽ����npc��ʼ����ֵ
	 */
	public void setNpcHealthByMode() {
		for (SuperElement superElement : ElementManager.getManager().getMap().get("npc")) {
			Character npc = (Character) superElement;
			switch (BeginJPanel.getModeNum()) {
				case 1://���˼�
					npc.initHealthPoint(3);
					break;
				case 2://��������
					npc.initHealthPoint(4);
					break;
				case 3://˫�˼�
					npc.initHealthPoint(5);
					break;
				case 4://˫������
					npc.initHealthPoint(6);
					break;
			}
		}

	}

	/**
	 * ��ȡ��ͼij��ķ�������
	 * @param i
	 * @param j
	 * @return ��������
	 */
	public SquareType getBlockSquareType(int i,int j) {
		String str = mapList.get(i).get(j);
		return SquareType.valueOf(str.charAt(0));
	}

	/**
	 * ��ȡ��ͼij��ķ�������
	 * @param list ij�б�
	 * @return ��������
	 */
	public SquareType getBlockSquareType(List<Integer> list) {
		String str = mapList.get(list.get(0)).get(list.get(1));
		return SquareType.valueOf(str.charAt(0));
	}

	/**
	 * ���õ�ͼij�㷽������
	 * @param list ij�б�
	 * @param type
	 */
	public void setBlockSquareType(List<Integer> list,SquareType type) {
		mapList.get(list.get(0)).set(list.get(1), type.value+"");
	}

	/**
	 * ���õ�ͼij�㷽������
	 * @param i
	 * @param j
	 * @param type
	 */
	public void setBlockSquareType(int i,int j,SquareType type) {
		mapList.get(i).set(j, type.value+"");
	}

	/**
	 * �жϷ����Ƿ����ϰ���
	 * @param i
	 * @param j
	 * @return �Ƿ����ϰ���
	 */
	public boolean blockIsObstacle(int i,int j) {
		if(outOfBoundary(i, j)) return true;

		String type = mapList.get(i).get(j);
		if(type.charAt(0) == SquareType.OBSTACLE.value) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ȡijλ���Ƿ��ͨ��
	 * @param list
	 * @return ��ͨ��
	 */
	public boolean blockIsWalkable(List<Integer> list) {
		String type = mapList.get(list.get(0)).get(list.get(1));
		if(type.charAt(0) == SquareType.OBSTACLE.value
				||type.charAt(0) == SquareType.FRAGILITY.value
				||type.charAt(0) == SquareType.ITEM.value) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ��ȡijλ���Ƿ��ͨ��
	 * @param
	 * @return ��ͨ��
	 */
	public boolean blockIsWalkable(int i,int j) {
		String type = mapList.get(i).get(j);
		if(type.charAt(0) == SquareType.OBSTACLE.value
				||type.charAt(0) == SquareType.FRAGILITY.value
				||type.charAt(0) == SquareType.ITEM.value) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * �ж��Ƿ񳬳��߽�
	 * @param list ij�б�
	 * @return �Ƿ񳬳��߽�
	 */
	public boolean outOfBoundary(List<Integer> list) {
		int i = list.get(0);
		int j = list.get(1);
		if (i<0||i>=mapRows||j<0||j>=mapCols) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ж��Ƿ񳬳��߽�
	 * @param i
	 * @param j
	 * @return �Ƿ񳬳��߽�
	 */
	public boolean outOfBoundary(int i,int j) {
		if (i<0||i>=mapRows||j<0||j>=mapCols) {
			return true;
		} else {
			return false;
		}
	}


	//��xyת��Ϊij 0��i 1��j
	public static List<Integer> getIJ(int x,int y){
		List<Integer> list = new ArrayList<>();
		list.add((y-biasY)/ MapSquare.PIXEL_Y);
		list.add((x-biasX)/ MapSquare.PIXEL_X);
		return list;
	}

	//��ijת��Ϊxy 0��y 1��x
	public static List<Integer> getXY(int i,int j){
		List<Integer> tempList = new ArrayList<>();
		tempList.add(i* MapSquare.PIXEL_Y+biasY);
		tempList.add(j* MapSquare.PIXEL_X+biasX);
		return tempList;
	}
	public static List<Integer> getXY(List<Integer> list){
		List<Integer> tempList = new ArrayList<>();
		tempList.add(list.get(1)* MapSquare.PIXEL_X+biasX);
		tempList.add(list.get(0)* MapSquare.PIXEL_Y+biasY);
		return tempList;
	}
	/**
	 * ��յ�ͼ�г��������Ķ���
	 */
	public static void clearMapOther() {
		ElementManager.getManager().getElementList("obstacle").clear();
		ElementManager.getManager().getElementList("fragility").clear();
		ElementManager.getManager().getElementList("floor").clear();
		ElementManager.getManager().getElementList("explode").clear();
		ElementManager.getManager().getElementList("magicBox").clear();
		ElementManager.getManager().getElementList("npc").clear();
		ElementManager.getManager().getElementList("bubble").clear();
	}

	/**
	 * ��յ�ͼ���ж���
	 */
	public static void clearMapALL() {
		ElementManager.getManager().getElementList("player").clear();
		clearMapOther();
	}

	public static List<List<String>> getMapList(){
		return mapList;
	}
	public static int getBiasX() {
		return biasX;
	}
	public static int getBiasY() {
		return biasY;
	}
	public static int getMapRows() {
		return mapRows;
	}
	public static int getMapCols() {
		return mapCols;
	}


}
