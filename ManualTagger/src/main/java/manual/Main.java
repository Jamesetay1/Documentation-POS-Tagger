package manual;

import manual.gui.ManualGUI;

import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("ManualGUI");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		frame.setContentPane(new ManualGUI().MainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 200));
		frame.pack();
		frame.setVisible(true);
	}
}
