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
 * @description： 游戏开始部分界面
 * @author： He Xiaofei
 * @CreateTime: 2022-12-27、2022-12-28
 */
public class BeginJPanel extends JPanel{
	//背景图
	private ImageIcon img;
	//界面宽度
	private int w;
	//界面高度
	private int h;
	//音乐设置按钮的点击次数
	private int clickNum = 0;
	//模式选择
	//1代表单人简单；2代表单人困难；3代表双人简单；4代表双人困难
	private static int modeNum = 0;
	//角色选择
	//1代表红人；2代表黄鸟；3代表红色瞌睡人；4代表橙人
	private static int oneRoleNum = 0;
	private static int twoRoleNum = 0;
	//玩家昵称
	private static String player1Name;
	private static String player2Name;

	//构造函数
	public BeginJPanel(){
		//data=windowSize
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.img = ElementLoader.getElementLoader().getImageMap().get("beginBackground");
		this.w = new Integer(data.get(0)).intValue();//读取windowSize中的宽度
		this.h = new Integer(data.get(1)).intValue();//读取windowSize中的长度
		mainJPanel();
	}
	//初始化
	private void mainJPanel() {
		//手工布局管理器
		this.setLayout(null);
		//背景图片标签
		JLabel jLabel = new JLabel(img);
		//设置图片为窗口尺寸
		img.setImage(img.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		//设置图片标签的x,y,w,h
		jLabel.setBounds(0, 0, w, h);

		//显示人物操作说明界面
		ImageIcon playerInstrImg = new ImageIcon("img/bg/playerInstruction.png");
		//设置图片及尺寸
		playerInstrImg.setImage(playerInstrImg.getImage().getScaledInstance(809, 537,Image.SCALE_DEFAULT ));
		JLabel playerInstruction = new JLabel(playerInstrImg);
		playerInstruction.setBounds(314, 151, 807, 566);
		playerInstruction.setVisible(false);

		//显示道具说明界面
		ImageIcon magicImg = new ImageIcon("img/bg/magicBoxInstruction.png");
		//设置图片和尺寸
		magicImg.setImage(magicImg.getImage().getScaledInstance(809,537,Image.SCALE_DEFAULT));
		JLabel magicBoxInstruction = new JLabel(magicImg);
		magicBoxInstruction.setBounds(314,151,807,566);
		//初始状态设置为不可见
		magicBoxInstruction.setVisible(false);

		//显示选择模式界面
		ImageIcon bgimg2 = new ImageIcon("img/bg/selectMode.png");
		//选择框标签
		JLabel bgJLabel2 = new JLabel(bgimg2);
		//设置图片尺寸
		bgimg2.setImage(bgimg2.getImage().getScaledInstance(687,533,Image.SCALE_DEFAULT));
		//设置图片标签尺寸
		bgJLabel2.setBounds(450,151,687,533);
		bgJLabel2.setVisible(false);

		//显示选择角色界面
		ImageIcon selCharacterImg = new ImageIcon("img/bg/selectCharacter.png");
		//选择框标签
		JLabel bgJLabel3 = new JLabel(selCharacterImg);
		//设置图片尺寸
		selCharacterImg.setImage(selCharacterImg.getImage().getScaledInstance(674,506,Image.SCALE_DEFAULT));
		//设置图片标签尺寸
		bgJLabel3.setBounds(450,151,674,506);
		bgJLabel3.setVisible(false);


		//开始按钮
		JButton gameStartButton = createButton("img/bg/gameStartButton.png",500,200,500,500);
		//音乐设置图标
		JButton closeMusic = createButton("img/bg/keepMusic.png",0,0,111,109);
		//关闭1按钮
		JButton closeButton = createButton("img/bg/closeButton.png",370,635,224,50 );
		//道具按钮
		JButton magicBoxButton = createButton("img/bg/magicBox.png",820,635,224,50);
		//关闭2按钮
		JButton close2Button = createButton("img/bg/closeButton.png",830,640,224,50 );
		//操作按钮
		JButton caozuoButton = createButton("img/bg/instruction.png",360,640,224,50);
		//操作说明图标
		JButton instructionButton = createButton("img/bg/instructionButton.png",1380,0,111,109);
		//返回按钮
		JButton backButton = createButton("img/bg/back.png",585,615,180,65);
		//继续按钮
		JButton nextButton = createButton("img/bg/next.png",815,615,180,65);
		//单人简单模式
		JButton oneEasyButton = createButton("img/bg/oneEasy.png",560,300,200,56);
		//单人困难模式
		JButton oneHardButton = createButton("img/bg/oneHard.png",840,295,200,66);
		//双人简单模式
		JButton twoEasyButton = createButton("img/bg/twoEasy.png",560,450,200,58);
		//双人困难模式
		JButton twoHardButton = createButton("img/bg/twoHard.png",840,450,200,59);
		//角色选择按钮
		JButton selCharButton = createButton("img/bg/character.png",700,165,192,41);
		selCharButton.setVisible(false);
		//玩家一按钮
		JButton player1 = createButton("img/bg/player1.png",700,165,148,41);
		player1.setVisible(false);
		//玩家二按钮
		JButton player2 = createButton("img/bg/player2.png",700,165,137,41);
		player2.setVisible(false);
		//返回2按钮
		JButton back2Button = createButton("img/bg/back.png",575,590,180,65);
		//继续2按钮
		JButton next2Button = createButton("img/bg/next.png",805,590,180,65);
		//返回按钮3
		JButton back3Button = createButton("img/bg/back.png",575,595,180,65);
		//开始游戏按钮
		JButton startGame = createButton("img/bg/startGame.png",805,595,180,55);
		//角色1头
		JButton role1Head = createButton("img/player/player1/2.png",550,280,94,71);
		//角色1全身
		JButton role1all = createButton("img/player/player1/1.png",860,255,192,239);
		role1all.setVisible(false);
		//角色2头
		JButton role2Head = createButton("img/player/player2/2.png",720,280,94,71);
		//角色2全身
		JButton role2all = createButton("img/player/player2/1.png",860,255,192,239);
		role2all.setVisible(false);
		//角色3头
		JButton role3Head = createButton("img/player/player3/2.png",550,385,94,71);
		//角色3全身
		JButton role3all = createButton("img/player/player3/1.png",860,255,192,239);
		role3all.setVisible(false);
		//角色4头
		JButton role4Head = createButton("img/player/player4/2.png",720,385,94,71);
		//角色4全身
		JButton role4all = createButton("img/player/player4/1.png",860,255,192,239);
		role4all.setVisible(false);

		//输入昵称文本框
		JTextField name1 = new JTextField("请输入昵称");
		name1.setVisible(false);
		name1.setBounds(600,485,180,55);
		//设置文本框边框粗细为0
		name1.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
		name1.setForeground(new Color(255,255,0));
		//设置文本框背景透明
		name1.setOpaque(false);
		//设置文本框字体
		name1.setFont(new Font("微软雅黑", Font.PLAIN,20));
		JTextField name2 = new JTextField("请输入昵称");
		name2.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
		name2.setBounds(600,485,180,55);
		name2.setFont(new Font("微软雅黑",Font.PLAIN,20));
		name2.setForeground(new Color(255,255,0));
		name2.setOpaque(false);
		name2.setVisible(false);

		//开始按钮事件
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

		//音乐设置图标事件
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

		//关闭1按钮事件
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
		//道具介绍按钮事件
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
		//关闭2按钮事件
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

		//玩家操作按钮事件
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

		//操作说明按钮事件
		instructionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//当界面显示模式选择或者角色选择的时候，操作说明按钮不做出响应
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

		//返回按钮事件
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

		//next按钮事件
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
					name1.setText("请输入昵称");
					role1Head.setVisible(true);
					role2Head.setVisible(true);
					role3Head.setVisible(true);
					role4Head.setVisible(true);
					back2Button.setVisible(true);
					//如果是单人模式
					if(modeNum == 1||modeNum == 2){
						startGame.setVisible(true);
						selCharButton.setVisible(true);
					}else {//双人模式
						next2Button.setVisible(true);
						player1.setVisible(true);
					}
					bgJLabel3.setVisible(true);
				}
			}
		});

		//单人简单模式事件
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

		//单人困难模式事件
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

		//双人简单模式事件
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

		//双人困难模式
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

		//返回2按钮事件
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
		//继续2按钮事件
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
		//角色1按钮事件
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
				//如果标题为玩家一或者角色选择
				if(player1.isVisible()||selCharButton.isVisible()){
					oneRoleNum = 1;
				}else {//标题为玩家二

					twoRoleNum =1;

				}
			}
		});
		//角色2按钮事件
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
		//角色3按钮事件
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

		//角色4按钮事件
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
		//返回3按钮,返回到玩家一
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

		//开始游戏按钮
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
					if(modeNum == 1 ||modeNum ==2){//单人模式
						player1Name = name1.getText();
						GameController.setTwoPlayer(false);
						GameStart.startNewGame();
					}else if((modeNum == 3 ||modeNum ==4)&& twoRoleNum!=0){//双人模式
						player2Name = name2.getText();
						GameController.setTwoPlayer(true);
						GameStart.startNewGame();
					}
				}

			}
		});
		//添加控件
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
	 * 创建界面按钮
	 * @param file  图片位置
	 * @param x  坐标x
	 * @param y  坐标y
	 * @param w  按钮宽度
	 * @param h  按钮高度
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
	//获取用户选择的模式
	public static int getModeNum(){
		return modeNum;
	}
	//获取玩家一选择的角色形象
	public static int getOneRoleNum(){
		return oneRoleNum;
	}
	//获取玩家二选择的角色形象
	public static int getTwoRoleNum(){
		return twoRoleNum;
	}
	//获取玩家一填写的昵称
	public static String getPlayer1Name(){
		return player1Name;
	}
	//获取玩家二填写的昵称
	public static String getPlayer2Name(){
		return player2Name;
	}
}
