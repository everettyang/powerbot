package hauntbrave.ZamWine.tasks;

import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Magic;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.TileMatrix;
import org.powerbot.script.rt4.ClientContext;

import hauntbrave.ZamWine.utils.WorldHopper;

public class Grab extends Task<ClientContext>{

	private final int numFailsBeforeHop = 4;
	private	final int wineId = 245;
	private	final int lawRuneId = 563;
	private final int [] bounds = new int[]{-16,16,-136,-100,-16,16};

	private GroundItem wine = null;

	private TileMatrix table = new TileMatrix(ctx, new Tile(2930, 3515));
	private TileMatrix runAway = new TileMatrix(ctx, new Tile(2942, 3490));

	private int wineCount;

	private WalktoWine walk;
	private WorldHopper worldHopper;

	private int inARow = 0;
	private int wineGrabbed = 0;
	private int failedGrabs = 0;

	public Grab(ClientContext ctx, WalktoWine walk, WorldHopper worldHopper) {
		super(ctx);
		this.walk = walk;
		this.worldHopper = worldHopper;
	}
	@Override
	public boolean activate(){
		//activates if inventory is not full and if not walking
		wineCount = ctx.inventory.select().id(wineId).count();
		boolean bool = ctx.inventory.select().count() < 28 
				&& !walk.getWalking();
		if (!bool) { failedGrabs = 0; }
		return bool;
	}

	@Override
	public void execute() {

		try {

			int randInt;
			int probability = Random.nextInt(0, 5);

			//if one law rune or less left, stop script.
			if (ctx.inventory.select().id(lawRuneId).count(true) <= 1) {  ctx.controller.stop(); }

			//check if world hop is needed
			if (failedGrabs < numFailsBeforeHop)
			{

					if (wine == null) {

						//this sets up the waiting configuration
						ctx.magic.cast(Magic.Spell.TELEKINETIC_GRAB);

						//random camera turn. 0 is arbitrary
						if (probability == 0){
							randCamera();
						}

						//hovers mouse over tile before click
						table.hover();
					}


				/* I am aware that while looping is terrible but it is substantially faster than poll. */
				do {

					wine = ctx.groundItems.select().id(wineId).poll();

				} while ((!wine.valid()) && (!ctx.controller.isStopping()));

				wine.bounds(bounds);
				wine.click(true);

				wine = null;
				randInt = Random.nextInt(5000, 5500);
				Thread.sleep(randInt);
				//checks if wine is grabbed and sets appropriate variable
				if (wineCount < ctx.inventory.select().id(wineId).count())
				{
					wineGrabbed++;
					//reset fails if grabbed 5 in a row
					if (++inARow % 5 == 0) { failedGrabs = 0; inARow = 0; }
				}

				else
				{
					randCamera();
					failedGrabs++;
				}

				//random camera turn. 3 is arbitrary.
				if (probability == 3){
					randCamera();
				}
			}
			
			else
			{
				if (worldHopper.hopWorld())  { failedGrabs = 0; } 
			}

		}

		catch (InterruptedException e){
			System.out.println("sleep failed");	
		}

	}

      private void randCamera() {

		int angle = Random.nextInt(0, 180);
		int pitch = Random.nextInt(70, 99);

		ctx.camera.pitch(pitch);
		ctx.camera.angle(angle);
	}

     public int getGrabbed() { return wineGrabbed; }

    }
