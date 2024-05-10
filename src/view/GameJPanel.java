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
 * 游戏面板
 * @author Zhao Wenjie
 * 窗体容器：画板类
 */

public class GameJPanel extends JPanel implements Runnable{

	//游戏暂停/开始图标点击次数
	private int clickNum;

	//背景图
	private ImageIcon background;

	//玩家形象
	//玩家1形象
	private static ImageIcon player1Img;
	//玩家2形象
	private static ImageIcon player2Img;

	//npc形象
	//npc1形象
	private static ImageIcon npc1Img;
	//npc2形象
	private static ImageIcon npc2Img;
	//npc3形象
	private static ImageIcon npc3Img;

	//界面宽度
	private int w;
	//界面高度
	private int h;

	//玩家1图片
	private static JLabel player1JLabel;
	//玩家1生命值
	private static JLabel health1JLabel;
	//玩家1速度
	private static JLabel speed1JLabel;
	//玩家1泡泡数
	private static JLabel num1JLabel;
	//泡泡威力
	private static JLabel power1JLabel;
	//玩家1分数
	private static JLabel score1JLabel;

	//玩家2图片
	private static JLabel player2JLabel;
	//玩家2生命值
	private static JLabel health2JLabel;
	//玩家2速度
	private static JLabel speed2JLabel;
	//玩家2泡泡数
	private static JLabel num2JLabel;
	//泡泡威力
	private static JLabel power2JLabel;
	//玩家2分数
	private static JLabel score2JLabel;

	//npc1
	private static JLabel npc1JLabel;
	//npc1生命值
	private static JLabel npc1Health;

	//npc2
	private static JLabel npc2JLabel;
	//npc2生命值
	private static JLabel npc2Health;

	//npc3
	private static JLabel npc3JLabel;
	//npc3生命值
	private static JLabel npc3Health;

	//游戏暂停/继续按钮
	private static JButton runGame;

	/**
	 * 构造函数
	 */
	public GameJPanel(){

		//data=windowSize
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.background = new ImageIcon("img/bg/background.png");
		this.w = new Integer(data.get(0));//读取windowSize中的宽度
		this.h = new Integer(data.get(1));//读取windowSize中的长度
		initJPanel();
	}

