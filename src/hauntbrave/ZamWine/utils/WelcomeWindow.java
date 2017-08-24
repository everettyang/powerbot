package hauntbrave.ZamWine.utils;

import org.powerbot.script.rt4.World.Type;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeWindow extends ClientAccessor {

	private final String greeting = "Welcome to ZamWine! - By hauntbrave";
	private final String prompt = "Choose worlds...";
	private final String empty = "Please choose world(s)...";

	private JCheckBox pvp = new JCheckBox("PVP");
	private JCheckBox free = new JCheckBox("Free");
	private JCheckBox members = new JCheckBox("Members");
	private JCheckBox deadman = new JCheckBox("Deadman");
	private JCheckBox all = new JCheckBox("All");
	private JButton confirm = new JButton("Confirm");

	private Label headerLabel;
	private Panel controlPanel;

	private JFrame mainFrame;
	private Object lock = new Object();

	private List<Type> typeList = new ArrayList<Type>();

	public WelcomeWindow(ClientContext ctx){ super(ctx); prepGUI(); }

	private void prepGUI() {

		Font labelfont = new Font("Serif", Font.BOLD, 18);

		mainFrame = new JFrame(greeting);
		mainFrame.setSize(600,100);
		mainFrame.setLayout(new GridLayout(3, 1));

		//prompt but changes if confirm is clicked with nothing selected
		headerLabel = new Label();
		headerLabel.setAlignment(Label.CENTER);
		headerLabel.setText(prompt); 
		headerLabel.setFont(labelfont); 
		
		//control panel is used for organizing buttons
		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);

		JRootPane rootPane = mainFrame.getRootPane();
		rootPane.setDefaultButton(confirm);
	}

	public void show(){

		//initialize the buttons and add event listener
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg) {
					System.out.println("X was clicked. Exiting script...");

					//destroy window
					mainFrame.setVisible(false);
					mainFrame.dispose();
					ctx.controller.stop();
				}
			 });

		pvp.setActionCommand("pvp");
		free.setActionCommand("free");
		members.setActionCommand("members");
		deadman.setActionCommand("deadman");
		confirm.setActionCommand("confirm");
		all.setActionCommand("all");

		pvp.addActionListener(new ButtonListener()); 
		free.addActionListener(new ButtonListener()); 
		members.addActionListener(new ButtonListener()); 
		deadman.addActionListener(new ButtonListener()); 
		confirm.addActionListener(new ButtonListener()); 
		all.addActionListener(new ButtonListener()); 

		controlPanel.add(pvp);
		controlPanel.add(free);
		controlPanel.add(members);
		controlPanel.add(deadman);
		controlPanel.add(confirm);
		controlPanel.add(all);

		mainFrame.setVisible(true); 
		waitForClose();
	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			handleCommand(command);
		}
	}

	private void handleCommand(String type){

			switch (type){

				case "free":
					if (free.isSelected())
						typeList.add(Type.FREE);
					else
						typeList.remove(Type.FREE);
					break;
				case "members":
					if (members.isSelected())
						typeList.add(Type.MEMBERS);
					else
						typeList.remove(Type.MEMBERS);
					break;

				//deadman and pvp enums are switched. plz fix that lol
				case "deadman":
					if (deadman.isSelected())
						typeList.add(Type.PVP);
					else
						typeList.remove(Type.PVP);
					break;
				case "pvp":
					if (pvp.isSelected())
						typeList.add(Type.DEAD_MAN);
					else
						typeList.remove(Type.DEAD_MAN);
					break;

				case "all":
					if (all.isSelected())
					{

						typeList.clear();
						typeList.addAll(Arrays.asList(Type.PVP,
										Type.DEAD_MAN,
										Type.FREE,
										Type.MEMBERS));
						enableButtons(false);
					}
					else
					{
						typeList.clear();
						enableButtons(true);
					}
					break;

				case "confirm":
					if (!typeList.isEmpty())
					{
						printList(typeList);
						close();
					}
					else
						headerLabel.setText(empty);
					break;
			}
	}

	public List<Type> getTypeList() { return typeList; }

	private void printList(List<Type> typeList) {

		for (int i = 0; i < typeList.size(); ++i) {
			System.out.println(typeList.get(i));
		}

	}

	private void enableButtons(boolean enabled) {
		
		if (!enabled){

			free.setSelected(enabled);
			pvp.setSelected(enabled);
			deadman.setSelected(enabled);
			members.setSelected(enabled);
	
		}

		free.setEnabled(enabled);
		pvp.setEnabled(enabled);
		deadman.setEnabled(enabled);
		members.setEnabled(enabled);
	}

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
