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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TeamPanel extends JPanel implements ActionListener,
ListSelectionListener, MouseListener
{
	OptionFile of ;

	OptionFile of2;

	JList list;

	JTextField editor;

	JTextField abvEditor;

	TransferPanel tran;

	String[] team = new String[Clubs.total];

	String[] abv = new String[Clubs.total];

	JButton badgeButton;

	JButton backButton;

	JComboBox stadiumBox;

	JPanel panel3;

	EmblemChooserDialog flagChooser;

	LogoChooserDialog logoChooser;

	LogoPanel imagePan;

	EmblemPanel flagPan;

	private boolean ok = false;

	private GlobalPanel globalPanel;

	private BackChooserDialog backChooser;

	private JButton colour1But;

	private JButton colour2But;

	private KitImportDialog kitImpDia;

	private DefaultIcon defaultIcon = new DefaultIcon();

	public TeamPanel(OptionFile opf, TransferPanel tr, EmblemChooserDialog fc,
		OptionFile opf2, LogoPanel ip, GlobalPanel gp, KitImportDialog kd,
		LogoChooserDialog lc)
	{
		super(new BorderLayout()); of = opf;
		of2 = opf2;
		tran = tr;
		flagChooser = fc;
		logoChooser = lc;
		imagePan = ip;
		kitImpDia = kd;
		globalPanel = gp;

		backChooser = new BackChooserDialog(null);

		PESUtils.javaUI();

		backButton = new JButton(new ImageIcon(Emblems.get16( of , -1, false, false)));
		backButton.setBackground(new Color(204, 204, 204));
		backButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int t = list.getSelectedIndex();
				if (t != -1)
				{
					byte f = backChooser.getBack(getEmblemImage(), Clubs
						.getRed( of , t), Clubs.getGreen( of , t), Clubs
						.getBlue( of , t));
					if (f != 99)
					{
						Clubs.setBack( of , t, f);
						backButton.setIcon(backChooser.flagButton[f].getIcon());
					}
				}
			}
		});

		colour1But = new JButton();
		colour1But.setPreferredSize(new Dimension(20, 20));
		colour1But.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int t = list.getSelectedIndex();
				if (t != -1)
				{
					Color newColor = JColorChooser.showDialog(null, "BG Colour 1", Clubs.getColor( of , t, false));
					if (newColor != null)
					{
						Clubs.setColor( of , t, false, newColor);
						colour1But.setBackground(newColor);
						updateBackBut();
					}
				}
			}
		});

		colour2But = new JButton();
		colour2But.setPreferredSize(new Dimension(20, 20));
		colour2But.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int t = list.getSelectedIndex();
				if (t != -1)
				{
					Color newColor = JColorChooser.showDialog(null, "BG Colour 2", Clubs.getColor( of , t, true));
					if (newColor != null)
					{
						Clubs.setColor( of , t, true, newColor);
						colour2But.setBackground(newColor);
						updateBackBut();
					}
				}
			}
		});

		badgeButton = new JButton(new ImageIcon(Emblems.get16( of , -1, false, false)));
		badgeButton.setBackground(new Color(204, 204, 204));
		badgeButton.addMouseListener(this);
		badgeButton.setToolTipText("Left click to change, right click to default");
		badgeButton.setAlignmentX(CENTER_ALIGNMENT);

		PESUtils.systemUI();

		JButton copyBut = new JButton(new CopySwapIcon(false));
		copyBut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int t = list.getSelectedIndex();
				if (t != -1)
				{
					Clubs.setColor( of , t, true, Clubs.getColor( of , t, false));
					colour2But.setBackground(colour1But.getBackground());
					updateBackBut();
				}
			}
		});
		JButton swapBut = new JButton(new CopySwapIcon(true));
		swapBut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int t = list.getSelectedIndex();
				if (t != -1)
				{

					Color col = Clubs.getColor( of , t, false);
					Clubs.setColor( of , t, false, Clubs.getColor( of , t, true));
					Clubs.setColor( of , t, true, col);
					colour1But.setBackground(Clubs.getColor( of , t, false));
					colour2But.setBackground(Clubs.getColor( of , t, true));

					updateBackBut();
				}
			}
		});

		stadiumBox = new JComboBox();
		stadiumBox.setAlignmentX(CENTER_ALIGNMENT);
		stadiumBox.setPreferredSize(new Dimension(375, 25));
		stadiumBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int s = stadiumBox.getSelectedIndex();
				int t = list.getSelectedIndex();
				if (e.getActionCommand() == "y" && s != -1 && t != -1)
				{
					Clubs.setStadium( of , t, s);
				}
			}
		});

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(11);
		list.addListSelectionListener(this);
		list.addMouseListener(this);
		editor = new JTextField(14);
		editor.setToolTipText("Enter new name and press return");
		abvEditor = new JTextField(4);
		abvEditor.setToolTipText("Enter new short name and press return");
		editor.addActionListener(this);
		abvEditor.addActionListener(this);
		JPanel flagPanel = new JPanel();
		JPanel stadiumPanel = new JPanel();
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
		stadiumPanel.add(stadiumBox);
		panel.add(editor);
		panel2.add(abvEditor);
		panel3.add(panel);
		panel3.add(panel2);
		JLabel badgeLab = new JLabel("Emblem");
		badgeLab.setAlignmentX(CENTER_ALIGNMENT);
		panel3.add(badgeLab);
		panel3.add(badgeButton);
		JLabel flagLab = new JLabel("Flag");
		flagLab.setAlignmentX(CENTER_ALIGNMENT);

		JPanel bgColPan = new JPanel(new BorderLayout());
		JPanel colPan = new JPanel(new GridLayout(0, 1));
		colPan.add(colour1But);
		colPan.add(colour2But);
		bgColPan.add(copyBut, BorderLayout.WEST);
		bgColPan.add(colPan, BorderLayout.CENTER);
		bgColPan.add(swapBut, BorderLayout.EAST);

		JPanel backPanel = new JPanel(new BorderLayout());
		backPanel.add(bgColPan, BorderLayout.NORTH);
		backPanel.add(backButton, BorderLayout.SOUTH);
		flagPanel.add(backPanel);

		panel3.add(Box.createRigidArea(new Dimension(0, 10)));

		panel3.add(flagLab);
		panel3.add(flagPanel);
		panel3.add(Box.createRigidArea(new Dimension(0, 30)));
		JLabel stadLab = new JLabel("Stadium");
		stadLab.setAlignmentX(CENTER_ALIGNMENT);
		panel3.add(stadLab);
		panel3.add(stadiumPanel);

		JPanel bPanel = new JPanel();
		panel3.add(bPanel);

		JScrollPane scroll = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(list);
		add(scroll, BorderLayout.WEST);
		add(panel3, BorderLayout.CENTER);
	}

	public void refresh()
	{
		String[] listText = new String[67 + Clubs.total];
		stadiumBox.setActionCommand("n");
		stadiumBox.removeAllItems();
		for (int s = 0; s < Stadia.total; s++)
		{
			stadiumBox.addItem(Stadia.get( of , s));
		}

		stadiumBox.setSelectedIndex(-1);
		stadiumBox.setActionCommand("y");
		backButton.setIcon(new ImageIcon(Emblems.get16( of , -1, false, false)));
		badgeButton.setIcon(new ImageIcon(Emblems.get16( of , -1, false, false)));
		team = Clubs.getNames( of );
		for (int t = 0; t < Clubs.total; t++)
		{
			listText[t] = Clubs.getAbv( of , t) + "     " + team[t];
		}
		globalPanel.updateTeamBox(team);
		for (int n = 0; n < 57; n++)
		{
			listText[n + Clubs.total] = Stats.nation[n];
		}
		for (int n = 0; n < 7; n++)
		{
			listText[n + Clubs.total + 57] = PESUtils.extraSquad[n];
		}
		ok = false;
		list.setListData(listText);
		panel3.setVisible(false);
		ok = true;

	}

	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getSource() == editor)
		{
			String text = editor.getText();
			if (text.length() <= 48 && text.length() > 0)
			{
				int t = list.getSelectedIndex();
				Clubs.setName( of , t, text);
				refresh();
				tran.refresh();
				if (t < list.getModel().getSize() - 1)
				{
					list.setSelectedIndex(t + 1);
					list.ensureIndexIsVisible(list.getSelectedIndex());
					editor.requestFocusInWindow();
					editor.selectAll();
				}

			}
		}
		else
		{
			String text = abvEditor.getText();
			if (text.length() == 3)
			{
				text = text.toUpperCase();
				int t = list.getSelectedIndex();
				Clubs.setAbv( of , t, text);
				refresh();
				tran.refresh();
				if (t < list.getModel().getSize() - 1)
				{
					list.setSelectedIndex(t + 1);
					list.ensureIndexIsVisible(list.getSelectedIndex());
					abvEditor.requestFocusInWindow();
					abvEditor.selectAll();
				}

			}
		}
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (ok && e.getValueIsAdjusting() == false)
		{
			int i = list.getSelectedIndex();
			if (i >= 0 && i < Clubs.total)
			{
				if (!panel3.isVisible())
				{
					panel3.setVisible(true);
				}

				int f = Clubs.getEmblem( of , i);
				if (f >= Clubs.firstFlag &&
					f < Clubs.firstFlag + Emblems.total + Emblems.totalB)
				{
					f = f - Clubs.firstFlag;
					badgeButton.setIcon(new ImageIcon(Emblems.getImage( of , f)));
				}
				else
				{
					if (f == i + Clubs.firstDefEmblem)
					{
						badgeButton.setIcon(defaultIcon);
					}
					else
					{
						badgeButton.setIcon(new ImageIcon(Emblems.get16( of , -1,
							false, false)));
					}
				}

				colour1But.setBackground(Clubs.getColor( of , i, false));
				colour2But.setBackground(Clubs.getColor( of , i, true));

				updateBackBut();

				stadiumBox.setActionCommand("n");
				stadiumBox.setSelectedIndex(Clubs.getStadium( of , i));
				stadiumBox.setActionCommand("y");
				editor.setText(team[i]);

				abvEditor.setText(Clubs.getAbv( of , i));

			}
			else
			{
				editor.setText("");
				abvEditor.setText("");
				stadiumBox.setActionCommand("n");
				stadiumBox.setSelectedIndex(-1);
				stadiumBox.setActionCommand("y");
				badgeButton.setIcon(new ImageIcon(Emblems.get16( of , -1, false,
					false)));
				panel3.setVisible(false);
			}
		}
	}

	public int getInt(byte[] b)
	{
		int i = 0;
		if (b.length == 2)
		{
			i = ( of .toInt(b[1]) << 8) | ( of .toInt(b[0]) & 0xFF);
		}
		return i;
	}

	public void mousePressed(MouseEvent e)
	{}

	public void mouseReleased(MouseEvent e)
	{}

	public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}

	public void mouseClicked(MouseEvent e)
	{
		int clicks = e.getClickCount();
		int ti = list.getSelectedIndex();
		if (e.getSource() == list && e.getButton() == MouseEvent.BUTTON1 &&
			clicks == 2)
		{
			if (of2.fileName != null)
			{
				if (ti != -1)
				{
					int t2 = kitImpDia.show(ti);
					if (t2 != -1)
					{
						importKit(ti, t2);
					}
				}
			}
		}

		if (e.getSource() == badgeButton && clicks == 1)
		{
			if ((e.getButton() == MouseEvent.BUTTON3 || (e.getButton() == MouseEvent.BUTTON1 && e
					.isControlDown())))
			{

				if (ti != -1 && ti < Clubs.total)
				{
					Clubs.setEmblem( of , ti, null);
					badgeButton.setIcon(defaultIcon);
					updateBackBut();
				}

			}
			else if (e.getButton() == MouseEvent.BUTTON1)
			{
				int t = list.getSelectedIndex();
				if (t != -1)
				{
					int f = flagChooser.getFlag("Choose Emblem", 0);
					if (f != -1)
					{
						if (f < Emblems.total)
						{
							badgeButton.setIcon(new ImageIcon(Emblems.get128( of , f, false, false)));
						}
						else
						{
							badgeButton.setIcon(new ImageIcon(Emblems.get16( of ,
								f - Emblems.total, false, false)));
						}
						Clubs.setEmblem( of , t, Emblems.getIndex( of , f));
						updateBackBut();
					}
				}
			}
		}
	}

	private void updateBackBut()
	{
		int i = list.getSelectedIndex();
		backButton.setIcon(backChooser.getFlagBG(getEmblemImage(), Clubs
			.getBack( of , i), Clubs.getRed( of , i), Clubs.getGreen( of , i),
			Clubs.getBlue( of , i)));
	}

	private Image getEmblemImage()
	{
		Image image = null;
		int id = list.getSelectedIndex();
		int f = Clubs.getEmblem( of , id);
		if (f >= Clubs.firstFlag &&
			f < Clubs.firstFlag + Emblems.total + Emblems.totalB)
		{
			ImageIcon icon = (ImageIcon) badgeButton.getIcon();
			image = icon.getImage();
		}
		return image;
	}

	private void importKit(int t1, int t2)
	{
		int emblem1 = 0;
		if (t1 < Clubs.total)
		{
			emblem1 = Clubs.getEmblem( of , t1) - Clubs.firstFlag;
			if (emblem1 >= 0 && emblem1 < Emblems.total + Emblems.totalB)
			{
				Emblems.deleteImage( of , emblem1);
			}
		}
		byte[] logos = new byte[4];
		boolean[] delete = new boolean[4];
		for (int l = 0; l < 4; l++)
		{
			delete[l] = true;
			if (Kits.logoUsed( of , t1, l))
			{
				logos[l] = Kits.getLogo( of , t1, l);
			}
			else
			{
				logos[l] = 88;
			}
		}
		for (int t = 0; t < Clubs.total + Kits.totalN; t++)
		{
			for (int l = 0; t != t1 && l < 4; l++)
			{
				if (logos[l] != 88)
				{
					for (int k = 0; k < 4; k++)
					{
						if (Kits.getLogo( of , t, k) == logos[l])
						{
							if (Kits.logoUsed( of , t, k))
							{
								delete[l] = false;
							}
							else
							{
								Kits.setLogoUnused( of , t, k);
							}
						}
					}
				}
			}
		}

		for (int l = 0; l < 4; l++)
		{
			if (delete[l] && logos[l] >= 0 && logos[l] < 80)
			{
				Logos.delete( of , logos[l]);
			}
		}

		if (t1 < Clubs.total)
		{
			int emblem2 = Clubs.getEmblem(of2, t2) - Clubs.firstFlag;
			byte[] embIndex = null;
			if (emblem2 >= 0 && emblem2 < Emblems.total + Emblems.totalB)
			{
				embIndex = Emblems.getIndex(of2, emblem2);
				if (emblem2 < Emblems.total)
				{
					if (Emblems.getFree128( of ) > 0)
					{
						Emblems.import128( of , Emblems.count128( of ), of2,
							Emblems.getLoc(of2, emblem2));
						embIndex = Emblems.getIndex( of ,
							Emblems.count128( of ) - 1);
					}
					else
					{
						int rep = flagChooser.getFlag("Replace Emblem", 2);
						if (rep != -1)
						{
							Emblems.import128( of , rep, of2, Emblems.getLoc(of2,
								emblem2));
							embIndex = Emblems.getIndex( of , rep);
						}
						else
						{
							embIndex = null;
						}
					}
				}
				else
				{
					if (Emblems.getFree16( of ) > 0)
					{
						Emblems.import16( of , Emblems.count16( of ), of2, Emblems
							.getLoc(of2, emblem2) -
							Emblems.total);
						embIndex = Emblems.getIndex( of , Emblems.count16( of ) +
							Emblems.total - 1);
					}
					else
					{
						int rep = flagChooser.getFlag("Replace Emblem", 1);
						if (rep != -1)
						{
							Emblems.import16( of , rep - Emblems.total, of2,
								Emblems.getLoc(of2, emblem2) -
								Emblems.total);
							embIndex = Emblems.getIndex( of , rep);
						}
						else
						{
							embIndex = null;
						}
					}
				}
			}

			Clubs.importClub( of , t1, of2, t2);
			if (emblem2 >= 0 && emblem2 < Emblems.total + Emblems.totalB)
			{
				Clubs.setEmblem( of , t1, embIndex);
			}
		}

		Kits.importKit( of , t1, of2, t2);

		for (int l = 0; l < 4; l++)
		{
			if (Kits.logoUsed(of2, t2, l))
			{
				boolean dupe = false;
				for (int k = 0; !dupe && k < l; k++)
				{
					if (Kits.getLogo(of2, t2, l) == Kits.getLogo(of2, t2, k))
					{
						dupe = true;
					}
				}
				if (!dupe)
				{
					byte targ = logoChooser.getFlag("Choose logo to replace",
						Logos.get(of2, Kits.getLogo(of2, t2, l), false));
					if (targ != 88)
					{
						Logos.importLogo(of2, Kits.getLogo(of2, t2, l), of ,
							targ);
						for (int k = l; k < 4; k++)
						{
							if (Kits.getLogo(of2, t2, l) == Kits.getLogo(of2,
									t2, k))
							{
								Kits.setLogo( of , t1, k, targ);
							}
						}
					}
					else
					{
						for (int k = l; k < 4; k++)
						{
							if (Kits.getLogo(of2, t2, l) == Kits.getLogo(of2, t2, k))
							{
								Kits.setLogoUnused( of , t1, k);
							}
						}
					}
				}
			}
		}

		flagPan.refresh();
		imagePan.refresh();
		tran.refresh();
		refresh();
	}

}
