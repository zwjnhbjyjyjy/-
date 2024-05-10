package view;

import controller.thread.GameKeyListener;
import controller.thread.GameThread;
import model.loader.ElementLoader;
import model.manager.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;


/**
 * 游戏窗体
 * @author Zhao Wenjie
 */

public class GameFrame extends JFrame {

	//主面板
	private JPanel contentPane;

	//开始画板
	private BeginJPanel beginJPanel;

	//画板
	private GameJPanel gameJPanel;

	//结束画板
	private OverJPanel overJPanel;

	//游戏按键
	private KeyListener keyListener;

	//卡片布局
	private CardLayout layout;

	/**
	 * 构造函数，初始化窗口
	 */
	public GameFrame() {
		init();
	}

	/**
	 * 初始化GameFrame
	 */
	protected void init() {
		this.setTitle("CrazyArcade");
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.setSize(new Integer(data.get(0)).intValue(), new Integer(data.get(1)).intValue());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		keyListener = new GameKeyListener();

		this.contentPane = new JPanel();
		this.setContentPane(contentPane);

		this.layout = new CardLayout();
		this.contentPane.setLayout(layout);

		this.beginJPanel = new BeginJPanel();
		this.contentPane.add("begin",beginJPanel);

		this.overJPanel = new OverJPanel();
		this.contentPane.add("over",overJPanel);

		this.layout.show(contentPane, "begin");
		this.setVisible(true);
	}

	/**
	 * 切换画板
	 * @param name 待切换的面板名称
	 */
	public void changePanel(String name) {
		layout.show(contentPane, name);
	}

	/**
	 * 游戏启动
	 */
	public void startGame() {

		//新建游戏面板
		gameJPanel = new GameJPanel();
		//添加进入frame
		contentPane.add("game",gameJPanel);

		//界面刷新线程启动
		if(gameJPanel instanceof Runnable) {
			new Thread(gameJPanel).start();
		}
		//线程启动
		GameThread gameThread = new GameThread();
		gameThread.start();
		this.setFocusable(true);
	}

	public void restartGame() {

		GameMap.clearMapALL();
		//新建游戏面板
		gameJPanel = new GameJPanel();
		//添加进入frame
		contentPane.add("game",gameJPanel);

		//界面刷新线程启动
		if(gameJPanel instanceof Runnable) {
			new Thread(gameJPanel).start();
		}
		//线程启动
		GameThread gameThread = new GameThread();
		gameThread.start();

		//使添加按钮后仍能监听到键盘
		this.setFocusable(true);
	}

	/**
	 * 绑定监听
	 */
	public void addListener() {
		if(keyListener!=null)
			this.addKeyListener(keyListener);
	}

	/**
	 * 移除监听
	 */
	public void removeListener() {
		this.removeKeyListener(keyListener);
	}

	/**
	 * getter and setter
	 */
	public KeyListener getKeyListener() {
		return keyListener;
	}

	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	public void setBeginJPanel(BeginJPanel beginJPanel) {
		this.beginJPanel = beginJPanel;
	}

	public GameJPanel getGameJPanel() {
		return gameJPanel;
	}

	public void setGameJPanel(GameJPanel gameJPanel) {
		this.gameJPanel = gameJPanel;
	}


}
