package hauntbrave.curse;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="Curses", description = "Curses the zammy monk at varrock castle", properties = "author=hauntbrave; topic=1337139; client = 4;")
public class Curse extends PollingScript<ClientContext> {
	private List<Task> taskList = new ArrayList<Task>();

	@Override
	public void start(){
		taskList.addAll(Arrays.asList(new Cast(ctx)));
	}

	@Override
	public void poll(){
		for (Task task: taskList){ 
			if (task.activate()) {
				task.execute();
			}
		}
	}
}
