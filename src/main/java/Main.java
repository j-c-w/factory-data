package main.java;

import main.scala.Backend;
import main.scala.datatypes.LineListObject;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Created by Jackson Woodruff on 20/07/2014 
 * 
 */

public class Main {
	private JList dataList;
	private JPanel root;
	private JButton search;
	private JTextField searchBar;

	LineListObject[] data;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Main");
		frame.setContentPane(new Main().root);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public Main() {
		super();
		data = Backend.loadData();
		dataList.setListData(data);

		search.setText("search");
		search.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//dataList.setListData(Backend.filterData(data, Integer.parseInt(searchBar.getText())));
			}
		});


	}


}
