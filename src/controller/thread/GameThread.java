package controller.thread;

import controller.GameController;
import gameStart.GameStart;
import model.element.*;
import model.manager.ElementManager;
import view.GameJPanel;
import view.OverJPanel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: GameThread
 * @Description:  ����Ϊ��Ϸ�߳̿���
 * @author:Xiongting
 * @CreateDate:2022/12/27-2022/12/31
 */
public class GameThread extends Thread{
	private boolean running;//��ʾ��ǰ�ؿ��Ƿ��ڽ���
	private boolean over=false;//��ʾ��Ϸ�Ƿ�������������ؿ�ʼ�˵�
	private static int sleepTime=20;//runGameˢ��ʱ��
	private static int allTime=600*1000;//ʮ����
	@Override
	public void run(){
		while (!over){

			running = true;//��ǰ�ؿ����ڽ���
			//����Ԫ��
			loadElement();
			//��ʾ������̣��Զ���
			runGame();
			//������ǰ��
			overGame(over);
		}
		GameStart.changeJPanel("over");
	}
	//����Ԫ��
	private void loadElement(){
		ElementManager.getManager().loadMap();
	}

	/**
	 * �ؿ�����
	 * ���overΪ������Ϸʧ�ܷ��ؽ��棬���������һ��
	 * @param over
	 */
	private void overGame(Boolean over){
		ElementManager.getManager().overGame(over);
	}

	//��ʾ�����Ϸ���̣��Զ���
	private void runGame(){
		allTime=600*1000;
		while (running) {
			//������Ϸ���̵���ͣ

			Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
			Set<String> set = map.keySet();
			if (!GameController.isPausing()) {
				for (String key : set) {
					List<SuperElement> list = map.get(key);
					for (int i = list.size() - 1; i >= 0; i--) {
						list.get(i).update();
						if (!list.get(i).isAlive())
							list.remove(i);
					}
				}
				//�����ը����ײ����
				playerBoom();
				//���ƻ�����ը����ײ
				fragilityBoom();
				//npc��ը����ײ����
				npcBoom();
				//����������ײЧ��
				playerMagicBox();
				//����Ƿ����ȫ������
				defeat();
				//����runGame����
				allTime = allTime - sleepTime;
				try {
					sleep(20);
				} catch (InterruptedException e) {
					//TODO:handle exception
					e.printStackTrace();
				}
			}
		}
		OverJPanel.showWinner(GameController.getWinning());
	}
	private void defeat(){
		boolean allDead=true;
		int surviveP=0;
		int winner=2;
		List<SuperElement> playerList=ElementManager.getManager().getElementList("player");
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		for(SuperElement se:playerList) {
			if(!((Player)se).isDead()) {
				surviveP++;
			}
		}
		for(SuperElement npc:npcList) {
			if(!((Npc)npc).isDead()) {
				allDead = false;
			}
		}
		//���ʧ��
		if(surviveP==0||(allTime<=0 && !allDead)) {
			running = false;
			over = true;
			GameController.setWinning(0);
		}
		//���ʤ��
		if(allDead&&surviveP==1) {
			running = false;
			over = true;
			for(SuperElement se:playerList) {
				if(!((Player)se).isDead()) {
					surviveP++;
					winner = ((Player)se).getPlayerNum();
				}
			}
			GameController.setWinning(winner+1);
		}
		//ʱ�䵽��������Ҷ����
		if(allTime<=0&&surviveP==2&&allDead) {
			running = false;
			over = true;
			int score1 = ((Player)playerList.get(0)).score;
			int score2 = ((Player)playerList.get(0)).score;
			if(score1==score2){
				GameController.setWinning(0);
			}else if(score1>score2){
				GameController.setWinning(1);
			}else {
				GameController.setWinning(2);
			}
		}
	}
	//�����ը����ײ�ж�
	private void playerBoom(){
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(int i=0; i<playerList.size(); i++) {
			for(int j=0; j<explodeList.size(); j++) {
				if(explodeList.get(j).crash(playerList.get(i))){
					Player player = (Player) playerList.get(i);
					player.setHealthPoint(-1);//����ֵ-1
					GameJPanel.setPlayer("health", player.getHeathPoint(), i);
				}
			}
		}
	}
	//npc�������ײ�ж�
	private void npcBoom(){
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(int i=0; i<npcList.size(); i++) {
			for(int j=0; j<explodeList.size(); j++) {
				if(explodeList.get(j).crash(npcList.get(i))) {
					Npc npc = (Npc) npcList.get(i);
					npc.setHealthPoint(-1);
					GameJPanel.setNpc(npc.getHeathPoint(), i + 2);
					BubbleExplode e = (BubbleExplode) explodeList.get(j);
					if (e.getPlayerNum() < 2) {//Ŀǰֻ����ҼƷ�
						((Player) playerList.get(e.getPlayerNum())).setScore(((Player) playerList.get(e.getPlayerNum())).getScore() + 50);
						GameJPanel.setPlayer("score", ((Player) playerList.get(e.getPlayerNum())).getScore(), e.getPlayerNum());
					}
				}
			}
		}
	}
	//�ϰ�����ը����ײ�ж�
	private void fragilityBoom(){
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> explodes = ElementManager.getManager().getElementList("explode");
		List<SuperElement> fragility = ElementManager.getManager().getElementList("fragility");
		for(int i=0; i<fragility.size(); i++) {
			for(int j=0; j<explodes.size(); j++) {
				if(explodes.get(j).crash(fragility.get(i))) {
					MapFragility mapFragility = (MapFragility) fragility.get(i);
					mapFragility.setDestoried(true);
					BubbleExplode e = (BubbleExplode) explodes.get(j);
					if (e.getPlayerNum() < 2) {//Ŀǰֻ����ҼƷ�
						((Player) playerList.get(e.getPlayerNum())).setScore(((Player) playerList.get(e.getPlayerNum())).getScore() + 10);
						GameJPanel.setPlayer("score", ((Player) playerList.get(e.getPlayerNum())).getScore(), e.getPlayerNum());
					}
				}
			}
		}
	}
	//����������ײ�ж�
	private void playerMagicBox(){
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
		for(int i=0; i<playerList.size(); i++) {
			for(int j=magicBoxList.size()-1; j>=0; j--) {
				if(magicBoxList.get(j).crash(playerList.get(i))){
					MagicBox magicBox = (MagicBox) magicBoxList.get(j);
					magicBox.setCharacterIndex(i);//˭�Է���
					magicBox.setEaten(true);//���鱻��
					((Player)playerList.get(i)).setScore(((Player)playerList.get(i)).getScore()+30);
					GameJPanel.setPlayer("score",((Player) playerList.get(i)).getScore(),i);
				}

			}
		}
	}
	//npc�������ײ�ж�
	private void npcMagicBox(){
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
		for(int i=0; i<npcList.size(); i++) {
			for(int j=magicBoxList.size()-1; j>=0; j--) {
				if(magicBoxList.get(j).crash(npcList.get(i))){
					MagicBox magicBox = (MagicBox) magicBoxList.get(j);
					magicBox.setCharacterIndex(i+2);//˭�Է���
					magicBox.setEaten(true);//���鱻��
				}
			}
		}
	}
	//runGame���ã�������չ
	public void linkGame(){

	}
	public static int getAllTime(){
		return allTime;
	}

	public static List<SuperElement> getNpcList(){

		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		return npcList;
	}
}
