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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TeamSetPanel extends JPanel {
	private OptionFile of;

	//private SquadList list;

	//private PositionList posList;

	int alt = 0;

	int squad = 0;

	//private JComboBox attDefBox;

	//private JComboBox defSysBox;

	//private JComboBox sweeperBox;

	private JComboBox[] box = new JComboBox[4];

	//private JComboBox pressBox;

	//private JComboBox trapBox;

	//private JComboBox counterBox;

	private boolean ok = false;

	public TeamSetPanel(OptionFile opf) {
		super(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Team Settings"));
		of = opf;
		//list = l;
		//posList = pl;
		/*String[] items = { "Manual", "Auto-Defence", "Normal", "Auto-Attack" };
		attDefBox = new JComboBox(items);
		attDefBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					of.data[670786 + (628 * squad) + 6232 + (alt * 171)] = (byte) attDefBox
							.getSelectedIndex();
				}
			}
		});
		String[] items2 = { "Normal", "Sweeper", "Line" };
		defSysBox = new JComboBox(items2);
		defSysBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					of.data[670784 + (628 * squad) + 6232 + (alt * 171)] = (byte) defSysBox
							.getSelectedIndex();
					if (defSysBox.getSelectedIndex() == 1
							&& of.data[670785 + (628 * squad) + 6232
									+ (alt * 171)] == 0) {
						for (int i = 1; of.data[670785 + (628 * squad) + 6232
								+ (alt * 171)] == 0
								&& i < 11; i++) {
							String pos = (String) posList.getModel()
									.getElementAt(i);
							if (pos.equals("CBT") || pos.equals("CBW")
									|| pos.equals("ASW")) {
								// System.out.println("A");
								of.data[670785 + (628 * squad) + 6232
										+ (alt * 171)] = (byte) i;
								// System.out.println("A = " + i);
							}
						}
						if (of.data[670785 + (628 * squad) + 6232 + (alt * 171)] == 0) {
							of.data[670784 + (628 * squad) + 6232 + (alt * 171)] = 0;
						}
					}

					refresh(squad);
				}
			}
		});
		sweeperBox = new JComboBox();
		sweeperBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					// System.out.println("B");
					SweItem item = (SweItem) sweeperBox.getSelectedItem();
					if (item != null) {
						of.data[670785 + (628 * squad) + 6232 + (alt * 171)] = item.index;
					}
					// System.out.println("B = " + item.index);
				}
			}
		});*/
		
		ActionListener act = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					int b = new Integer(e.getActionCommand()).intValue();
					Formations.setTeam(of, squad, alt, b, box[b].getSelectedIndex());
				}
			}
		};
		
		String[] items3 = { "A", "B", "C" };
		for (int i = 0; i < 4; i++) {
			box[i] = new JComboBox(items3);
			box[i].setActionCommand(String.valueOf(i));
			box[i].addActionListener(act);
		}
		/*lineBox = new JComboBox(items3);
		lineBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					of.data[670787 + (628 * squad) + 6232 + (alt * 171)] = (byte) lineBox
							.getSelectedIndex();
				}
			}
		});
		pressBox = new JComboBox(items3);
		pressBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					of.data[670788 + (628 * squad) + 6232 + (alt * 171)] = (byte) pressBox
							.getSelectedIndex();
				}
			}
		});
		trapBox = new JComboBox(items3);
		trapBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					of.data[670789 + (628 * squad) + 6232 + (alt * 171)] = (byte) trapBox
							.getSelectedIndex();
				}
			}
		});
		counterBox = new JComboBox(items3);
		counterBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ok) {
					of.data[670790 + (628 * squad) + 6232 + (alt * 171)] = (byte) counterBox
							.getSelectedIndex();
				}
			}
		});*/

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 10, 0, 1);

		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Back line"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(new JLabel("Pressure"), c);
		
		c.gridx = 2;
		c.gridy = 0;
		add(new JLabel("Offside Trap"), c);
		
		c.gridx = 2;
		c.gridy = 1;
		add(new JLabel("Counter Attack"), c);

		c.insets = new Insets(0, 1, 0, 10);
		
		c.gridx = 1;
		c.gridy = 0;
		add(box[0], c);

		c.gridx = 1;
		c.gridy = 1;
		add(box[1], c);

		c.gridx = 3;
		c.gridy = 0;
		add(box[2], c);

		c.gridx = 3;
		c.gridy = 1;
		add(box[3], c);

		/*c.anchor = GridBagConstraints.WEST;
		// c.weightx = 1;

		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Attack/Defence"), c);

		//c.gridx = 0;
		//c.gridy = 1;
		//add(attDefBox, c);

		c.gridx = 0;
		c.gridy = 2;
		add(new JLabel("Defence System"), c);

		c.gridx = 0;
		c.gridy = 3;
		add(defSysBox, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		add(sweeperBox, c);*/
	}

	public void refresh(int s) {
		squad = s;
		ok = false;
		/*attDefBox.setSelectedIndex(of.data[670786 + (628 * squad) + 6232
				+ (alt * 171)]);
		int ds = of.data[670784 + (628 * squad) + 6232 + (alt * 171)];
		// System.out.println("ds = " + ds);
		defSysBox.setSelectedIndex(ds);*/
		for (int i = 0; i < 4; i++) {
			box[i].setSelectedIndex(Formations.getTeam(of, squad, alt, i));
		}
		/*lineBox.setSelectedIndex(of.data[670787 + (628 * squad) + 6232
				+ (alt * 171)]);
		pressBox.setSelectedIndex(of.data[670788 + (628 * squad) + 6232
				+ (alt * 171)]);
		trapBox.setSelectedIndex(of.data[670789 + (628 * squad) + 6232
				+ (alt * 171)]);
		counterBox.setSelectedIndex(of.data[670790 + (628 * squad) + 6232
				+ (alt * 171)]);*/
		
		/*sweeperBox.removeAllItems();

		// sweeperBox.addItem("---");
		int sw = of.data[670785 + (628 * squad) + 6232 + (alt * 171)];
		// System.out.println("sw = " + sw);
		// System.out.println(sw);
		if (sw != 0) {
			byte count = 0;
			byte sel = -1;
			for (byte i = 1; i < 11; i++) {
				String pos = (String) posList.getModel().getElementAt(i);
				if (pos.equals("CBT") || pos.equals("CBW") || pos.equals("ASW")) {
					if (i == sw) {
						sel = count;
					}
					sweeperBox.addItem(new SweItem(i));
					count++;
				}
			}
			// System.out.println("sel = " + sel);
			sweeperBox.setSelectedIndex(sel);
		}
			 * else { sweeperBox.setSelectedIndex(11); }
			 

		if (ds == 1 && sweeperBox.getItemCount() != 0) {
			sweeperBox.setEnabled(true);
		} else {
			sweeperBox.setEnabled(false);
		}*/
		ok = true;
	}

	/*private class SweItem {
		String name;

		byte index;

		public SweItem(byte i) {
			index = i;
			name = ((Player) list.getModel().getElementAt(index)).name;
		}

		public String toString() {
			return name;
		}

	}*/

}
