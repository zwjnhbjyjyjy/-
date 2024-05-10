package view;

import controller.GameController;
import controller.thread.GameThread;
import gameStart.GameStart;
import model.element.Character;
import model.element.MapSquare;
import model.element.SuperElement;
import model.loader.ElementLoader;
import model.manager.ElementManager;
import model.manager.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * ��Ϸ���
 * @author Zhao Wenjie
 * ����������������
 */

public class GameJPanel extends JPanel implements Runnable{

	//��Ϸ��ͣ/��ʼͼ��������
	private int clickNum;

	//����ͼ
	private ImageIcon background;

	//�������
	//���1����
	private static ImageIcon player1Img;
	//���2����
	private static ImageIcon player2Img;

	//npc����
	//npc1����
	private static ImageIcon npc1Img;
	//npc2����
	private static ImageIcon npc2Img;
	//npc3����
	private static ImageIcon npc3Img;

	//������
	private int w;
	//����߶�
	private int h;

	//���1ͼƬ
	private static JLabel player1JLabel;
	//���1����ֵ
	private static JLabel health1JLabel;
	//���1�ٶ�
	private static JLabel speed1JLabel;
	//���1������
	private static JLabel num1JLabel;
	//��������
	private static JLabel power1JLabel;
	//���1����
	private static JLabel score1JLabel;

	//���2ͼƬ
	private static JLabel player2JLabel;
	//���2����ֵ
	private static JLabel health2JLabel;
	//���2�ٶ�
	private static JLabel speed2JLabel;
	//���2������
	private static JLabel num2JLabel;
	//��������
	private static JLabel power2JLabel;
	//���2����
	private static JLabel score2JLabel;

	//npc1
	private static JLabel npc1JLabel;
	//npc1����ֵ
	private static JLabel npc1Health;

	//npc2
	private static JLabel npc2JLabel;
	//npc2����ֵ
	private static JLabel npc2Health;

	//npc3
	private static JLabel npc3JLabel;
	//npc3����ֵ
	private static JLabel npc3Health;

	//��Ϸ��ͣ/������ť
	private static JButton runGame;

	/**
	 * ���캯��
	 */
	public GameJPanel(){

		//data=windowSize
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.background = new ImageIcon("img/bg/background.png");
		this.w = new Integer(data.get(0));//��ȡwindowSize�еĿ��
		this.h = new Integer(data.get(1));//��ȡwindowSize�еĳ���
		initJPanel();
	}

