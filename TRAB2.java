import java.util.*;
import java.lang.*;
import java.io.IOException;




class Node{
	
	private char[][] game;
	private int column;
	private int value;
	private int profundidade;
	
	Node(char[][] game, int column, int value){
		this.game = game;
		this.column = column;
		this.value = value;
	} 

	public char[][] getJogo(){
		return game;
	}

	public int getColumn(){
		return column;
	}

	public int getValue(){
		return value;
	}
}


public class TRAB2 {
	static long startTime;
	static long endTime;

	static int limite = 4;
	static int numgerados = 0;

	static LinkedList<Node> makeSuccessors(char[][] jogo, char piece, int on){
		LinkedList<Node> filhos = new LinkedList<Node>();

		for(int i = 0; i < 7; i++){
			if(jogo[0][i] ==  '-'){
				char[][] temp = new char[6][7];
				for(int j = 0; j < 6; j++){
					for(int z = 0; z < 7; z++){
						temp[j][z] = jogo[j][z];
					}
				}

				//System.out.println("ewrwgwr");
				for(int j = 5; j >= 0; j--){
					if(temp[j][i] == '-'){
						temp[j][i] = piece;
						break;
					}
				}
				if(on == 1)
					numgerados++;


				filhos.add(new Node(temp, i, utility(temp, checkWin(temp))));
			}
		}

		return filhos;
	}


	//MINIMAX

	static int mini_Max(char[][] jogo, int count){
		int countfor = count;
		// int v = max_Value(jogo, 0);
		startTime = System.nanoTime();
		int v = Integer.MIN_VALUE;

		int column=6;
		//System.out.println("V = " + v);
		Node save = new Node(jogo,0,0);

		for(Node s : makeSuccessors(jogo, 'O', 1)){
			//System.out.println("VALUE = " + s.getValue());
			// if(s.getValue() == v)
			// 	column = s.getColumn();
			int check = min_Value(s.getJogo(), countfor + 1);

			if(check > v) {
				//System.out.println("boas");
				v = check;
				if(v==512)
					return s.getColumn();
				column = s.getColumn();
				save = s;
				
			}

			// v = Math.min(v, min_Value(s.getJogo(), 0));
			

		}

		// System.out.println("-------------");
		// utility_Value(save.getJogo());
		// System.out.println("Utility: " + utility_Value(save.getJogo()));
		// System.out.println("Diagonal: "+ diagonal_Check(0, save.getJogo()));
		// System.out.println("Coluna: " + save.getColumn());
		// System.out.println("-------------");
		return column;

	}

	static int max_Value(char[][] jogo, int count){
		int countfor = count;

		//System.out.println("COUNTTTTT = " + countfor);
		if(terminal_Test(jogo)){
			//System.out.println("Ganhoumax: " + utility(jogo, checkWin(jogo)));
			return utility(jogo, checkWin(jogo));
		}

		if(countfor==limite) {
			//System.out.println("UTILITY = " + utility_Value(jogo));
			//System.out.println("É RETORNADO maxvalue = " +  utility_Value(jogo));
			return utility_Value(jogo);
		}


		int v = Integer.MIN_VALUE;


		for(Node s : /*list*/makeSuccessors(jogo, 'O', 1)){
			v = Math.max(v, min_Value(s.getJogo(),  countfor+1));
			//printBoard(s.getJogo());
		}
		//System.out.println("É RETORNADO maxvalue = " + v);

		return v;
	}


	static int min_Value(char[][] jogo, int count){
		int countfor=count;
		
		
		if(terminal_Test(jogo)){
			return utility(jogo, checkWin(jogo));
		}

		if(countfor==limite){
			return utility_Value(jogo);
		}
		int v = Integer.MAX_VALUE;


		for(Node s : /*list*/makeSuccessors(jogo, 'X', 1)){
			v = Math.min(v, max_Value(s.getJogo(), countfor+1));
		}
		return v;

	}

	//ALPHABET

