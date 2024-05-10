package model.element;


import model.loader.ElementLoader;
import model.manager.ElementManager;
import model.manager.GameMap;
import view.GameJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * ���߹�����
 *
 * @ClassName: MagicBox
 * @Description:������߳��ֵĸ����Լ�ÿ�����ߵ�Ч��
 * @author: LiuXingtong
 * @CreateDate: 2022��12��25�� ����5:27:20
 */
public class MagicBox extends MapSquare {
	private static Random rd = new Random();
	private boolean eaten;//���Ե���ʧ��
	private int moveX;//ͼƬ�任
	private String type;//��������
	private int characterIndex;//�ĸ�player��õ���0-1���2-4����

	static Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();

	public MagicBox(int i, int j, ImageIcon img,
					int sx, int sy, int dx, int dy, int scaleX, int scaleY, String type) {
		super(i, j, img, sx, sy, dx, dy, scaleX, scaleY);
		moveX = 0;
		eaten = false;
		this.type = type;
	}

	// ���и������ָ���
	static Map<String, Integer> keyChanceMap = new HashMap<String, Integer>();

	static {
		keyChanceMap.put("1", 10);// rate = 0.1
		keyChanceMap.put("3", 5); // rate = 0.05
		keyChanceMap.put("4", 20);// rate = 0.2
		keyChanceMap.put("5", 30);// rate = 0.3
		keyChanceMap.put("7", 10);// rate = 0.1
		keyChanceMap.put("8", 20);// rate = 0.2
		keyChanceMap.put("9", 5); // rate = 0.05
	}

	public static String chanceSelect(Map<String, Integer> keyChanceMap) {
		if (keyChanceMap == null || keyChanceMap.size() == 0)
			return null;

		Integer sum = 0;
		for (Integer value : keyChanceMap.values()) {
			sum += value;
		}
		// ��1��ʼ
		Integer rand = new Random().nextInt(sum) + 1;

		for (Map.Entry<String, Integer> entry : keyChanceMap.entrySet()) {
			rand -= entry.getValue();
			// ѡ��
			if (rand <= 0) {
				String item = entry.getKey();
				return item;
			}
		}
		return null;
	}

	public static MagicBox createMagicBox(int i, int j) {
		String letter = "0";
		letter = chanceSelect(keyChanceMap);
		String boxtype = "3" + letter;
		List<String> data = typeMap.get(boxtype);
		int sx = Integer.parseInt(data.get(1));
		int sy = Integer.parseInt(data.get(2));
		int dx = Integer.parseInt(data.get(3));
		int dy = Integer.parseInt(data.get(4));
		int scaleX = Integer.parseInt(data.get(6));
		int scaleY = Integer.parseInt(data.get(7));
		ImageIcon img = ElementLoader.getElementLoader().getImageMap().get(data.get(0));
		MagicBox magicBox = new MagicBox(i, j, img, sx, sy, dx, dy, scaleX, scaleY, boxtype);
		return magicBox;
	}

	//	��дcrash��������С��ײ���
	@Override
	public boolean crash(SuperElement se) {
		Rectangle r1 = new Rectangle(getX() + getW() / 4, getY() + getH() / 4, getW() / 2, getH() / 2);
		Rectangle r2 = new Rectangle(se.getX() + se.getW() / 4, se.getY() + se.getH() / 4, se.getW() / 2, se.getH() / 2);
		return r1.intersects(r2);//�н�����Χtrue
	}

	@Override
	public void update() {
		// TODO �Զ����ɵķ������
		super.update();
		updateImage();
	}

	//	�л�ͼƬ
	public void updateImage() {
		if (eaten) return;
		if (++moveX >= 40)
			moveX = 0;
		int sx = (moveX / 10) * 32;
		int sy = Integer.parseInt(typeMap.get(type).get(2));
		int dx = (moveX / 10 + 1) * 32;
		int dy = Integer.parseInt(typeMap.get(type).get(4));
		setPictureLoc(sx, sy, dx, dy);
	}


	@Override
	public void destroy() {
		if (eaten) {
//			�����ݻٷ�������Ϊ�ذ�
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> list = GameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(list.get(0), list.get(1), GameMap.SquareType.FLOOR);
//			�õ�buff
			List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
			List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
			Character character;
			if (characterIndex < 2) {
				character = (Character) playerList.get(characterIndex);
			} else {
				character = (Character) npcList.get(characterIndex - 2);
			}
			switch (type) {
				case "31": //ʹ�ƶ����� 10s
					if(Character.getChangeDirectionCount() == 0){
						character.changeDirection(10);//���뷽��ı�ĳ���ʱ�䣨�룩
					}
					break;
				case "33": //��������ֵ
					character.setHealthPoint(1);//�������ӵ�����ֵ����
					if(character instanceof Player){
						GameJPanel.setPlayer("health", character.getHeathPoint(), characterIndex);
					}
					break;
				case "34": //�����ƶ��ٶ� 10s
					character.changeSpeed(1.5, 10, characterIndex);//�����������ӱ����ͳ���ʱ�䣨�룩
					if(character instanceof Player){
						GameJPanel.setPlayer("speed", character.getSpeed(), characterIndex);
					}
					break;
				case "35": //���ݸ�������
					character.setBubbleLargest(character.getBubbleLargest() + 1);
					if(character instanceof Player){
						GameJPanel.setPlayer("num", character.getBubbleLargest(), characterIndex);
					}
					break;
				case "37": //�������ֹͣ5s
					character.setOtherStop(5);
					break;
				case "38": //��������
					character.bubbleAddPower();//���뷽��ı�ĳ���ʱ�䣨�룩
					if(character instanceof Player){
						GameJPanel.setPlayer("power", character.getBubblePower(), characterIndex);
					}
					break;
				case "39"://�޵�5s
					character.setUnstoppable(5);//���뷽��ı�ĳ���ʱ�䣨�룩
					break;
				default:

					break;
			}

			eaten = false;
			setAlive(false);
		}

	}

	//	����Ӵ�����
	public boolean isEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}

	public int getCharacterIndex() {
		return characterIndex;
	}

	public void setCharacterIndex(int characterIndex) {
		this.characterIndex = characterIndex;
	}


}
