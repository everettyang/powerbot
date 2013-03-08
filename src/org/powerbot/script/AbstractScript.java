package org.powerbot.script;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import org.powerbot.script.xenon.util.Random;

/**
 * An abstract implementation of {@code Script}.
 *
 * @author Paris
 */
public abstract class AbstractScript implements Script {
	protected final Logger log = Logger.getLogger(getClass().getName());
	private final Map<State, Collection<FutureTask<Boolean>>> tasks;
	private ScriptController controller;

	public AbstractScript() {
		tasks = new ConcurrentHashMap<State, Collection<FutureTask<Boolean>>>(State.values().length);

		for (final State state : State.values()) {
			tasks.put(state, new ArrayDeque<FutureTask<Boolean>>());
		}

		tasks.get(State.START).add(new FutureTask<Boolean>(this, true));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Collection<FutureTask<Boolean>> getTasks(final State state) {
		return tasks.get(state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getPriority() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ScriptController getScriptController() {
		return controller;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setScriptController(final ScriptController controller) {
		this.controller = controller;
	}

	public void sleep(final int millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
		}
	}

	public void sleep(final int min, final int max) {
		sleep(Random.nextInt(min, max));
	}
}
