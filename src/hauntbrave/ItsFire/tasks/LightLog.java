package hauntbrave.ItsFire.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.awt.Point;

public class LightLog extends Task<ClientContext>{

	private int logId;
	private int experience;
	private Walker walk;
	private Item tinderBox = ctx.inventory.select().id(590).poll();

	private int logCount;

	public LightLog(ClientContext ctx, int logId, Walker walk) {
		super(ctx);
		this.logId = logId;
		this.walk = walk;
	}
	@Override
	public boolean activate(){
		//activates if inventory is not full and if not walking
		experience = ctx.skills.experience(11);
		logCount = ctx.inventory.select().id(logId).count();	
		return logCount > 0 && !walk.getWalking();
	}

	@Override
	public void execute() {

		try {

			Item logs = ctx.inventory.select().id(logId).poll();
			int x = tinderBox.centerPoint().x - 11 + Random.nextInt(0, 25);
			int y = tinderBox.centerPoint().y - 12 + Random.nextInt(0, 25);

			int x1 = logs.centerPoint().x - 11 + Random.nextInt(0, 25);
			int y1 = logs.centerPoint().y - 12 + Random.nextInt(0, 25);

			int randInt;
			int probability = Random.nextInt(0, 10);

			tinderBox.hover();
			ctx.input.click(new Point(x,y), true);

			//random camera turn. 0 is arbitrary
			Thread.sleep(500);

			//hovers mouse over tile before click
			

			logs.hover();
			ctx.input.click(new Point(x1, y1), true);

			if (probability == 0){
				randCamera();
			}

			do { ; } while (!(ctx.skills.experience(11) > experience) && !ctx.controller.isStopping());

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


    }
