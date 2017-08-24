package hauntbrave.ZamWine.utils;

import org.powerbot.script.rt4.Worlds;
import org.powerbot.script.rt4.World;
import org.powerbot.script.rt4.World.Type;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldHopper extends ClientAccessor {

	private Worlds worlds;
	private World newWorld;
	private Type[] types;

	public WorldHopper(ClientContext ctx) {
		super(ctx);
	}

	public boolean hopWorld() {

		try {

			ctx.worlds.open();
			
			Thread.sleep(Random.nextInt(1000, 2000));
			
			newWorld = worlds.shuffle().peek();

			//recache if newWorld is bad
			if (newWorld.id() == -1) { worlds = worlds.select().types(types);
							newWorld = worlds.shuffle().peek(); }

			if (!ctx.controller.isStopping())
			{
				System.out.println("Hopping to world "  + newWorld.id() + "...");
				boolean hopped = newWorld.hop();
				Thread.sleep(2000, 3000);
				return hopped;
			}

		}

		catch (InterruptedException e){
			System.out.println("sleep failed");	
		}
		return false;
	}
	
	public void setWorlds(List<Type> types) { 

		/* a list of world types is also kept in this class
		in case recaching is needed*/

		this.types = new Type[types.size()];

		for (int i = 0; i < types.size(); ++i){
			this.types[i] = types.get(i);
		}

		worlds = ctx.worlds
			.select()
			.types(this.types);
	}
}
