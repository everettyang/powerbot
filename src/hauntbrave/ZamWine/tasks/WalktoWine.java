package hauntbrave.ZamWine.tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.Random;
import org.powerbot.script.Locatable.WithinRange;
import org.powerbot.script.rt4.ClientContext;

public class WalktoWine extends Task<ClientContext> {

	private final Tile destination = new Tile(2931, 3515);
	private final WithinRange range = new WithinRange(destination, 3.0);
	private boolean walking;

	public WalktoWine(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate(){
		boolean bool = ctx.inventory.select().count() < 28 && 
				!range.accept(ctx.players.local().tile());
		walking = bool;	
		return bool;
	}

	@Override
	public void execute() {

		try {

			if (!ctx.controller.isStopping())
			{
				int probability = Random.nextInt(0, 9);
				int randInt = Random.nextInt(1000, 2000);

				ctx.movement.step(destination);

				if (probability == 0)
					ctx.camera.turnTo(destination);

				Thread.sleep(randInt);
			}
			
		}

		catch (InterruptedException e){
			System.out.println("sleep failed");	
		}

				
	}

	public boolean getWalking() { return walking; }
}
