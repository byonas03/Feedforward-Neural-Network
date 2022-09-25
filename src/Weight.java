
public class Weight {
	private double val;
	private Neuron neuron1;
	private Neuron neuron2;
	public Weight(Neuron n1, Neuron n2) {
		neuron1 = n1;
		neuron2 = n2;
		val = Math.random() * 6 - 3;
	}
	 
	public Neuron getNeuron1() {
		return neuron1;
	}
	public Neuron getNeuron2() {
		return neuron2;
	}
	public double getWeightValue() {
		return val;
	}
	public void changeVal(double change) {
		val += change;
	} 
}
