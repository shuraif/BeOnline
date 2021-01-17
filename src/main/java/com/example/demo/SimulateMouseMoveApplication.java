package com.example.demo;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SimulateMouseMoveApplication extends JFrame implements KeyListener {

	JLabel label;
	boolean bool=true;
	public SimulateMouseMoveApplication(String s) throws AWTException, InterruptedException {
		super(s);
		JPanel p = new JPanel();
		label = new JLabel("press any key to stop mouse move!");
		p.add(label);
		add(p);
		addKeyListener(this);
		setSize(400, 100);
		setVisible(true);
		mouseMove();
	}


	public void mouseMove() throws InterruptedException, AWTException {
		while(bool) {
			PointerInfo a = MouseInfo.getPointerInfo();
			Point b = a.getLocation();
			int xl = (int) b.getX();
			int yl = (int) b.getY();
			if(xl>1300){
				xl=0;
			}
			Robot bot=new Robot();

			if(bool) {
				xl+=15;
			}
			bot.mouseMove(xl, yl);
			Thread.sleep(3000);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Mouse moving stopped.");
		bool=false;

	}

	public static void main(String[] args) throws AWTException, InterruptedException  {
		new SimulateMouseMoveApplication("Key Listener Tester");
	}
}
