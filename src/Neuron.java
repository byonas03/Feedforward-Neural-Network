
public class Neuron {
	private int type;
	private double activation;
	private double bias;
	private double recChange;
	
	public Neuron(int t) {
		type = t; 
		activation = 0;
		bias =  Math.random() * 6 - 3;
	}
	
	public double getActivation() {
		return activation;
	}
	public void setActivation(double act) {
		activation = act;
	}
	public double getBias() {
		return bias;
	 }
	public void changeActivation(double change) {
		if (activation + change >= 0)
			activation += change;
		else
			activation = 0;
	}
	public void changeBias(double change) {
		bias += change;
	}
	public double getRecChange() {
		return recChange;
	}
	public void changeRecChange(double rc) {
		recChange = rc;
	}
	public void resetRecChange() {
		recChange = 0;
	}
}
