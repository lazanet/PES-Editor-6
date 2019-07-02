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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PositionPanel extends JPanel implements ActionListener {
	OptionFile of;
	// Stats stats;
	JComboBox regBox;

	int player;

	JCheckBox[] checkBox;

	int regPos;

	public PositionPanel(OptionFile opf) {
		super(new BorderLayout());
		of = opf;
		setBorder(BorderFactory.createTitledBorder("Position"));
		JPanel panel = new JPanel(new GridLayout(4, 4));
		JLabel regLabel = new JLabel("Registered Position");
		JPanel regPanel = new JPanel();
		// of = opf;
		// stats = s;
		checkBox = new JCheckBox[Stats.roles.length];
		for (int i = 0; i < Stats.roles.length; i++) {
			checkBox[i] = new JCheckBox(Stats.roles[i].name);

			if (i != 1) {
				checkBox[i].setActionCommand(String.valueOf(i));
				checkBox[i].addActionListener(this);
				panel.add(checkBox[i]);
			}
			if (i == 0) {
				panel.add(new JPanel());
				panel.add(new JPanel());
				panel.add(new JPanel());
			}
		}

		regBox = new JComboBox();
		regBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "y") {
					String p = (String) regBox.getSelectedItem();
					int r = 0;
					for (int i = 0; i < Stats.roles.length; i++) {
						if (Stats.roles[i].name.equals(p)) {
							r = i;
						}
					}
					// System.out.println(r);
					regPos = r;

					// stats.regPos.setValue(player, i);
				}
			}
		});
		regPanel.add(regLabel);
		regPanel.add(regBox);
		add(panel, BorderLayout.CENTER);
		add(regPanel, BorderLayout.SOUTH);
	}

	public void load(int p) {
		player = p;
		regPos = Stats.getValue(of, player, Stats.regPos);
		for (int i = 0; i < Stats.roles.length; i++) {
			if (i != 1) {
				if (Stats.getValue(of, player, Stats.roles[i]) == 1 || regPos == i) {
					checkBox[i].setSelected(true);
				} else {
					checkBox[i].setSelected(false);
				}
			}
		}
		updateRegBox();
	}

	public void updateRegBox() {
		regBox.setActionCommand("n");
		regBox.removeAllItems();
		for (int i = 0; i < Stats.roles.length; i++) {
			if (checkBox[i].isSelected()) {
				regBox.addItem(Stats.roles[i].name);
			}
		}
		regBox.setSelectedItem(Stats.roles[regPos].name);
		regBox.setActionCommand("y");
	}

	public void actionPerformed(ActionEvent e) {
		int box = 0;
		try {
			box = new Integer(e.getActionCommand()).intValue();
		} catch (NumberFormatException nfe) {
		}
		if (regPos == box) {
			checkBox[box].setSelected(true);
		}
		updateRegBox();
	}

}
