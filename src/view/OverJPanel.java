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
 * @descripton: ����ʵ������Ϸ�������
 * @author: 
 * @createTime: 2022-12-29
 */
public class OverJPanel extends JPanel{
	//�������汳��
	private ImageIcon img;
	//���ڿ��
	private int w;
	//���ڸ߶�
	private int h;
	//��ɫ1
	private static JButton role1 = createButton("img/player/end/1.png",720,370,103,103);
	//��ɫ2
	private static JButton role2 = createButton("img/player/end/2.png",720,370,103,103);
	//��ɫ3
	private static JButton role3 = createButton("img/player/end/3.png",720,370,103,103);
	//��ɫ4
	private static JButton role4 = createButton("img/player/end/4.png",720,370,103,103);
	//npc
	private static JButton npc = createButton("img/player/end/5.png",720,370,103,103);

	//Ӯ���ı���
	private static 	JTextField name = new JTextField();

	//���캯��
	public OverJPanel(){
		List<String> data = ElementLoader.getElementLoader().getGameInfoMap().get("windowSize");
		this.img = ElementLoader.getElementLoader().getImageMap().get("gameOver");
		this.w = new Integer(data.get(0)).intValue();
		this.h = new Integer(data.get(1)).intValue();
		init();
	}

	private void init() {

		this.setLayout(null);

		//�������汳��
		JLabel jLabel = new JLabel(img);
		img.setImage(img.getImage().getScaledInstance(w, h,Image.SCALE_DEFAULT ));
		jLabel.setBounds(0, 0, w, h);

		//�����ͼƬ
		ImageIcon resultImg = new ImageIcon("img/bg/resultJLabel.png");
		resultImg.setImage(resultImg.getImage().getScaledInstance(650,713,Image.SCALE_DEFAULT));
		JLabel resultJLabel = new JLabel(resultImg);
		resultJLabel.setBounds(450,0,650,713);

		//��ɫ1
		role1.setVisible(false);
		//��ɫ2
		role2.setVisible(false);
		//��ɫ3
		role3.setVisible(false);
		//��ɫ4
		role4.setVisible(false);
		//npc
		npc.setVisible(false);

		//����������
		JButton backMainJPanel = createButton("img/bg/backMainJPanel.png",500,650,225,73);
		//���¿�ʼ
		JButton reStart = createButton("img/bg/restart.png",820,650,196,73);


		//��ʾӮ�ҵ��ı���
		name.setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
		name.setFont(new Font("΢���ź�",Font.PLAIN,35));
		name.setForeground(new Color(65,105,225));
		name.setOpaque(false);
		//����ֻ��״̬
		name.setEditable(false);
		name.setBounds(740,515,260,45);

		//���������水ť�¼�
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

		//���¿�ʼ�¼�
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
	 * �������水ť
	 * @param file  ͼƬλ��
	 * @param x  ����x
	 * @param y  ����y
	 * @param w  ��ť���
	 * @param h  ��ť�߶�
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
	 * ��ʾӮ�ҵ�ͷ����ǳơ�1�������һ��2�������2��3����npc
	 * @param winner
	 */
	public static void showWinner(int winner){
		//Ӯ�������һ
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
		}else if(winner==2){//Ӯ������Ҷ�
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

