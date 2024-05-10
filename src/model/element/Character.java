package model.element;
import java.awt.Graphics;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import model.manager.ElementManager;
import model.manager.MoveTypeEnum;
import view.GameJPanel;

/**
 * ��ɫ��
 * @ClassName: Character
 * @Description:Ϊ��Һ͵��Եĸ��࣬�����������Թ��е����Ժͷ���
 * @author: Jiaxiaoyang
 * @CreateDate: 2022��12��25�� ����6:27:20
 */
public class Character extends SuperElement{
	public final static int PLAYER_INIT_SPEED = 4; //��ҳ�ʼ�ƶ��ٶ�
	public final static int NPC_INIT_SPEED = 3; //NPC��ʼ�ƶ��ٶ�
	public final static int  TOP_SPEED = 9;//�����ٶ�����
	protected boolean dead;//��¼�Ƿ���
	protected MoveTypeEnum moveType;
	protected int speed;//�ƶ��ٶ�
	protected int speedItemCount;//��Ч�еļ��ٿ�����
	protected static int changeDirectionCount;//��Ч�еķ���ı俨����
	protected int stopitemCount;//��Ч�е��������ֹͣ������
	protected int bubblePower;//�ڵ�����
	protected int bubbleNum;//��¼����Ѿ����˶��ٸ�ը��
	protected int bubbleLargest;//��������ԷŶ��ٸ�ը������ʼֵΪ3
	public int score;//�÷�
	protected int heathPoint;//�������ֵ
	protected boolean isUnstoppable;//����Ƿ����޵�
	protected int unstoppableCount;//�޵п�����
	protected boolean isShowing;//�Ƿ�ҪչʾԪ��
	// ���Գ�ʼ��

	/**
	 *
	 * @param x Ԫ������x
	 * @param y Ԫ������y
	 * @param w Ԫ�س���
	 * @param h Ԫ�ظ߶�
	 */
	public Character(int x, int y, int w, int h,String character) {
		super(x, y, w, h);
		moveType = MoveTypeEnum.STOP;
		speedItemCount = 0;
		changeDirectionCount=0;
		stopitemCount=0;
		bubblePower = 1;
		bubbleNum = 0;
		bubbleLargest = 1;
		heathPoint = 4;
		isUnstoppable = false;
		unstoppableCount = 0;
		isShowing = true;
		score = 0;
		dead = false;
		if(character.equals("player")){
			speed = PLAYER_INIT_SPEED;
		}
		else if(character.equals("npc")){
			speed = NPC_INIT_SPEED;
		}
	}
	/**
	 * ͨ�����߸ı��ɫ����ֵ
	 * @param change �ı���ֵ
	 */
	public void setHealthPoint(int change) {
		if(change<0)
		{
			if(isUnstoppable)return;
			setUnstoppable(3);//����ֵ����ʱ���޵�һ��ʱ��
		}
		heathPoint += change;
//		����ֵΪ0������
		if(heathPoint<=0)
		{
			setDead(true);
			setX(-100);
			setY(-100);
		}
	}
	/**
	 * ���ý�ɫ����ֵ
	 * @param  init ��ɫ����ֵ
	 */
	public void initHealthPoint(int init){
		heathPoint = init;
	}

