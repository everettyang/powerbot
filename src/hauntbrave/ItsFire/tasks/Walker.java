package hauntbrave.ItsFire.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.Area;
import org.powerbot.script.rt4.TileMatrix;
import org.powerbot.script.rt4.ClientContext;

public class Walker extends Task<ClientContext> {

	private int logId;
	private boolean toggle = false;
	private TileMatrix destination = new TileMatrix(ctx, new Tile(3125, 3263));
	private TileMatrix destination1 = new TileMatrix(ctx, new Tile(3125, 3264));
	private Area area = new Area(new Tile(3126, 3264, 0), new Tile(3090, 3263));
	private Area area1 = new Area(new Tile(3126, 3265, 0), new Tile(3090, 3264));
	private boolean walking = false;

	public Walker(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate(){
		System.out.println(ctx.players.local().tile());
		System.out.println("x: " + area.getRandomTile().x());
		System.out.println("y: " + area.getRandomTile().y());
			 
		if (toggle) { 
			walking = (ctx.inventory.select().id(logId).count() > 0) &&
			 !area.contains(ctx.players.local().tile()); 
		}
		else { 
			walking = (ctx.inventory.select().id(logId).count() > 0) &&
			 !area1.contains(ctx.players.local().tile()); 
		}

		return walking;
	}

	@Override
	public void execute() {
		try {
			if (toggle)
				ctx.movement.step(destination);
			else 
				ctx.movement.step(destination1);
			Thread.sleep(Random.nextInt(1000, 2000));
		}
		catch (InterruptedException e) { System.out.println("sleep failed"); }
	}
     
    public boolean getWalking() { return walking; }

    public void setLogId(int var) { logId = var; }
    public void setToggle(boolean var) { toggle = var; }
    public boolean getToggle() { return toggle; }

}
