import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

/**
 * The Application class is the main class of the application that creates the GUIs as well as has
 * all the event handlers for the components of the GUI. Moreover, the class also implements MouseListener 
 * class that handles the events when the user plays the game by clicking the integer buttons.
 * 
 * @author Yaksh J Haranwala
 * @version
 */

public class Application implements MouseListener {
	
	// The components of the GUI that needed to be used inside the class.
	private JFrame frame;
	private JPanel topPanel, bottomPanel;
	private GridSquare [][]gridSquares;
	private Container contentPane;
	private JButton newGame;
	private JLabel turnLabel;
	private JLabel score;
	private Component rigidArea;
	private int column, row;
	
	// Score once the user starts playing.
	private int total;
	private int playerID;

	
	/**
	 * Initiate the frame and set its layout, then call on the helper functions to setup the GUI and display it.
	 */
	public Application(int x, int y) {
		
		// For cross platform performance.
		try
		{
		   UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		}
		catch (Exception e)
		{
		   e.printStackTrace();

		}
		
		frame = new JFrame("Pontoon");
		frame.setBounds(100, 100, 620, 416);
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		createMenuBar();
		createButtons(x, y);
		createTop();

		frame.setVisible(true);
		JOptionPane.showMessageDialog(frame, "Keep the Total below 22 and force the other player to exceed it!\n"
										+ "Click on New Game to begin!", "Instructions", JOptionPane.INFORMATION_MESSAGE);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		total = 0;
		
	}

	
	/**
	 * Create the buttons panel as required by aligning in the Grid Layout.
	 * 
	 * The purpose of this function is to create all the buttons, set their size set their default text and then disable all the 
	 * buttons until the user clicks on New Game button to start playing.
	 * 
	 * @param x is the number of rows.
	 * @param y is the number of columns.
	 */
	private void createButtons(int x, int y) {

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(5, 5));

