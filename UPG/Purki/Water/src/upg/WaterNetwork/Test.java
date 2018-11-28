package upg.WaterNetwork;

public class Test {
	
	
	public static void main(String[] args) throws InterruptedException {
		WaterNetwork wn = new WaterNetwork();
		
		System.out.println("Running at normal speed.");
		runSimulation(wn);
		
		System.out.println("Running at fast speed.");
		wn.runFast();
		runSimulation(wn);
		
		System.out.println("Running at normal speed.");
		wn.runNormal();
		runSimulation(wn);
	}

	private static void runSimulation(WaterNetwork wn) throws InterruptedException {
		for (int i = 0; i < 100; i++)
		{			
			wn.updateState();	//updateState of the system 			
			wn.printState();
			
			Thread.sleep(20);	//small delay			
		}
	}
}
