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
 * @Description:  该类为游戏线程控制
 * @author:Xiongting
 * @CreateDate:2022/12/27-2022/12/31
 */
public class GameThread extends Thread{
	private boolean running;//表示当前关卡是否在进行
	private boolean over=false;//表示游戏是否结束，结束返回开始菜单
	private static int sleepTime=20;//runGame刷新时间
	private static int allTime=600*1000;//十分钟
	@Override
	public void run(){
		while (!over){

			running = true;//当前关卡正在进行
			//加载元素
			loadElement();
			//显示人物，流程，自动化
			runGame();
			//结束当前关
			overGame(over);
		}
		GameStart.changeJPanel("over");
	}
	//加载元素
	private void loadElement(){
		ElementManager.getManager().loadMap();
	}

	/**
	 * 关卡结束
	 * 如果over为真则游戏失败返回界面，否则进入下一关
	 * @param over
	 */
	private void overGame(Boolean over){
		ElementManager.getManager().overGame(over);
	}

	//显示人物，游戏流程，自动化
	private void runGame(){
		allTime=600*1000;
		while (running) {
			//控制游戏进程的暂停

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
				//玩家与炸弹碰撞死亡
				playerBoom();
				//可破坏物与炸弹碰撞
				fragilityBoom();
				//npc与炸弹碰撞死亡
				npcBoom();
				//玩家与道具碰撞效果
				playerMagicBox();
				//检测是否玩家全部死亡
				defeat();
				//控制runGame进程
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
		//玩家失败
		if(surviveP==0||(allTime<=0 && !allDead)) {
			running = false;
			over = true;
			GameController.setWinning(0);
		}
		//玩家胜利
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
		//时间到，两个玩家都存活
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
	//玩家与炸弹碰撞判断
	private void playerBoom(){
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
		for(int i=0; i<playerList.size(); i++) {
			for(int j=0; j<explodeList.size(); j++) {
				if(explodeList.get(j).crash(playerList.get(i))){
					Player player = (Player) playerList.get(i);
					player.setHealthPoint(-1);//生命值-1
					GameJPanel.setPlayer("health", player.getHeathPoint(), i);
				}
			}
		}
	}
	//npc与玩家碰撞判断
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
					if (e.getPlayerNum() < 2) {//目前只有玩家计分
						((Player) playerList.get(e.getPlayerNum())).setScore(((Player) playerList.get(e.getPlayerNum())).getScore() + 50);
						GameJPanel.setPlayer("score", ((Player) playerList.get(e.getPlayerNum())).getScore(), e.getPlayerNum());
					}
				}
			}
		}
	}
	//障碍物与炸弹碰撞判断
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
					if (e.getPlayerNum() < 2) {//目前只有玩家计分
						((Player) playerList.get(e.getPlayerNum())).setScore(((Player) playerList.get(e.getPlayerNum())).getScore() + 10);
						GameJPanel.setPlayer("score", ((Player) playerList.get(e.getPlayerNum())).getScore(), e.getPlayerNum());
					}
				}
			}
		}
	}
	//玩家与道具碰撞判断
	private void playerMagicBox(){
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
		for(int i=0; i<playerList.size(); i++) {
			for(int j=magicBoxList.size()-1; j>=0; j--) {
				if(magicBoxList.get(j).crash(playerList.get(i))){
					MagicBox magicBox = (MagicBox) magicBoxList.get(j);
					magicBox.setCharacterIndex(i);//谁吃方块
					magicBox.setEaten(true);//方块被吃
					((Player)playerList.get(i)).setScore(((Player)playerList.get(i)).getScore()+30);
					GameJPanel.setPlayer("score",((Player) playerList.get(i)).getScore(),i);
				}

			}
		}
	}
	//npc与道具碰撞判断
	private void npcMagicBox(){
		List<SuperElement> npcList = ElementManager.getManager().getElementList("npc");
		List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
		for(int i=0; i<npcList.size(); i++) {
			for(int j=magicBoxList.size()-1; j>=0; j--) {
				if(magicBoxList.get(j).crash(npcList.get(i))){
					MagicBox magicBox = (MagicBox) magicBoxList.get(j);
					magicBox.setCharacterIndex(i+2);//谁吃方块
					magicBox.setEaten(true);//方块被吃
				}
			}
		}
	}
	//runGame调用，加入拓展
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
