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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EmblemImportDialog extends JDialog {
	private JButton[] flagButton;

	private boolean trans = true;

	OptionFile of;

	int slot;

	byte max;

	int type;

	boolean of2Open = false;

	JLabel fileLabel;

	public EmblemImportDialog(Frame owner, OptionFile opt) {
		super(owner, true);
		of = opt;
		JPanel flagPanel;
		max = 100;
		flagPanel = new JPanel(new GridLayout(10, 10));
		flagButton = new JButton[max];
		fileLabel = new JLabel("From:");
		
		PESUtils.javaUI();
		
		for (int l = 0; l < max; l++) {
			flagButton[l] = new JButton(new ImageIcon(Emblems.get16(of, -1,
					false, true)));
			// flagButton[l].setIcon();
			flagButton[l].setMargin(new Insets(0, 0, 0, 0));
			flagButton[l].setActionCommand((new Integer(l)).toString());
			flagButton[l].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent b) {
					slot = (new Integer(((JButton) b.getSource())
							.getActionCommand())).intValue();
					if (slot >= Emblems.count16(of)) {
						slot = 99 - slot;
					} else {
						slot = slot + 50;
					}
					setVisible(false);
				}
			});
			flagPanel.add(flagButton[l]);
		}
		
		PESUtils.systemUI();
		
		JButton transButton = new JButton("Transparency");
		transButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent t) {
				trans = !trans;
				refresh();
				// System.out.println(trans);
				/*
				 * for (int f = 0; f < 64; f++) { if (trans) {
				 * flagButton[f].setIcon(of.tranFlag[f]); } else {
				 * flagButton[f].setIcon(of.flag[f]); } }
				 */
			}
		});
		CancelButton cancelButton = new CancelButton(this);
		JPanel pan1 = new JPanel(new BorderLayout());
		pan1.add(transButton, BorderLayout.NORTH);
		pan1.add(cancelButton, BorderLayout.SOUTH);
		pan1.add(flagPanel, BorderLayout.CENTER);
		getContentPane().add(fileLabel, BorderLayout.NORTH);
		getContentPane().add(pan1, BorderLayout.CENTER);
		slot = -1;
		setResizable(false);
		pack();
	}

	public void refresh() {
		if (type == 0 || type == 1) {
			for (int i = 0; i < Emblems.count16(of); i++) {
				flagButton[i].setIcon(new ImageIcon(Emblems.get16(of, i, !trans,
						true)));
				flagButton[i].setVisible(true);
			}
		}
		if (type == 0 || type == 2) {
			for (int i = 0; i < Emblems.count128(of); i++) {
				flagButton[99 - i].setIcon(new ImageIcon(Emblems.get128(of, i,
						!trans, true)));
				flagButton[99 - i].setVisible(true);
			}
		}

		int s = Emblems.count16(of);
		int e = 100 - Emblems.count128(of);
		if (type == 1) {
			e = 100;
		}
		if (type == 2) {
			s = 0;
		}
		for (int i = s; i < e; i++) {
			flagButton[i].setVisible(false);
		}
	}

	public int getFlag(String title, int t) {
		type = t;
		slot = -1;
		setTitle(title);
		fileLabel.setText("  From:  " + of.fileName);
		refresh();
		setVisible(true);
		return slot;
	}

}
