package model.manager;

import model.element.Player;
import model.element.SuperElement;
import model.loader.ElementLoader;

import java.util.List;
import java.util.Map;

/**
 * Ԫ�ع�����
 * @ClassName: ElementFactory
 * @Description: ���ڹ����������Ԫ�ع�����
 * @author: LiuXingtong
 * @CreateDate: 2022��12��25�� ����5:07:57
 */
public class ElementFactory {
	private static ElementFactory elementFactory;

	//���캯��
	private ElementFactory() {}

	public static ElementFactory getElementFactory() {
		if(elementFactory == null) {
			elementFactory = new ElementFactory();
		}
		return elementFactory;
	}

	public SuperElement produceElement(String name) {
		//TODO:д����
		Map<String, List<String>> gameInfoMap =
				ElementLoader.getElementLoader().getGameInfoMap();//��ȡ��Դ����������Ϸ��Ϣ�ֵ�

		switch(name) {
			case "playerOne":
				return Player.createPlayer(gameInfoMap.get(name),0);
			case "playerTwo":
				return Player.createPlayer(gameInfoMap.get(name),1);
//		case "bubble":
//			return Bubble.createBubble(gameInfoMap.get(name));
		}
		return null;
	}


}
