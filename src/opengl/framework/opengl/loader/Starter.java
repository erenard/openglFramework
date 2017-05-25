package opengl.framework.opengl.loader;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import opengl.framework.test.selection.Selection3DTest;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class Starter extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton goButton = new JButton("GO");
	private JComboBox comboBox = new JComboBox();
	private JCheckBox checkBox = new JCheckBox();
	
	public Starter() {
		LayoutManager layoutManager = new FlowLayout();
		this.setLayout(layoutManager);
		{
			JPanel panel = new JPanel(new FlowLayout());
			panel.add(comboBox);
			panel.add(checkBox);
			JLabel label = new JLabel("fullscreen");
			panel.add(label);
			this.add(panel);
		}
		goButton.addActionListener(this);
		this.add(goButton);

		this.setTitle("Options");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(300, 80);
		try {
			DisplayMode[] displayModeTab = Display.getAvailableDisplayModes();
			SortedSet<DisplayMode> displayModes = new TreeSet<DisplayMode>(
					new DisplayModeComparator());
			for (DisplayMode displayMode : displayModeTab) {
				displayModes.add(displayMode);
			}

			List<String> items = new ArrayList<String>();
			for (DisplayMode displayMode : displayModes) {
				String displayModeName = displayMode.getWidth() + "x" + displayMode.getHeight();
				if (!items.contains(displayModeName))
					items.add(displayModeName);
			}

			for (String item : items) {
				comboBox.addItem(item);
			}

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}

	public static void main(String [] args) {
		try {
			Loader.appendNativePathToUserPath("native");
			new Starter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(goButton)) {
			String displayMode = (String) comboBox.getItemAt(comboBox.getSelectedIndex());
			String [] tab = displayMode.split("x");
			boolean fullscreen = checkBox.isSelected();
			this.setVisible(false);
			new Selection3DTest(new Integer(tab[0]), new Integer(tab[1]), fullscreen);
		}
	}

}
