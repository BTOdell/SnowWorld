import java.util.Random;

public class Methods {
	
	private static Random rand = new Random();
	
	public static int random(int min, int max) {
		int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : rand.nextInt(n));
	}
	
	public static float random(float min, float max) { /// 0 to 5
		return Math.min(min, max) + (rand.nextFloat() * Math.abs(max - min));
	}
	
	public static double random(double min, double max) { /// 0 to 5
		return Math.min(min, max) + (rand.nextDouble() * Math.abs(max - min));
	}
	
}