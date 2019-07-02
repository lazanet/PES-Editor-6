/*
 * Copyright 2008-9 Compulsion
 * <pes_compulsion@yahoo.co.uk>
 * <http://www.purplehaze.eclipse.co.uk/>
 * <http://uk.geocities.com/pes_compulsion/>
 *
 * This file is part of PES Editor.
 *
 * PES Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PES Editor is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PES Editor.  If not, see <http://www.gnu.org/licenses/>.
 */
package editor;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ProgressUI implements Runnable {

	public String frameTitle;
	public JDialog frame;
	public JProgressBar pb;
	public JLabel label;

	private ProgressTask task;

	public ProgressUI(final String frameTitle) {
		this.frameTitle = frameTitle;
	}

	public void doTask(ProgressTask task) {
		this.task = task;
		makeProgressFrame(0,100,"");
	}

	private JDialog makeProgressFrame(int min, int max, String labelText) {
		frame = new JDialog(Editor.mainFrame, frameTitle);
		pb = new JProgressBar(min, max);
		pb.setValue(min);
		pb.setStringPainted(true);

		label = new JLabel(labelText);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(pb, BorderLayout.NORTH);
		panel.add(label, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(Editor.mainFrame);
		frame.setModal(true);

		new Thread(this).start();
		frame.setVisible(true);
		return frame;
	}

	public void resetParameters(int min, int max, String labelText) {
		pb.setMinimum(min);
		pb.setMaximum(max);
		label.setText(labelText);
		pb.setValue(min);
		frame.pack();
	}

	public void updateProgress(int value) {
		pb.setValue(value);
	}

	public void done() {
		frame.setVisible(false);
		frame.dispose();
	}

	public void run() {
		task.doIt();
	}
}
