package controller;

/**
 * 游戏控制信息类
 * @ClassName:GameController
 * @Description:
 * @author XiongTing
 * @CreateDate:2022.12.29
 */
public class GameController {
	private static boolean gameRunning=false;
	private static boolean twoPlayer;
	private static int npcNum;
	private static boolean playingmusic=false;
	private static int winning=3;
	private static boolean pausing=false;

	public static boolean isPausing() {
		return pausing;
	}

	public static void setPausing(boolean pausing) {
		GameController.pausing = pausing;
	}

	public static void setWinning(int winning) {
		GameController.winning = winning;
	}

	public static int getWinning() {
		return winning;
	}

	public static boolean isPlayingmusic() {
		return playingmusic;
	}

	public static void setPlayingmusic(boolean playingmusic) {
		GameController.playingmusic = playingmusic;
	}

	public static  boolean isGameRunning(){
		return gameRunning;
	}
	public static void setGameRunning(boolean gameRunning){
		GameController.gameRunning=gameRunning;
	}
	public static boolean isTwoPlayer(){
		return twoPlayer;
	}
	public static void setTwoPlayer(boolean twoPlayer){
		GameController.twoPlayer=twoPlayer;
	}
	public static int getNpcNum(){
		return npcNum;
	}
	public static void setNpcNum(int npcNum){
		GameController.npcNum=npcNum;
	}
}
