import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javax.swing.*;

enum BlockType{
	SNAKE,LADDER,NORMAL
}


class Block{
	int x;
	int y;
	int position;
	BlockType type;	
	Block nextNode;
	Block preNode;

	Block(int x,int y,int position,Block next,Block pre,BlockType type){
		this.x=x;
		this.y=y;
		this.position=position;
		this.nextNode=next;
		this.preNode=pre;
		this.type=type;
	}

	Block readForward(){
		return this.nextNode;
	}

	Block readBackward(){
		return this.preNode;
	}

	public String toString(){
		return this.type+"=>"+position;
	}

}

class Player{
	Block currentBlock;
	String name;
	Player(String name,Block currentBlock){
		this.name=name;
		this.currentBlock=currentBlock;
	}
}

class GameBoard{

	Block gameBlock;
	Map<Integer,Block> gameBoard=new HashMap();
	Map<Integer,Block> gameBoardObjects=new HashMap();

	Map<Integer,Player> players=new HashMap();

	Random rand=new Random();

	GameBoard(String ...names){
		this.gameBlock=new Block(0,0,0,null,null,BlockType.NORMAL);
		int playerId=0;
		for (String name :names ) {
			players.put(playerId,new Player(name,this.gameBlock));
			playerId++;
		}

		int rows=10;
		int cols=10;
		int numberOfObstacles=15;

		Block tempBlock=this.gameBlock;
		for (int r=0;r<rows ;r++ ) {
			for (int c=0;c<cols ;c++ ) {
				int pos= (r*rows)+c;
				Block nextBlock= new Block(r,c,pos,null,
					null,BlockType.NORMAL);
				this.gameBoard.put(pos,tempBlock);
				tempBlock=nextBlock;
			}
		}

		//int lastActivePos=0;
		Boolean boardBuild=false;
		while(!boardBuild){

			int rI1=Math.abs(rand.nextInt()%cols);
			int rI2=Math.abs(rand.nextInt()%cols);
			int rJ1=Math.abs(rand.nextInt()%rows);
			int rJ2=Math.abs(rand.nextInt()%rows);



			int pos1 = (rJ1*rows)+rI1;
			int pos2 = (rJ2*rows)+rI2;

			Block object1=gameBoardObjects.get(pos1);
			Block object2=gameBoardObjects.get(pos2);
			if(rJ1!=rJ2&&pos1>0&&pos2>0&&pos1!=pos2&& object1==null&&object2==null){

				Block one=this.gameBoard.get(pos1);
				Block two=this.gameBoard.get(pos2);

				if(pos1>pos2){
					one.type=BlockType.SNAKE;
					two.type=BlockType.SNAKE;					
				}else if(pos1<pos2){
					one.type=BlockType.LADDER;
					two.type=BlockType.LADDER;
				}

				one.nextNode=two;
				this.gameBoardObjects.put(pos1,one);

			}

			if(this.gameBoardObjects.size()==(numberOfObstacles)){
				boardBuild=true;
			}


		}
		

		
		for (int position :this.gameBoardObjects.keySet() ) {
			Block block=this.gameBoardObjects.get(position);
			if(block.readForward()!=null){
				System.out.print(" One = "+block);
				System.out.println("\t Two = "+block.readForward());
			}
		}


	}


	int playerID=0;

	Boolean play(int diceValue){





		Player player=players.get(playerID);
		playerID++;
		if(playerID==players.size()){
			playerID=0;
		}

		System.out.print("Player "+player.name +" => "+diceValue +"\t");

		Block playerBlock=player.currentBlock;
		System.out.print(" Current Position =>"+playerBlock.position +" \t");

		if(playerBlock.position==0 && diceValue!=5){
			System.out.println("Need dice value 6 for play ");
			return true;
		}

		int playerPos=playerBlock.position;
		playerPos+=diceValue;

		Block gameObject=this.gameBoardObjects.get(playerPos);
		if(gameObject!=null){
			System.out.println("Object Hit "+gameObject);			
			playerPos=gameObject.readForward().position;
		}

		System.out.println("Next position "+playerPos);

		if(playerPos>=this.gameBoard.size()){
			System.out.println("Winner "+player.name);
			return false;
		}
		
		playerBlock = this.gameBoard.get(playerPos);
		player.currentBlock= playerBlock;
		players.put(playerID,player);
		System.out.println("=> "+playerBlock.position);

		

		return true;
	}

}


public class Main {
	static  int  diceValue(){
		return Math.abs(new Random().nextInt()%6);
	}
	public static void main(String[] args) {
		GameBoard board=new GameBoard("P1","P2");

		
		while (board.play(diceValue())) {
			//  Game Loop
		}



	}
}