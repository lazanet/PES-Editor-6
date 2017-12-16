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

import java.awt.Dimension;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class SquadList extends JList {
	private OptionFile of;

	public int team;

	public SquadList(OptionFile opf, boolean setSize)
	{
		super();
		// System.out.println("team-" + team);
		of = opf;
		// refresh(team);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setVisibleRowCount(32);
		// setFont(new Font("Dialog", Font.BOLD, 12));
		// System.out.println(getFont());
		if (setSize)
			setPreferredSize(new Dimension(118, 576));
	}

	public void refresh(int t, boolean normal)
	{
		team = t;
		if (!normal)
		{
			if (team > 63)
				team = team + 9;
		}
		if (team == 219)
		{
			Vector model = new Vector();
			for (int p = 1; p < Player.total; p++)
				model.addElement(new Player(of, p, 0));

			for (int p = Player.firstEdit; p < 32952; p++)
				model.addElement(new Player(of, p, 0));
			Collections.sort(model);
			model.trimToSize();
			setListData(model);
		}
		else
		{
			Player[] players;
			int size;
			int firstAdr;
			int ft;
			int a;
			if (team < 71)
			{
				size = 23;
				firstAdr = Squads.slot23 + (team * size * 2);
				ft = team;
			}
			else if (team == 71)
			{
				size = 14;
				firstAdr = Squads.slot23 + (team * 23 * 2);
				ft = team;
			}
			else
			{
				size = 32;
				firstAdr = Squads.slot32 + ((team - 73) * size * 2);
				ft = team - 9;
			}
			players = new Player[size];
			for (int p = 0; p < size; p++)
			{
				a = firstAdr + (p * 2);
				if ((team >= 0 && team < 64) || (team >= 73 && team < 213))
					a = firstAdr + (Formations.getSlot(of, ft, p) * 2);
				
				players[p] = new Player(of, (of.toInt(of.data[a + 1]) << 8) | of.toInt(of.data[a]), a);
			}
			setListData(players);
		}
	}

}
