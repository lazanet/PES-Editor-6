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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StrategyPanel extends JPanel
{
	private OptionFile of ;

	private SquadList list;

	int squad = 0;

	private JComboBox[] butBox = new JComboBox[4];

	private JComboBox overBox;

	private boolean ok = false;

	private JLabel[] label = new JLabel[4];

	public StrategyPanel(OptionFile opf, SquadList l, PositionList pl)
	{
		super(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Strategy")); of = opf;
		list = l;
		ActionListener act = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (ok)
				{
					int b = new Integer(e.getActionCommand()).intValue();
					byte strat = (byte) butBox[b].getSelectedIndex();
					
					Formations.setStrat( of , squad, b, strat);
					
					if (strat == 6 && Formations.getStratOlCB( of , squad) == 0)
					{
						for (int i = 1; Formations.getStratOlCB( of , squad) == 0 && i < 11; i++)
						{
							byte pos = Formations.getPos( of , squad, 0, i);
							if (pos > 0 && pos < 8)
								Formations.setStratOlCB( of , squad, i);
						}
					}
					refresh(squad);
				}
			}
		};

		String[] items = {
			"No Strategy",
			"Centre Att.",
			"R. Side Att.",
			"L. Side Att.",
			"Opp. Side Att.",
			"Change Sides",
			"CB Overlap",
			"Pressure",
			"Counter Attack",
			"Offside Trap",
			"Strategy Plan A",
			"Strategy Plan B"
		};
		for (int i = 0; i < 4; i++)
		{
			label[i] = new JLabel();
			label[i].setPreferredSize(new Dimension(42, 17));
			label[i].setText(null);
			label[i].setIcon(new PS2ButtonIcon(i));
			butBox[i] = new JComboBox(items);
			butBox[i].setActionCommand(String.valueOf(i));
			butBox[i].addActionListener(act);
		}
		
		overBox = new JComboBox();
		overBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (ok)
				{
					SweItem item = (SweItem) overBox.getSelectedItem();
					if (item != null)
						Formations.setStratOlCB( of , squad, item.index);
				}
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		int x = 0;
		int y = 0;
		for (int i = 0; i < 4; i++)
		{
			if (i < 2)
			{
				x = i + 1;
				y = 0;
			}
			else
			{
				x = i - 1;
				y = 2;
			}
			c.gridx = x;
			c.gridy = y;
			add(label[i], c);

			c.gridx = x;
			c.gridy = y + 1;
			add(butBox[i], c);
		}

		c.gridx = 0;
		c.gridy = 4;
		add(new JLabel("Overlap CB:"), c);
		c.gridwidth = 1;

		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 4;
		add(overBox, c);
	}

	public void refresh(int s)
	{
		squad = s;
		ok = false;
		
		boolean ol = false;
		for (int i = 0; i < 4; i++)
		{
			int strat = Formations.getStrat( of , squad, i);
			butBox[i].setSelectedIndex(strat);
			if (strat == 6)
				ol = true;
		}

		overBox.removeAllItems();

		byte count = 0;
		byte sel = -1;
		for (byte i = 1; i < 11; i++)
		{
			byte pos = Formations.getPos( of , squad, 0, i);
			if (pos > 0 && pos < 8)
			{
				if (i == Formations.getStratOlCB( of , squad))
					sel = count;

				overBox.addItem(new SweItem(i));
				count++;
			}
		}
		overBox.setSelectedIndex(sel);
		

		if (ol && overBox.getItemCount() != 0)
			overBox.setEnabled(true);
		else
			overBox.setEnabled(false);

		ok = true;
	}

	private class SweItem
	{
		String name;

		byte index;

		public SweItem(byte i)
		{
			index = i;
			name = ((Player) list.getModel().getElementAt(index)).name;
		}

		public String toString()
		{
			return name;
		}

	}

}
