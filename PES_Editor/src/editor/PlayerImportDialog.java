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
 * PES Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PlayerImportDialog extends JDialog implements
		ListSelectionListener, MouseListener {
	OptionFile of;

	OptionFile of2;

	JLabel fileLabel;

	SelectByTeam plList;

	InfoPanel infoPanel;

	boolean of2Open;

	int index;

	int replacement;

	JRadioButton allButton;

	JRadioButton statsButton;

	public PlayerImportDialog(Frame owner, OptionFile opf, OptionFile opf2) {
		super(owner, "Import Player", true);
		of = opf;
		of2 = opf2;
		fileLabel = new JLabel("From:");
		plList = new SelectByTeam(of2, false);
		infoPanel = new InfoPanel(of2);
		plList.squadList.addListSelectionListener(this);
		plList.squadList.addMouseListener(this);
		CancelButton cancelButton = new CancelButton(this);

		allButton = new JRadioButton(
				"Import everything (name, appearance, stats, etc.)");
		statsButton = new JRadioButton(
				"Import only the stats editable on the 'Edit Player' dialog");
		JRadioButton exStatsButton = new JRadioButton(
				"Import everything except stats (name, appearance, etc.)");
		ButtonGroup group = new ButtonGroup();
		group.add(allButton);
		group.add(statsButton);
		group.add(exStatsButton);
		allButton.setSelected(true);

		JPanel topPanel = new JPanel(new GridLayout(4, 1));
		topPanel.add(fileLabel);
		topPanel.add(allButton);
		topPanel.add(statsButton);
		topPanel.add(exStatsButton);

		getContentPane().add(plList, BorderLayout.WEST);
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		getContentPane().add(cancelButton, BorderLayout.SOUTH);
		getContentPane().add(topPanel, BorderLayout.NORTH);

		of2Open = false;
		index = 0;
		replacement = 0;
		pack();
		setResizable(false);
	}

	public void show(int i) {
		index = i;
		setVisible(true);
	}

	public void refresh() {
		plList.refresh();
		of2Open = true;
		fileLabel.setText("  From:  " + of2.fileName);
		index = 0;
		replacement = 0;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) 
		{
			if (!plList.squadList.isSelectionEmpty())
				infoPanel.refresh(((Player) plList.squadList.getSelectedValue()).index,0);
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		int clicks = e.getClickCount();
		JList list = (JList) (e.getSource());
		int pi = ((Player) list.getSelectedValue()).index;
		if (clicks == 2 && pi != 0) {
			replacement = pi;
			importPlayer();
			setVisible(false);
		}
	}

	private void importPlayer() {
		int ia = Player.startAdr + (index * 124);
		if (index >= Player.firstEdit) {
			ia = Player.startAdrE + ((index - Player.firstEdit) * 124);
		}
		int ra = Player.startAdr + (replacement * 124);
		if (replacement >= Player.firstEdit) {
			ra = Player.startAdrE + ((replacement - Player.firstEdit) * 124);
		}
		if (allButton.isSelected()) {
			System.arraycopy(of2.data, ra, of.data, ia, 124);
			Stats.setValue(of, index, Stats.nameEdited, 1);
			Stats.setValue(of, index, Stats.callEdited, 1);
			Stats.setValue(of, index, Stats.shirtEdited, 1);
			Stats.setValue(of, index, Stats.abilityEdited, 1);
			/*
			 * if (!of.isWE() && of2.isWE()) { Convert.player(of, index,
			 * Convert.WE2007_PES6); } if (of.isWE() && !of2.isWE()) {
			 * Convert.player(of, index, Convert.PES6_WE2007); }
			 */
		} else if (statsButton.isSelected()) {
			Stats.setValue(of, index, Stats.nationality, Stats.getValue(of2,
					replacement, Stats.nationality));
			Stats.setValue(of, index, Stats.age, Stats.getValue(of2,
					replacement, Stats.age));
			Stats.setValue(of, index, Stats.height, Stats.getValue(of2,
					replacement, Stats.height));
			Stats.setValue(of, index, Stats.weight, Stats.getValue(of2,
					replacement, Stats.weight));
			Stats.setValue(of, index, Stats.foot, Stats.getValue(of2,
					replacement, Stats.foot));
			Stats.setValue(of, index, Stats.favSide, Stats.getValue(of2,
					replacement, Stats.favSide));
			Stats.setValue(of, index, Stats.wfa, Stats.getValue(of2,
					replacement, Stats.wfa));
			Stats.setValue(of, index, Stats.wff, Stats.getValue(of2,
					replacement, Stats.wff));
			Stats.setValue(of, index, Stats.condition, Stats.getValue(of2,
					replacement, Stats.condition));
			Stats.setValue(of, index, Stats.consistency, Stats.getValue(of2,
					replacement, Stats.consistency));
			Stats.setValue(of, index, Stats.injury, Stats.getValue(of2,
					replacement, Stats.injury));
			Stats.setValue(of, index, Stats.dribSty, Stats.getValue(of2,
					replacement, Stats.dribSty));
			Stats.setValue(of, index, Stats.pkStyle, Stats.getValue(of2,
					replacement, Stats.pkStyle));
			Stats.setValue(of, index, Stats.freekick, Stats.getValue(of2,
					replacement, Stats.freekick));
			Stats.setValue(of, index, Stats.dkSty, Stats.getValue(of2,
					replacement, Stats.dkSty));
			Stats.setValue(of, index, Stats.regPos, Stats.getValue(of2,
					replacement, Stats.regPos));

			for (int i = 0; i < Stats.roles.length; i++) {
				Stats.setValue(of, index, Stats.roles[i], Stats.getValue(of2,
						replacement, Stats.roles[i]));
			}
			for (int i = 0; i < Stats.ability99.length; i++) {
				Stats.setValue(of, index, Stats.ability99[i], Stats.getValue(
						of2, replacement, Stats.ability99[i]));
			}
			for (int i = 0; i < Stats.abilitySpecial.length; i++) {
				Stats.setValue(of, index, Stats.abilitySpecial[i], Stats
						.getValue(of2, replacement, Stats.abilitySpecial[i]));
			}
			Stats.setValue(of, index, Stats.abilityEdited, 1);
		} else {
			byte[] temp = new byte[124];
			System.arraycopy(of2.data, ra, temp, 0, 124);

			Stats.setValue(of2, replacement, Stats.nationality, Stats.getValue(
					of, index, Stats.nationality));
			Stats.setValue(of2, replacement, Stats.age, Stats.getValue(of,
					index, Stats.age));
			Stats.setValue(of2, replacement, Stats.height, Stats.getValue(of,
					index, Stats.height));
			Stats.setValue(of2, replacement, Stats.weight, Stats.getValue(of,
					index, Stats.weight));
			Stats.setValue(of2, replacement, Stats.foot, Stats.getValue(of,
					index, Stats.foot));
			Stats.setValue(of2, replacement, Stats.favSide, Stats.getValue(of,
					index, Stats.favSide));
			Stats.setValue(of2, replacement, Stats.wfa, Stats.getValue(of,
					index, Stats.wfa));
			Stats.setValue(of2, replacement, Stats.wff, Stats.getValue(of,
					index, Stats.wff));
			Stats.setValue(of2, replacement, Stats.condition, Stats.getValue(
					of, index, Stats.condition));
			Stats.setValue(of2, replacement, Stats.consistency, Stats.getValue(
					of, index, Stats.consistency));
			Stats.setValue(of2, replacement, Stats.injury, Stats.getValue(of,
					index, Stats.injury));
			Stats.setValue(of2, replacement, Stats.dribSty, Stats.getValue(of,
					index, Stats.dribSty));
			Stats.setValue(of2, replacement, Stats.pkStyle, Stats.getValue(of,
					index, Stats.pkStyle));
			Stats.setValue(of2, replacement, Stats.freekick, Stats.getValue(of,
					index, Stats.freekick));
			Stats.setValue(of2, replacement, Stats.dkSty, Stats.getValue(of,
					index, Stats.dkSty));
			Stats.setValue(of2, replacement, Stats.regPos, Stats.getValue(of,
					index, Stats.regPos));

			for (int i = 0; i < Stats.roles.length; i++) {
				Stats.setValue(of2, replacement, Stats.roles[i], Stats
						.getValue(of, index, Stats.roles[i]));
			}
			for (int i = 0; i < Stats.ability99.length; i++) {
				Stats.setValue(of2, replacement, Stats.ability99[i], Stats
						.getValue(of, index, Stats.ability99[i]));
			}
			for (int i = 0; i < Stats.abilitySpecial.length; i++) {
				Stats.setValue(of2, replacement, Stats.abilitySpecial[i], Stats
						.getValue(of, index, Stats.abilitySpecial[i]));
			}

			System.arraycopy(of2.data, ra, of.data, ia, 124);
			Stats.setValue(of, index, Stats.nameEdited, 1);
			Stats.setValue(of, index, Stats.callEdited, 1);
			Stats.setValue(of, index, Stats.shirtEdited, 1);
			
			if (!of.isWE() && of2.isWE()) 
				Convert.player(of, index, Convert.WE2007_PES6);
			if (of.isWE() && !of2.isWE())
			 	Convert.player(of, index, Convert.PES6_WE2007); 
			 

			System.arraycopy(temp, 0, of2.data, ra, 124);
		}
	}
}
