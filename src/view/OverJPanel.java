package view;

import controller.GameController;
import gameStart.GameStart;
import model.loader.ElementLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
/**
 * @className: OverJPanel
 * @descripton: 此类实现了游戏结算界面
 * @author: 
 * @createTime: 2022-12-29
 */
public class OverJPanel extends JPanel{
	//结束界面背景
	private ImageIcon img;
	//窗口宽度
	private int w;
	//窗口高度
	private int h;
	//角色1
	private static JButton role1 = createButton("img/player/end/1.png",720,370,103,103);
	//角色2
	private static JButton role2 = createButton("img/player/end/2.png",720,370,103,103);
	//角色3
	private static JButton role3 = createButton("img/player/end/3.png",720,370,103,103);
	//角色4
	private static JButton role4 = createButton("img/player/end/4.png",720,370,103,103);
	//npc
	private static JButton npc = createButton("img/player/end/5.png",720,370,103,103);

	//赢家文本框
	private static 	JTextField name = new JTextField();

	//构造函数
	public OverJPanel(){
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.img = ElementLoader.getElementLoader().getImageMap().get("gameOver");
		this.w = new Integer(data.get(0)).intValue();
		this.h = new Integer(data.get(1)).intValue();
		init();
	}

	private void init() {

		this.setLayout(null);

		//结束界面背景
		JLabel jLabel = new JLabel(img);
		img.setImage(img.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		jLabel.setBounds(0, 0, w, h);

		//结算框图片
		ImageIcon resultImg = new ImageIcon("img/bg/resultJLabel.png");
		resultImg.setImage(resultImg.getImage().getScaledInstance(650,713,Image.SCALE_DEFAULT));
		JLabel resultJLabel = new JLabel(resultImg);
		resultJLabel.setBounds(450,0,650,713);

		//角色1
		role1.setVisible(false);
		//角色2
		role2.setVisible(false);
		//角色3
		role3.setVisible(false);
		//角色4
		role4.setVisible(false);
		//npc
		npc.setVisible(false);

		//返回主界面
		JButton backMainJPanel = createButton("img/bg/backMainJPanel.png",500,650,225,73);
		//重新开始
		JButton reStart = createButton("img/bg/restart.png",820,650,196,73);


		//显示赢家的文本框
		name.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
		name.setFont(new Font("微软雅黑",Font.PLAIN,35));
		name.setForeground(new Color(65,105,225));
		name.setOpaque(false);
		//设置只读状态
		name.setEditable(false);
		name.setBounds(740,515,260,45);

		//返回主界面按钮事件
		backMainJPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultJLabel.setVisible(false);
				reStart.setVisible(false);
				backMainJPanel.setVisible(false);
				npc.setVisible(false);
				role1.setVisible(false);
				role2.setVisible(false);
				role3.setVisible(false);
				role4.setVisible(false);
				name.setVisible(false);
				GameStart.changeJPanel("begin");
			}
		});

		//重新开始事件
		reStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				npc.setVisible(false);
				role1.setVisible(false);
				role2.setVisible(false);
				role3.setVisible(false);
				role4.setVisible(false);
				GameStart.startNewGame();
			}
		});

		this.add(name);
		this.add(role1);
		this.add(role2);
		this.add(role3);
		this.add(role4);
		this.add(npc);
		this.add(backMainJPanel);
		this.add(reStart);
		this.add(resultJLabel);
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
	private  static JButton createButton(String file ,int x,int y,int w, int h){
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
	 * 显示赢家的头像和昵称。1代表玩家一；2代表玩家2；3代表npc
	 * @param winner
	 */
	public static void showWinner(int winner){
		//赢家是玩家一
		if(winner==1){
			switch (BeginJPanel.getOneRoleNum()){
				case 1:
					role1.setVisible(true);
					break;
				case 2:
					role2.setVisible(true);
					break;
				case 3:
					role3.setVisible(true);
					break;
				case 4:
					role4.setVisible(true);
					break;
			}
			name.setText(BeginJPanel.getPlayer1Name());
		}else if(winner==2){//赢家是玩家二
			switch (BeginJPanel.getTwoRoleNum()){
				case 1:
					role1.setVisible(true);
					break;
				case 2:
					role2.setVisible(true);
					break;
				case 3:
					role3.setVisible(true);
					break;
				case 4:
					role4.setVisible(true);
					break;
			}
			name.setText(BeginJPanel.getPlayer2Name());
		}else {
			npc.setVisible(true);
			name.setText("NPC");
		}
	}

}

