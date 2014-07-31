package backend.java.chartPatch;

public class Driver {
	public static void main(String[] args) throws Exception  {
		try {
			CategoryLabelDemo.run("Category chart with tick label truncation");
		}
        catch (Exception excep) {
        	System.out.println("Except caught in MAIN");
        	excep.printStackTrace();
        }
		
	}
}
