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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TeamSetPanel extends JPanel
{
	private OptionFile of ;
	int alt = 0;
	int squad = 0;
	private JComboBox[] box = new JComboBox[4];
	private boolean ok = false;

	public TeamSetPanel(OptionFile opf)
	{
		super(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Team Settings")); of = opf;

		ActionListener act = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (ok)
				{
					int b = new Integer(e.getActionCommand()).intValue();
					Formations.setTeam( of , squad, alt, b, box[b].getSelectedIndex());
				}
			}
		};

		String[] items3 = { "A", "B", "C" };

		for (int i = 0; i < 4; i++)
		{
			box[i] = new JComboBox(items3);
			box[i].setActionCommand(String.valueOf(i));
			box[i].addActionListener(act);
		}
		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 10, 0, 1);

		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Back line"), c);

		c.gridx = 0;
		c.gridy = 1;
		add(new JLabel("Pressure"), c);

		c.gridx = 2;
		c.gridy = 0;
		add(new JLabel("Offside Trap"), c);

		c.gridx = 2;
		c.gridy = 1;
		add(new JLabel("Counter Attack"), c);

		c.insets = new Insets(0, 1, 0, 10);

		c.gridx = 1;
		c.gridy = 0;
		add(box[0], c);

		c.gridx = 1;
		c.gridy = 1;
		add(box[1], c);

		c.gridx = 3;
		c.gridy = 0;
		add(box[2], c);

		c.gridx = 3;
		c.gridy = 1;
		add(box[3], c);
	}

	public void refresh(int s)
	{
		squad = s;
		ok = false;
		
		for (int i = 0; i < 4; i++)
			box[i].setSelectedIndex(Formations.getTeam( of , squad, alt, i));
		
		ok = true;
	}
}
