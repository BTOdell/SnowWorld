package snowworld;

public class TimedEvent implements Tickable {

	private final Runnable runnable;
	private final Function<Long> dueTime;
	private final Function<Long> periodTime;
	
	private Long nextTime;
	
	public TimedEvent(Runnable runnable, Function<Long> dueTime, Function<Long> periodTime) {
		this.runnable = runnable;
		this.dueTime = dueTime;
		this.periodTime = periodTime;
	}
	
	public void start() {
		this.nextTime = System.nanoTime() + this.dueTime.invoke();
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
			this.nextTime += periodTime.invoke();
		}
	}
	
}
