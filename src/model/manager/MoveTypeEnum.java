package model.manager;
/*
 *@ClassName: manager.MoveTypeEnum
 * @Description:该类将游戏所需操作键的ASCII码转换为人物移动的方向
 * @author:He Xiaofei
 * @Time:2022-12-26
 */

public enum MoveTypeEnum {
	LEFT,TOP,RIGHT,DOWN,STOP;
	public static MoveTypeEnum codeToMoveType(int code){
		switch (code){
			case 37:  //VK_LEFT
			case 65:  //A
				return MoveTypeEnum.LEFT;
			case 38:  //VK_UP
			case 87:  //W
				return MoveTypeEnum.TOP;
			case 39:  //VK_RIGHT
			case 68:  //D
				return MoveTypeEnum.RIGHT;
			case 40:  //VK_DOWN
			case 83:  //S
				return MoveTypeEnum.DOWN;
			//当玩家输入非操作键的时候，人物保持静止
			default:
				return MoveTypeEnum.STOP;
		}
	}
}
