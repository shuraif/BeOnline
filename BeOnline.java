import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BeOnline extends JFrame implements ActionListener {

	private Robot robot;
	private boolean isMoving = false;
	private JButton startButton, stopButton;
	private JLabel statusLabel, hintLabel;
	JTextField inputField;

	public static void main(String[] args) {

		new BeOnline();

	}

	public BeOnline() {
		setTitle("BeOnline");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(25, 25, 25, 25);

		hintLabel = new JLabel("How log you want to be online");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		add(hintLabel, constraints);

		inputField = new JTextField(20);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(inputField, constraints);

		startButton = new JButton("Start");
		startButton.addActionListener(this);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		add(startButton, constraints);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(this);
		constraints.gridx = 1;
		constraints.gridwidth = 10;
		stopButton.setEnabled(false);
		add(stopButton, constraints);

		statusLabel = new JLabel("");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		add(statusLabel, constraints);

		pack();
		setLocationRelativeTo(null);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			isMoving = true;
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
			String input = inputField.getText();

			Integer minutes;
			try {
				minutes = Integer.parseInt(input);
				inputField.setText("");

				LocalTime startTime = LocalTime.now();
				Duration duration = Duration.ofMinutes(minutes);
				LocalTime endTime = startTime.plus(duration);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

				String timeString = endTime.format(formatter);

				statusLabel.setText("Started with input: " + timeString);

				new Thread(() -> {
					while (LocalTime.now().isBefore(endTime) && isMoving) {
						LocalTime currentTime = LocalTime.now();
						inputField.setEditable(!isMoving);
						Duration diff = Duration.between(currentTime, endTime);
						long minuteDifference = diff.toMinutes();
						long secondsDifference = diff.minusMinutes(minuteDifference).getSeconds();

						statusLabel.setText("BeOnline for : " + minuteDifference + " :" + secondsDifference);

					}

					isMoving = false;
					statusLabel.setText("");
					inputField.setEditable(!isMoving);
					startButton.setEnabled(!isMoving);
					stopButton.setEnabled(isMoving);
				}).start();

				new Thread(() -> {
					while (isMoving) {

						PointerInfo a = MouseInfo.getPointerInfo();
						Point b = a.getLocation();
						int xl = (int) b.getX();
						int yl = (int) b.getY();
						if (xl > 1300) {
							xl = 0;
						} else {
							xl += 5;
						}
						robot.mouseMove(xl, yl);

						try {
							Thread.sleep(180000); 
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}

				}).start();

			} catch (NumberFormatException numberFormatException) {
				statusLabel.setText("Invalid input. Please enter an integer.");
			}

		} else if (e.getSource() == stopButton) {
			isMoving = false;
			startButton.setEnabled(!isMoving);
			stopButton.setEnabled(isMoving);
			statusLabel.setText("");
			inputField.setEditable(!isMoving);
		}
	}
}
