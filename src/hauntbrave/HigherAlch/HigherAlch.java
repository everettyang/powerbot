package hauntbrave.HigherAlch;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import HigherAlch.SelectItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="HigherAlch", description = "Just alchs", properties = "author=hauntbrave; topic=1337138; client = 4;")
public class HigherAlch extends PollingScript<ClientContext> {
	private List<Task> taskList = new ArrayList<Task>();
	SelectItem selectItem = new SelectItem(ctx);

	@Override
	public void start(){
		selectItem.show();
		taskList.addAll(Arrays.asList(new Cast(ctx, selectItem.getItemId())));
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
