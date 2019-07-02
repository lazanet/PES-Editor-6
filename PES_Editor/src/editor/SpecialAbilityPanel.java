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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class SpecialAbilityPanel extends JPanel {
	OptionFile of;
	int player;

	JCheckBox[] checkBox;

	String[] ability = { "Dribbling", "Tactical dribble", "Positioning",
			"Reaction", "Playmaking", "Passing", "Scoring", "1-1 Scoring",
			"Post player", "Lines", "Middle shooting", "Side", "Centre",
			"Penalties", "1-Touch pass", "Outside", "Marking", "Sliding",
			"Covering", "D-Line control", "Penalty stopper", "1-On-1 stopper",
			"Long throw" };

	public SpecialAbilityPanel(OptionFile opf) {
		super(new GridLayout(0, 1));
		of = opf;
		setBorder(BorderFactory.createTitledBorder("Special Ability"));
		// stats = s;
		checkBox = new JCheckBox[ability.length];
		for (int i = 0; i < ability.length; i++) {
			checkBox[i] = new JCheckBox(ability[i]);
			add(checkBox[i]);
		}
	}

	public void load(int p) {
		player = p;
		for (int i = 0; i < Stats.abilitySpecial.length; i++) {
			if (Stats.getValue(of, player, Stats.abilitySpecial[i]) == 1) {
				checkBox[i].setSelected(true);
			} else {
				checkBox[i].setSelected(false);
			}
		}
		/*if (stats.drib.getValue(player) == 1) {
			checkBox[0].setSelected(true);
		} else {
			checkBox[0].setSelected(false);
		}
		if (stats.dribKeep.getValue(player) == 1) {
			checkBox[1].setSelected(true);
		} else {
			checkBox[1].setSelected(false);
		}
		if (stats.post.getValue(player) == 1) {
			checkBox[8].setSelected(true);
		} else {
			checkBox[8].setSelected(false);
		}
		if (stats.posit.getValue(player) == 1) {
			checkBox[2].setSelected(true);
		} else {
			checkBox[2].setSelected(false);
		}
		if (stats.offside.getValue(player) == 1) {
			checkBox[3].setSelected(true);
		} else {
			checkBox[3].setSelected(false);
		}
		if (stats.linePos.getValue(player) == 1) {
			checkBox[9].setSelected(true);
		} else {
			checkBox[9].setSelected(false);
		}
		if (stats.scorer.getValue(player) == 1) {
			checkBox[6].setSelected(true);
		} else {
			checkBox[6].setSelected(false);
		}
		if (stats.play.getValue(player) == 1) {
			checkBox[4].setSelected(true);
		} else {
			checkBox[4].setSelected(false);
		}
		if (stats.pass.getValue(player) == 1) {
			checkBox[5].setSelected(true);
		} else {
			checkBox[5].setSelected(false);
		}
		if (stats.midShot.getValue(player) == 1) {
			checkBox[10].setSelected(true);
		} else {
			checkBox[10].setSelected(false);
		}
		if (stats.pk.getValue(player) == 1) {
			checkBox[13].setSelected(true);
		} else {
			checkBox[13].setSelected(false);
		}
		if (stats.k11.getValue(player) == 1) {
			checkBox[7].setSelected(true);
		} else {
			checkBox[7].setSelected(false);
		}
		if (stats.longThrow.getValue(player) == 1) {
			checkBox[22].setSelected(true);
		} else {
			checkBox[22].setSelected(false);
		}
		if (stats.direct.getValue(player) == 1) {
			checkBox[14].setSelected(true);
		} else {
			checkBox[14].setSelected(false);
		}
		if (stats.side.getValue(player) == 1) {
			checkBox[11].setSelected(true);
		} else {
			checkBox[11].setSelected(false);
		}
		if (stats.centre.getValue(player) == 1) {
			checkBox[12].setSelected(true);
		} else {
			checkBox[12].setSelected(false);
		}
		if (stats.outside.getValue(player) == 1) {
			checkBox[15].setSelected(true);
		} else {
			checkBox[15].setSelected(false);
		}
		if (stats.man.getValue(player) == 1) {
			checkBox[16].setSelected(true);
		} else {
			checkBox[16].setSelected(false);
		}
		if (stats.dLine.getValue(player) == 1) {
			checkBox[19].setSelected(true);
		} else {
			checkBox[19].setSelected(false);
		}
		if (stats.slide.getValue(player) == 1) {
			checkBox[17].setSelected(true);
		} else {
			checkBox[17].setSelected(false);
		}
		if (stats.cover.getValue(player) == 1) {
			checkBox[18].setSelected(true);
		} else {
			checkBox[18].setSelected(false);
		}
		if (stats.keeperPK.getValue(player) == 1) {
			checkBox[20].setSelected(true);
		} else {
			checkBox[20].setSelected(false);
		}
		if (stats.keeper11.getValue(player) == 1) {
			checkBox[21].setSelected(true);
		} else {
			checkBox[21].setSelected(false);
		}*/

	}

}
