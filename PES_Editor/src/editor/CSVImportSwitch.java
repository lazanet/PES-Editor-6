/*
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

public class CSVImportSwitch extends JPanel {
	JCheckBox extra;

	JCheckBox create;

	JCheckBox updateClubTeams;

	JCheckBox updateNationalTeams;

	JCheckBox updateClassicTeams;

	public CSVImportSwitch() {
		super(new GridLayout(0, 1));
		updateClubTeams = new JCheckBox("Update Club Squads");
		updateNationalTeams = new JCheckBox("Update National Squads");
		updateClassicTeams = new JCheckBox("Update Classic Squads");
		// head.setToolTipText("
		add(updateClubTeams);
		add(updateNationalTeams);
		add(updateClassicTeams);
	}

}
