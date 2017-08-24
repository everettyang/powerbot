package hauntbrave.ItsFire.utils;

import org.powerbot.script.rt4.World.Type;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectLog extends ClientAccessor {

	private final String greeting = "Welcome to ItsFire! - By hauntbrave";
	private final String prompt = "Please enter a log's ID...";
	private final String noItemError = "This item isn't in your inventory!";
	private final String invalidInputError = "Enter a valid ID!";

	private int logId;
	private JLabel headerLabel;
	private Panel controlPanel;
	private Panel bottomPanel;
	
	private JButton confirm = new JButton("Confirm");

	private TextField textField = new TextField();

	private JFrame mainFrame;
	private Object lock = new Object();

	public SelectLog(ClientContext ctx){ super(ctx); prepGUI(); }

	private void prepGUI() {

		Font labelfont = new Font("Serif", Font.BOLD, 18);

		mainFrame = new JFrame(greeting);
		mainFrame.setSize(600,200);
		mainFrame.setLayout(new GridLayout(3,1));

		headerLabel = new JLabel(prompt, SwingConstants.CENTER);
		headerLabel.setFont(labelfont); 
		
		controlPanel = new Panel(new GridLayout());
		bottomPanel = new Panel();

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(bottomPanel);

		JRootPane rootPane = mainFrame.getRootPane();
		rootPane.setDefaultButton(confirm);
	}

	public void show(){

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg) {
					System.out.println("X was clicked. Exiting script...");

					//destroy window
					mainFrame.setVisible(false);
					mainFrame.dispose();
					ctx.controller.stop();
				}
			 });

		confirm.setActionCommand("confirm");

		confirm.addActionListener(new ButtonListener()); 
		controlPanel.add(textField);
		bottomPanel.add(confirm);

		mainFrame.setVisible(true); 
		waitForClose();
	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//sets id and closes
			try { 
				logId = Integer.parseInt(textField.getText());
				if (ctx.inventory.select().id(logId).count() == 0)

				{ headerLabel.setText(noItemError); }

				else{ close(); }
			 }

			catch (NumberFormatException err) { headerLabel.setText(invalidInputError); }
		}
	}

	public int getLogId() { return logId; }

	private void waitForClose()
	{
		/* creates a lock that is active
		until the window is closed by clicking the
		traditional X or is confirm is pressed. */

		Thread pause = new Thread() {
		public void run() {
		    synchronized(lock) {
			if (mainFrame.isVisible())
			{
			    try {
				lock.wait();
			    } catch (InterruptedException e) {
				e.printStackTrace();
			    }
			}
		    }
		}
	    };
		pause.start();

		//join() haults the main thread and wait until thread t is done
		try { pause.join(); }
	        catch (InterruptedException e) { e.printStackTrace(); }
	}

	public boolean isOpen() { return mainFrame.isShowing(); }

	private void close() { 
		//invoked when confirm is pressed
		synchronized (lock) {
			//destroys and unlocks main thread
			mainFrame.setVisible(false);
			mainFrame.dispose();
			lock.notify();
		}

	}
}
