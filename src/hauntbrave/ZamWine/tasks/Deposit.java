package hauntbrave.ZamWine.tasks;

import org.powerbot.script.rt4.Magic;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.Tile;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

public class Deposit extends Task<ClientContext> {

	private Tile bankLocation;
	private Bank bank;

	private boolean teleported = false;

	public Deposit(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate(){
		int count = ctx.inventory.select().count();
		boolean full = count == 28;
		
		if (!full) { teleported = false; }
		return full;
	}

	@Override
	public void execute() {

		try {


			if (!teleported) {

				//cast tele, turn run on, locate and run to bank
				int randInt = Random.nextInt(2000, 3000);

				ctx.magic.cast(Magic.Spell.FALADOR_TELEPORT);
				teleported = true;

				Thread.sleep(randInt);
				
				ctx.movement.running(true);

				bankLocation = ctx.bank.nearest().tile();
				bank = ctx.bank;

			}


			Thread.sleep(Random.nextInt(2000, 3000));
			bankDeposit();
		}

		catch (InterruptedException e){
			System.out.println("sleep failed");	
		}

				
	}

      private void bankDeposit() {

		try{
			if (!bank.open()){
				//step towards bank if cant open
				int probability = Random.nextInt(0, 9);

				ctx.movement.step(bankLocation);

				if (probability == 0)
					ctx.camera.turnTo(bankLocation);
			}

			//deposit
			else {
				int sleep_time = Random.nextInt(2000, 3000);
				bank.depositAllExcept("Law rune", "Water rune");
				Thread.sleep(sleep_time);
				bank.close();
			}

		}
		catch (InterruptedException e){
			System.out.println("sleep failed");	
		}
      }

}
