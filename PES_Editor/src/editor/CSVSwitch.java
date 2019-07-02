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

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class CSVSwitch extends JPanel {
	JCheckBox head;

	JCheckBox extra;

	JCheckBox create;

	public CSVSwitch() {
		super(new GridLayout(0, 1));
		// PeterC10 MOD: Remove Column Headings Option
		// head = new JCheckBox("Column Headings");
		extra = new JCheckBox("Extra Players");
		create = new JCheckBox("Created Players");
		// head.setToolTipText("
		// add(head);
		// PeterC10 MOD: Check options by default
		extra.setSelected(true);
		create.setSelected(true);
		add(extra);
		add(create);
	}

}
