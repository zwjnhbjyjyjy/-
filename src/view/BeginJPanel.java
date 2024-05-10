package view;

import controller.GameController;
import controller.thread.GameMusicPlayer;
import gameStart.GameStart;
import model.loader.ElementLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
/**
 * @className:BeginJPanel
 * @description�� ��Ϸ��ʼ���ֽ���
 * @author�� He Xiaofei
 * @CreateTime: 2022-12-27��2022-12-28
 */
public class BeginJPanel extends JPanel{
	//����ͼ
	private ImageIcon img;
	//������
	private int w;
	//����߶�
	private int h;
	//�������ð�ť�ĵ������
	private int clickNum = 0;
	//ģʽѡ��
	//1�����˼򵥣�2���������ѣ�3����˫�˼򵥣�4����˫������
	private static int modeNum = 0;
	//��ɫѡ��
	//1������ˣ�2�������3�����ɫ�˯�ˣ�4�������
	private static int oneRoleNum = 0;
	private static int twoRoleNum = 0;
	//����ǳ�
	private static String player1Name;
	private static String player2Name;

	//���캯��
	public BeginJPanel(){
		//data=windowSize
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.img = ElementLoader.getElementLoader().getImageMap().get("beginBackground");
		this.w = new Integer(data.get(0)).intValue();//��ȡwindowSize�еĿ��
		this.h = new Integer(data.get(1)).intValue();//��ȡwindowSize�еĳ���
		mainJPanel();
	}
	//��ʼ��
	private void mainJPanel() {
		//�ֹ����ֹ�����
		this.setLayout(null);
		//����ͼƬ��ǩ
		JLabel jLabel = new JLabel(img);
		//����ͼƬΪ���ڳߴ�
		img.setImage(img.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		//����ͼƬ��ǩ��x,y,w,h
		jLabel.setBounds(0, 0, w, h);

		//��ʾ�������˵������
		ImageIcon playerInstrImg = new ImageIcon("img/bg/playerInstruction.png");
		//����ͼƬ���ߴ�
		playerInstrImg.setImage(playerInstrImg.getImage().getScaledInstance(809, 537,Image.SCALE_DEFAULT ));
		JLabel playerInstruction = new JLabel(playerInstrImg);
		playerInstruction.setBounds(314, 151, 807, 566);
		playerInstruction.setVisible(false);

		//��ʾ����˵������
		ImageIcon magicImg = new ImageIcon("img/bg/magicBoxInstruction.png");
		//����ͼƬ�ͳߴ�
		magicImg.setImage(magicImg.getImage().getScaledInstance(809,537,Image.SCALE_DEFAULT));
		JLabel magicBoxInstruction = new JLabel(magicImg);
		magicBoxInstruction.setBounds(314,151,807,566);
		//��ʼ״̬����Ϊ���ɼ�
		magicBoxInstruction.setVisible(false);

		//��ʾѡ��ģʽ����
		ImageIcon bgimg2 = new ImageIcon("img/bg/selectMode.png");
		//ѡ����ǩ
		JLabel bgJLabel2 = new JLabel(bgimg2);
		//����ͼƬ�ߴ�
		bgimg2.setImage(bgimg2.getImage().getScaledInstance(687,533,Image.SCALE_DEFAULT));
		//����ͼƬ��ǩ�ߴ�
		bgJLabel2.setBounds(450,151,687,533);
		bgJLabel2.setVisible(false);

		//��ʾѡ���ɫ����
		ImageIcon selCharacterImg = new ImageIcon("img/bg/selectCharacter.png");
		//ѡ����ǩ
		JLabel bgJLabel3 = new JLabel(selCharacterImg);
		//����ͼƬ�ߴ�
		selCharacterImg.setImage(selCharacterImg.getImage().getScaledInstance(674,506,Image.SCALE_DEFAULT));
		//����ͼƬ��ǩ�ߴ�
		bgJLabel3.setBounds(450,151,674,506);
		bgJLabel3.setVisible(false);


		//��ʼ��ť
		JButton gameStartButton = createButton("img/bg/gameStartButton.png",500,200,500,500);
		//��������ͼ��
		JButton closeMusic = createButton("img/bg/keepMusic.png",0,0,111,109);
		//�ر�1��ť
		JButton closeButton = createButton("img/bg/closeButton.png",370,635,224,50 );
		//���߰�ť
		JButton magicBoxButton = createButton("img/bg/magicBox.png",820,635,224,50);
		//�ر�2��ť
		JButton close2Button = createButton("img/bg/closeButton.png",830,640,224,50 );
		//������ť
		JButton caozuoButton = createButton("img/bg/instruction.png",360,640,224,50);
		//����˵��ͼ��
		JButton instructionButton = createButton("img/bg/instructionButton.png",1380,0,111,109);
		//���ذ�ť
		JButton backButton = createButton("img/bg/back.png",585,615,180,65);
		//������ť
		JButton nextButton = createButton("img/bg/next.png",815,615,180,65);
		//���˼�ģʽ
		JButton oneEasyButton = createButton("img/bg/oneEasy.png",560,300,200,56);
		//��������ģʽ
		JButton oneHardButton = createButton("img/bg/oneHard.png",840,295,200,66);
		//˫�˼�ģʽ
		JButton twoEasyButton = createButton("img/bg/twoEasy.png",560,450,200,58);
		//˫������ģʽ
		JButton twoHardButton = createButton("img/bg/twoHard.png",840,450,200,59);
		//��ɫѡ��ť
		JButton selCharButton = createButton("img/bg/character.png",700,165,192,41);
		selCharButton.setVisible(false);
		//���һ��ť
		JButton player1 = createButton("img/bg/player1.png",700,165,148,41);
		player1.setVisible(false);
		//��Ҷ���ť
		JButton player2 = createButton("img/bg/player2.png",700,165,137,41);
		player2.setVisible(false);
		//����2��ť
		JButton back2Button = createButton("img/bg/back.png",575,590,180,65);
		//����2��ť
		JButton next2Button = createButton("img/bg/next.png",805,590,180,65);
		//���ذ�ť3
		JButton back3Button = createButton("img/bg/back.png",575,595,180,65);
		//��ʼ��Ϸ��ť
		JButton startGame = createButton("img/bg/startGame.png",805,595,180,55);
		//��ɫ1ͷ
		JButton role1Head = createButton("img/player/player1/2.png",550,280,94,71);
		//��ɫ1ȫ��
		JButton role1all = createButton("img/player/player1/1.png",860,255,192,239);
		role1all.setVisible(false);
		//��ɫ2ͷ
		JButton role2Head = createButton("img/player/player2/2.png",720,280,94,71);
		//��ɫ2ȫ��
		JButton role2all = createButton("img/player/player2/1.png",860,255,192,239);
		role2all.setVisible(false);
		//��ɫ3ͷ
		JButton role3Head = createButton("img/player/player3/2.png",550,385,94,71);
		//��ɫ3ȫ��
		JButton role3all = createButton("img/player/player3/1.png",860,255,192,239);
		role3all.setVisible(false);
		//��ɫ4ͷ
		JButton role4Head = createButton("img/player/player4/2.png",720,385,94,71);
		//��ɫ4ȫ��
		JButton role4all = createButton("img/player/player4/1.png",860,255,192,239);
		role4all.setVisible(false);

		//�����ǳ��ı���
		JTextField name1 = new JTextField("�������ǳ�");
		name1.setVisible(false);
		name1.setBounds(600,485,180,55);
		//�����ı���߿��ϸΪ0
		name1.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
		name1.setForeground(new Color(255,255,0));
		//�����ı��򱳾�͸��
		name1.setOpaque(false);
		//�����ı�������
		name1.setFont(new Font("΢���ź�", Font.PLAIN,20));
		JTextField name2 = new JTextField("�������ǳ�");
		name2.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
		name2.setBounds(600,485,180,55);
		name2.setFont(new Font("΢���ź�",Font.PLAIN,20));
		name2.setForeground(new Color(255,255,0));
		name2.setOpaque(false);
		name2.setVisible(false);

		//��ʼ��ť�¼�
		gameStartButton.setVisible(true);
		gameStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStartButton.setVisible(false);
				bgJLabel2.setVisible(true);
				backButton.setVisible(true);
				nextButton.setVisible(true);
				oneEasyButton.setVisible(true);
				oneHardButton.setVisible(true);
				twoEasyButton.setVisible(true);
				twoHardButton.setVisible(true);
			}
		});

		//��������ͼ���¼�
		closeMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameMusicPlayer gameMusicPlayer = new GameMusicPlayer();
				if(clickNum%2==0){
					gameMusicPlayer.overMusic();
					ImageIcon img = new ImageIcon("img/bg/closeMusic.png");
					closeMusic.setIcon(img);
				}else {
					gameMusicPlayer.restartMusic();
					ImageIcon img = new ImageIcon("img/bg/keepMusic.png");
					closeMusic.setIcon(img);
				}
				clickNum++;
			}
		});

		//�ر�1��ť�¼�
		closeButton.setVisible(false);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerInstruction.setVisible(false);
				magicBoxInstruction.setVisible(false);
				closeButton.setVisible(false);
				magicBoxButton.setVisible(false);
				gameStartButton.setVisible(true);

			}
		});
		//���߽��ܰ�ť�¼�
		magicBoxButton.setVisible(false);
		magicBoxButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerInstruction.setVisible(false);
				magicBoxInstruction.setVisible(true);
				closeButton.setVisible(false);
				magicBoxButton.setVisible(false);
				close2Button.setVisible(true);
				caozuoButton.setVisible(true);

			}
		});
		//�ر�2��ť�¼�
		close2Button.setVisible(false);
		close2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				magicBoxInstruction.setVisible(false);
				playerInstruction.setVisible(false);
				close2Button.setVisible(false);
				caozuoButton.setVisible(false);
				gameStartButton.setVisible(true);

			}
		});

		//��Ҳ�����ť�¼�
		caozuoButton.setVisible(false);
		caozuoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				magicBoxInstruction.setVisible(false);
				playerInstruction.setVisible(true);
				caozuoButton.setVisible(false);
				close2Button.setVisible(false);
				closeButton.setVisible(true);
				magicBoxButton.setVisible(true);
			}
		});

		//����˵����ť�¼�
		instructionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//��������ʾģʽѡ����߽�ɫѡ���ʱ�򣬲���˵����ť��������Ӧ
				if(bgJLabel2.isVisible()||bgJLabel3.isVisible()){

				}else {
					gameStartButton.setVisible(false);
					bgJLabel2.setVisible(false);
					bgJLabel3.setVisible(false);
					oneEasyButton.setVisible(false);
					oneHardButton.setVisible(false);
					twoEasyButton.setVisible(false);
					twoHardButton.setVisible(false);
					backButton.setVisible(false);
					nextButton.setVisible(false);
					back2Button.setVisible(false);
					startGame.setVisible(false);
					playerInstruction.setVisible(true);
					closeButton.setVisible(true);
					magicBoxButton.setVisible(true);
				}
			}
		});

		//���ذ�ť�¼�
		backButton.setVisible(false);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bgJLabel2.setVisible(false);
				backButton.setVisible(false);
				nextButton.setVisible(false);
				oneEasyButton.setVisible(false);
				oneHardButton.setVisible(false);
				twoEasyButton.setVisible(false);
				twoHardButton.setVisible(false);
				gameStartButton.setVisible(true);
			}
		});

		//next��ť�¼�
		nextButton.setVisible(false);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(modeNum != 0){
					role1all.setVisible(false);
					role2all.setVisible(false);
					role3all.setVisible(false);
					role4all.setVisible(false);
					bgJLabel2.setVisible(false);
					backButton.setVisible(false);
					nextButton.setVisible(false);
					oneEasyButton.setVisible(false);
					oneHardButton.setVisible(false);
					twoEasyButton.setVisible(false);
					twoHardButton.setVisible(false);
					name1.setVisible(true);
					name1.setText("�������ǳ�");
					role1Head.setVisible(true);
					role2Head.setVisible(true);
					role3Head.setVisible(true);
					role4Head.setVisible(true);
					back2Button.setVisible(true);
					//����ǵ���ģʽ
					if(modeNum == 1||modeNum == 2){
						startGame.setVisible(true);
						selCharButton.setVisible(true);
					}else {//˫��ģʽ
						next2Button.setVisible(true);
						player1.setVisible(true);
					}
					bgJLabel3.setVisible(true);
				}
			}
		});

		//���˼�ģʽ�¼�
		oneEasyButton.setVisible(false);
		oneEasyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeNum = 1;
				oneHardButton.setBorderPainted(false);
				twoEasyButton.setBorderPainted(false);
				twoHardButton.setBorderPainted(false);
				oneEasyButton.setBorderPainted(true);
			}
		});

		//��������ģʽ�¼�
		oneHardButton.setVisible(false);
		oneHardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeNum = 2;
				twoEasyButton.setBorderPainted(false);
				twoHardButton.setBorderPainted(false);
				oneEasyButton.setBorderPainted(false);
				oneHardButton.setBorderPainted(true);
			}
		});

		//˫�˼�ģʽ�¼�
		twoEasyButton.setVisible(false);
		twoEasyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeNum = 3;
				oneHardButton.setBorderPainted(false);
				twoHardButton.setBorderPainted(false);
				oneEasyButton.setBorderPainted(false);
				twoEasyButton.setBorderPainted(true);
			}
		});

		//˫������ģʽ
		twoHardButton.setVisible(false);
		twoHardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeNum = 4;
				oneHardButton.setBorderPainted(false);
				twoEasyButton.setBorderPainted(false);
				oneEasyButton.setBorderPainted(false);
				twoHardButton.setBorderPainted(true);
			}
		});

		//����2��ť�¼�
		back2Button.setVisible(false);
		back2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player1.setVisible(false);
				selCharButton.setVisible(false);
				bgJLabel3.setVisible(false);
				back2Button.setVisible(false);
				next2Button.setVisible(false);
				startGame.setVisible(false);
				role1Head.setVisible(false);
				role2Head.setVisible(false);
				role3Head.setVisible(false);
				role4Head.setVisible(false);
				role1all.setVisible(false);
				role2all.setVisible(false);
				role3all.setVisible(false);
				role4all.setVisible(false);
				name1.setVisible(false);
				switch (oneRoleNum){
					case 1:
						role1Head.setEnabled(true);
						break;
					case 2:
						role2Head.setEnabled(true);
						break;
					case 3:
						role3Head.setEnabled(true);
						break;
					case 4:
						role4Head.setEnabled(true);
						break;

				}
				oneEasyButton.setVisible(true);
				oneHardButton.setVisible(true);
				twoEasyButton.setVisible(true);
				twoHardButton.setVisible(true);
				nextButton.setVisible(true);
				backButton.setVisible(true);
				bgJLabel2.setVisible(true);
			}
		});
		//����2��ť�¼�
		next2Button.setVisible(false);
		next2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(oneRoleNum!=0){
					player1.setVisible(false);
					next2Button.setVisible(false);
					back2Button.setVisible(false);
					role1all.setVisible(false);
					role2all.setVisible(false);
					role3all.setVisible(false);
					role4all.setVisible(false);
					switch (oneRoleNum){
						case 1:
							role1Head.setEnabled(false);
							break;
						case 2:
							role2Head.setEnabled(false);
							break;
						case 3:
							role3Head.setEnabled(false);
							break;
						case 4:
							role4Head.setEnabled(false);
							break;

					}
					name1.setVisible(false);
					name2.setVisible(true);
					back3Button.setVisible(true);
					startGame.setVisible(true);
					player2.setVisible(true);
					player1Name = name1.getText();
				}
			}
		});
		//��ɫ1��ť�¼�
		role1Head.setVisible(false);
		role1Head.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				role2all.setVisible(false);
				role3all.setVisible(false);
				role4all.setVisible(false);
				role1all.setVisible(true);
				role2Head.setBorderPainted(false);
				role3Head.setBorderPainted(false);
				role4Head.setBorderPainted(false);
				role1Head.setBorderPainted(true);
				//�������Ϊ���һ���߽�ɫѡ��
				if(player1.isVisible()||selCharButton.isVisible()){
					oneRoleNum = 1;
				}else {//����Ϊ��Ҷ�

					twoRoleNum =1;

				}
			}
		});
		//��ɫ2��ť�¼�
		role2Head.setVisible(false);
		role2Head.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				role1all.setVisible(false);
				role3all.setVisible(false);
				role4all.setVisible(false);
				role2all.setVisible(true);
				role1Head.setBorderPainted(false);
				role3Head.setBorderPainted(false);
				role4Head.setBorderPainted(false);
				role2Head.setBorderPainted(true);
				if(player1.isVisible()||selCharButton.isVisible()){
					oneRoleNum =2;
				}else {
					twoRoleNum = 2;
				}
			}
		});
		//��ɫ3��ť�¼�
		role3Head.setVisible(false);
		role3Head.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				role1all.setVisible(false);
				role2all.setVisible(false);
				role4all.setVisible(false);
				role3all.setVisible(true);
				role1Head.setBorderPainted(false);
				role2Head.setBorderPainted(false);
				role4Head.setBorderPainted(false);
				role3Head.setBorderPainted(true);
				if(player1.isVisible()||selCharButton.isVisible()){
					oneRoleNum = 3;
				}else {
					twoRoleNum = 3;
				}
			}
		});

		//��ɫ4��ť�¼�
		role4Head.setVisible(false);
		role4Head.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				role1all.setVisible(false);
				role2all.setVisible(false);
				role3all.setVisible(false);
				role4all.setVisible(true);
				role1Head.setBorderPainted(false);
				role2Head.setBorderPainted(false);
				role3Head.setBorderPainted(false);
				role4Head.setBorderPainted(true);
				if(player1.isVisible()||selCharButton.isVisible()){
					oneRoleNum = 4;
				}else {
					twoRoleNum = 4;
				}
			}
		});
		//����3��ť,���ص����һ
		back3Button.setVisible(false);
		back3Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				role1all.setVisible(false);
				role2all.setVisible(false);
				role3all.setVisible(false);
				role4all.setVisible(false);
				player2.setVisible(false);
				back3Button.setVisible(false);
				startGame.setVisible(false);
				name2.setVisible(false);
				switch (oneRoleNum){
					case 1:
						role1Head.setEnabled(true);
						break;
					case 2:
						role2Head.setEnabled(true);
						break;
					case 3:
						role3Head.setEnabled(true);
						break;
					case 4:
						role4Head.setEnabled(true);
						break;

				}
				name1.setVisible(true);
				next2Button.setVisible(true);
				back2Button.setVisible(true);
				player1.setVisible(true);
			}
		});

		//��ʼ��Ϸ��ť
		startGame.setVisible(false);
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(oneRoleNum!=0){
					jLabel.setVisible(true);
					bgJLabel3.setVisible(false);
					player2.setVisible(false);
					back3Button.setVisible(false);
					startGame.setVisible(false);
					role1all.setVisible(false);
					role2all.setVisible(false);
					role3all.setVisible(false);
					role4all.setVisible(false);
					name1.setVisible(false);
					selCharButton.setVisible(false);
					back2Button.setVisible(false);
					name2.setVisible(false);
					role1Head.setVisible(false);
					role2Head.setVisible(false);
					role3Head.setVisible(false);
					role4Head.setVisible(false);
					gameStartButton.setVisible(true);
					if(modeNum == 1 ||modeNum ==2){//����ģʽ
						player1Name = name1.getText();
						GameController.setTwoPlayer(false);
						GameStart.startNewGame();
					}else if((modeNum == 3 ||modeNum ==4)&& twoRoleNum!=0){//˫��ģʽ
						player2Name = name2.getText();
						GameController.setTwoPlayer(true);
						GameStart.startNewGame();
					}
				}

			}
		});
		//��ӿؼ�
		this.add(gameStartButton);
		this.add(closeMusic);
		this.add(closeButton);
		this.add(magicBoxButton);
		this.add(caozuoButton);
		this.add(close2Button);
		this.add(instructionButton);
		this.add(backButton);
		this.add(nextButton);
		this.add(oneEasyButton);
		this.add(oneHardButton);
		this.add(twoEasyButton);
		this.add(twoHardButton);
		this.add(selCharButton);
		this.add(player1);
		this.add(player2);
		this.add(back2Button);
		this.add(next2Button);
		this.add(back3Button);
		this.add(startGame);
		this.add(role1Head);
		this.add(role1all);
		this.add(role2Head);
		this.add(role2all);
		this.add(role3Head);
		this.add(role3all);
		this.add(role4Head);
		this.add(role4all);
		this.add(name1);
		this.add(name2);
		this.add(playerInstruction);
		this.add(magicBoxInstruction);
		this.add(bgJLabel2);
		this.add(bgJLabel3);
		this.add(jLabel);
		this.setVisible(true);
		this.setOpaque(true);

	}


	/**
	 * �������水ť
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
	//��ȡ�û�ѡ���ģʽ
	public static int getModeNum(){
		return modeNum;
	}
	//��ȡ���һѡ��Ľ�ɫ����
	public static int getOneRoleNum(){
		return oneRoleNum;
	}
	//��ȡ��Ҷ�ѡ��Ľ�ɫ����
	public static int getTwoRoleNum(){
		return twoRoleNum;
	}
	//��ȡ���һ��д���ǳ�
	public static String getPlayer1Name(){
		return player1Name;
	}
	//��ȡ��Ҷ���д���ǳ�
	public static String getPlayer2Name(){
		return player2Name;
	}
}
