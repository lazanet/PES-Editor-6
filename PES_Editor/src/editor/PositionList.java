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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

//import java.util.*;

public class PositionList extends JList {
	private OptionFile of;

	boolean tran;

	int[] posNum;

	int alt = 0;

	public PositionList(OptionFile opf, boolean t) {
		super();
		tran = t;
		// System.out.println("team-" + team);
		of = opf;
		// refresh(team);
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setVisibleRowCount(32);
		setBackground(new Color(255, 255, 224));
		setPreferredSize(new Dimension(32, 576));
		// setFont(new Font("Dialog", Font.BOLD, 12));
		// System.out.println(getFont());
	}

	public void refresh(int t) {
		String[] pos = new String[32];
		posNum = new int[32];
		if (tran && ((t > 63 && t < 73) || t > 212)) {
			setListData(pos);
		} else {
			if (tran && t > 72) {
				t = t - 9;
			}
			int p;
			pos[0] = "GK   ";
			posNum[0] = 0;
			for (int i = 0; i < 10; i++) {
				p = Formations.getPos(of, t, alt, i + 1);
				posNum[i + 1] = p;
				if (p == 0) {
					pos[i + 1] = "GK";
				}
				if ((p > 0 && p < 4) || (p > 5 && p < 8)) {
					pos[i + 1] = "CBT";
				}
				if (p == 4) {
					pos[i + 1] = "CWP";
				}
				if (p == 5) {
					pos[i + 1] = "ASW";
				}
				if (p == 8) {
					pos[i + 1] = "LB";
				}
				if (p == 9) {
					pos[i + 1] = "RB";
				}
				if (p > 9 && p < 15) {
					pos[i + 1] = "DM";
				}

				if (p == 15) {
					pos[i + 1] = "LWB";
				}
				if (p == 16) {
					pos[i + 1] = "RWB";
				}

				if (p > 16 && p < 22) {
					pos[i + 1] = "CM";
				}
				if (p == 22) {
					pos[i + 1] = "LM";
				}
				if (p == 23) {
					pos[i + 1] = "RM";
				}
				if (p > 23 && p < 29) {
					pos[i + 1] = "AM";
				}
				if (p == 29) {
					pos[i + 1] = "LW";
				}
				if (p == 30) {
					pos[i + 1] = "RW";
				}
				if (p > 30 && p < 36) {
					pos[i + 1] = "SS";
				}
				if (p > 35 && p < 41) {
					pos[i + 1] = "CF";
				}

				if (p > 40) {
					pos[i + 1] = String.valueOf(p);
				}
			}
			for (int i = 11; i < 32; i++) {
				pos[i] = " ";
			}
			setListData(pos);
		}
	}

	public void selectPos(JList sl, int li) {
		clearSelection();
		if (li >= 0 && li < 11) {
			int size = sl.getModel().getSize();
			int selPos = posNum[li];
			int pli;
			int[] temp = new int[32];
			int c = 0;
			Stat stat = Stats.gk;
			if ((selPos > 0 && selPos < 4) || (selPos > 5 && selPos < 8)) {
				stat = Stats.cbt;
			}
			if (selPos == 4) {
				stat = Stats.cwp;
			}
			if (selPos == 5) {
				stat = Stats.cwp;
			}
			if (selPos == 8) {
				stat = Stats.sb;
			}
			if (selPos == 9) {
				stat = Stats.sb;
			}
			if (selPos > 9 && selPos < 15) {
				stat = Stats.dm;
			}

			if (selPos == 15) {
				stat = Stats.wb;
			}
			if (selPos == 16) {
				stat = Stats.wb;
			}

			if (selPos > 16 && selPos < 22) {
				stat = Stats.cm;
			}
			if (selPos == 22) {
				stat = Stats.sm;
			}
			if (selPos == 23) {
				stat = Stats.sm;
			}
			if (selPos > 23 && selPos < 29) {
				stat = Stats.am;
			}
			if (selPos == 29) {
				stat = Stats.wg;
			}
			if (selPos == 30) {
				stat = Stats.wg;
			}
			if (selPos > 30 && selPos < 36) {
				stat = Stats.ss;
			}
			if (selPos > 35 && selPos < 41) {
				stat = Stats.cf;
			}

			c = 0;
			for (int i = 0; i < size; i++) {
				pli = ((Player) (sl.getModel().getElementAt(i))).index;
				if (pli != 0 && Stats.getValue(of, pli, stat) == 1) {
					// System.out.println(i);
					temp[c] = i;
					c++;
				}
			}
			int[] select = new int[c];
			System.arraycopy(temp, 0, select, 0, c);
			/*
			 * for (int i = 0; i < select.length; i++) {
			 * System.out.println(select[i]); }
			 */
			// setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			setSelectedIndices(select);
		} else {
			clearSelection();
		}
	}

}
