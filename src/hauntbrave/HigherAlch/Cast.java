package hauntbrave.HigherAlch;

import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Magic;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Cast extends Task<ClientContext> {
	private int itemId;
	private final int natureRuneId = 561;

	private int numCasts = 0;
	private int itemCount = 0;
	private int natureCount = 0;

	public Cast(ClientContext ctx, int itemId) {
		super(ctx);
		this.itemId = itemId;
	}
	@Override
	//always activated
	public boolean activate(){
		natureCount = ctx.inventory.select().id(natureRuneId).count();
		itemCount = ctx.inventory.select().id(itemId).count(); 

		if (natureCount == 0 || itemCount == 0) { ctx.controller.stop(); }

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
		    ctx.magic.cast(Magic.Spell.HIGH_ALCHEMY);

		    Thread.sleep(500);

		    ctx.inventory.select().id(itemId).poll().click(true);
		    Thread.sleep(1600);

		    System.out.println("Casts: " + ++numCasts);

			//rest for a minute to 5 minutes 
		    int breakAmount = Random.nextInt(100, 200);
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

      public void setItemId(int id) { itemId = id; }
}

