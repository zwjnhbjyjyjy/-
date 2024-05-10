package gameStart;

import controller.GameController;
import controller.thread.GameMusicPlayer;
import model.loader.ElementLoader;
import view.GameFrame;

import java.io.IOException;

/**
 * @ClassName: GameStart
 * @Description:  ����Ϊ��Ϸ������
 * @author:Xiongting
 * @CreateDate:2022/12/29-2022/12/31
 */
public class GameStart {
	private static GameFrame gameFrame;
	//��Ϸ�������
	public static void main(String[] args) {
		//��Դ����
		try {
			ElementLoader.getElementLoader().readGamePro();
			ElementLoader.getElementLoader().readImagePro();
			ElementLoader.getElementLoader().readBubblePro();
			ElementLoader.getElementLoader().readCharactorsPro();
			ElementLoader.getElementLoader().readSquarePro();
		}catch (IOException e){
			System.out.println("��Դ����ʧ��");
			e.printStackTrace();
		}
		//��ʼ��
		gameFrame=new GameFrame();
		//������ʾ
		gameFrame.setVisible(true);
		//���ֲ���
		GameMusicPlayer musicPlayer=new GameMusicPlayer();
		musicPlayer.start();
	}

	/**
	 * �����л�
	 * @param panelName ��������
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

		//ǿ��ˢ��
		gameFrame.setVisible(false);
		gameFrame.setVisible(true);
	}

	/**
	 * ���¿�ʼ
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
	 * ����������
	 */
	public static void backGame(){
		GameController.setGameRunning(true);
		gameFrame.startGame();
		changeJPanel("begin");
	}

	/**
	 * ��ͣ��Ϸ
	 */
	public static void pauseGame(){
		GameController.setPausing(true);
	}

	/**
	 * ������Ϸ
	 */
	public static void continueGame(){
		GameController.setPausing(false);
		gameFrame.requestFocus();
		gameFrame.setFocusable(true);
	}
}
