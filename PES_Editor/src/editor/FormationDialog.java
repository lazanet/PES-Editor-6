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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class FormationDialog extends JDialog implements WindowListener {
	OptionFile of;

	FormPanel formPan;

	byte[] original = new byte[Formations.size];

	int squadIndex;

	public FormationDialog(Frame owner, OptionFile opf) {
		super(owner, "Edit Formation", true);
		// setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		of = opf;
		formPan = new FormPanel(of);
		JButton acceptButton = new JButton("Accept");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				setVisible(false);
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				int adr = Formations.startAdr + (squadIndex * Formations.size);
				System.arraycopy(original, 0, of.data, adr, Formations.size);
				setVisible(false);
			}
		});
		JPanel buttonPanel = new JPanel();
		JPanel panel = new JPanel(new BorderLayout());
		buttonPanel.add(acceptButton);
		buttonPanel.add(cancelButton);
		panel.add(formPan, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(panel);
		pack();
		setResizable(false);
	}

	public void show(int t, String title) {
		setTitle("Edit Formation - " + title);
		squadIndex = t;
		int a = Formations.startAdr + (t * Formations.size);
		System.arraycopy(of.data, a, original, 0, Formations.size);
		formPan.refresh(t);
		setVisible(true);
	}

	public void windowClosing(WindowEvent e) {
		int adr = Formations.startAdr + (squadIndex * Formations.size);
		System.arraycopy(original, 0, of.data, adr, Formations.size);
		// setVisible(false);
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

}