	/**
	 * ��ʾ�������ݣ��滭
	 * @param g  the <code>Graphics</code> context in which to paint
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		gameRuntime(g);
	}

	@Override
	public void run() {
		while(GameController.isGameRunning()) {
			try {
				//����20ms��1sˢ��50��
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.repaint(); //ÿ��100����ˢ�»���
		}
	}

	/**
	 * չʾԪ�ع����������е�Ԫ��
	 */
	public void gameRuntime(Graphics g) {
		Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
		//Ԫ����ʾ���ȼ�����
		Set<String> set = map.keySet();
		Set<String> sortSet = new TreeSet<String>(ElementManager.getManager().getMapKeyComparator());
		sortSet.addAll(set);
		for(String key:sortSet) {
			List<SuperElement> list = map.get(key);
			for(int i=0; i<list.size(); i++) {
				//����ÿ�����Լ���show���������ʾ
				list.get(i).showElement(g);
			}
		}

		g.setFont(new Font("΢���ź�", Font.BOLD, 24));
		//�õ�������ʱ��
		int allTime = GameThread.getAllTime()/1000;
		//��Ϊ�֡������ʽ
		int minute = allTime / 60;
		int second = allTime % 60;
		String m;
		String s;
		if(minute < 10)
			m = "0" + minute;
		else
			m = Integer.toString(minute);
		if(second<10)
			s = "0" + second;
		else
			s = Integer.toString(second);
		//��ʾʱ��
		g.drawString("ʣ��ʱ��: ", 30, 40);
		g.drawString( m + ":" + s, 40, 70);

		//����ģʽ�޸�npc����
		if(BeginJPanel.getModeNum() == 1){
			//���˼�ģʽ
			//������ʱ���޸�npc����
			if(allTime <= 480 && allTime >= 300){
				if(GameThread.getNpcList().get(0) instanceof Character){
					changeNpcAttribute(0, 2);
					changeNpcAttribute(1, 2);
					changeNpcAttribute(2, 2);
				}
			} else if(allTime < 300 && allTime >= 120){
				changeNpcAttribute(0, 3);
				changeNpcAttribute(1, 3);
				changeNpcAttribute(2, 3);
			} else if(allTime < 120 && allTime >= 0){
				changeNpcAttribute(0, 4);
				changeNpcAttribute(1, 4);
				changeNpcAttribute(2, 4);
			}
		}else if (BeginJPanel.getModeNum() == 2){
			//��������ģʽ
			//����ʱ���޸�npc����
			if(allTime <= 480 && allTime >= 360){
				System.out.println("�޸�����");
				if(GameThread.getNpcList().get(0) instanceof Character){
					changeNpcAttribute(0, 2);
					changeNpcAttribute(1, 2);
					changeNpcAttribute(2, 2);
				}
			} else if(allTime < 360 && allTime >= 240){
				changeNpcAttribute(0, 3);
				changeNpcAttribute(1, 3);
				changeNpcAttribute(2, 3);
			} else if(allTime < 240 && allTime >= 120){
				changeNpcAttribute(0, 4);
				changeNpcAttribute(1, 4);
				changeNpcAttribute(2, 4);
			} else if (allTime < 120 && allTime >= 0){
				changeNpcAttribute(0, 5);
				changeNpcAttribute(1, 5);
				changeNpcAttribute(2, 5);
			}
		} else if (BeginJPanel.getModeNum() == 3){
			//˫�˼�ģʽ
			//������ʱ���޸�npc����
			if(allTime <= 480 && allTime >= 300){
				if(GameThread.getNpcList().get(0) instanceof Character){
					changeNpcAttribute(0, 2);
					changeNpcAttribute(1, 2);
				}
			} else if(allTime < 300 && allTime >= 120){
				changeNpcAttribute(0, 3);
				changeNpcAttribute(1, 3);
			} else if(allTime < 120 && allTime >= 0){
				changeNpcAttribute(0, 4);
				changeNpcAttribute(1, 4);
			}
		}else if (BeginJPanel.getModeNum() == 4){
			//˫������ģʽ
			//����ʱ���޸�npc����
			if(allTime <= 480 && allTime >= 360){
				System.out.println("�޸�����");
				if(GameThread.getNpcList().get(0) instanceof Character){
					changeNpcAttribute(0, 2);
					changeNpcAttribute(1, 2);
				}
			} else if(allTime < 360 && allTime >= 240){
				changeNpcAttribute(0, 3);
				changeNpcAttribute(1, 3);
			} else if(allTime < 240 && allTime >= 120){
				changeNpcAttribute(0, 4);
				changeNpcAttribute(1, 4);
			} else if (allTime < 120 && allTime >= 0){
				changeNpcAttribute(0, 5);
				changeNpcAttribute(1, 5);
			}
		}
	}

