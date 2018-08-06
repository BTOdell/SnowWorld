package snowworld;

public interface Tickable {
	
	void init();
	
	void tick(long currentTime, long deltaTime);
	
}
