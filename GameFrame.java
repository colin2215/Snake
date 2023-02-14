import javax.swing.JFrame;

public class GameFrame extends JFrame {

	GameFrame(){
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); //Sizes the JFrame to auto size around all of its components
		this.setVisible(true);
		this.setLocationRelativeTo(null); //JFrame located in the middle of Computer Screen 
	}
}