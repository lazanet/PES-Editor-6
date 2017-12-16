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

import java.util.Collections;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class NationalityList extends JList
{
	private OptionFile of ;

	public NationalityList(OptionFile opf)
	{
		super(); of = opf;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setVisibleRowCount(32);
	}

	public void refresh(int nation, boolean alpha)
	{
		int a;
		int index;
		Vector model = new Vector();
		if (nation == Stats.nation.length + 5)
		{
			for (int p = 1; p < Player.total; p++)
			{
				model.addElement(new Player( of , p, 0));
			}
			for (int p = Player.firstEdit; p < 32952; p++)
			{
				model.addElement(new Player( of , p, 0));
			}
		}
		else if (nation == Stats.nation.length + 4)
		{
			boolean free;

			for (int p = 1; p < Player.firstClassic; p++)
			{
				free = true;
				a = Squads.slot32 - 2;
				do {
					a = a + 2;
					index = ( of.toInt( of.data[a + 1]) << 8) |
						of.toInt( of.data[a]);
					if (index == p)
					{
						free = false;
					}
				} while (a < Squads.slot32 + (Clubs.total * 64) - 2 && index != p);

				if (free)
				{
					model.addElement(new Player( of , p, 0));
				}
			}

			for (int p = Player.firstClub; p < Player.firstML; p++)
			{
				free = true;
				a = Squads.slot32 - 2;
				do {
					a = a + 2;
					index = ( of.toInt( of.data[a + 1]) << 8) |
						of.toInt( of.data[a]);
					if (index == p)
					{
						free = false;
					}
				} while (a < Squads.slot32 + (Clubs.total * 64) - 2 &&
					index != p);
				if (free)
				{
					model.addElement(new Player( of , p, 0));
				}
			}
		}
		else if (nation == Stats.nation.length + 3)
		{
			for (int p = Player.firstClub; p < Player.firstPESUnited; p++)
			{
				int dupe = getDupe(p);
				if (dupe != -1)
				{
					model.addElement(new Player( of , p, 0));
					model.addElement(new Player( of , dupe, 0));
				}
			}
		}
		else if (nation == Stats.nation.length + 2)
		{
			for (int p = Player.firstYoung; p < Player.firstOld; p++)
				model.addElement(new Player( of , p, 0));
		}
		else if (nation == Stats.nation.length + 1)
		{
			for (int p = Player.firstOld; p < Player.firstUnused; p++)
				model.addElement(new Player( of , p, 0));
		}
		else if (nation == Stats.nation.length)
		{	
			for (int p = Player.firstUnused; p < Player.total; p++)
				model.addElement(new Player( of , p, 0));
		}
		else
		{
			for (int p = 1; p < Player.total; p++)
			{
				if (Stats.getValue( of , p, Stats.nationality) == nation)
				{
					model.addElement(new Player( of , p, 0));
				}
			}
			for (int p = Player.firstEdit; p < 32952; p++)
			{
				if (Stats.getValue( of , p, Stats.nationality) == nation)
				{
					model.addElement(new Player( of , p, 0));
				}
			}
		}
		if (nation != Stats.nation.length + 3 && alpha)
		{
			Collections.sort(model);
		}
		model.trimToSize();
		setListData(model);
	}

	private int getDupe(int p)
	{
		for (int i = 1; i < Player.firstClassic; i++)
		{
			boolean isDupe = true;
			if (Stats.getValue( of , p, Stats.nationality) != Stats.getValue( of ,
					i, Stats.nationality))
			{
				isDupe = false;
			}
			else
			{
				int score = 0;
				if (Stats.getValue( of , p, Stats.age) == Stats.getValue( of , i,
						Stats.age))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.height) == Stats.getValue( of ,
						i, Stats.height))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.weight) == Stats.getValue( of ,
						i, Stats.weight))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.foot) == Stats.getValue( of , i,
						Stats.foot))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.favSide) == Stats.getValue( of ,
						i, Stats.favSide))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.regPos) == Stats.getValue( of ,
						i, Stats.regPos))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.attack) == Stats.getValue( of ,
						i, Stats.attack))
				{
					score++;
				}
				if (Stats.getValue( of , p, Stats.accel) == Stats.getValue( of , i,
						Stats.accel))
				{
					score++;
				}
				if (score < 7)
				{
					isDupe = false;
				}
			}
			if (isDupe)
			{
				return i;
			}
		}
		return -1;
	}

}
