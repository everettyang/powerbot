package org.powerbot.gui.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.powerbot.bot.Bot;
import org.powerbot.gui.BotChrome;
import org.powerbot.gui.controller.BotInteract;
import org.powerbot.gui.controller.BotInteract.Action;
import org.powerbot.script.internal.ScriptManager;
import org.powerbot.service.NetworkAccount;
import org.powerbot.util.Tracker;
import org.powerbot.util.io.CryptFile;
import org.powerbot.util.io.IOHelper;
import org.powerbot.util.io.Resources;

/**
 * @author Paris
 */
public final class BotToolBar extends JToolBar {
	private static final long serialVersionUID = 6279235497882884115L;
	private final BotChrome parent;
	private final JButton add, accounts, play, stop, feedback, input, view;
	private final JToggleButton signin, logger;
	private final ImageIcon[] playIcons;
	private final CryptFile loggerPref;

	public BotToolBar(final BotChrome parent) {
		setFloatable(false);
		setBorder(new EmptyBorder(1, 3, 1, 3));
		final int d = 16;

		this.parent = parent;
		loggerPref = new CryptFile("logpane.txt", BotToolBar.class);

		add = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.ADD)));
		add.setToolTipText(BotLocale.NEWTAB);
		add.setFocusable(false);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				BotInteract.tabAdd();
			}
		});
		add(add);

		add(Box.createHorizontalStrut(d));

		accounts = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.ADDRESS)));
		accounts.setToolTipText(BotLocale.ACCOUNTS);
		accounts.setFocusable(false);
		accounts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				BotInteract.showDialog(Action.ACCOUNTS);
			}
		});
		add(accounts);
		signin = new JToggleButton(new ImageIcon(Resources.getImage(Resources.Paths.KEYS)));
		signin.setToolTipText(BotLocale.SIGNIN);
		signin.setFocusable(false);
		signin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				BotInteract.showDialog(Action.SIGNIN);
			}
		});
		add(signin);

		add(Box.createHorizontalGlue());

		playIcons = new ImageIcon[]{new ImageIcon(Resources.getImage(Resources.Paths.PLAY)), new ImageIcon(Resources.getImage(Resources.Paths.PAUSE))};
		play = new JButton(playIcons[0]);
		play.setToolTipText(BotLocale.PLAYSCRIPT);
		play.setFocusable(false);
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				BotInteract.scriptPlayPause();
			}
		});
		add(play);
		stop = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.STOP)));
		stop.setToolTipText(BotLocale.STOPSCRIPT);
		stop.setFocusable(false);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				BotInteract.scriptStop();
			}
		});
		add(stop);
		feedback = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.COMMENTS)));
		feedback.setToolTipText(BotLocale.FEEDBACK);
		feedback.setVisible(false);
		feedback.setFocusable(false);
		feedback.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
			}
		});
		add(feedback);

		add(Box.createHorizontalStrut(d));

		input = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.KEYBOARD)));
		input.setToolTipText(BotLocale.INPUT);
		input.setFocusable(false);
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				new BotMenuInput().show((Component) e.getSource(), input.getWidth() / 2, input.getHeight() / 2);
			}
		});
		add(input);
		view = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.VIEW)));
		view.setToolTipText(BotLocale.VIEW);
		view.setFocusable(false);
		view.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				new BotMenuView().show((Component) e.getSource(), view.getWidth() / 2, input.getHeight() / 2);
			}
		});
		add(view);

		add(Box.createHorizontalStrut(d));

		logger = new JToggleButton(new ImageIcon(Resources.getImage(Resources.Paths.LIST)));
		logger.setToolTipText(BotLocale.LOGPANE);
		logger.setVisible(false);
		logger.setSelected(false);
		logger.setFocusable(false);
		logger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				toggleLogPane();
			}
		});
		add(logger);
		final JButton about = new JButton(new ImageIcon(Resources.getImage(Resources.Paths.INFO)));
		about.setToolTipText(BotLocale.ABOUT);
		about.setFocusable(false);
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				track(e);
				BotInteract.showDialog(Action.ABOUT);
			}
		});
		add(about);

		updateControls();
	}

	public void track(final ActionEvent e) {
		final Component c = (Component) e.getSource();
		final String s = c == signin ? BotLocale.SIGNIN : c instanceof JToggleButton ? ((JToggleButton) c).getToolTipText() : ((JButton) c).getToolTipText();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Tracker.getInstance().trackPage("/toolbar", s);
			}
		});
	}

	public void toggleLogPane() {
		final JScrollPane logpane = BotChrome.getInstance().logpane;
		logpane.setVisible(!logpane.isVisible());
		logger.setSelected(logpane.isVisible());
		final int[] h = {logpane.getSize().height, logpane.getPreferredSize().height};
		parent.setSize(new Dimension(parent.getSize().width, parent.getSize().height + h[h[0] == 0 ? 1 : 0] * (logpane.isVisible() ? 1 : -1)));

		if (logpane.isVisible()) {
			try {
				IOHelper.write(new ByteArrayInputStream(new byte[]{1}), loggerPref.getOutputStream());
			} catch (final IOException ignored) {
			}
		} else {
			loggerPref.delete();
		}
	}

	public void registerPreferences() {
		if (loggerPref.exists() && !logger.isSelected()) {
			toggleLogPane();
		}
		logger.setVisible(true);
	}

	public void setVisibleEx(final boolean r) {
		accounts.setVisible(r);
		signin.setVisible(r);
	}

	public void updateControls() {
		final NetworkAccount a = NetworkAccount.getInstance();
		add.setEnabled(a.isLoggedIn());
		signin.setToolTipText(a.isLoggedIn() ? a.getDisplayName() : BotLocale.SIGNIN);
		signin.setSelected(a.isLoggedIn());

		final boolean e = Bot.instantiated();
		for (final Component c : new Component[]{play, stop, input, view}) {
			c.setVisible(e);
		}

		final ScriptManager container = e ? Bot.instance().getScriptController() : null;
		final boolean active = container != null, running = active && !container.isSuspended();
		play.setIcon(playIcons[running ? 1 : 0]);
		play.setToolTipText(running ? BotLocale.PAUSESCRIPT : active ? BotLocale.RESUMESCRIPT : BotLocale.PLAYSCRIPT);
		stop.setEnabled(active);
	}
}
