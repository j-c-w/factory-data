package backend.java.swing;

/*
 * Created by Jackson Woodruff on 27/07/2014 
 * 
 */

import backend.scala.Backend;

import javax.swing.*;

public class SwingMain {
	private JList datalist;
	private JPanel root;

	public static void main(String[] args) {
		JFrame frame = new JFrame("SwingMain");
		frame.setContentPane(new SwingMain().root);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public SwingMain() {
		datalist.setListData(Backend.loadData());
	}
}
