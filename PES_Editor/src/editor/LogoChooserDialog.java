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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LogoChooserDialog extends JDialog
{
	private JButton[] flagButton;

	private boolean trans = true;

	private OptionFile of ;

	byte slot;

	private JLabel repLabel;

	public LogoChooserDialog(Frame owner, OptionFile opt)
	{
		super(owner, true); of = opt;
		JPanel flagPanel;
		flagPanel = new JPanel(new GridLayout(8, 10));
		flagButton = new JButton[Logos.total];

		PESUtils.javaUI();

		for (int l = 0; l < Logos.total; l++)
		{
			flagButton[l] = new JButton(new ImageIcon(Logos.get( of , -1, false)));
			// flagButton[l].setIcon();
			flagButton[l].setMargin(new Insets(0, 0, 0, 0));
			flagButton[l].setActionCommand((new Integer(l)).toString());
			flagButton[l].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent b)
				{
					slot = (byte)((new Integer(((JButton) b.getSource())
						.getActionCommand())).intValue());
					setVisible(false);
				}
			});
			flagPanel.add(flagButton[l]);
		}

		PESUtils.systemUI();

		JButton transButton = new JButton("Transparency");
		transButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent t)
			{
				trans = !trans;
				refresh();
				// System.out.println(trans);
				/*
				 * for (int f = 0; f < 64; f++) { if (trans) {
				 * flagButton[f].setIcon(of.tranFlag[f]); } else {
				 * flagButton[f].setIcon(of.flag[f]); } }
				 */
			}
		});
		CancelButton cancelButton = new CancelButton(this);

		repLabel = new JLabel(new ImageIcon(Logos.get( of , -1, false)));
		JPanel centrePanel = new JPanel(new BorderLayout());
		// JLabel repText = new JLabel("
		centrePanel.add(repLabel, BorderLayout.NORTH);
		centrePanel.add(flagPanel, BorderLayout.CENTER);
		getContentPane().add(transButton, BorderLayout.NORTH);
		getContentPane().add(cancelButton, BorderLayout.SOUTH);
		getContentPane().add(centrePanel, BorderLayout.CENTER);
		slot = 88;
		setResizable(false);
		pack();
	}

	public void refresh()
	{
		for (int f = 0; f < Logos.total; f++)
		{
			flagButton[f].setIcon(new ImageIcon(Logos.get( of , f, !trans)));
		}
		// slot = 99;
	}

	public byte getFlag(String title, Image image)
	{
		slot = 88;
		setTitle(title);
		repLabel.setIcon(new ImageIcon(image));
		refresh();
		setVisible(true);
		return slot;
	}

}
