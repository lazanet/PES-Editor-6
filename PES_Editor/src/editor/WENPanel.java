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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WENPanel extends JPanel implements ActionListener {
	OptionFile of;

	JLabel current;

	JTextField field;

	public WENPanel(OptionFile opf) {
		super();
		of = opf;
		JPanel pan = new JPanel(new GridLayout(0, 1));
		pan.setBorder(BorderFactory.createTitledBorder("PES Points"));
		current = new JLabel("");
		field = new JTextField(8);
		field.setToolTipText("Enter an amount (0-99999) and press return");
		field.addActionListener(this);
		pan.add(field);
		pan.add(current);
		add(pan);
		refresh();
	}

	public void refresh() {
		int wen = (of.toInt(of.data[50]) << 16) + (of.toInt(of.data[49]) << 8)
				+ of.toInt(of.data[48]);
		current.setText("Current:  " + String.valueOf(wen));
		field.setText("");
	}

	public void setWEN(int newWen) {
		if (newWen >= 0 && newWen <= 99999) {
			of.data[48] = of.toByte(newWen & 0xFF);
			of.data[49] = of.toByte((newWen & 0xFF00) >>> 8);
			of.data[50] = of.toByte((newWen & 0xFF0000) >>> 16);
			of.data[5208] = of.toByte(newWen & 0xFF);
			of.data[5209] = of.toByte((newWen & 0xFF00) >>> 8);
			of.data[5210] = of.toByte((newWen & 0xFF0000) >>> 16);
			refresh();
		} else {
			field.setText("");
			JOptionPane.showMessageDialog(null,
					"Amount must be in the range 0-99999", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent evt) {
		try {
			setWEN(new Integer(field.getText()).intValue());
		} catch (NumberFormatException nfe) {
		}
	}

}
