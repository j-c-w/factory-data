package backend.java;

/*
 * Created by Jackson Woodruff on 26/07/2014 
 * 
 */

import backend.scala.Backend;

import javax.swing.*;

public class Main {
	private JPanel root;
	private JList dataList;

	String[] data;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Main");
		frame.setContentPane(new Main().dataList);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public Main() {
		super();
		root = new JPanel();
		dataList = new JList();
		root.add(dataList);
		data = Backend.loadData();
		dataList.setListData(data);

	}
}
