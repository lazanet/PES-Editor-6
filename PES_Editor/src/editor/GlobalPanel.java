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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GlobalPanel extends JPanel {

	private JTextField numField;

	private JComboBox statBox;

	private JComboBox opBox;

	private JButton doButton;

	private JComboBox scopeBox;

	private String[] statNames = { "1-99 Stats", "ATTACK", "DEFENSE",
			"BALANCE", "STAMINA", "TOP SPEED", "ACCELERATION", "RESPONSE",
			"AGILITY", "DRIBBLE ACCURACY", "DRIBBLE SPEED",
			"SHORT PASS ACCURACY", "SHORT PASS SPEED", "LONG PASS ACCURACY",
			"LONG PASS SPEED", "SHOT ACCURACY", "SHOT POWER", "SHOT TECHNIQUE",
			"FREE KICK ACCURACY", "CURLING", "HEADING", "JUMP", "TECHNIQUE",
			"AGGRESSION", "MENTALITY", "GOAL KEEPING", "TEAM WORK",
			"1-8 Stats", "WEAK FOOT ACCURACY", "WEAK FOOT FREQUENCY",
			"CONSISTENCY", "CONDITION / FITNESS", "AGE" };

	private String[] ops = { "+", "-", "=", "+ %", "- %" };

	private String[] scopes = { "All Players", "GK", "CWP", "CBT", "SB", "DM",
			"WB", "CM", "SM", "AM", "WG", "SS", "CF" };

	private JComboBox teamBox;

	private OptionFile of;

	private JCheckBox checkBox;

	private TransferPanel tranPanel;

	public GlobalPanel(OptionFile of1, TransferPanel tp) {
		super();
		of = of1;
		tranPanel = tp;
		statBox = new JComboBox(statNames);
		numField = new JTextField(2);
		opBox = new JComboBox(ops);
		JPanel panel99 = new JPanel();
		JPanel scopePanel = new JPanel(new GridLayout(2, 3));
		JPanel main = new JPanel(new GridLayout(0, 1));
		scopePanel.setBorder(BorderFactory.createTitledBorder("Scope"));
		panel99.setBorder(BorderFactory.createTitledBorder("Adjustment"));
		scopeBox = new JComboBox(scopes);
		teamBox = new JComboBox();
		checkBox = new JCheckBox();

		doButton = new JButton("Adjust");
		doButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				int num = getNum();
				int si = statBox.getSelectedIndex();
				int v = num;
				int oi = opBox.getSelectedIndex();
				int max = 99;
				int min = 1;
				if (si == 32) {
					max = 46;
					min = 15;
				} else if (si > 26) {
					max = 8;
				}

				if (num == 0) {
					int dm = 1;
					int dx = 99;
					if (oi == 2) {
						dm = min;
						dx = max;
					}
					JOptionPane.showMessageDialog(null, "Enter a number: "
							+ dm + "-" + dx, "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (si == 0) {
						for (int p = 1; p < Player.total; p++) {
							if (adj(p) && adjTeam(p)) {
								for (int i = 1; i < 27; i++) {
									switch (oi) {
									case 0:
										v = getStat(i, p) + num;
										break;
									case 1:
										v = getStat(i, p) - num;
										break;
									case 2:
										v = num;
										break;
									case 3:
										v = getStat(i, p)
												+ (getStat(i, p) * num / 100);
										break;
									case 4:
										v = getStat(i, p)
												- (getStat(i, p) * num / 100);
										break;
									}
									if (v > 99) {
										v = 99;
									}
									if (v < 1) {
										v = 1;
									}
									setStat(i, p, v);
								}
							}
						}
						if (checkBox.isSelected()) {
							for (int p = Player.firstEdit; p < 32952; p++) {
								if (adj(p) && adjTeam(p)) {
									for (int i = 1; i < 27; i++) {
										switch (oi) {
										case 0:
											v = getStat(i, p) + num;
											break;
										case 1:
											v = getStat(i, p) - num;
											break;
										case 2:
											v = num;
											break;
										case 3:
											v = getStat(i, p)
													+ (getStat(i, p) * num / 100);
											break;
										case 4:
											v = getStat(i, p)
													- (getStat(i, p) * num / 100);
											break;
										}
										if (v > 99) {
											v = 99;
										}
										if (v < 1) {
											v = 1;
										}
										setStat(i, p, v);
									}
								}
							}
						}
					}

					if (si == 27) {
						for (int p = 1; p < Player.total; p++) {
							if (adj(p) && adjTeam(p)) {
								for (int i = 28; i < 32; i++) {
									switch (oi) {
									case 0:
										v = getStat(i, p) + num;
										break;
									case 1:
										v = getStat(i, p) - num;
										break;
									case 2:
										v = num;
										break;
									case 3:
										v = getStat(i, p)
												+ (getStat(i, p) * num / 100);
										break;
									case 4:
										v = getStat(i, p)
												- (getStat(i, p) * num / 100);
										break;
									}
									if (v > 8) {
										v = 8;
									}
									if (v < 1) {
										v = 1;
									}
									setStat(i, p, v);
								}
							}
						}
						if (checkBox.isSelected()) {
							for (int p = Player.firstEdit; p < 32952; p++) {
								if (adj(p) && adjTeam(p)) {
									for (int i = 28; i < 32; i++) {
										switch (oi) {
										case 0:
											v = getStat(i, p) + num;
											break;
										case 1:
											v = getStat(i, p) - num;
											break;
										case 2:
											v = num;
											break;
										case 3:
											v = getStat(i, p)
													+ (getStat(i, p) * num / 100);
											break;
										case 4:
											v = getStat(i, p)
													- (getStat(i, p) * num / 100);
											break;
										}
										if (v > 8) {
											v = 8;
										}
										if (v < 1) {
											v = 1;
										}
										setStat(i, p, v);
									}
								}
							}
						}
					}

					if (si != 0 && si != 27) {
						for (int p = 1; p < Player.total; p++) {
							if (adj(p) && adjTeam(p)) {
								switch (oi) {
								case 0:
									v = getStat(si, p) + num;
									break;
								case 1:
									v = getStat(si, p) - num;
									break;
								case 2:
									v = num;
									break;
								case 3:
									v = getStat(si, p)
											+ (getStat(si, p) * num / 100);
									break;
								case 4:
									v = getStat(si, p)
											- (getStat(si, p) * num / 100);
									break;
								}
								if (v > max) {
									v = max;
								}
								if (v < min) {
									v = min;
								}
								setStat(si, p, v);
							}
						}
						if (checkBox.isSelected()) {
							for (int p = Player.firstEdit; p < 32952; p++) {
								if (adj(p) && adjTeam(p)) {
									switch (oi) {
									case 0:
										v = getStat(si, p) + num;
										break;
									case 1:
										v = getStat(si, p) - num;
										break;
									case 2:
										v = num;
										break;
									case 3:
										v = getStat(si, p)
												+ (getStat(si, p) * num / 100);
										break;
									case 4:
										v = getStat(si, p)
												- (getStat(si, p) * num / 100);
										break;
									}
									if (v > max) {
										v = max;
									}
									if (v < 1) {
										v = 1;
									}
									setStat(si, p, v);
								}
							}
						}
					}
					JOptionPane.showMessageDialog(null,
							"Stats have been adjusted", "Stats Adjusted",
							JOptionPane.INFORMATION_MESSAGE);
					/*
					 * String messageText1 = " set to: "; switch (oi) { case 0:
					 * messageText1 = " increased by: "; break; case 1:
					 * messageText1 = " decreased by: "; break; } String
					 * messageText2 = "every player"; if
					 * (scopeBox.getSelectedIndex() != 0) { messageText2 = "all
					 * players registered in the " +
					 * scopes[scopeBox.getSelectedIndex()] + " position"; }
					 * JOptionPane.showMessageDialog(null, statNames[si] + " of "+
					 * messageText2 + messageText1 + num, "Stats Adjusted",
					 * JOptionPane.INFORMATION_MESSAGE);
					 */
					tranPanel.refresh();
				}
			}
		});
		scopePanel.add(new JLabel("Registered Position:"));
		scopePanel.add(new JLabel("Exclude Team:"));
		scopePanel.add(new JLabel("Created Players:"));
		scopePanel.add(scopeBox);
		scopePanel.add(teamBox);
		scopePanel.add(checkBox);
		panel99.add(statBox);
		panel99.add(opBox);
		panel99.add(numField);
		panel99.add(doButton);
		main.add(scopePanel);
		main.add(panel99);
		add(main);
	}

	private int getNum() {
		int v;
		int max = 99;
		int min = 1;
		int si = statBox.getSelectedIndex();
		if (opBox.getSelectedIndex() == 2) {
			if (si == 32) {
				max = 46;
				min = 15;
			} else if (si > 26) {
				max = 8;
			}
		}
		try {
			v = new Integer(numField.getText()).intValue();
			if (v < min || v > max) {
				v = 0;
			}
		} catch (NumberFormatException nfe) {
			v = 0;
		}
		return v;
	}

	private void setStat(int si, int p, int num) {
		if (si == 32) {
			num = num - 15;
		} else if (si > 26) {
			num = num - 1;
		}
		if (si > 0 && si <= 26) {
			Stats.setValue(of, p, Stats.ability99[si - 1], num);
		}
		switch (si) {
		case 28:
			Stats.setValue(of, p, Stats.wfa, num);
			break;
		case 29:
			Stats.setValue(of, p, Stats.wff, num);
			break;
		case 30:
			Stats.setValue(of, p, Stats.consistency, num);
			break;
		case 31:
			Stats.setValue(of, p, Stats.condition, num);
			break;
		case 32:
			Stats.setValue(of, p, Stats.age, num);
			break;
		}
		Stats.setValue(of, p, Stats.abilityEdited, 1);
	}

	private int getStat(int si, int p) {
		int v = 0;
		if (si > 0 && si <= 26) {
			v = Stats.getValue(of, p, Stats.ability99[si - 1]);
		}
		switch (si) {
		case 28:
			v = Stats.getValue(of, p, Stats.wfa);
			break;
		case 29:
			v = Stats.getValue(of, p, Stats.wff);
			break;
		case 30:
			v = Stats.getValue(of, p, Stats.consistency);
			break;
		case 31:
			v = Stats.getValue(of, p, Stats.condition);
			break;
		case 32:
			v = Stats.getValue(of, p, Stats.age);
			break;
		}
		if (si == 32) {
			v = v + 15;
		} else if (si > 26) {
			v = v + 1;
		}
		return v;
	}

	private boolean adj(int p) {
		boolean result = false;
		int si = scopeBox.getSelectedIndex();
		if (si == 0) {
			result = true;
		} else {
			int v = Stats.getValue(of, p, Stats.regPos);
			if (si == 1) {
				si = si - 1;
			}
			if (v == si) {
				result = true;
			}
		}
		return result;
	}

	public void updateTeamBox(String[] teams) {
		String[] clubs = new String[Clubs.total + 1];
		clubs[0] = "None";
		System.arraycopy(teams, 0, clubs, 1, Clubs.total);
		DefaultComboBoxModel model = new DefaultComboBoxModel(clubs);
		teamBox.setModel(model);
	}

	private boolean adjTeam(int p) {
		boolean result = true;
		int ti = teamBox.getSelectedIndex();
		if (ti != 0) {
			int firstAdr = Squads.slot32 + ((ti - 1) * 64);
			int a;
			int i;
			for (int sp = 0; result && sp < 32; sp++) {
				a = firstAdr + (sp * 2);
				i = (of.toInt(of.data[a + 1]) << 8) | of.toInt(of.data[a]);
				if (i == p) {
					result = false;
				}
			}
		}
		return result;
	}

}
