package hauntbrave.Curse;
import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

// uses type param so it will work with RS3 and ORSR
public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();
}
