package hauntbrave.ItsFire.tasks;

import org.powerbot.script.rt4.Bank.Amount;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Deposit extends Task<ClientContext> {

	private int logId;
	private Walker walk;

	public Deposit(ClientContext ctx, int logId, Walker walk) {
		super(ctx);
		this.logId = logId;
		this.walk = walk;
	}

	@Override
	public boolean activate(){
		int count = ctx.inventory.select().id(logId).count(true);
		boolean empty = count == 0;
		return empty;
	}

	@Override
	public void execute() { try{ bankDeposit(); Thread.sleep(2000); } 
				catch (InterruptedException e) { System.out.println("sleep failed"); }}
      private void bankDeposit() {

		try{

			if (!ctx.bank.open()){
				//step towards bank if cant open
				Tile bankLocation = ctx.bank.nearest().tile();
				int probability = Random.nextInt(0, 9);

				ctx.movement.step(bankLocation);
				ctx.camera.turnTo(bankLocation);
			}

			//deposit
			else {
				int sleep_time = Random.nextInt(2000, 3000);
				ctx.bank.withdraw(logId, Amount.ALL);
				Thread.sleep(sleep_time);
				ctx.bank.close();
				ctx.movement.running(true);
				if (walk.getToggle()) { 
						System.out.println("Set false"); 
						walk.setToggle(false); 
				}
				else { walk.setToggle(true); 
					System.out.println("Set true"); 
				}

			}

		}
		catch (InterruptedException e){
			System.out.println("sleep failed");	
		}
      }

}
