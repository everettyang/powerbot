package hauntbrave.Curse;

import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Magic;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Cast extends Task<ClientContext> {
	private final int monkId = 2886;
	private final int bodyRuneId = 559;

	private int numCasts = 0;
	private int bodyCount = 0;

	public Cast(ClientContext ctx) {
		super(ctx);
	}
	@Override
	//always activated
	public boolean activate(){
		bodyCount = ctx.inventory.select().id(bodyRuneId).count(true);
		return true;
	}

	@Override
	public void execute() {

		ArrayList<List<Integer>> points = new ArrayList<List<Integer>>();

		points.add(Arrays.asList(70,85));
		points.add(Arrays.asList(295,99));
		points.add(Arrays.asList(233,80));
		points.add(Arrays.asList(274,76));

		int interval = Random.nextInt(2, 5);

		if (numCasts % interval == 0){

				List<Integer> position = points.get(interval - 1);
				move_camera(position.get(0), position.get(1));

			}

		try        
		{
		    //wait between 1 and 3 seconds
		    ctx.magic.cast(Magic.Spell.CURSE);

		    ctx.npcs.select().id(monkId).poll().click(true);
		    Thread.sleep(800);
			
		    numCasts++;

		    int breakAmount = Random.nextInt(200, 300);
		    int breakTime = Random.nextInt(60000, 300000);

		    if (numCasts == breakAmount) { 
				numCasts = 0; 
				System.out.println("Taking a break for " + breakTime / 60000 + " minutes...");
				Thread.sleep(breakTime);
		    }

		} 

		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
	}

      private void move_camera(int angle, int pitch) {
		ctx.camera.pitch(pitch);
		ctx.camera.angle(angle);
	}

}

