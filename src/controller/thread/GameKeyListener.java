package controller.thread;

import controller.GameController;
import model.element.Player;
import model.manager.ElementManager;
import model.manager.MoveTypeEnum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName: GameKeyListener
 * @Description: ���̼�������ʵ�ּ��̼������Կ���Player�������ƶ�
 * @author:Xiongting
 * @CreateDate:2022/12/27-2022/12/28
 */
public class GameKeyListener implements KeyListener{
	/**
	 * ��ջ�����������ͻ
	 * ÿ��ջ������Ų�ͬ�û��İ�����ͨ���жϰ�����code�������ƶ�������߹���
	 */
	private List<?> list;
	private Stack<Integer> p1PressStack=new Stack<>();
	private Stack<Integer> p2PressStack=new Stack<>();
	@Override
	public void keyPressed(KeyEvent e){
		list= ElementManager.getManager().getElementList("player");
		Player player1=(Player)list.get(0);
		int code=e.getKeyCode();
		switch (code){
			case 10://ը����
				if(player1.isKeepAttack())//������һֱ����ը������ÿ��ֻ�ܷ�һ��ը��
					player1.setAttack(false);
				else {
					player1.setKeepAttack(true);
					player1.setAttack(true);
				}
				break;
			case 37://��������
			case 38:
			case 39:
			case 40:
				if(!p1PressStack.contains(code)){
					p1PressStack.push(code);
				}
				player1.setMoveType(MoveTypeEnum.codeToMoveType(code));
				break;
			default://������������
				break;
		}
		if(GameController.isTwoPlayer()){
			Player player2=(Player)list.get(1);
			switch (code){
				case 32:
					if(player2.isKeepAttack())
						player2.setAttack(false);
					else {
						player2.setKeepAttack(true);
						player2.setAttack(true);
					}
					break;
				case 65:
				case 87:
				case 68:
				case 83:
					if(!p2PressStack.contains(code)){
						p2PressStack.push(code);
					}
					player2.setMoveType(MoveTypeEnum.codeToMoveType(code));
					break;
				default:
					break;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e){
		List<?> list=ElementManager.getManager().getElementList("player");
		int code=e.getKeyCode();
		Player player1=(Player)list.get(0);
		if(!player1.isDead()){
			switch (code){
				case 10:
					player1.setAttack(false);
					player1.setKeepAttack(false);
					break;
				case 37:
				case 38:
				case 39:
				case 40:
					if(p1PressStack.peek()!=code){
						p1PressStack.remove(new Integer(code));
					}else {
						p1PressStack.pop();
						if(p1PressStack.size()==0){
							player1.setMoveType(MoveTypeEnum.STOP);
						}else {
							player1.setMoveType(MoveTypeEnum.codeToMoveType(p1PressStack.peek()));
						}
					}
					break;
				default:
					break;
			}
		}
		if(GameController.isTwoPlayer()){
			Player player2=(Player)list.get(1);
			if(!player2.isDead()){
				switch (code){
					case 32:
						player2.setAttack(false);
						player2.setKeepAttack(false);
						break;
					case 65:
					case 87:
					case 68:
					case 83:
						if(p2PressStack.peek()!=code){
							player2.setMoveType(MoveTypeEnum.STOP);
						}else{
							p2PressStack.pop();
							if(p2PressStack.size()==0){
								player2.setMoveType(MoveTypeEnum.STOP);
							}else {
								player2.setMoveType(MoveTypeEnum.codeToMoveType(p2PressStack.peek()));
							}
						}
						break;
					default:
						break;
				}
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0){
		//TODO �Զ����ɵķ������
	}
	public void clearKeyStacks(){
		p1PressStack.clear();
		p2PressStack.clear();
	}
}
