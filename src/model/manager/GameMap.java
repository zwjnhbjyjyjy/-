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
 * @Description:实现游戏地图
 * @author:He Xiaofei
 * @CreateDate:2022-12-30
 */
public class GameMap {
	//地图宽度
	private int windowW;
	//地图高度
	private int windowH;
	//地图行数
	private static int mapRows;
	//地图列数
	private static int mapCols;
	//位置坐标x
	private static int biasX;
	//位置坐标Y
	private static int biasY;
	//地图
	private static List<List<String>> mapList;

	//自定义方块类型对应枚举类
	public enum SquareType{
		//0代表障碍物；1代表地板；2代表可摧毁物；3代表道具；6代表玩家1；7代表玩家2；8代表npc；9代表水泡
		OBSTACLE('0'),FLOOR('1'),FRAGILITY('2'),ITEM('3'),PLAYER_1('6'),PLAYER_2('7'),NPC('8'),BUBBLE('9');
		//value默认为 0
		private char value = 0;
		//修改value
		private SquareType(char value) {
			this.value = value;
		}
		//将char转换为对应的enum
		public static SquareType valueOf(char c) {
			switch (c) {
				case '0':  return OBSTACLE;	//障碍物
				case '1':  return FLOOR;	//地板
				case '2':  return FRAGILITY;//可摧毁物
				case '3':  return ITEM;  	//道具
				case '6':  return PLAYER_1;	//玩家1
				case '7':  return PLAYER_2;	//玩家2
				case '8':  return NPC;		//NPC
				case '9':  return BUBBLE;  	//水泡
				default:
					return null;
			}
		}
		//获取value
		public char value() {
			return this.value;
		}
	}

	//构造函数,定义地图的宽度和高度
	public GameMap(int windowW, int windowH) {
		this.windowW = windowW;
		this.windowH = windowH;
	}

