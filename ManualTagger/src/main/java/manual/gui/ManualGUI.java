package manual.gui;

import manual.files.Driver;
import manual.models.TaggableToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ManualGUI {
	public JPanel MainPanel;

	private JButton openDirectoryButton;
	private JTextField filePath;
	private JButton startButton;
	private JTextPane tokens;
	private JTextArea tagInput;
	private JButton tagButton;
	private JLabel fileName;
	private JButton nextButton;

	private TagAutoCompleter autoCompleter;

	private boolean running = false;

	Driver driver;

	public ManualGUI() {
		MainPanel.setPreferredSize(new Dimension(700, 150));

		openDirectoryButton.addActionListener((e) -> openDirectory());
		startButton.addActionListener((e) -> startStop());
		tagButton.addActionListener((e) -> tag());
		nextButton.addActionListener((e) -> next());

		autoCompleter = new TagAutoCompleter(tagInput);

		InputMap im = tagInput.getInputMap();
		ActionMap am = tagInput.getActionMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), "tag");
		am.put("tag", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				tag();
			}
		});
	}

	private void openDirectory() {
		var directoryChooser = new JFileChooser();
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = directoryChooser.showOpenDialog(MainPanel);

		if (result == JFileChooser.APPROVE_OPTION) {
			filePath.setText(directoryChooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void stop() {
		startButton.setText("Start");
		openDirectoryButton.setEnabled(true);
		clearTagArea();

		running = false;
	}

	private void start() {
		startButton.setText("Stop");
		openDirectoryButton.setEnabled(false);

		running = true;

		try {
			driver = new Driver(filePath.getText());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(MainPanel, "Invalid Directory");
			stop();
			return;
		}

		showNextTag();
	}

	private void next() {
		showNextTag();
	}

	private void startStop() {
		if (running) {
			stop();
		} else {
			if ("".equals(filePath.getText())) {
				return;
			}

			start();
		}
	}

	private void showNextTag() {
		TaggableToken nextTaggable = null;

		try {
			nextTaggable = driver.getNext();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (nextTaggable == null) {
			stop();
			return;
		}

		StringBuilder sb = new StringBuilder();

		for (String pre : nextTaggable.preContext) {
			sb.append(pre.replace("\\", "\\\\"));
			sb.append(" ");
		}

		sb.append("<b style='color: red'>");
		sb.append(nextTaggable.token.replace("<", "&lt").replace(">", "&gt"));
		sb.append("</b>");
		sb.append(" ");

		for (String post : nextTaggable.postContext) {
			sb.append(post);
			sb.append(" ");
		}

		fileName.setText(driver.getCurFileName());

		tokens.setText(sb.toString());

		tagInput.setEditable(true);
		tagButton.setEnabled(true);

		tagInput.requestFocus();
	}

	private void clearTagArea() {
		tokens.setText("");
		tagInput.setText("");
		fileName.setText("");

		tagInput.setEditable(false);
		tagButton.setEnabled(false);
	}

	private void tag() {
		String enteredTag = tagInput.getText();

		// Don't allow empty tags
		if (enteredTag.isEmpty()) {
			return;
		}

		// If a tag is not in the list, make sure the user actually intended to add it to the tag list
		if (!autoCompleter.getTagList().getTags().contains(enteredTag)) {
			String message = "The tag \"" + enteredTag + "\" is not in the tag list. Add it?";
			int result = JOptionPane.showConfirmDialog(MainPanel, message, "New tag", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}

		// If it's a new tag, add it to the list and save it
		boolean newTag = autoCompleter.getTagList().addTag(enteredTag);
		if (newTag) {
			try {
				autoCompleter.getTagList().save();
			} catch (IOException e) {
				e.printStackTrace();

				JOptionPane.showMessageDialog(MainPanel, "Failed to update file");
			}
		}

		try {
			driver.updateCurrent(enteredTag);
		} catch (IOException e) {
			e.printStackTrace();
		}

		clearTagArea();
		showNextTag();
	}
}
