package model.manager;

import model.element.SuperElement;
import model.loader.ElementLoader;
import util.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElementManager类
 *  @ClassName: ElementManager
 * 元素管理器
 * 单例模式
 * @author： Zhao Wenjie
 * @CreateDate: 2022年12月29日
 */
public class ElementManager {

	//元素管理器单例
	private static ElementManager elementManager;
	//实例化对象
	static {
		elementManager = new ElementManager();
	}

	//元素的Map集合
	private Map<String, List<SuperElement>> map;

	//图层顺序map
	private Map<String,Integer> priorityMap;

	//游戏地图
	private GameMap gameMap;


	/**
	 * 初始化地图相关信息
	 */
	protected void init() {

		//获取地图信息
		Map<String, List<String >> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();

		//获取地图尺寸信息
		List<String> windowSize = gameInfoMap.get("windowSize");

		gameMap = new GameMap(Integer.parseInt(windowSize.get(0)),Integer.parseInt(windowSize.get(1)));
		map = new HashMap<>();
		priorityMap = new HashMap<>();
	}

	/**
	 *初始化元素列表字典
	 */
	private void initMap() {

		//玩家
		map.put("player", new ArrayList<SuperElement>());

		//水泡
		map.put("bubble", new ArrayList<SuperElement>());

		//水泡爆炸
		map.put("explode",new ArrayList<SuperElement>());

		//可破坏方块
		map.put("fragility", new ArrayList<SuperElement>());

		//地板
		map.put("floor", new ArrayList<SuperElement>());

		//不可破坏方块
		map.put("obstacle", new ArrayList<SuperElement>());

		//道具
		map.put("magicBox", new ArrayList<SuperElement>());

		//npc
		map.put("npc", new ArrayList<SuperElement>());
	}

	/**
	 * 初始化图层优先级字典
	 */
	private void initPriorityMap() {

		//玩家
		priorityMap.put("player", 50);

		//npc
		priorityMap.put("npc", 45);

		//不可破坏方块
		priorityMap.put("obstacle", 40);

		//水泡爆炸
		priorityMap.put("explode", 30);

		//道具
		priorityMap.put("magicBox", 25);

		//可破坏方块
		priorityMap.put("fragility", 20);

		//水泡
		priorityMap.put("bubble", 10);

		//地板
		priorityMap.put("floor", -10);
	}

	/**
	 * 初始化元素性质的构造函数
	 */
	private ElementManager() {

		//初始化变量
		init();

		//初始化元素列表字典
		initMap();

		//初始化图层优先级字典
		initPriorityMap();
	}

	/**
	 * 图层优先级比较器
	 *
	 * @return 自定义比较器 用于比较图层优先级
	 */
	public Comparator<String> getMapKeyComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int p1 = priorityMap.get(o1);
				int p2 = priorityMap.get(o2);
				if(p1 > p2) {
					return 1;
				} else if(p1 < p2) {
					return -1;
				} else {
					return 0;
				}
			}
		};
	}

	/**
	 * 获得所map集合
	 *
	 * @return 元素的map集合
	 */
	public Map<String, List<SuperElement>> getMap(){

		return map;
	}

	/**
	 * 获得map中的value列表
	 *
	 * @param key Map中的键值
	 * @return map中的value列表
	 */
	public List<SuperElement> getElementList(String key){

		return map.get(key);
	}

	/**
	 * 元素管理器入口
	 *
	 * @return 元素管理器
	 */
	public static ElementManager getManager() {

		return elementManager;
	}

	/**
	 * 获取游戏的地图类
	 *
	 * @return 游戏的地图类
	 */
	public GameMap getGameMap() {

		return gameMap;
	}

	/**
	 * 加载地图信息
	 */
	public void loadMap(){

		//获取地图数量
		int mapNum = Integer.parseInt(ElementLoader.getElementLoader().getGameInfoMap().get("mapNum").get(0));

		//随机选取地图
		gameMap.createMap("stage"+(Utils.random.nextInt(mapNum)+1)+"Map");
	}

	/**
	 * 游戏结束后清空所有元素
	 *
	 * @param over 游戏结束信号，若已结束，为true
	 */
	public void overGame(Boolean over) {
		if(over) {
			gameMap.clearMapALL();
		} else {
			gameMap.clearMapOther();
		}
	}


}