	private void initJPanel(){

		//�ֹ����ֹ�����
		this.setLayout(null);
		//����ͼƬ��ǩ
		JLabel jLabel = new JLabel(background);
		//���ñ���ͼƬΪ���ڳߴ�
		background.setImage(background.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		//���ñ���ͼƬ��ǩ��x,y,w,h
		jLabel.setBounds(0, 0, w, h);

		//��ʾ��������
		//�ж���Ϸģʽ
		if(BeginJPanel.getModeNum() == 1 || BeginJPanel.getModeNum() == 2){
			//���ѡ�����Ϸ��������
			switch (BeginJPanel.getOneRoleNum()){
				case 1:
					player1Img = new ImageIcon("img/player/player1/1.png");
					break;
				case 2:
					player1Img = new ImageIcon("img/player/player2/1.png");
					break;
				case 3:
					player1Img = new ImageIcon("img/player/player3/1.png");
					break;
				case 4:
					player1Img = new ImageIcon("img/player/player4/1.png");
					break;
			}

			//��ʾ���1ͼƬ
			//�������1ͼƬ��λ�á��ߴ�
			player1Img.setImage(player1Img.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			player1JLabel = new JLabel(player1Img);
			player1JLabel.setBounds(10, 70, 150, 150);
			this.add(player1JLabel);

			//��ʾ���1����
			//����ֵ
			health1JLabel = new JLabel();
			health1JLabel.setText("����ֵ��4");
			health1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			health1JLabel.setForeground(new Color(0, 0, 0));
			health1JLabel.setBounds(30, 185, 150, 100);
			this.add(health1JLabel);

			//�ٶ�
			speed1JLabel = new JLabel();
			speed1JLabel.setText("�ٶȣ�4");
			speed1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			speed1JLabel.setForeground(Color.BLACK);
			speed1JLabel.setBounds(30, 225, 150, 100);
			this.add(speed1JLabel);

			//������
			num1JLabel = new JLabel();
			num1JLabel.setText("��������1");
			num1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			num1JLabel.setForeground(Color.BLACK);
			num1JLabel.setBounds(30, 265, 150 ,100);
			this.add(num1JLabel);

			//����
			power1JLabel =new JLabel();
			power1JLabel.setText("������1");
			power1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			power1JLabel.setForeground(Color.BLACK);
			power1JLabel.setBounds(30 ,305, 150, 100);
			this.add(power1JLabel);

			//����
			score1JLabel = new JLabel();
			score1JLabel.setText("�÷֣�0");
			score1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			score1JLabel.setForeground(Color.BLACK);
			score1JLabel.setBounds(30 ,345, 150, 100);
			this.add(score1JLabel);

			//��ʾnpc����
			//��ʾnpc1ͼƬ
			npc1Img = new ImageIcon("img/computer/1/down.png");
			npc1Img.setImage(npc1Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc1JLabel = new JLabel(npc1Img);
			npc1JLabel.setBounds(1350, 50, 80, 80);
			this.add(npc1JLabel);

			//��ʾnpc1����ֵ
			npc1Health = new JLabel();
			npc1Health.setFont(new Font("΢���ź�", Font.BOLD, 20));
			npc1Health.setForeground(Color.BLACK);
			npc1Health.setBounds(1350, 115, 150, 100);
			this.add(npc1Health);

			//��ʾnpc2ͼƬ
			npc2Img = new ImageIcon("img/computer/2/down.png");
			npc2Img.setImage(npc2Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc2JLabel = new JLabel(npc2Img);
			npc2JLabel.setBounds(1350, 195, 80, 80);
			this.add(npc2JLabel);

			//��ʾnpc2����ֵ
			npc2Health = new JLabel();
			npc2Health.setFont(new Font("΢���ź�", Font.BOLD, 20));
			npc2Health.setForeground(Color.BLACK);
			npc2Health.setBounds(1350, 260, 150, 100);
			this.add(npc2Health);

			//��ʾnpc3ͼƬ
			npc3Img = new ImageIcon("img/computer/3/down.png");
			npc3Img.setImage(npc3Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc3JLabel = new JLabel(npc3Img);
			npc3JLabel.setBounds(1350, 340, 80, 80);
			this.add(npc3JLabel);

			//��ʾnpc3����ֵ
			npc3Health = new JLabel();
			npc3Health.setFont(new Font("΢���ź�", Font.BOLD, 20));
			npc3Health.setForeground(Color.BLACK);
			npc3Health.setBounds(1350, 405, 150, 100);
			this.add(npc3Health);
		} else if(BeginJPanel.getModeNum() == 3 || BeginJPanel.getModeNum() == 4){

			//���1ѡ�����Ϸ��������
			switch (BeginJPanel.getOneRoleNum()){
				case 1:
					player1Img = new ImageIcon("img/player/player1/1.png");
					break;
				case 2:
					player1Img = new ImageIcon("img/player/player2/1.png");
					break;
				case 3:
					player1Img = new ImageIcon("img/player/player3/1.png");
					break;
				case 4:
					player1Img = new ImageIcon("img/player/player4/1.png");
					break;
			}

			//���2ѡ�����Ϸ��������
			switch (BeginJPanel.getTwoRoleNum()){
				case 1:
					player2Img = new ImageIcon("img/player/player1/1.png");
					break;
				case 2:
					player2Img = new ImageIcon("img/player/player2/1.png");
					break;
				case 3:
					player2Img = new ImageIcon("img/player/player3/1.png");
					break;
				case 4:
					player2Img = new ImageIcon("img/player/player4/1.png");
					break;
			}

			//��ʾ���1ͼƬ
			//�������1ͼƬ��λ�á��ߴ�
			player1Img.setImage(player1Img.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			player1JLabel = new JLabel(player1Img);
			player1JLabel.setBounds(10, 70, 150, 150);
			this.add(player1JLabel);

			//��ʾ���1����
			//����ֵ
			health1JLabel = new JLabel();
			health1JLabel.setText("����ֵ��4" );
			health1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			health1JLabel.setForeground(new Color(0, 0, 0));
			health1JLabel.setBounds(30, 185, 150, 100);
			this.add(health1JLabel);

			//�ٶ�
			speed1JLabel = new JLabel();
			speed1JLabel.setText("�ٶȣ�4");
			speed1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			speed1JLabel.setForeground(Color.BLACK);
			speed1JLabel.setBounds(30, 225, 150, 100);
			this.add(speed1JLabel);

			//������
			num1JLabel = new JLabel();
			num1JLabel.setText("��������1");
			num1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			num1JLabel.setForeground(Color.BLACK);
			num1JLabel.setBounds(30, 265, 150 ,100);
			this.add(num1JLabel);

			//����
			power1JLabel =new JLabel();
			power1JLabel.setText("������1");
			power1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			power1JLabel.setForeground(Color.BLACK);
			power1JLabel.setBounds(30 ,305, 150, 100);
			this.add(power1JLabel);

			//����
			score1JLabel = new JLabel();
			score1JLabel.setText("������0");
			score1JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			score1JLabel.setForeground(Color.BLACK);
			score1JLabel.setBounds(30 ,345, 150, 100);
			this.add(score1JLabel);

			//��ʾ���2ͼƬ
			//�������2ͼƬ��λ�á��ߴ�
			player2Img.setImage(player2Img.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			player2JLabel = new JLabel(player2Img);
			player2JLabel.setBounds(10, 440, 150, 150);
			this.add(player2JLabel);

			//��ʾ���2����
			//����ֵ
			health2JLabel = new JLabel();
			health2JLabel.setText("����ֵ��4" );
			health2JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			health2JLabel.setForeground(new Color(0, 0, 0));
			health2JLabel.setBounds(30, 555, 150, 100);
			this.add(health2JLabel);

			//�ٶ�
			speed2JLabel = new JLabel();
			speed2JLabel.setText("�ٶȣ�4");
			speed2JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			speed2JLabel.setForeground(Color.BLACK);
			speed2JLabel.setBounds(30, 595, 150, 100);
			this.add(speed2JLabel);

			//������
			num2JLabel = new JLabel();
			num2JLabel.setText("��������1");
			num2JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			num2JLabel.setForeground(Color.BLACK);
			num2JLabel.setBounds(30, 635, 150 ,100);
			this.add(num2JLabel);

			//����
			power2JLabel =new JLabel();
			power2JLabel.setText("������1");
			power2JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			power2JLabel.setForeground(Color.BLACK);
			power2JLabel.setBounds(30 ,675, 150, 100);
			this.add(power2JLabel);

			//����
			//����
			score2JLabel = new JLabel();
			score2JLabel.setText("������0");
			score2JLabel.setFont(new Font("΢���ź�", Font.BOLD, 22));
			score2JLabel.setForeground(Color.BLACK);
			score2JLabel.setBounds(30 ,715, 150, 100);
			this.add(score2JLabel);

			//��ʾnpc����
			//��ʾnpc1ͼƬ
			npc1Img = new ImageIcon("img/computer/1/down.png");
			npc1Img.setImage(npc1Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc1JLabel = new JLabel(npc1Img);
			npc1JLabel.setBounds(1350, 50, 80, 80);
			this.add(npc1JLabel);

			//��ʾnpc1����ֵ
			npc1Health = new JLabel();
			npc1Health.setFont(new Font("΢���ź�", Font.BOLD, 20));
			npc1Health.setForeground(Color.BLACK);
			npc1Health.setBounds(1350, 115, 150, 100);
			this.add(npc1Health);

			//��ʾnpc2ͼƬ
			npc2Img = new ImageIcon("img/computer/2/down.png");
			npc2Img.setImage(npc2Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc2JLabel = new JLabel(npc2Img);
			npc2JLabel.setBounds(1350, 195, 80, 80);
			this.add(npc2JLabel);

			//��ʾnpc2����ֵ
			npc2Health = new JLabel();
			npc2Health.setFont(new Font("΢���ź�", Font.BOLD, 20));
			npc2Health.setForeground(Color.BLACK);
			npc2Health.setBounds(1350, 260, 150, 100);
			this.add(npc2Health);

		}

		//������Ϸģʽ����npc����ֵ
		switch (BeginJPanel.getModeNum()){
			case 1:
				npc1Health.setText("����ֵ��3");
				npc2Health.setText("����ֵ��3");
				npc3Health.setText("����ֵ��3");
				break;
			case 2:
				npc1Health.setText("����ֵ��4");
				npc2Health.setText("����ֵ��4");
				npc3Health.setText("����ֵ��4");
				break;
			case 3:
				npc1Health.setText("����ֵ��5");
				npc2Health.setText("����ֵ��5");
				break;
			case 4:
				npc1Health.setText("����ֵ��6");
				npc2Health.setText("����ֵ��6");
				break;
		}

		this.setFocusable(true);

		//��Ϸ����
		runGame = createButton("img/other/pause.png", 1340, 500, 100, 100);
		runGame.setVisible(true);
		this.add(runGame);

		//��Ϸ��ͣ�¼�
		runGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				continueGame();
			}
		});

		//���¿�ʼ
		JButton restartGame = createButton("img/other/restart.png", 1340, 600, 100, 100);
		restartGame.setVisible(true);
		this.add(restartGame);

		//���¿�ʼ�¼�
		restartGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStart.restartGame();
			}
		});

		//������Ϸ������
		JButton backToMain = createButton("img/other/backToMain.png", 1340, 700, 100, 100);
		backToMain.setVisible(true);
		this.add(backToMain);

		//���������水ť�¼�
		backToMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameMap.clearMapALL();
				new GameFrame();
			}
		});

		//��ӿؼ�
		this.add(jLabel);

	}

