package snowworld;

public class Wind {

	double velocity = Methods.random(-3.0D, 3.0D);
	
	public Wind() {}
	
	public void change() {
		if (Methods.random(0, 2) == 0) {
			increase();
		} else {
			decrease();
		}
	}
	
	private void decrease() {
		if (velocity - 1 <= -6.0D) {
			velocity = -6.0D;
		} else velocity -= Methods.random(0D, 1.0D);
	}
	
	private void increase() {
		if (velocity + 1 >= 6.0D) {
			velocity = 6.0D;
		} else velocity += Methods.random(0D, 1.0D);
	}
	
	public void apply(Flake flake) {
		flake.setX(flake.getX() + (velocity / flake.getWidth()));
	}
	
}