package gameStart;

import controller.GameController;
import controller.thread.GameMusicPlayer;
import model.loader.ElementLoader;
import view.GameFrame;

import java.io.IOException;

/**
 * @ClassName: GameStart
 * @Description:  该类为游戏启动类
 * @author:Xiongting
 * @CreateDate:2022/12/29-2022/12/31
 */
public class GameStart {
	private static GameFrame gameFrame;
	//游戏启动入口
	public static void main(String[] args) {
		//资源加载
		try {
			ElementLoader.getElementLoader().readGamePro();
			ElementLoader.getElementLoader().readImagePro();
			ElementLoader.getElementLoader().readBubblePro();
			ElementLoader.getElementLoader().readCharactorsPro();
			ElementLoader.getElementLoader().readSquarePro();
		}catch (IOException e){
			System.out.println("资源加载失败");
			e.printStackTrace();
		}
		//初始化
		gameFrame=new GameFrame();
		//界面显示
		gameFrame.setVisible(true);
		//音乐播放
		GameMusicPlayer musicPlayer=new GameMusicPlayer();
		musicPlayer.start();
	}

	/**
	 * 界面切换
	 * @param panelName 界面名称
	 */
	public static void changeJPanel(String panelName){
		if(panelName=="game"){
			GameController.setGameRunning(true);
			gameFrame.addListener();
		}else {
			GameController.setGameRunning(false);
			gameFrame.removeListener();
		}
		gameFrame.changePanel(panelName);

		//强制刷新
		gameFrame.setVisible(false);
		gameFrame.setVisible(true);
	}

	/**
	 * 重新开始
	 */
	public static void  restartGame(){
		GameController.setGameRunning(true);
		gameFrame.restartGame();
		changeJPanel("game");
	}
	public static void startNewGame(){
		GameController.setGameRunning(true);
		gameFrame.startGame();
		changeJPanel("game");
	}

	/**
	 * 返回主界面
	 */
	public static void backGame(){
		GameController.setGameRunning(true);
		gameFrame.startGame();
		changeJPanel("begin");
	}

	/**
	 * 暂停游戏
	 */
	public static void pauseGame(){
		GameController.setPausing(true);
	}

	/**
	 * 继续游戏
	 */
	public static void continueGame(){
		GameController.setPausing(false);
		gameFrame.requestFocus();
		gameFrame.setFocusable(true);
	}
}
