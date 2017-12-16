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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShopPanel extends JPanel
{
	OptionFile of ;

	JLabel status;

	// JButton lock;
	JButton unlock;

	public ShopPanel(OptionFile opf)
	{
		super(); of = opf;
		JPanel pan = new JPanel(new GridLayout(0, 1));
		pan.setBorder(BorderFactory.createTitledBorder("Shop Items"));
		JPanel bpan = new JPanel();
		status = new JLabel("");
	
		unlock = new JButton("Unlock");
		unlock.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent u)
			{
				for (int i = 0; i < 20; i++)
					of.data[5144 + i] = -1;
 
				of.data[5164] = -6;
				of.data[5165] = 11;
				of.data[5166] = -2;
				of.data[5167] = -1;
				of.data[5168] = -17;
				of.data[5169] = Byte.MAX_VALUE;
				of.data[52] = 100;
				status.setText("Unlocked");
			}
		});
		// bpan.add(lock);
		bpan.add(unlock);
		pan.add(bpan);
		pan.add(status);
		add(pan);
	}

}