	/**
	 * �ı�һ��ʱ����ƶ��ٶ�,�����ٶ���Ҫ�����ı����ͳ�����ʱ�䣨�룩
	 * @param times �ı��ٶȵı���
	 * @param lastTime ����ʱ��
	 */
	public void changeSpeed(double times,int lastTime, int characterIndex) {
		if(speed != 9){
			speed = (int)(speed*times);
			Timer timer = new Timer(true);
			speedItemCount++;
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					speedItemCount--;
					System.out.println(speedItemCount);
					if(speedItemCount==0) {
						speed = PLAYER_INIT_SPEED;
						GameJPanel.setPlayer("speed", speed, characterIndex);
					}
				}
			};
			timer.schedule(task,lastTime*1000);
		}
		else{
			return ;
		}

	}

	/**
	 * �ı�һ��ʱ��ķ��򣬷����෴
	 * @param lastTime ����ʱ��
	 */
	public void changeDirection(int lastTime) {
		speed = -speed;
		Timer timer = new Timer(true);
		changeDirectionCount++;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				changeDirectionCount--;
				if(changeDirectionCount==0) {
					speed = PLAYER_INIT_SPEED;

				}
			}
		};
		timer.schedule(task,lastTime*1000);
	}

	/**
	 * ʹ������Ҿ�ֹһ��ʱ��
	 * @param lastTime ����ʱ��
	 */
	public void setOtherStop(int lastTime) {
		//�����Լ���������
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		for (int i = 0; i < playerList.size(); i++) {
			Player player = (Player) playerList.get(i);
			if(player != this)
			{
				player.setSpeed(0);
				setSpeedToInital(lastTime,player);
			}
		}
		List<SuperElement> NPCList = ElementManager.getManager().getElementList("npc");
		for (int i = 0; i < NPCList.size(); i++) {
			Npc npc = (Npc) NPCList.get(i);
			if(npc!=this)
			{
				npc.setSpeed(0);
				setSpeedToInital(lastTime,npc);
			}

		}
	}

	/**
	 * һ��ʱ���ָ���ʼ�ٶ�
	 * @param lastTime ����ʱ��
	 * @param character �������õõ���Ч������
	 */
	public void setSpeedToInital(int lastTime,final Character character)
	{
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				character.stopitemCount = character.stopitemCount-1;
				if(speedItemCount==0) {
					if(character.equals("player")){
						character.speed = PLAYER_INIT_SPEED;
					}
					else if(character.equals("npc")){
						character.speed = NPC_INIT_SPEED;
					}

				}
			}
		};
		timer.schedule(task,lastTime*1000);
	}

	/**
	 * �޵�һ��ʱ��
	 * @param lastTime ����ʱ��
	 */
	public void setUnstoppable(int lastTime)
	{
		isUnstoppable = true;
		unstoppableCount++;
		unstoppableChangeImg(lastTime);
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				unstoppableCount--;
				if(unstoppableCount==0) isUnstoppable = false;
			}
		};
		timer.schedule(task,lastTime*1000);
	}
	//	ͼƬ��˸
	public void unstoppableChangeImg(final int lastTime) {
		Timer timer = new Timer();
		final int times = lastTime*1000/100;//����
		TimerTask task1 = new TimerTask() {// ͼƬ��ʧ
			int count = 0;
			@Override
			public void run() {
				isShowing = false;
				count++;
				if(count == times/5) {
					isShowing = true;//����Ϊ������ʾ
					this.cancel();
				}
			}
		};
		TimerTask task2 =  new TimerTask() {//ͼƬ����
			int count = 0;
			@Override
			public void run() {
				isShowing = true;
				count++;
				if(count == times) this.cancel();
			}
		};
		timer.scheduleAtFixedRate(task1, 0, 500);//0�ӳ٣�ÿ500ms����һ��
		timer.scheduleAtFixedRate(task2, 0, 100);//0�ӳ٣�ÿ100ms����һ��
	}

	public void bubbleAddPower() {
		bubblePower++;
	}
	@Override
	public void showElement(Graphics g) {}

	@Override
	public void move() {}

	@Override
	public void destroy() {}



	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public MoveTypeEnum getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveTypeEnum moveType) {
		this.moveType = moveType;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeedItemCount() {
		return speedItemCount;
	}

	public void setSpeedItemCount(int speedItemCount) {
		this.speedItemCount = speedItemCount;
	}

	public int getBubblePower() {
		return bubblePower;
	}

	public void setBubblePower(int bubblePower) {
		this.bubblePower = bubblePower;
	}

	public int getBubbleNum() {
		return bubbleNum;
	}

	public void setBubbleNum(int bubbleNum) {
		this.bubbleNum = bubbleNum;
	}

	public int getBubbleLargest() {
		return bubbleLargest;
	}

	public void setBubbleLargest(int bubbleLargest) {
		this.bubbleLargest = bubbleLargest;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}



	public static int getChangeDirectionCount() {
		return changeDirectionCount;
	}

	public void setChangeDirectionCount(int changeDirectionCount) {
		this.changeDirectionCount = changeDirectionCount;
	}

	public int getStopitemCount() {
		return stopitemCount;
	}

	public void setStopitemCount(int stopitemCount) {
		this.stopitemCount = stopitemCount;
	}

	public int getHeathPoint() {
		return heathPoint;
	}

	public boolean isisUnstoppable() {
		return isUnstoppable;
	}

	public void setisUnstoppable(boolean unstoppable) {
		this.isUnstoppable = unstoppable;
	}
	/**
	 *toStirng ������д
	 */
	@Override
	public String toString() {
		return "Character{" +
				"dead=" + dead +
				", moveType=" + moveType +
				", speed=" + speed +
				", speedItemCount=" + speedItemCount +
				", changeDirectionCount=" + changeDirectionCount +
				", stopitemCount=" + stopitemCount +
				", bubblePower=" + bubblePower +
				", bubbleNum=" + bubbleNum +
				", bubbleLargest=" + bubbleLargest +
				", score=" + score +
				", heathPoint=" + heathPoint +
				", isUnstoppable=" + isUnstoppable +
				", unstoppableCount=" + unstoppableCount +
				", isShowing=" + isShowing +
				'}';
	}
}
