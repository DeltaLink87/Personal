import javax.swing.*;
import java.awt.event.*;

public class MineSweeper extends JPanel implements ActionListener{
	
	
	//setting up the screen
	public static void main(String[] args){
		JFrame frame = new JFrame("Mine Sweeper");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Game game = new Game(frame);
		frame.getContentPane().add(game);frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	JButton[][] tiles;
	boolean[][] mines;
	int boardWidth, boardHeight;
	int tileWidth, tileHeight;
	
	JButton reset;
	boolean lost, win;
	JLabel lblMessage;
	
	public MineSweeper(JFrame frame){
		//setting variables
		this.setPreferredSize(frame.getSize());
		this.setSize(frame.getSize());
		this.setLayout(null);
		int width = this.getWidth(), height = this.getHeight();
		
		tileWidth = 50;
		tileHeight = 30;
		
		boardWidth = width / tileWidth;
		boardHeight = height / tileHeight - 2;
		
		//creating tiles and setting if they have a mine under them
		tiles = new JButton[boardHeight][boardWidth];
		mines = new boolean[boardHeight][boardWidth];
		for (int y = 0; y < boardHeight; y++)
			for (int x = 0; x < boardWidth; x++){
				tiles[y][x] = new JButton("");
				tiles[y][x].setBounds(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
				tiles[y][x].addActionListener(this);
				tiles[y][x].setActionCommand((x * boardHeight + y) + "");
				this.add(tiles[y][x]);
				
				if (Math.floor(Math.random() * 8) == 0)
					mines[y][x] = true;
			}
		
		//adding the win/lose message box to the screen
		lblMessage.setText("");
		this.add(lblMessage);
		
		//setting reset button possition
		reset = new JButton("Reset Game");
		reset.setBounds(width / 2 - tileWidth * 3/2, height - tileHeight * 3/2, tileWidth * 3, tileHeight);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				resetGame();
			}
		});
		this.add(reset);	//adding the reset button to the screen
		
		//settin gthe win/lose message boxes postion
		lblMessage = new JLabel("Hello");
		lblMessage.setBounds(width/4*3-tileWidth * 3/2, height-tileHeight * 3/2, tileWidth * 3, tileHeight);
	}
	
	//setups up the game for a new game
	public void resetGame(){
		//resetting board
		for (int y = 0; y < boardHeight; y++)
			for (int x = 0; x < boardWidth; x++){
				tiles[y][x].setText("");
				
				if (Math.floor(Math.random() * 8) == 0)
					mines[y][x] = true;
			}
		
		lblMessage.setText("");	//reseting win/lose message
		
		//reseting the win/lose variables
		lost = false;
		win = false;
	}
	
	//processes tiles being pressed
	public void actionPerformed(ActionEvent e){
		if (lost || win) return;	//checking if you already won or lost the game
		
		//getting the coordinate of the tile pressed
		int coordinates = Integer.parseInt(e.getActionCommand());
		int x = coordinates / boardHeight, y = coordinates % boardHeight;
		
		//checking the button pressed
		checkTile(x, y, true);
	}
	
	//checks around the buttons to see if its a mine when pressed
	public void checkTile(int x, int y, boolean first){
		//checking if the tile was a mine
		if (mines[y][x]){
			tiles[y][x].setText("M");
			if (first){	//causing a game lose if mine was the first tile clicked
				lost = true;
				lblMessage.setText("You Lost");
				revealBoard();
			}
		}	
		else{
			//checking number of mines around tile and displaying number
			int numOfMinesAround = 0;
			for(int y2 = -1; y2 < 2; y2++)
				for (int x2 = -1; x2 < 2; x2++){
					if ((x2 == 0 && y2 == 0) || x + x2 < 0 || x + x2 >= boardWidth || y + y2 < 0 || y + y2 >= boardHeight)
						continue;
					if (mines[y+y2][x+x2])	numOfMinesAround++;
				}
			tiles[y][x].setText(numOfMinesAround + "");
			
			
			//if no mines around the tile, automatically checking all tiles around this tile if not already checked
			if (numOfMinesAround == 0)
				for(int y2 = -1; y2 < 2; y2++)
					for (int x2 = -1; x2 < 2; x2++)
						if (x + x2 >= 0 && x + x2 < boardWidth && y + y2 >= 0 && y + y2 < boardHeight)
							if (tiles[y + y2][x + x2].getText().equals(""))
								checkTile(x + x2, y + y2, false);
			
			if (first)	checkWin();	//checking if you won the game
		}
	}
	
	//checks if the player won
	public void checkWin(){
		//checking all tiles to see if all non mine tiles have bben checked
		boolean won = true;
		for (int y = 0; y < boardHeight && won; y++)
			for (int x = 0; x < boardWidth && won; x++)
				if (tiles[y][x].getText().equals("") && !mines[y][x])
					won = false;
		
		//telling the player that they won
		if (won){
			lblMessage.setText("You Won");
			win = true;
			revealBoard();
		}
	}
	
	
	//reavealing the board to the player
	public void revealBoard(){
		//checking all not checked tiles
		for (int y = 0; y < boardHeight; y++)
			for (int x = 0; x < boardWidth; x++)
				if (tiles[y][x].getText().equals(""))
					checkTile(x, y, false);
	}
}