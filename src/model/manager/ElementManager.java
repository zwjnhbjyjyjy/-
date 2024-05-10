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
 * ElementManager��
 *  @ClassName: ElementManager
 * Ԫ�ع�����
 * ����ģʽ
 * @author�� Zhao Wenjie
 * @CreateDate: 2022��12��29��
 */
public class ElementManager {

	//Ԫ�ع���������
	private static ElementManager elementManager;
	//ʵ��������
	static {
		elementManager = new ElementManager();
	}

	//Ԫ�ص�Map����
	private Map<String, List<SuperElement>> map;

	//ͼ��˳��map
	private Map<String,Integer> priorityMap;

	//��Ϸ��ͼ
	private GameMap gameMap;


	/**
	 * ��ʼ����ͼ�����Ϣ
	 */
	protected void init() {

		//��ȡ��ͼ��Ϣ
		Map<String, List<String >> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();

		//��ȡ��ͼ�ߴ���Ϣ
		List<String> windowSize = gameInfoMap.get("windowSize");

		gameMap = new GameMap(Integer.parseInt(windowSize.get(0)),Integer.parseInt(windowSize.get(1)));
		map = new HashMap<>();
		priorityMap = new HashMap<>();
	}

	/**
	 *��ʼ��Ԫ���б��ֵ�
	 */
	private void initMap() {

		//���
		map.put("player", new ArrayList<SuperElement>());

		//ˮ��
		map.put("bubble", new ArrayList<SuperElement>());

		//ˮ�ݱ�ը
		map.put("explode",new ArrayList<SuperElement>());

		//���ƻ�����
		map.put("fragility", new ArrayList<SuperElement>());

		//�ذ�
		map.put("floor", new ArrayList<SuperElement>());

		//�����ƻ�����
		map.put("obstacle", new ArrayList<SuperElement>());

		//����
		map.put("magicBox", new ArrayList<SuperElement>());

		//npc
		map.put("npc", new ArrayList<SuperElement>());
	}

	/**
	 * ��ʼ��ͼ�����ȼ��ֵ�
	 */
	private void initPriorityMap() {

		//���
		priorityMap.put("player", 50);

		//npc
		priorityMap.put("npc", 45);

		//�����ƻ�����
		priorityMap.put("obstacle", 40);

		//ˮ�ݱ�ը
		priorityMap.put("explode", 30);

		//����
		priorityMap.put("magicBox", 25);

		//���ƻ�����
		priorityMap.put("fragility", 20);

		//ˮ��
		priorityMap.put("bubble", 10);

		//�ذ�
		priorityMap.put("floor", -10);
	}

	/**
	 * ��ʼ��Ԫ�����ʵĹ��캯��
	 */
	private ElementManager() {

		//��ʼ������
		init();

		//��ʼ��Ԫ���б��ֵ�
		initMap();

		//��ʼ��ͼ�����ȼ��ֵ�
		initPriorityMap();
	}

	/**
	 * ͼ�����ȼ��Ƚ���
	 *
	 * @return �Զ���Ƚ��� ���ڱȽ�ͼ�����ȼ�
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
	 * �����map����
	 *
	 * @return Ԫ�ص�map����
	 */
	public Map<String, List<SuperElement>> getMap(){

		return map;
	}

	/**
	 * ���map�е�value�б�
	 *
	 * @param key Map�еļ�ֵ
	 * @return map�е�value�б�
	 */
	public List<SuperElement> getElementList(String key){

		return map.get(key);
	}

	/**
	 * Ԫ�ع��������
	 *
	 * @return Ԫ�ع�����
	 */
	public static ElementManager getManager() {

		return elementManager;
	}

	/**
	 * ��ȡ��Ϸ�ĵ�ͼ��
	 *
	 * @return ��Ϸ�ĵ�ͼ��
	 */
	public GameMap getGameMap() {

		return gameMap;
	}

	/**
	 * ���ص�ͼ��Ϣ
	 */
	public void loadMap(){

		//��ȡ��ͼ����
		int mapNum = Integer.parseInt(ElementLoader.getElementLoader().getGameInfoMap().get("mapNum").get(0));

		//���ѡȡ��ͼ
		gameMap.createMap("stage"+(Utils.random.nextInt(mapNum)+1)+"Map");
	}

	/**
	 * ��Ϸ�������������Ԫ��
	 *
	 * @param over ��Ϸ�����źţ����ѽ�����Ϊtrue
	 */
	public void overGame(Boolean over) {
		if(over) {
			gameMap.clearMapALL();
		} else {
			gameMap.clearMapOther();
		}
	}


}