	//创建地板
	private void createFloor() {
		//获取Square.pro配置文件的信息
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		//floor元素列表
		List<SuperElement> floorList = ElementManager.getManager().getElementList("floor");
		String type = null;
		//从地图配置文件中得到是哪一种地板
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				//11代表绿色方块
				if(mapList.get(i).get(j).equals("11")){
					type = "11";
					break;
				}
				//12代表灰色地板
				if(mapList.get(i).get(j).equals("12")){
					type = "12";
					break;
				}
			}
		}
		//因此在地板构造函数增加了一个参数
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				//第i行j列的位置上创建地板对象
				floorList.add(MapFloor.createMapFloor(typeMap.get(type),i, j));
			}
		}


	}

	//创建地图元素
	private void createSquare() {
		//获取Squ.pro中的配置信息
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		//元素字典
		Map<String, List<SuperElement>>elmenteMap = ElementManager.getManager().getMap();
		//游戏信息字典
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
		//npc数量
		int npcNum = 0;
		for (int i = 0; i < mapRows; i++) {
			for (int j = 0; j < mapCols; j++) {
				//获得i行j列元素的类型
				String type = mapList.get(i).get(j);
				//根据元素类型添加到对应的元素列表中
				switch (type.charAt(0)) {
					case '0':
						if(type.equals("00")) break;//空气墙
						elmenteMap.get("obstacle").add(MapObstacle.createMapObstacle(typeMap.get(type), i, j));
						break;
					case '2':
						elmenteMap.get("fragility").add(MapFragility.createMapFragility(typeMap.get(type), i, j));
						break;
					case '3':
						elmenteMap.get("magicBox").add(MagicBox.createMagicBox(i, j));
						break;
					case '6':
						//初始化玩家1
						initPlayer(i, j, 0);
						break;
					case '7':
						//判断是不是双人模式
						if(GameController.isTwoPlayer())
							//初始化玩家2
							initPlayer(i, j, 1);
						else {
							//否则表示为npc
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
		//确定此地图中npc的数量
		GameController.setNpcNum(npcNum);
	}

	public void createMap(String pro){
		try {
			mapList = ElementLoader.getElementLoader().readMapPro(pro);
			//获取地图的行、列数
			List<String> size = ElementLoader.getElementLoader().getGameInfoMap().get("mapSize");
			mapRows = Integer.parseInt(size.get(0));
			mapCols = Integer.parseInt(size.get(1));
			//计算坐标使地图位于居中位置
			biasX = (windowW- MapSquare.PIXEL_X*mapCols)/2;
			biasY = (windowH- MapSquare.PIXEL_Y*mapRows)/2;
			//搭建地板元素
			createFloor();
			//搭建方块元素
			createSquare();
			//修改npc生命数值
			setNpcHealthByMode();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按地图加载角色
	 * @param i
	 * @param j
	 * @param num 编号，玩家1传0，玩家2传1
	 */
	private void initPlayer(int i, int j, int num) {
		//玩家列表
		List<SuperElement> playerList = ElementManager.getManager().getMap().get("player");
		//当玩家数量与模式匹配就设置玩家坐标
		if(playerList.size()==(GameController.isTwoPlayer()?2:1)) {
			List<Integer> locList = GameMap.getXY(i,j);
			playerList.get(num).setX(locList.get(0));
			playerList.get(num).setY(locList.get(1));
		} else {//不匹配就新建Player
			Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
			for(SuperElement se:playerList) {
				Player player = (Player) se;
				if(player.getPlayerNum()==num) {
					return;
				}
			}
			Player player = null;
			if(num==0) {//如果是玩家一
				//实现玩家一选的角色
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

			} else if(num==1) {//如果是玩家二
				//实现玩家二选的角色
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
			//将新建角色添加到玩家列表
			playerList.add(num, player);
		}
	}
	/**
	 *根据模式设置npc初始生命值
	 */
	public void setNpcHealthByMode() {
		for (SuperElement superElement : ElementManager.getManager().getMap().get("npc")) {
			Character npc = (Character) superElement;
			switch (BeginJPanel.getModeNum()) {
				case 1://单人简单
					npc.initHealthPoint(3);
					break;
				case 2://单人困难
					npc.initHealthPoint(4);
					break;
				case 3://双人简单
					npc.initHealthPoint(5);
					break;
				case 4://双人困难
					npc.initHealthPoint(6);
					break;
			}
		}

	}

	/**
	 * 获取地图ij点的方块类型
	 * @param i
	 * @param j
	 * @return 方块类型
	 */
	public SquareType getBlockSquareType(int i,int j) {
		String str = mapList.get(i).get(j);
		return SquareType.valueOf(str.charAt(0));
	}

	/**
	 * 获取地图ij点的方块类型
	 * @param list ij列表
	 * @return 方块类型
	 */
	public SquareType getBlockSquareType(List<Integer> list) {
		String str = mapList.get(list.get(0)).get(list.get(1));
		return SquareType.valueOf(str.charAt(0));
	}

	/**
	 * 设置地图ij点方块类型
	 * @param list ij列表
	 * @param type
	 */
	public void setBlockSquareType(List<Integer> list,SquareType type) {
		mapList.get(list.get(0)).set(list.get(1), type.value+"");
	}

	/**
	 * 设置地图ij点方块类型
	 * @param i
	 * @param j
	 * @param type
	 */
	public void setBlockSquareType(int i,int j,SquareType type) {
		mapList.get(i).set(j, type.value+"");
	}

	/**
	 * 判断方块是否是障碍物
	 * @param i
	 * @param j
	 * @return 是否是障碍物
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
	 * 获取ij位置是否可通过
	 * @param list
	 * @return 可通过
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
	 * 获取ij位置是否可通过
	 * @param
	 * @return 可通过
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
	 * 判断是否超出边界
	 * @param list ij列表
	 * @return 是否超出边界
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
	 * 判断是否超出边界
	 * @param i
	 * @param j
	 * @return 是否超出边界
	 */
	public boolean outOfBoundary(int i,int j) {
		if (i<0||i>=mapRows||j<0||j>=mapCols) {
			return true;
		} else {
			return false;
		}
	}


	//将xy转换为ij 0是i 1是j
	public static List<Integer> getIJ(int x,int y){
		List<Integer> list = new ArrayList<>();
		list.add((y-biasY)/ MapSquare.PIXEL_Y);
		list.add((x-biasX)/ MapSquare.PIXEL_X);
		return list;
	}

	//将ij转换为xy 0是y 1是x
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
	 * 清空地图中除玩家以外的对象
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
	 * 清空地图所有对象
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
