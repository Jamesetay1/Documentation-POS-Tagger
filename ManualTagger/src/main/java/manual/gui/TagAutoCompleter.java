package manual.gui;

import manual.files.TagList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class TagAutoCompleter implements DocumentListener {
	private enum Mode {
		INSERT,
		COMPLETION
	}

	private TagList tagList;

	private JTextArea textArea;
	private Mode mode;

	public TagAutoCompleter(JTextArea textArea) {
		try {
			tagList = TagList.loadTagList();
		} catch (IOException e) {
			tagList = new TagList();
		}

		mode = Mode.INSERT;

		this.textArea = textArea;
		textArea.getDocument().addDocumentListener(this);

		InputMap im = textArea.getInputMap();
		ActionMap am = textArea.getActionMap();
		im.put(KeyStroke.getKeyStroke("TAB"), "commit");
		am.put("commit", new CommitAction());
	}

	public TagList getTagList() {
		return tagList;
	}

	@Override
	public void insertUpdate(DocumentEvent event) {
		if (event.getLength() != 1) {
			return;
		}

		int pos = event.getOffset();
		String content = null;
		try {
			content = textArea.getText(0, pos + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		int firstSpace;
		for (firstSpace = pos; firstSpace >= 0; firstSpace--) {
			if (content.charAt(firstSpace) == ' ') {
				break;
			}
		}

		String prefix = content.substring(firstSpace + 1).toLowerCase();

		String[] matches = tagList.startsWith(prefix);

		if (matches.length == 1) {
			String match = matches[0].substring(prefix.length());
			SwingUtilities.invokeLater(new CompletionTask(match, pos + 1));
		}
	}

	@Override
	public void removeUpdate(DocumentEvent documentEvent) {

	}

	@Override
	public void changedUpdate(DocumentEvent documentEvent) {

	}

	private class CompletionTask implements Runnable {
		String completion;
		int position;

		CompletionTask(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}

		public void run() {
			textArea.insert(completion, position);
			textArea.setCaretPosition(position + completion.length());
			textArea.moveCaretPosition(position);
			mode = Mode.COMPLETION;
		}
	}

	private class CommitAction extends AbstractAction {
		public void actionPerformed(ActionEvent ev) {
			if (mode == Mode.COMPLETION) {
				int pos = textArea.getSelectionEnd();
				textArea.setCaretPosition(pos);
				mode = Mode.INSERT;
			} else {
				textArea.replaceSelection("\n");
			}
		}
	}
}
