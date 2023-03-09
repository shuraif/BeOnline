import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SimulateMouseMoveApplication extends JFrame implements ActionListener {

	private Robot robot;
	private boolean isMoving = false;
	private JButton startButton, stopButton;
	private JLabel statusLabel;

	public SimulateMouseMoveApplication() {
		setTitle("Mouse Mover");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 80);

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

		startButton = new JButton("Start");
		startButton.addActionListener(this);
		panel.add(startButton);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		panel.add(stopButton);

		statusLabel = new JLabel("Click start button to start moving mouse");
		panel.add(statusLabel);

		add(panel);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	public static void main(String[] args) {

		new SimulateMouseMoveApplication();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			isMoving = true;
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
			statusLabel.setText("Mouse is moving. Click stop button to end mouse move.");
			new Thread(() -> {
				while (isMoving) {
					//robot.mouseMove((int) (Math.random() * 2) - 1, (int) (Math.random() * 2) - 1);
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int xl = (int) b.getX();
					int yl = (int) b.getY();
					if(xl>1300){
						xl=0;
					}else{
						xl+=5;
					}
					robot.mouseMove(xl, yl);
					//robot.mouseMove((int) (Math.random() * 10) - 5, (int) (Math.random() * 10) - 5);
					try {
						Thread.sleep(180000); // move every 3 minute
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				statusLabel.setText("Mouse moving stopped. Click start button to move mouse.");
			}).start();
		} else if (e.getSource() == stopButton) {
			isMoving = false;
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
			statusLabel.setText("Mouse moving stopped. Click start button to move mouse.");
		}
	}
}