	static int alpha_Beta(char[][] jogo, int count){
		startTime = System.nanoTime();
		int countfor = count;
		int v = Integer.MIN_VALUE;

		int column=6;
		Node save = new Node(jogo,0,0);

		for(Node s : makeSuccessors(jogo, 'O', 1)){
			int check = min_Value(s.getJogo(), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

			if(check > v) {
				v = check;
				if(v==512)
					return s.getColumn();
				column = s.getColumn();
				save = s;
				
			}
			
		}

		return column;
	}

	static int max_Value(char[][] jogo, int alfa, int beta, int count){
		int countfor = count;

		
		if(terminal_Test(jogo)){
			return utility(jogo, checkWin(jogo));
		}

		if(countfor==limite) {
			return utility_Value(jogo);
		}
		int v = Integer.MIN_VALUE;

		for(Node s : makeSuccessors(jogo, 'O', 1)){
			v = Math.max(v, min_Value(s.getJogo(), alfa, beta, countfor + 1));
			if(v >= beta){
				return v;
			}

			alfa = Math.max(alfa, v);
		}


		
		return v;
	}

	static int min_Value(char[][] jogo, int alfa, int beta, int count){
		int countfor = count;

		if(terminal_Test(jogo)){
			return utility(jogo, checkWin(jogo));
		}

		if(countfor==limite) {
			return utility_Value(jogo);
		}

		int v = Integer.MAX_VALUE;

		for(Node s : makeSuccessors(jogo, 'X', 1)){
			v = Math.min(v, max_Value(s.getJogo(), alfa, beta, countfor + 1));
			if(v <= alfa){
				return v; //momento da poda
			}

			beta = Math.min(beta, v);
		}

		
		return v;
	}


	static boolean terminal_Test(char[][] jogo){
		int v = checkWin(jogo);
		if( v == 2 || v == 3 || v == 0)
			return true;
		else
			return false;
	}

	static int utility(char[][] jogo, int a) {
		if(a == 0){
			return 0;
		}

		else if(a == 2){
			return -512;
		}

		else if(a == 3){
			return 512;
		}

		else{
			return utility_Value(jogo);
		}
	}


	static int utility_Value(char[][] jogo){
		int total = 0;
		int totalh=0;
		int temp=0;

		//horizontal
		for(int i = 0; i < 6; i++ ) {
			for(int j = 0; j < 4; j++) {
				int countX = 0, countO = 0;
				for(int k = j; k < j+4; k++){
					if(jogo[i][k]=='X') {
						countX++;
					}
					else if(jogo[i][k]=='O') {
						countO++;
					}

				}
				if(countX>0 && countO==0) {
					switch (countX) {
						case 1: temp=1;
						break;

						case 2: temp=10;
						break;

						case 3: temp=50;
						break;
					}
				}

				else if (countO>0 && countX==0) {
					switch(countO) {
						case 1: temp=-1;
						break;

						case 2: temp=-10;
						break;

						case 3: temp=-50;
						break;
					}
				}

				else
					temp=0;

				totalh = totalh + temp;
				

			}

			
		}
		//vertical
		int totalv=0;

		for(int i = 0; i < 7; i++ ) {
			for(int j = 0; j < 3; j++) {
				int countX = 0, countO = 0;
				temp=0;
				for(int k = j; k < j+4; k++){
					if(jogo[k][i]=='X') {
						countX++;
					}
					else if(jogo[k][i]=='O') {
						countO++;
					}

				}
				if(countX>0 && countO==0) {
					switch (countX) {
						case 1: temp=1;
						break;

						case 2: temp=10;
						break;

						case 3: temp=50;
						break;
					}
				}

				else if (countO>0 && countX==0) {
					switch(countO) {
						case 1: temp=-1;
						break;

						case 2: temp=-10;
						break;

						case 3: temp=-50;
						break;
					}
				}

				else
					temp=0;

				totalv = totalv + temp;

			}	
		}
		total = totalv + totalh +  diagonal_Check(total, jogo);
		total = -total;

		return total;


	}


	static int diagonal_Check(int total, char[][] matrix){
		int temp=0, returnable=total, countO=0, countX=0;

		for(int i=0; i<3; i++) {
			for(int j=0; j<4; j++) {
				countX=0; countO=0;
				for(int k=0; k<4; k++) {
					if(matrix[i+k][j+k]=='X')
						countX++;
					else if(matrix[i+k][j+k]=='O')
						countO++;
				}
				if(countX>0 && countO==0) {
					switch (countX) {
						case 1: temp=1;
						break;

						case 2: temp=10;
						break;

						case 3: temp=50;
						break;
					}
				}

				else if (countO>0 && countX==0) {
					switch(countO) {
						case 1: temp=-1;
						break;

						case 2: temp=-10;
						break;

						case 3: temp=-50;
						break;
					}
				}
				else 
					temp=0;
				returnable += temp;

			}
		}

		temp=0; countO=0; countX=0;
		for(int i=0; i<3; i++) {
			for(int j=6; j>2; j--) {
				countX=0; countO=0;
				for(int k=0; k<4; k++) {
					if(matrix[i+k][j-k]=='X')
						countX++;
					else if(matrix[i+k][j-k]=='O')
						countO++;
				}
				if(countX>0 && countO==0) {
					switch (countX) {
						case 1: temp=1;
						break;

						case 2: temp=10;
						break;

						case 3: temp=50;
						break;
					}
				}

				else if (countO>0 && countX==0) {
					switch(countO) {
						case 1: temp=-1;
						break;

						case 2: temp=-10;
						break;

						case 3: temp=-50;
						break;
					}
				}
				else 
					temp=0;
				returnable += temp;
			}
		}
		return returnable;


	}



	static int checkWin(char[][] tabuleiro){
		if(ler_Vertical(tabuleiro) == 3 && ler_Horizontal(tabuleiro) == 3 && ler_Diagonal(tabuleiro) == 3){
			if(checkEmpate(tabuleiro)){
				//empate
				return 0;
			}
			else{
				//nao empate
				return 1;	
			}
		}

		else if(ler_Vertical(tabuleiro) == 1 || ler_Horizontal(tabuleiro) == 1 || ler_Diagonal(tabuleiro) == 1){
			//player wins
			return 2;
		}

		else if(ler_Vertical(tabuleiro) == 2 || ler_Horizontal(tabuleiro) == 2 || ler_Diagonal(tabuleiro) == 2){
			//pc wins
			return 3;
		}
		return 1;
	}

	static boolean checkEmpate(char[][] tabuleiro){
		for(int i = 0; i <= 6; i++){
			if(tabuleiro[0][i] == '-'){
				return false;
			}
		} 
		return true;
	}


	static int ler_Horizontal(char[][] matrix){
		int countX = 0;
		int countO = 0;

		for(int i = 5; i >= 0; i--) {
			for(int j = 0; j < 7; j++) {
				if(matrix[i][j]=='X') {
					countX++;
					countO = 0;
					if(countX == 4) {
						//player wins
						return 1;
					}
				}

				else if(matrix[i][j]=='O') {
					countO++;
					countX = 0; 
					if(countO == 4) {
						//PC wins
						return 2;
					}
				}	


				else{
					countX = 0;
					countO = 0;
				}

			}
			countX = 0;
			countO = 0;
		}
		return 3;
	}

	static int ler_Vertical(char[][] matrix) {
		int countX = 0;
		int countO = 0;
		for(int i = 0; i < 7; i++) {
			for(int j = 5; j >= 0; j--) {
				if(matrix[j][i]=='X') {
					countX++;
					countO = 0;
					if(countX == 4) {
						//player wins
						return 1;
					}
				}

				else if(matrix[j][i]=='O') {
					countO++;
					countX = 0; 
					if(countO == 4) {
						//PC wins
						return 2;
					}
				}


				else{
					countX = 0;
					countO = 0;
				}	

			}
			countX = 0;
			countO = 0;

		}

		return 3;
	}


	static int ler_Diagonal(char[][] matrix) {
		int a=3;
		int countX = 0;
		int countO = 0;
		for(int i=2; i>=0; i--) {
			int k=i;
			for(int j=0; j<=a; j++) {
				if(matrix[k][j]=='X') {
					countX++;
					countO = 0;
					if(countX == 4) {
						//player wins
						return 1;
					}
				}

				else if(matrix[k][j]=='O') {
					countO++;
					countX = 0; 
					if(countO == 4) {
						//PC wins
						return 2;
					}
				}	


				else{
					countX = 0;
					countO = 0;
				}

				k++;
			}
			countX = 0;
			countO = 0;
			a++;

		}

		a=5;
		countX = 0;
		countO = 0;

		for(int i=1; i<=3; i++) {
			int k=i;
			for(int j=0; j<=a; j++) {

				if(matrix[j][k]=='X') {
					countX++;
					countO = 0;
					if(countX == 4) {
						//player wins
						return 1;
					}
				}

				else if(matrix[j][k]=='O') {
					countO++;
					countX = 0; 
					if(countO == 4) {
						//PC wins
						return 2;
					}
				}	


				else{
					countX = 0;
					countO = 0;
				}

				k++;
			}
			countX = 0;
			countO = 0;
			a--;

		}


		int temp = 5;

		for(int i = temp; i >= 3; i--){
			int k = i;
			for(int j = 0; j <= i; j++){
				if(matrix[j][k]=='X') {
					countX++;
					countO = 0;
					if(countX == 4) {
						//player wins
						return 1;
					}
				}

				else if(matrix[j][k]=='O') {
					countO++;
					countX = 0; 
					if(countO == 4) {
						//PC wins
						return 2;
					}
				}


				else{
					countX = 0;
					countO = 0;
				}

				k--;
			}

			countX = 0;
			countO = 0;
		}

		int k = 6;
		for(int i = k; i >= 4 ; i--){

			for(int j = a; j <= 5 ; j++){
				if(matrix[j][k]=='X') {
					countX++;
					countO = 0;
					if(countX == 4) {
						//player wins
						return 1;
					}
				}

				else if(matrix[j][k]=='O') {
					countO++;
					countX = 0; 
					if(countO == 4) {
						//PC wins
						return 2;
					}
				}


				else{
					countX = 0;
					countO = 0;
				}

				k--;
			}
			k = 6;
			a++;
			countX = 0;
			countO = 0;
		}

		return 3;
	}



	static boolean makePlay(char[][] config, int coluna, int jogador){

		if(coluna < 0 || coluna > 6){
			System.out.println("Essa coluna nao existe! Jogada Impossivel... Escolha outra coluna.");
			return false;
		}

		for(int i = 5; i >= 0; i--){
			if(config[i][coluna] == '-'){
				// jogador == 1 ---> Human	
				if(jogador == 1){
					config[i][coluna] = 'X';
					return true;
				}

				// jogador == 2 ---> PC
				else if(jogador == 2){
					config[i][coluna] = 'O';
					return true;
				}
			}
		}

		System.out.println("Essa coluna esta cheia! Jogada Impossivel... Escolha outra coluna.");
		return false;
	}

	static void printBoard(char[][] tabuleiro){
		for(int i = 0; i <= 5; i++){
			for(int j = 0; j <= 6; j++){
				System.out.print(tabuleiro[i][j] + " ");
			}
			System.out.println();
		} 
		System.out.println("0 1 2 3 4 5 6");
	}


	public static void main(String[] args){
		char[][] tabuleiro = {{'-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-'},
		{'-','-','-','-','-','-','-'}};

		printBoard(tabuleiro);

		Scanner input = new Scanner(System.in);
		System.out.println("Quem deve ser o primeiro a jogar?");
		System.out.println("Tu ---> 1");
		System.out.println("PC ---> 2");
		int toPlay = input.nextInt();
		while(toPlay != 1 && toPlay != 2){
			System.out.println("Escolha uma das opcoes apresentadas!");
			toPlay = input.nextInt();
		}

		System.out.println("Que algoritmo quer usar?");
		System.out.println("MINIMAX    ---> 1");
		System.out.println("ALPHA-BETA ---> 2");
		int algorithm = input.nextInt();
		while(algorithm != 1 && algorithm != 2){
			System.out.println("Escolha uma das opcoes apresentadas!");
			algorithm = input.nextInt();
		}
		int temp = 2;
		int temp2 = toPlay;
		if(toPlay == 2){
			temp = 1;
		}



		for(int i = 0; i < 42; i++){
			int move;
			if(toPlay == 1){
				System.out.println("Escolha a coluna onde pretendes jogar (0 - 6):");
				move = input.nextInt();
				while(!makePlay(tabuleiro, move, 1)){
					move = input.nextInt();
				}
				System.out.println("Jogador:");
				printBoard(tabuleiro);
				System.out.println("--------------------------------------------");
			}
			else{
				if(algorithm == 1){
					move = mini_Max(tabuleiro, 0);
					endTime = System.nanoTime();
					makePlay(tabuleiro, move, 2);
				}
				else{
					
					move = alpha_Beta(tabuleiro, 0);
					endTime = System.nanoTime();
					makePlay(tabuleiro, move, 2);
				}
				System.out.println("Computador:");
				printBoard(tabuleiro);
				System.out.println("Nos Gerados = " + numgerados);
				System.out.println("Time = " + ((endTime - startTime)/1000000) + " ms");
				System.out.println("--------------------------------------------");
				numgerados = 0;
			}



			int v = checkWin(tabuleiro);
			if(v == 2 || v == 3 || v == 0){
				switch(v){
					case 2: System.out.println("Ganhasteeeee");
					break;

					case 3: System.out.println("Perdesteeeee");
					break;

					case 0: System.out.println("Empatezaooooooo");
					break;
				}

				return;
			}


			i++;
			if(i%2 == 0){
				toPlay = temp2;
			}
			else{
				toPlay = temp;
			}
			i--;

		}
	}

}