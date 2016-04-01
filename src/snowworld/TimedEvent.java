package snowworld;

import java.util.function.Supplier;

public class TimedEvent implements Tickable {

	private final Runnable runnable;
	private final Supplier<Long> dueTime;
	private final Supplier<Long> periodTime;
	
	private Long nextTime;
	
	public TimedEvent(Runnable runnable, Supplier<Long> dueTime, Supplier<Long> periodTime) {
		this.runnable = runnable;
		this.dueTime = dueTime;
		this.periodTime = periodTime;
	}
	
	public void start() {
		this.nextTime = System.nanoTime() + this.dueTime.get();
	}
	
	public void stop() {
		this.nextTime = null;
	}
	
	@Override
	public void init() {}

	@Override
	public void tick(long currentTime, long deltaTime) {
		if (this.nextTime == null) {
			return;
		}
		while (currentTime >= this.nextTime) {
			this.runnable.run();
			this.nextTime += this.periodTime.get();
		}
	}
	
}
