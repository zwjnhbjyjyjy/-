package model.loader;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ��Դ������
 * ʹ�õ������ģʽ
 * @ClassName: ElementLoader
 * @Description: �������е���Դ�ķ���
 * @author: LiuXingtong
 * @CreateDate: 2022��12��25�� ����1:30:19
 */
public class ElementLoader {
	private static ElementLoader elementLoader;
	private Properties properties;
	private Map<String, List<String>> gameInfoMap;//��Ϸ��Ϣ�ֵ�
	private Map<String, ImageIcon> imageMap;//ͼƬ�ֵ�
	private Map<String, List<String>> squareTypeMap;//���������ֵ�

	//���캯��
	private ElementLoader() {
		properties = new Properties();
		gameInfoMap = new HashMap<>();
		imageMap = new HashMap<>();
		squareTypeMap = new HashMap<>();
	}

	//����ģʽ
	public static ElementLoader getElementLoader() {
		if (elementLoader == null) {
			elementLoader = new ElementLoader();
		}
		return elementLoader;
	}

	//��ȡ�������ļ�
	public void readGamePro() throws IOException {
		InputStream inputStream = ElementLoader.class.getClassLoader().getResourceAsStream("pro/Game.pro");
		properties.clear();
		properties.load(inputStream);
		for(Object o:properties.keySet()) {
			String info = properties.getProperty(o.toString());
			gameInfoMap.put(o.toString(), infoStringToList(info,","));
		}
	}

	//��ȡͼƬ
	public void readImagePro() throws IOException{
		InputStream inputStream =
				ElementLoader.class.getClassLoader().getResourceAsStream(gameInfoMap.get("imageProPath").get(0));
		properties.clear();
		properties.load(inputStream);
		for(Object o:properties.keySet()) {
			String loc = properties.getProperty(o.toString());
			imageMap.put(o.toString(), new ImageIcon(loc));
		}
	}

	//��ȡ��Ϸ�������
	public void readCharactorsPro() throws IOException {
		InputStream inputStream =
				ElementLoader.class.getClassLoader().getResourceAsStream(gameInfoMap.get("charatersPath").get(0));
		properties.clear();
		properties.load(inputStream);
		for(Object o:properties.keySet()) {
			String info = properties.getProperty(o.toString());
			gameInfoMap.put(o.toString(),infoStringToList(info, ","));//����Map��value�е����Ѿ��ָ���������
		}
	}

	//��ȡnpcͼƬ�б�
	public List<ImageIcon> getNpcImageList(String s){ //s��ֵΪnpcA,npcB��npcC ��Ӧ��Ӧ��npc
		List<ImageIcon> imageList = new ArrayList<>();
		String npc = new String();
		for(int i=0; i<4; i++) {//4��ͼƬ
			npc = s + (char)(i+'0');
			imageList.add(imageMap.get(npc));
		}
		return imageList;
	}


	//��ȡ����ը���ͱ�ըЧ������Bubble.pro
	public void readBubblePro() throws IOException
	{
		InputStream inputStream =
				ElementLoader.class.getClassLoader().getResourceAsStream(gameInfoMap.get("bubblePath").get(0));
		properties.clear();
		properties.load(inputStream);
		for(Object o:properties.keySet()) {
			String info = properties.getProperty(o.toString());
			gameInfoMap.put(o.toString(),infoStringToList(info, ","));//����Map��value�е����Ѿ��ָ���������
		}
	}


	//��ȡ����������Ϣ
	public void readSquarePro() throws IOException{
		InputStream inputStream =
				ElementLoader.class.getClassLoader().getResourceAsStream(gameInfoMap.get("squareProPath").get(0));
		properties.clear();
		properties.load(inputStream);
		for(Object o:properties.keySet()) {
			String info = properties.getProperty(o.toString());
			squareTypeMap.put(o.toString(),infoStringToList(info, ","));
		}
	}

	//��ȡ�ض���ͼ
	public List<List<String>> readMapPro(String mapPro) throws IOException{
		List<List<String>> mapList = new ArrayList<>();
		InputStream inputStream =
				ElementLoader.class.getClassLoader().getResourceAsStream(gameInfoMap.get(mapPro).get(0));
		properties.clear();
		properties.load(inputStream);
		Set<Object> sortSet = new TreeSet<>(new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				try {
					int a = Integer.parseInt(o1.toString());
					int b = Integer.parseInt(o2.toString());
					if(a<b) {
						return 1;
					} else if (a>b) {
						return -1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					return -1;
				}
			}
		});
		sortSet.addAll(properties.keySet());
		for(Object o:sortSet) {
			String info = properties.getProperty(o.toString());
			if(o.toString().equals("size")) {//��ͼ��С
				gameInfoMap.put("mapSize", infoStringToList(info,","));
			} else {//��ͼ��Ϣ
				mapList.add(infoStringToList(info,","));
			}
		}
		Collections.reverse(mapList);
		return mapList;
	}


	/**
	 * ���������ָ���ַ����и��תΪ�ַ���List
	 * @param info �������ַ���
	 * @param splitString �и��ַ���
	 * @return �и����ַ���List
	 */
	private List<String> infoStringToList(String info,String splitString){
		return Arrays.asList(info.split(splitString));
	}

	public Map<String, List<String>> getGameInfoMap() {
		return gameInfoMap;
	}

	public Map<String, ImageIcon> getImageMap() {
		return imageMap;
	}

	public Map<String, List<String>> getSquareTypeMap() {
		return squareTypeMap;
	}

	//���ش���w,h,windowSize[0]=w
	public List<Integer> getWindowSize(){
		List<String> data = gameInfoMap.get("windowSize");
		List<Integer> windowSize = new ArrayList<>();
		for(int i=0; i<data.size(); i++)
			windowSize.add(Integer.parseInt(data.get(i)));
		return windowSize;
	}

}
