import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener { //Implement interfaces (Listener) all methods in interface must be defined 

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // How big do we want he objects in our game
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / UNIT_SIZE; // How many objects we can fit on the screen
	static final int DELAY = 125;// Higher the number the slower the game speed
	final int[] x = new int[GAME_UNITS]; // x coordinates for snake body parts
	final int[] y = new int[GAME_UNITS]; // y coordinates for snake body parts
	int bodyParts = 6;
	int applesEaten = 0;
	int appleX; // x coordinate for apple (will appear randomly)
	int appleY; // y coordinate for apple (will appear randomly)
	char direction = 'R'; // Direction the snake will begin the game moving in
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true); //Gives panel Keyboard focus, makes panel prominent on screen
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) { // The Graphics parameter is used to draw or paint on Components 
		super.paintComponent(g); //Black Background
		draw(g); //Grid  Lines 
	}
	public void draw(Graphics g) { // The Graphics parameter is used to draw or paint on Components
		//Draw a grid 
		if(running) {
			/*for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.setColor(Color.gray);
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); // Vertical Lines 
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); // Horizontal Lines 
			}*/
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // X and Y coordinates then the Width and Height of the apple
		
			// Create the snake
			for(int i=0;i<bodyParts;i++) {
				if(i ==0) { // Means it is the snake head
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // X and Y coordinates then the Width and Height of the snake
				}
				else { // Different color for the body of the snake
					//g.setColor(new Color(45,180,0));
					// Set random snake color
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); 
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			//Display the current score 
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		}
		else gameOver(g); // if not running then means game over
	}
	public void newApple() { // Generate coordinates of a new apple
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //This will cause the apple to Randomly generate in the middle of a unit
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; // Need help understanding how this works 
	}
	public void move() { //Moving the snake
	for(int i=bodyParts;i>0;i--) {
		 x[i] = x[i-1]; // Shifting the snake across the board
		 y[i] = y[i-1]; // Shifting the snake across the board
		}
	switch(direction) { // Change the direction of the snake
	case 'U':
		y[0] = y[0] - UNIT_SIZE;
		break;
	case 'D':
		y[0] = y[0] + UNIT_SIZE;
		break;
	case 'L':
		x[0] = x[0] - UNIT_SIZE;
		break;
	case 'R':
		x[0] = x[0] + UNIT_SIZE;
		break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX)&&(y[0]==appleY)) { // If the heads X and Y coordinates equals the apples X and Y coordinates
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		// Does the head collide with the body of the snake
		for(int i=bodyParts;i>0;i--) {
			if((x[0] == x[i])&&(y[0] == y[i])) running = false;
		}
		// Does the head collide with left boarder
		if(x[0] < 0) running = false;
		// Does the head collide with right boarder
		if(x[0] > SCREEN_WIDTH) running = false;
		// Does the head collide with top boarder
		if(y[0] < 0) running = false;
		// Does the head collide with bottom boarder
		if(y[0] > SCREEN_HEIGHT) running = false;
		
		
		if(!running) timer.stop();
	}
	public void gameOver(Graphics g) { // The Graphics parameter is used to draw or paint on Components 
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont()); //Used to line up text
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); //Place text in the cent of the screen width
		
		// Display score after game over
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint(); // If the game is no longer running repaint to the original start up screen
	}
	
public class MyKeyAdapter extends KeyAdapter { // Extend a class (Adapter) only define methods you care about
	@Override 
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		//Four cases for each of the arrow keys
		// To limit 180 degree turns into themselves user can go left as long as not moving right
		case KeyEvent.VK_LEFT:
			if(direction != 'R') direction = 'L';
			break;
		//user can go right as long as not moving left
		case KeyEvent.VK_RIGHT:
			if(direction != 'L') direction = 'R';
			break;
		//user can go up as long as not moving down
		case KeyEvent.VK_UP:
			if(direction != 'D') direction = 'U';
			break;
		//user can go down as long as not moving up
		case KeyEvent.VK_DOWN:
			if(direction != 'U') direction = 'D';
			break;
		}
	}
	
}
}
