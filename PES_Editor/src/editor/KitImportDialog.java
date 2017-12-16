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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class KitImportDialog extends JDialog implements MouseListener
{
	OptionFile of2;

	JLabel fileLabel;

	JList list;

	int index;

	public KitImportDialog(Frame owner, OptionFile opf2)
	{
		super(owner, "Import Kit", true);
		of2 = opf2;
		JPanel panel = new JPanel(new BorderLayout());
		fileLabel = new JLabel("From:");
		list = new JList();
		// list.addListSelectionListener(this);
		list.addMouseListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(20);
		JScrollPane scroll = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(list);
		CancelButton cancelButton = new CancelButton(this);
		panel.add(fileLabel, BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);
		panel.add(cancelButton, BorderLayout.SOUTH);
		getContentPane().add(panel);
		index = 0;

		// setResizable(false);
		// setPreferredSize(new Dimension(462, 677));
		// System.out.println(getWidth() + ", " + getHeight());
	}

	public int show(int i)
	{
		index = -1;
		refresh(i);
		setVisible(true);
		return index;
	}

	public void refresh(int i)
	{
		DefaultListModel model = new DefaultListModel();
		model.removeAllElements();

		if (i < Clubs.total)
		{
			model.addElement(new KitItem(Clubs.getName(of2, i), i));
			for (int c = 0; c < Clubs.total; c++)
			{
				if (c != i && !Kits.isLic(of2, c))
				{
					model.addElement(new KitItem(Clubs.getName(of2, c), c));
				}
			}
		}
		else
		{
			if (i - Clubs.total > 59)
			{
				model.addElement(new KitItem(PESUtils.extraSquad[i -
					Clubs.total - 60], i));
			}
			else
			{
				model.addElement(new KitItem(Stats.nation[i - Clubs.total], i));
			}
			for (int n = Clubs.total; n < Clubs.total + 67; n++)
			{
				if (n != i && !Kits.isLic(of2, n))
				{
					if (n - Clubs.total > 59)
					{
						model.addElement(new KitItem(PESUtils.extraSquad[n -
							Clubs.total - 60], n));
					}
					else
					{
						model.addElement(new KitItem(Stats.nation[n -
							Clubs.total], n));
					}
				}
			}
		}
		model.trimToSize();
		list.setModel(model);
		fileLabel.setText("  From:  " + of2.fileName);
		pack();
	}

	/*
	 * public void valueChanged(ListSelectionEvent e) { if
	 * (e.getValueIsAdjusting() == false) { if (!list.isSelectionEmpty()) { } }
	 * }
	 */

	public void mousePressed(MouseEvent e)
	{}

	public void mouseReleased(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}

	public void mouseClicked(MouseEvent e)
	{
		int clicks = e.getClickCount();
		JList list = (JList)(e.getSource());
		KitItem i = (KitItem) list.getSelectedValue();
		if (clicks == 2 && i != null)
		{
			index = i.num;
			setVisible(false);
		}
	}

	private class KitItem
	{
		String team;
		int num;

		public KitItem(String s, int n)
		{
			team = s;
			num = n;
		}

		public String toString()
		{
			return team;
		}
	}

}