	/**
	 * 显示画板内容，绘画
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
				//休眠20ms，1s刷新50次
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.repaint(); //每隔100毫秒刷新画板
		}
	}

	/**
	 * 展示元素管理器中所有的元素
	 */
	public void gameRuntime(Graphics g) {
		Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
		//元素显示优先级处理
		Set<String> set = map.keySet();
		Set<String> sortSet = new TreeSet<String>(ElementManager.getManager().getMapKeyComparator());
		sortSet.addAll(set);
		for(String key:sortSet) {
			List<SuperElement> list = map.get(key);
			for(int i=0; i<list.size(); i++) {
				//调用每个类自己的show方法完成显示
				list.get(i).showElement(g);
			}
		}

		g.setFont(new Font("微软雅黑", Font.BOLD, 24));
		//得到总允许时间
		int allTime = GameThread.getAllTime()/1000;
		//化为分、秒的形式
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
		//显示时间
		g.drawString("剩余时间: ", 30, 40);
		g.drawString( m + ":" + s, 40, 70);

		//根据模式修改npc属性
		if(BeginJPanel.getModeNum() == 1){
			//单人简单模式
			//根据有时间修改npc属性
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
			//单人困难模式
			//根据时间修改npc属性
			if(allTime <= 480 && allTime >= 360){
				System.out.println("修改属性");
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
			//双人简单模式
			//根据有时间修改npc属性
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
			//双人困难模式
			//根据时间修改npc属性
			if(allTime <= 480 && allTime >= 360){
				System.out.println("修改属性");
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

		//手工布局管理器
		this.setLayout(null);
		//背景图片标签
		JLabel jLabel = new JLabel(background);
		//设置背景图片为窗口尺寸
		background.setImage(background.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		//设置背景图片标签的x,y,w,h
		jLabel.setBounds(0, 0, w, h);

		//显示人物属性
		//判断游戏模式
		if(BeginJPanel.getModeNum() == 1 || BeginJPanel.getModeNum() == 2){
			//玩家选择的游戏人物形象
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

			//显示玩家1图片
			//设置玩家1图片的位置、尺寸
			player1Img.setImage(player1Img.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			player1JLabel = new JLabel(player1Img);
			player1JLabel.setBounds(10, 70, 150, 150);
			this.add(player1JLabel);

			//显示玩家1属性
			//生命值
			health1JLabel = new JLabel();
			health1JLabel.setText("生命值：4");
			health1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			health1JLabel.setForeground(new Color(0, 0, 0));
			health1JLabel.setBounds(30, 185, 150, 100);
			this.add(health1JLabel);

			//速度
			speed1JLabel = new JLabel();
			speed1JLabel.setText("速度：4");
			speed1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			speed1JLabel.setForeground(Color.BLACK);
			speed1JLabel.setBounds(30, 225, 150, 100);
			this.add(speed1JLabel);

			//泡泡数
			num1JLabel = new JLabel();
			num1JLabel.setText("泡泡数：1");
			num1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			num1JLabel.setForeground(Color.BLACK);
			num1JLabel.setBounds(30, 265, 150 ,100);
			this.add(num1JLabel);

			//威力
			power1JLabel =new JLabel();
			power1JLabel.setText("威力：1");
			power1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			power1JLabel.setForeground(Color.BLACK);
			power1JLabel.setBounds(30 ,305, 150, 100);
			this.add(power1JLabel);

			//分数
			score1JLabel = new JLabel();
			score1JLabel.setText("得分：0");
			score1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			score1JLabel.setForeground(Color.BLACK);
			score1JLabel.setBounds(30 ,345, 150, 100);
			this.add(score1JLabel);

			//显示npc属性
			//显示npc1图片
			npc1Img = new ImageIcon("img/computer/1/down.png");
			npc1Img.setImage(npc1Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc1JLabel = new JLabel(npc1Img);
			npc1JLabel.setBounds(1350, 50, 80, 80);
			this.add(npc1JLabel);

			//显示npc1生命值
			npc1Health = new JLabel();
			npc1Health.setFont(new Font("微软雅黑", Font.BOLD, 20));
			npc1Health.setForeground(Color.BLACK);
			npc1Health.setBounds(1350, 115, 150, 100);
			this.add(npc1Health);

			//显示npc2图片
			npc2Img = new ImageIcon("img/computer/2/down.png");
			npc2Img.setImage(npc2Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc2JLabel = new JLabel(npc2Img);
			npc2JLabel.setBounds(1350, 195, 80, 80);
			this.add(npc2JLabel);

			//显示npc2生命值
			npc2Health = new JLabel();
			npc2Health.setFont(new Font("微软雅黑", Font.BOLD, 20));
			npc2Health.setForeground(Color.BLACK);
			npc2Health.setBounds(1350, 260, 150, 100);
			this.add(npc2Health);

			//显示npc3图片
			npc3Img = new ImageIcon("img/computer/3/down.png");
			npc3Img.setImage(npc3Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc3JLabel = new JLabel(npc3Img);
			npc3JLabel.setBounds(1350, 340, 80, 80);
			this.add(npc3JLabel);

			//显示npc3生命值
			npc3Health = new JLabel();
			npc3Health.setFont(new Font("微软雅黑", Font.BOLD, 20));
			npc3Health.setForeground(Color.BLACK);
			npc3Health.setBounds(1350, 405, 150, 100);
			this.add(npc3Health);
		} else if(BeginJPanel.getModeNum() == 3 || BeginJPanel.getModeNum() == 4){

			//玩家1选择的游戏人物形象
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

			//玩家2选择的游戏人物形象
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

			//显示玩家1图片
			//设置玩家1图片的位置、尺寸
			player1Img.setImage(player1Img.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			player1JLabel = new JLabel(player1Img);
			player1JLabel.setBounds(10, 70, 150, 150);
			this.add(player1JLabel);

			//显示玩家1属性
			//生命值
			health1JLabel = new JLabel();
			health1JLabel.setText("生命值：4" );
			health1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			health1JLabel.setForeground(new Color(0, 0, 0));
			health1JLabel.setBounds(30, 185, 150, 100);
			this.add(health1JLabel);

			//速度
			speed1JLabel = new JLabel();
			speed1JLabel.setText("速度：4");
			speed1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			speed1JLabel.setForeground(Color.BLACK);
			speed1JLabel.setBounds(30, 225, 150, 100);
			this.add(speed1JLabel);

			//泡泡数
			num1JLabel = new JLabel();
			num1JLabel.setText("泡泡数：1");
			num1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			num1JLabel.setForeground(Color.BLACK);
			num1JLabel.setBounds(30, 265, 150 ,100);
			this.add(num1JLabel);

			//威力
			power1JLabel =new JLabel();
			power1JLabel.setText("威力：1");
			power1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			power1JLabel.setForeground(Color.BLACK);
			power1JLabel.setBounds(30 ,305, 150, 100);
			this.add(power1JLabel);

			//分数
			score1JLabel = new JLabel();
			score1JLabel.setText("分数：0");
			score1JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			score1JLabel.setForeground(Color.BLACK);
			score1JLabel.setBounds(30 ,345, 150, 100);
			this.add(score1JLabel);

			//显示玩家2图片
			//设置玩家2图片的位置、尺寸
			player2Img.setImage(player2Img.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			player2JLabel = new JLabel(player2Img);
			player2JLabel.setBounds(10, 440, 150, 150);
			this.add(player2JLabel);

			//显示玩家2属性
			//生命值
			health2JLabel = new JLabel();
			health2JLabel.setText("生命值：4" );
			health2JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			health2JLabel.setForeground(new Color(0, 0, 0));
			health2JLabel.setBounds(30, 555, 150, 100);
			this.add(health2JLabel);

			//速度
			speed2JLabel = new JLabel();
			speed2JLabel.setText("速度：4");
			speed2JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			speed2JLabel.setForeground(Color.BLACK);
			speed2JLabel.setBounds(30, 595, 150, 100);
			this.add(speed2JLabel);

			//泡泡数
			num2JLabel = new JLabel();
			num2JLabel.setText("泡泡数：1");
			num2JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			num2JLabel.setForeground(Color.BLACK);
			num2JLabel.setBounds(30, 635, 150 ,100);
			this.add(num2JLabel);

			//威力
			power2JLabel =new JLabel();
			power2JLabel.setText("威力：1");
			power2JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			power2JLabel.setForeground(Color.BLACK);
			power2JLabel.setBounds(30 ,675, 150, 100);
			this.add(power2JLabel);

			//分数
			//分数
			score2JLabel = new JLabel();
			score2JLabel.setText("分数：0");
			score2JLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
			score2JLabel.setForeground(Color.BLACK);
			score2JLabel.setBounds(30 ,715, 150, 100);
			this.add(score2JLabel);

			//显示npc属性
			//显示npc1图片
			npc1Img = new ImageIcon("img/computer/1/down.png");
			npc1Img.setImage(npc1Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc1JLabel = new JLabel(npc1Img);
			npc1JLabel.setBounds(1350, 50, 80, 80);
			this.add(npc1JLabel);

			//显示npc1生命值
			npc1Health = new JLabel();
			npc1Health.setFont(new Font("微软雅黑", Font.BOLD, 20));
			npc1Health.setForeground(Color.BLACK);
			npc1Health.setBounds(1350, 115, 150, 100);
			this.add(npc1Health);

			//显示npc2图片
			npc2Img = new ImageIcon("img/computer/2/down.png");
			npc2Img.setImage(npc2Img.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT));
			npc2JLabel = new JLabel(npc2Img);
			npc2JLabel.setBounds(1350, 195, 80, 80);
			this.add(npc2JLabel);

			//显示npc2生命值
			npc2Health = new JLabel();
			npc2Health.setFont(new Font("微软雅黑", Font.BOLD, 20));
			npc2Health.setForeground(Color.BLACK);
			npc2Health.setBounds(1350, 260, 150, 100);
			this.add(npc2Health);

		}

		//根据游戏模式设置npc生命值
		switch (BeginJPanel.getModeNum()){
			case 1:
				npc1Health.setText("生命值：3");
				npc2Health.setText("生命值：3");
				npc3Health.setText("生命值：3");
				break;
			case 2:
				npc1Health.setText("生命值：4");
				npc2Health.setText("生命值：4");
				npc3Health.setText("生命值：4");
				break;
			case 3:
				npc1Health.setText("生命值：5");
				npc2Health.setText("生命值：5");
				break;
			case 4:
				npc1Health.setText("生命值：6");
				npc2Health.setText("生命值：6");
				break;
		}

		this.setFocusable(true);

		//游戏进行
		runGame = createButton("img/other/pause.png", 1340, 500, 100, 100);
		runGame.setVisible(true);
		this.add(runGame);

		//游戏暂停事件
		runGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				continueGame();
			}
		});

		//重新开始
		JButton restartGame = createButton("img/other/restart.png", 1340, 600, 100, 100);
		restartGame.setVisible(true);
		this.add(restartGame);

		//重新开始事件
		restartGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStart.restartGame();
			}
		});

		//返回游戏主界面
		JButton backToMain = createButton("img/other/backToMain.png", 1340, 700, 100, 100);
		backToMain.setVisible(true);
		this.add(backToMain);

		//返回主界面按钮事件
		backToMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameMap.clearMapALL();
				new GameFrame();
			}
		});

		//添加控件
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
	 * 创建界面按钮
	 *
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

	/**
	 * 角色死亡后图片变灰
	 *
	 * @param label 需变更的角色
	 * @param img 需变更的图片
	 * @param file 灰色图片文件
	 * @param x 图片X坐标
	 * @param y 图片Y坐标
	 * @param w 图片宽度
	 * @param h 图片高度
	 */
	private static void changeImg(JLabel label, ImageIcon img, String file, int x, int y, int w, int h){

		img = new ImageIcon(file);
		img.setImage(img.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
		label.setIcon(img);

	}

	/**
	 * 修改玩家属性值
	 *
	 * @param attribute 修改的属性类型
	 * @param i 碰到道具后的属性值
	 * @param characterIndex 玩家序号
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
					health1JLabel.setText("生命值：" + i);
				} else if(characterIndex == 1){
					if (i == 0){
						changeImg(player2JLabel, player2Img, "img/player/player" + BeginJPanel.getTwoRoleNum() + "/Gray.png", 10, 70, 150, 150);
						health2JLabel.setForeground(Color.GRAY);
						power2JLabel.setForeground(Color.GRAY);
						num2JLabel.setForeground(Color.GRAY);
						score2JLabel.setForeground(Color.GRAY);
						speed2JLabel.setForeground(Color.GRAY);
					}
					health2JLabel.setText("生命值：" + i);
				}
				break;
			case "speed":
				if(characterIndex == 0){
					speed1JLabel.setText("速度：" + i);
				} else if(characterIndex == 1){
					speed2JLabel.setText("速度：" + i);
				}
				break;
			case "power":
				if(characterIndex == 0){
					power1JLabel.setText("威力：" + i);
				} else if(characterIndex == 1){
					power2JLabel.setText("威力：" + i);
				}
				break;
			case "num":
				if(characterIndex == 0){
					num1JLabel.setText("泡泡数：" + i);
				} else if(characterIndex == 1){
					num2JLabel.setText("泡泡数：" + i);
				}
				break;
			case "score":
				if(characterIndex == 0){
					score1JLabel.setText("得分：" + i);
				} else if(characterIndex == 1){
					score2JLabel.setText("得分：" + i);
				}
				break;
		}
	}

	/**
	 * 修改npc的属性
	 *
	 * @param i 碰到道具后的属性值
	 * @param characterIndex npc序号
	 */
	public static void setNpc(int i, int characterIndex){
		switch (characterIndex){
			case 2:
				if(i == 0){
					changeImg(npc1JLabel, npc1Img, "img/computer/1/Gray.png", 1350, 50, 80, 80);
					npc1Health.setForeground(Color.GRAY);
					npc1Health.setText("npc死亡");
				} else {
					npc1Health.setText("生命值：" + i);
				}
				break;
			case 3:
				if(i == 0){
					changeImg(npc2JLabel, npc2Img, "img/computer/2/Gray.png", 1350, 260, 80, 80);
					npc2Health.setForeground(Color.GRAY);
					npc2Health.setText("npc死亡");
				} else {
					npc2Health.setText("生命值：" + i);
				}
				break;
			case 4:
				if(i == 0){
					changeImg(npc3JLabel, npc3Img, "img/computer/3/Gray.png", 1350, 260, 80, 80);
					npc3Health.setForeground(Color.GRAY);
					npc3Health.setText("npc死亡");
				} else {
					npc3Health.setText("生命值：" + i);
				}
				break;
		}
	}

	/**
	 * 修改npc属性，增加游戏趣味性
	 *
	 * @param i 待修改的npc序号
	 * @param attribute 修改的属性值
	 */
	public void changeNpcAttribute(int i, int attribute){

		((Character) GameThread.getNpcList().get(i)).setBubblePower(attribute);
		((Character) GameThread.getNpcList().get(i)).setBubbleLargest(attribute);
	}
}