		gridSquares = new GridSquare[x][y];
		for (column = 0; column < x; column++) {
			for (row = 0; row < y; row++) {
				gridSquares[column][row] = new GridSquare(x, y);		// Create the button.
				gridSquares[column][row].setSize(20, 20);				// Set the button size
				gridSquares[column][row].setText("-");					// Set the default text.
			    gridSquares[column][row].setBorder(BorderFactory.createLineBorder(Color.white, 2));	// Set the border to improve the look.
			    gridSquares[column][row].setFocusable(false);			// Avoid the highlight of buttons when the user clicks on it.
				bottomPanel.add(gridSquares[column][row]);
				gridSquares[column][row].setEnabled(false);
			}	
		}
		contentPane.add(bottomPanel, BorderLayout.CENTER);
	}
	

	/**
	 * Create the top panel of the main GUI window that has the New Game button, the label that indicates the player's turn,
	 * and the current total that is going on the game. 
	 * Apart from that, also add the action listeners of the associated New Game button.
	 */
	public void createTop() {
	    topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());

		contentPane.add(topPanel, BorderLayout.NORTH);
		newGame = new JButton("New Game");
		newGame.setFont(new Font("Tahoma", Font.PLAIN, 18));
		newGame.addActionListener(e -> newGame());
		newGame.setFocusable(false);

		turnLabel = new JLabel("Keep the total below 22!");
		turnLabel.setPreferredSize(new Dimension(225, 20));
		turnLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel current = new JLabel("Current: ");
		current.setVerticalAlignment(SwingConstants.BOTTOM);
		current.setHorizontalAlignment(SwingConstants.RIGHT);
		current.setFont(new Font("Tahoma", Font.PLAIN, 18));

		score = new JLabel("00");
		score.setFont(new Font("Tahoma", Font.PLAIN, 18));

		topPanel.add(newGame);
		topPanel.add(turnLabel);
		
		rigidArea = Box.createRigidArea(new Dimension(125, 20));
		topPanel.add(rigidArea);
		topPanel.add(current);
		topPanel.add(score);
	}

	/**
	 * Make the menu bar and its associated menus, also add the menu items and register their action listeners.
	 */
	public void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem resetGame = new JMenuItem("New Game");
		fileMenu.add(resetGame);
		resetGame.addActionListener(e -> newGame());
		
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		exit.addActionListener(e -> exit());
		
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);
		
		JMenuItem versionInfo = new JMenuItem("Version Info");
		aboutMenu.add(versionInfo);
		versionInfo.addActionListener(e -> showVersionInfo());
	}
	
	/**
	 * Mutator for changing the player after each turn.
	 * @param id is the player number.
	 */
	public void setPlayerID(int id) {
		playerID = id;
	}
	
	/**
	 * Accessor for the player ID that has the current turn.
	 * @return the player number.
	 */
	public int getPlayerID() {
		return playerID;
	}
	
	/**
	 * Exit the application with an Exit status 0 when the user clicks the Exit option in the file menu.
	 */
	private void exit() {
		System.exit(0);
	}
	
	/**
	 * Show the version information of the application when the user clicks the version info option from the
	 * about menu of the application.
	 */
	private void showVersionInfo() {
		JOptionPane.showMessageDialog(frame, "Version : 1.0\n" + "Author   : Yaksh J Haranwala",
									"Pontoon", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Reset the text of all the buttons when the user clicks the New Game Button.
	 * 
	 * The Text replaced are numbers between 1 to 5.
	 * Also note that the Current score is reset back to 0 upon the clicking of New Game by the user.
	 * The System also randomly assigns turn to player 1 or 2 to start the game.
	 */
	private void newGame() {
		Random rand = new Random();
		setPlayerID(rand.nextInt(2) + 1);
		turnLabel.setText("Player " + String.valueOf(getPlayerID()) + "'s turn...");
		score.setText("00");
		total = 0;
		for (int column = 0; column < gridSquares.length; column++) {
			for (int row = 0; row < gridSquares.length; row++) {
				gridSquares[column][row].setText(String.valueOf(rand.nextInt(5) + 1));
				gridSquares[column][row].setEnabled(true);
				gridSquares[column][row].resetColor();
				gridSquares[column][row].addMouseListener(this);
			}
		}
	}
	
	
	/**
	 * Implementing the mouseClicked events for our buttons panel.
	 * Note that upon initial launch of the application, the buttons are disabled and clicking on them will not trigger any MouseEvent. 
	 * 
	 */
	public void mouseClicked(MouseEvent ev) {
		Object selected = ev.getSource();
		GridSquare square = (GridSquare) selected;
		
		/*
		 * When the button is clicked, get the integer value of the button and then add it to total as well as set the respective color
		 * of the player that click the button and then remove the mouse listener to prevent further clicks on the button.
		 */
		if (selected instanceof GridSquare) {
			total += Integer.parseInt(square.getText());
			score.setText(String.valueOf(total));
			square.setColor(playerID);
			square.removeMouseListener(this);
		}
		
		/*
		 * Check whether the total has exceeded 21 or not. If it has, then announce the winner based on the conditions
		 * of the game and ask whether the user wants to play another game or exit.
		 */
		if (total > 21) {
			if (getPlayerID() == 1) {
				gameOver(2);
			}
			else {
				gameOver(1);
			}
		}
		
		/*
		 * If the total is below 21, then change the player turns based on who took the last turn. If it was player 1, 
		 * then player 2 goes next and vice versa and keep going until the total exceeds 21. 
		 */
		else {
			if (getPlayerID() == 1) {
				setPlayerID(2);
				turnLabel.setText("Player " + String.valueOf(getPlayerID()) + "'s turn...");
			}
			else					{
				setPlayerID(1);
				turnLabel.setText("Player " + String.valueOf(getPlayerID()) + "'s turn...");
			}
		}
	}
	
	/**
	 * Announce the winner and get user input whether to start a new game or exit the application. 
	 * Note that when the game is over and the winner is announced, then the buttons are disabled 
	 * and the mouseListeners are also removed so as to disable the game from interacting any further with the user.
	 *  The game wont do anything until the user clicks on New Game button.
	 * @param winner is the player who won the game!
	 */
	public void gameOver(int winner) {
		JOptionPane.showMessageDialog(frame, "Congratulations! Player " + String.valueOf(winner)  +" Wins!\n"
				+ "Click New Game to play again.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		turnLabel.setText("Player " + String.valueOf(winner) + " Wins!");
		
		for (int column = 0; column < 5; column++) {
			for (int row = 0; row < 5; row++) {
				gridSquares[column][row].removeMouseListener(this);
				gridSquares[column][row].setEnabled(true);
			}
		}
	}
	
	// Necessary function implementation to complete the implementation of MouseEvent class. 
	public void mouseEntered(MouseEvent voidArg){}
	public void mouseExited(MouseEvent voidArg) {}
	public void mousePressed(MouseEvent voidArg) {}
	public void mouseReleased(MouseEvent voidArg) {}

}
