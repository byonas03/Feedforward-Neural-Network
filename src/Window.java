import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask; 

import javax.swing.JFrame;

public class Window extends JFrame {
	private static final long serialVersionUID = -5378369984725370798L;
	static Window w = new Window();
	static ArrayList<Network> networks = new ArrayList<>();
	static int runnum = 1;
	static int xg = 0;
	static String percent = "";
	
	public static double x,y,k;
	public static double ox, oy;
	
	public Window() {
		setTitle("Neural Network");
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(1000, 1000));
		setIgnoreRepaint(true);
		pack();
		setBackground(Color.BLACK);
		setVisible(true);
	}
	public static void main(String args[]) throws Exception {
		for (int i = 0; i < 1; i++) {
			networks.add(new Network(2, 2, 6, 4));
		}
		for (int i = 0; i < 500000; i++) {
			iterate(Math.random(), Math.random());
		}
		for (double i = 0; i < 1; i += .01) {
			for (double j = 0; j < 1; j += .01) {
				iterate(i,j);
				Thread.sleep(1);
			}
		}
		while (true) {
		Scanner in = new Scanner(System.in);
        double x = Double.parseDouble(in.nextLine());
        double y = Double.parseDouble(in.nextLine());
        iterate(x,y); 
		}
	}
	
	public static void iterate(double xx, double yy) throws Exception {
		//input stuff
		for (int u = 0; u < networks.size(); u++) {
		double[] inp = new double[networks.get(u).in];
		boolean val = false;
		double sum = 0;
		inp[0] = xx;
		inp[1] = yy;
		x = inp[0];
		y = inp[1];
		int[] inpcor = new int[2];
		if (x > .5 && y > .5)
			inpcor[0] = 1;
		else
			inpcor[1] = 1;
		// System.out.println(Arrays.toString(inpcor));
		// System.out.println("Input: " + Arrays.toString(inp));
		// System.out.println("Correct Output: " + Arrays.toString(inpcor));
		String out = networks.get(u).run(inp, inpcor, runnum);
		if (runnum != 0)
		percent = (100 * Double.parseDouble(out.substring(0, out.indexOf(" "))) + "");
		if (percent.length() > 5)
		percent = percent.substring(0,5);
		runnum++;
		w.repaint();
		}
	}
	
	public void paint(Graphics g) {
		if (networks.get(0).outputneurons.get(0).getActivation() > networks.get(0).outputneurons.get(1).getActivation())
		g.setColor(new Color(125, 125, (int)(255 * 1)));
		else
		g.setColor(new Color(255, 125, 0));
		g.fillRect((int)(x * 1000), (int)(1000-(y * 1000)), 25, 25);
		/*
		if (runnum % 30 == 0) {
		for (int i = 0; i < networks.get(0).getNeurons(0).size(); i++) {
			g.setColor(new Color((int)(networks.get(0).getNeurons(0).get(i).getActivation() * 200), 3, 255 - ((int)(networks.get(0).getNeurons(0).get(i).getActivation() * 200))));
			g.fillOval(50, i * 100 + 500, 25, 25);
		}
		for (int i = 0; i < networks.get(0).getNeurons(1).size(); i++) {
			g.setColor(new Color((int)(networks.get(0).getNeurons(1).get(i).getActivation() * 200), 3, 255 - ((int)(networks.get(0).getNeurons(1).get(i).getActivation() * 200))));
			g.fillOval(200, i * 100 + 400, 25, 25);
		}
		for (int i = 0; i < networks.get(0).getNeurons(2).size(); i++) {
			g.setColor(new Color((int)(networks.get(0).getNeurons(2).get(i).getActivation() * 200), 3, 255 - ((int)(networks.get(0).getNeurons(2).get(i).getActivation() * 200))));
			g.fillOval(350, i * 100 + 500, 25, 25);
		}
		for (int i = 0; i < networks.get(0).getNeurons(3).size(); i++) {
			g.setColor(new Color((int)(networks.get(0).getNeurons(3).get(i).getActivation() * 200), 3, 255 - ((int)(networks.get(0).getNeurons(3).get(i).getActivation() * 200))));
			g.fillOval(500, i * 100 + 500, 25, 25);
		}
		
		for (int i = 0; i < networks.get(0).getWeights(0).size(); i++) {
			g.setColor(new Color((int)(Network.tanhmod(networks.get(0).getWeights(0).get(i).getWeightValue()) * 200), 3, 255 - ((int)(Network.tanhmod(networks.get(0).getWeights(0).get(i).getWeightValue()) * 200))));
			g.drawLine(200 + 12, 400 + (i / 2) * 100 + 12, 50 + 12, 500 + (i % 2) * 100 + 12);
			g.drawLine(200 + 13, 400 + (i / 2) * 100 + 13, 50 + 13, 500 + (i % 2) * 100 + 12);
			g.drawLine(200 + 14, 400 + (i / 2) * 100 + 14, 50 + 14, 500 + (i % 2) * 100 + 12);
		}
		for (int i = 0; i < networks.get(0).getWeights(1).size(); i++) {
			g.setColor(new Color((int)(Network.tanhmod(networks.get(0).getWeights(1).get(i).getWeightValue()) * 200), 3, 255 - ((int)(Network.tanhmod(networks.get(0).getWeights(1).get(i).getWeightValue()) * 200))));
			g.drawLine(350 + 12, 500 + (i / 4) * 100 + 12, 200 + 12, 400 + (i % 4) * 100 + 12);
			g.drawLine(350 + 13, 500 + (i / 4) * 100 + 12, 200 + 13, 400 + (i % 4) * 100 + 12);
			g.drawLine(350 + 14, 500 + (i / 4) * 100 + 12, 200 + 14, 400 + (i % 4) * 100 + 12);
		}
		for (int i = 0; i < networks.get(0).getWeights(2).size(); i++) {
			g.setColor(new Color((int)(Network.tanhmod(networks.get(0).getWeights(2).get(i).getWeightValue())  * 200), 3, 255 - ((int)(Network.tanhmod(networks.get(0).getWeights(2).get(i).getWeightValue()) * 200))));
			g.drawLine(500 + 12, 500 + (i / 2) * 100 + 12, 350 + 12, 500 + (i % 2) * 100 + 12);
			g.drawLine(500 + 13, 500 + (i / 2) * 100 + 12, 350 + 13, 500 + (i % 2) * 100 + 12);
			g.drawLine(500 + 14, 500 + (i / 2) * 100 + 12, 350 + 14, 500 + (i % 2) * 100 + 12);
		}
		}
		xg++;
		for (int i = 0; i < networks.size(); i++) {
		g.setColor(Color.BLACK);
		g.drawLine(xg, (int)(networks.get(i).graphnum) + 25, xg-1, (int)(networks.get(i).graphnumold) + 27);
		g.drawLine(0, (int)(375), 1366/2, (int)(375));
		g.drawLine(1366/2, 0, 1366/2, 768);
		networks.get(i).graphnumold = networks.get(i).graphnum;
		if (xg > 1366/2 || runnum == 1) {
			xg = 0;
			g.setColor(Color.WHITE);
			g.fillRect(0,0,1366/2,768);
			g.setColor(Color.BLACK);
			g.drawLine(1366/2, 27, 1366/2 - 10, 27);
			g.drawLine(1366/2, 62, 1366/2 - 10, 62);
			g.drawLine(1366/2, 97, 1366/2 - 10, 97);
			g.drawLine(1366/2, 132, 1366/2 - 10, 132);
			g.drawLine(1366/2, 167, 1366/2 - 10, 167);
			g.drawLine(1366/2, 202, 1366/2 - 10, 202);
			g.drawLine(1366/2, 237, 1366/2 - 10, 237);
			g.drawLine(1366/2, 272, 1366/2 - 10, 272);
			g.drawLine(1366/2, 307, 1366/2 - 10, 307);
			g.drawLine(1366/2, 342, 1366/2 - 10, 342);	
		}
		*/
		}
}