	private void continueGame(){
		clickNum++;
		if(clickNum % 2 == 1){
			ImageIcon img = new ImageIcon("img/other/run.png");
			runGame.setIcon(img);
			GameStart.pauseGame();
		} else if(clickNum % 2 == 0){
			ImageIcon img = new ImageIcon("img/other/pause.png");
			runGame.setIcon(img);
			GameStart.continueGame();
		}
	}

	/**
	 * �������水ť
	 *
	 * @param file  ͼƬλ��
	 * @param x  ����x
	 * @param y  ����y
	 * @param w  ��ť���
	 * @param h  ��ť�߶�
	 * @return JButton
	 */
	private JButton createButton(String file ,int x,int y,int w, int h){

		JButton jButton = new JButton();
		ImageIcon img = new ImageIcon(file);
		jButton.setIcon(img);
		jButton.setBounds(x,y,w,h);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);
		jButton.setContentAreaFilled(false);
		return jButton;
	}

	/**
	 * ��ɫ������ͼƬ���
	 *
	 * @param label �����Ľ�ɫ
	 * @param img ������ͼƬ
	 * @param file ��ɫͼƬ�ļ�
	 * @param x ͼƬX����
	 * @param y ͼƬY����
	 * @param w ͼƬ���
	 * @param h ͼƬ�߶�
	 */
	private static void changeImg(JLabel label, ImageIcon img, String file, int x, int y, int w, int h){

		img = new ImageIcon(file);
		img.setImage(img.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
		label.setIcon(img);

	}

	/**
	 * �޸��������ֵ
	 *
	 * @param attribute �޸ĵ���������
	 * @param i �������ߺ������ֵ
	 * @param characterIndex ������
	 */
	public static void setPlayer(String attribute, int i, int characterIndex){

		switch (attribute){
			case "health":
				if(characterIndex == 0){
					if (i == 0){
						changeImg(player1JLabel, player1Img, "img/player/player" + BeginJPanel.getOneRoleNum() + "/Gray.png", 10, 70, 150, 150);
						health1JLabel.setForeground(Color.GRAY);
						power1JLabel.setForeground(Color.GRAY);
						num1JLabel.setForeground(Color.GRAY);
						score1JLabel.setForeground(Color.GRAY);
						speed1JLabel.setForeground(Color.GRAY);
					}
					health1JLabel.setText("����ֵ��" + i);
				} else if(characterIndex == 1){
					if (i == 0){
						changeImg(player2JLabel, player2Img, "img/player/player" + BeginJPanel.getTwoRoleNum() + "/Gray.png", 10, 70, 150, 150);
						health2JLabel.setForeground(Color.GRAY);
						power2JLabel.setForeground(Color.GRAY);
						num2JLabel.setForeground(Color.GRAY);
						score2JLabel.setForeground(Color.GRAY);
						speed2JLabel.setForeground(Color.GRAY);
					}
					health2JLabel.setText("����ֵ��" + i);
				}
				break;
			case "speed":
				if(characterIndex == 0){
					speed1JLabel.setText("�ٶȣ�" + i);
				} else if(characterIndex == 1){
					speed2JLabel.setText("�ٶȣ�" + i);
				}
				break;
			case "power":
				if(characterIndex == 0){
					power1JLabel.setText("������" + i);
				} else if(characterIndex == 1){
					power2JLabel.setText("������" + i);
				}
				break;
			case "num":
				if(characterIndex == 0){
					num1JLabel.setText("��������" + i);
				} else if(characterIndex == 1){
					num2JLabel.setText("��������" + i);
				}
				break;
			case "score":
				if(characterIndex == 0){
					score1JLabel.setText("�÷֣�" + i);
				} else if(characterIndex == 1){
					score2JLabel.setText("�÷֣�" + i);
				}
				break;
		}
	}

	/**
	 * �޸�npc������
	 *
	 * @param i �������ߺ������ֵ
	 * @param characterIndex npc���
	 */
	public static void setNpc(int i, int characterIndex){
		switch (characterIndex){
			case 2:
				if(i == 0){
					changeImg(npc1JLabel, npc1Img, "img/computer/1/Gray.png", 1350, 50, 80, 80);
					npc1Health.setForeground(Color.GRAY);
					npc1Health.setText("npc����");
				} else {
					npc1Health.setText("����ֵ��" + i);
				}
				break;
			case 3:
				if(i == 0){
					changeImg(npc2JLabel, npc2Img, "img/computer/2/Gray.png", 1350, 260, 80, 80);
					npc2Health.setForeground(Color.GRAY);
					npc2Health.setText("npc����");
				} else {
					npc2Health.setText("����ֵ��" + i);
				}
				break;
			case 4:
				if(i == 0){
					changeImg(npc3JLabel, npc3Img, "img/computer/3/Gray.png", 1350, 260, 80, 80);
					npc3Health.setForeground(Color.GRAY);
					npc3Health.setText("npc����");
				} else {
					npc3Health.setText("����ֵ��" + i);
				}
				break;
		}
	}

	/**
	 * �޸�npc���ԣ�������ϷȤζ��
	 *
	 * @param i ���޸ĵ�npc���
	 * @param attribute �޸ĵ�����ֵ
	 */
	public void changeNpcAttribute(int i, int attribute){

		((Character) GameThread.getNpcList().get(i)).setBubblePower(attribute);
		((Character) GameThread.getNpcList().get(i)).setBubbleLargest(attribute);
	}
}
