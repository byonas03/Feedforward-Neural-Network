import java.util.ArrayList;
import java.util.Arrays;

public class Network {
	private double cor = 0;
	private double cost, runs;
	private double bias;
	private double gamma;
	public int in, ou, on, tw;
	public ArrayList<Neuron> inputneurons = new ArrayList<Neuron>();
	public ArrayList<Neuron> outputneurons = new ArrayList<Neuron>();
	private ArrayList<Neuron> hl1neurons = new ArrayList<Neuron>();
	private ArrayList<Neuron> hl2neurons = new ArrayList<Neuron>();
	private ArrayList<Weight> ioweights = new ArrayList<Weight>();
	private ArrayList<Weight> otweights = new ArrayList<Weight>();
	private ArrayList<Weight> toweights = new ArrayList<Weight>();
	public double graphnum, graphnumold;

	private ArrayList<Double> vals = new ArrayList<Double>();
	public Network(int numin, int numout, int numhl1, int numhl2) {
		in = numin; ou = numout; on = numhl1; tw = numhl2;
		cost = runs = bias = 0;
		//Neuron Initialization
		for (int i = 0; i < numin; i++) {
			inputneurons.add(new Neuron(0));
			bias += inputneurons.get(i).getBias();
		}
		for (int i = 0; i < numout; i++) {
			outputneurons.add(new Neuron(1));
			bias += outputneurons.get(i).getBias();
		}
		for (int i = 0; i < numhl1; i++) {
			hl1neurons.add(new Neuron(2));
			bias += hl1neurons.get(i).getBias();
		}
		for (int i = 0; i < numhl2; i++) {
			hl2neurons.add(new Neuron(3));
			bias += hl2neurons.get(i).getBias();
		}
		//Weight Initialization
		for (int i = 0; i < hl1neurons.size(); i++) {
			for (int j = 0; j < inputneurons.size(); j++) {
				ioweights.add(new Weight(hl1neurons.get(i), inputneurons.get(j)));
			}
		}
		for (int i = 0; i < hl2neurons.size(); i++) {
			for (int j = 0; j < hl1neurons.size(); j++) {
				otweights.add(new Weight(hl2neurons.get(i), hl1neurons.get(j)));
			}
		}
		for (int i = 0; i < outputneurons.size(); i++) {
			for (int j = 0; j < hl2neurons.size(); j++) {
				toweights.add(new Weight(outputneurons.get(i), hl2neurons.get(j)));
			}
		}
		gamma = .3; 
	}
	public String run(double[] inp, int[] inpcor, int num) {
		double ret = 0;
		cor = 0;
		if (vals.size() > 10000) {
			vals.remove(0);
		}
		for (int i = 0; i < vals.size(); i++) {
			cor += vals.get(i);
		}
		ret = cor / (double)vals.size();
		if (vals.size() == 0)
			ret = 1;
		runs++;
		//Set Values of Input Activation
		for (int i = 0; i < inputneurons.size(); i++)
			inputneurons.get(i).setActivation(inp[i]);
		//Running with Current Weights/Biases 
		int x = 0;
		for (int i = 0; i < hl1neurons.size(); i++) {
			double total = 0;
			for (int j = 0; j < inputneurons.size(); j++) {
				total += (inputneurons.get(j).getActivation() * ioweights.get(x).getWeightValue());
				x++;
			}
			total += (hl1neurons.get(i).getBias());
			hl1neurons.get(i).setActivation(tanhmod(total));
		}
		x = 0;
		for (int i = 0; i < hl2neurons.size(); i++) {
			double total = 0;
			for (int j = 0; j < hl1neurons.size(); j++) {
				total += (hl1neurons.get(j).getActivation() * otweights.get(x).getWeightValue());
				x++;
			}
			total += (hl2neurons.get(i).getBias());
			hl2neurons.get(i).setActivation(tanhmod(total));
		}
		x = 0;
		for (int i = 0; i < outputneurons.size(); i++) {
			double total = 0;
			for (int j = 0; j < hl2neurons.size(); j++) {
				total += (hl2neurons.get(j).getActivation() * toweights.get(x).getWeightValue());
				x++;
			}
			total += (outputneurons.get(i).getBias());
			outputneurons.get(i).setActivation(tanhmod(total));
		}

		//Cost Calculation
		double c = 0;
		for (int i = 0; i < inpcor.length; i++)
			c += Math.pow((outputneurons.get(i).getActivation() - inpcor[i]), 2);
		cost += c;
		//System.out.println(c);
		//Learning Portion
		if (num < 300000) {
			for (int i = 0; i < outputneurons.size();  i++) {
				double curAct = outputneurons.get(i).getActivation();
				double cv = (curAct * (1 - curAct)) * (2 * (curAct - inpcor[i]));
				outputneurons.get(i).changeBias(-cv * gamma * (c * 2));
			}
			for (int i = 0; i < hl2neurons.size(); i++) 
				hl2neurons.get(i).resetRecChange();
			for (int i = 0 ; i < toweights.size(); i++) {
				Weight w = toweights.get(i);
				double cv = w.getNeuron2().getActivation() * (w.getNeuron1().getActivation() * (1 - w.getNeuron1().getActivation())) * (2 * (w.getNeuron1().getActivation() - inpcor[i / ((tw * ou) / ou)]));
				toweights.get(i).changeVal(-cv * gamma * (c * 2));
				double rc = w.getWeightValue() * (w.getNeuron1().getActivation() * (1 - w.getNeuron1().getActivation())) * (2 * (w.getNeuron1().getActivation() - inpcor[i / ((tw * ou) / ou)]));
				toweights.get(i).getNeuron2().changeRecChange(rc);
			}
			for (int i = 0; i < hl2neurons.size(); i++) {
				double curAct = hl2neurons.get(i).getActivation();
				double cv = (curAct * (1 - curAct)) * hl2neurons.get(i).getRecChange();
				hl2neurons.get(i).changeBias(-cv * gamma * (c * 2));
			}
			for (int i = 0; i < hl1neurons.size(); i++) 
				hl1neurons.get(i).resetRecChange();
			for (int i = 0; i < otweights.size(); i++) {
				Weight w = otweights.get(i);
				double cv = w.getNeuron2().getActivation() * (w.getNeuron1().getActivation() * (1 - w.getNeuron1().getActivation())) * w.getNeuron1().getRecChange();
				otweights.get(i).changeVal(-cv * gamma * (c * 2));
				double rc = w.getWeightValue() * (w.getNeuron1().getActivation() * (1 - w.getNeuron1().getActivation())) * w.getNeuron1().getRecChange();
				otweights.get(i).getNeuron2().changeRecChange(rc);
			}
			for (int i = 0; i < hl1neurons.size(); i++) {
				double curAct = hl1neurons.get(i).getActivation();
				double cv = (curAct * (1 - curAct)) * hl1neurons.get(i).getRecChange();
				hl1neurons.get(i).changeBias(-cv * gamma * (c * 2));
			}
			for (int i = 0; i < ioweights.size(); i++) {
				Weight w = ioweights.get(i);
				double cv = w.getNeuron2().getActivation() * (w.getNeuron1().getActivation() * (1 - w.getNeuron1().getActivation())) * w.getNeuron1().getRecChange();
				ioweights.get(i).changeVal(-cv * gamma * (c * 2));
			}
		}
		if ((outputneurons.get(0).getActivation() > outputneurons.get(1).getActivation())) {
			Window.k = 1;
		} else { 
			Window.k = 0;
		}
		if ((outputneurons.get(0).getActivation() > outputneurons.get(1).getActivation() && inpcor[0] > inpcor[1]) || (outputneurons.get(0).getActivation() < outputneurons.get(1).getActivation() && inpcor[0] < inpcor[1])) {
			vals.add(1.0);
		} else {
			vals.add(0.0);
			//System.out.println(inputneurons.get(0).getActivation() + "/" + inputneurons.get(1).getActivation() + "/output: " + outputneurons.get(0).getActivation() + "/" + outputneurons.get(1).getActivation());
		}
		System.out.println(ret + "|" + outputneurons.get(0).getActivation() + "|" + outputneurons.get(1).getActivation() + "/" + Arrays.toString(inp) + "/" + Arrays.toString(inpcor) + "/" + num); 
		if (num != 0)
			graphnum = (Math.abs(ret * 100 - 100)) * 3.5;
		if (outputneurons.get(0).getActivation() > outputneurons.get(1).getActivation()) {
			return ret + " 1";
		} else { 
			return ret + " 0";
		}
	}

	public static double tanhmod(double x) {
		return 1/(1+Math.pow(Math.E, -x));
	}

	public ArrayList<Weight> getWeights(int code) {
		if (code == 0)
			return ioweights;
		else if (code == 1)
			return otweights;
		else if (code == 2)
			return toweights;
		return null;
	}
	public ArrayList<Neuron> getNeurons(int code) {
		if (code == 0)
			return inputneurons;
		else if (code == 1)
			return hl1neurons;
		else if (code == 2)
			return hl2neurons;
		else if (code == 3)
			return outputneurons;
		return null;
	}
}
