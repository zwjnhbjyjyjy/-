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
 * ��Ϸ����
 * @author Zhao Wenjie
 */

public class GameFrame extends JFrame {

	//�����
	private JPanel contentPane;

	//��ʼ����
	private BeginJPanel beginJPanel;

	//����
	private GameJPanel gameJPanel;

	//��������
	private OverJPanel overJPanel;

	//��Ϸ����
	private KeyListener keyListener;

	//��Ƭ����
	private CardLayout layout;

	/**
	 * ���캯������ʼ������
	 */
	public GameFrame() {
		init();
	}

	/**
	 * ��ʼ��GameFrame
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
	 * �л�����
	 * @param name ���л����������
	 */
	public void changePanel(String name) {
		layout.show(contentPane, name);
	}

	/**
	 * ��Ϸ����
	 */
	public void startGame() {

		//�½���Ϸ���
		gameJPanel = new GameJPanel();
		//��ӽ���frame
		contentPane.add("game",gameJPanel);

		//����ˢ���߳�����
		if(gameJPanel instanceof Runnable) {
			new Thread(gameJPanel).start();
		}
		//�߳�����
		GameThread gameThread = new GameThread();
		gameThread.start();
		this.setFocusable(true);
	}

	public void restartGame() {

		GameMap.clearMapALL();
		//�½���Ϸ���
		gameJPanel = new GameJPanel();
		//��ӽ���frame
		contentPane.add("game",gameJPanel);

		//����ˢ���߳�����
		if(gameJPanel instanceof Runnable) {
			new Thread(gameJPanel).start();
		}
		//�߳�����
		GameThread gameThread = new GameThread();
		gameThread.start();

		//ʹ��Ӱ�ť�����ܼ���������
		this.setFocusable(true);
	}

	/**
	 * �󶨼���
	 */
	public void addListener() {
		if(keyListener!=null)
			this.addKeyListener(keyListener);
	}

	/**
	 * �Ƴ�����
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
