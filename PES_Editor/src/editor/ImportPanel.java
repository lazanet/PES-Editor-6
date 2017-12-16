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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImportPanel extends JPanel
{
	JLabel importFile;

	JPanel panel;

	private OptionFile of ;

	private OptionFile of2;

	private JButton optionsButton;

	private JButton stadiumButton;

	private JButton leagueButton;

	private JButton bootsButton;

	private JButton clubNameButton;

	private JButton playerButton;

	private JButton allKitButton;

	private WENShopPanel wenShop;

	private StadiumPanel stadPan;

	private LeaguePanel leaguePan;

	private TeamPanel teamPan;

	EmblemPanel flagPan;

	LogoPanel imagePan;

	private TransferPanel tranPan;

	public ImportPanel(OptionFile opf, OptionFile opf2, WENShopPanel w,
		StadiumPanel s, LeaguePanel l, TeamPanel t, EmblemPanel fp,
		LogoPanel ip, TransferPanel tp)
	{ // , Stats s1, Stats s2) {
		super(new BorderLayout()); of = opf;
		of2 = opf2;
		wenShop = w;
		stadPan = s;
		leaguePan = l;
		teamPan = t;
		flagPan = fp;
		imagePan = ip;
		tranPan = tp;

		JPanel butPan = new JPanel(new GridLayout(0, 1));
		importFile = new JLabel(
			"To use the import options you must first use File > Open OF2...");
		panel = new JPanel();
		optionsButton = new JButton("Options / PES points / Shop items / Cup gallery / Memorial match data");
		optionsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				System.arraycopy(of2.data, OptionFile.block[0], of .data,
					OptionFile.block[0], OptionFile.blockSize[0]);
				System.arraycopy(of2.data, OptionFile.block[1], of .data,
					OptionFile.block[1], OptionFile.blockSize[1]);
				System.arraycopy(of2.data, OptionFile.block[9], of .data,
					OptionFile.block[9], OptionFile.blockSize[9]);
				System.arraycopy(of2.data, OptionFile.block[10], of .data,
					OptionFile.block[10], OptionFile.blockSize[10]);
				wenShop.wenPanel.refresh();
				wenShop.shopPanel.status.setText("");
				optionsButton.setEnabled(false);
			}
		});
		stadiumButton = new JButton("Stadium names");
		stadiumButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Stadia.importData(of2, of );
				stadPan.refresh();
				teamPan.refresh();
				stadiumButton.setEnabled(false);
			}
		});
		leagueButton = new JButton("League names");
		leagueButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Leagues.importData(of2, of );
				leaguePan.refresh();
				leagueButton.setEnabled(false);
			}
		});
		bootsButton = new JButton("Boots");
		bootsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				System.arraycopy(of2.data, 657116, of .data, 657116, 828);
				bootsButton.setEnabled(false);
			}
		});

		playerButton = new JButton("Players / Squads / Formations");
		playerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				System.arraycopy(of2.data, OptionFile.block[3], of .data,
					OptionFile.block[3], OptionFile.blockSize[3]);
				System.arraycopy(of2.data, OptionFile.block[4], of .data,
					OptionFile.block[4], OptionFile.blockSize[4]);
				System.arraycopy(of2.data, OptionFile.block[5], of .data,
					OptionFile.block[5], OptionFile.blockSize[5]);

				if (!of.isWE() && of2.isWE()) 
					Convert.allPlayers(of,Convert.WE2007_PES6);  

				if (of.isWE() && !of2.isWE())
					 Convert.allPlayers(of, Convert.PES6_WE2007);
				 

				tranPan.refresh();
				playerButton.setEnabled(false);
			}
		});

		clubNameButton = new JButton("Club names");
		clubNameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Clubs.importNames( of , of2);
				teamPan.refresh();
				tranPan.refresh();
				clubNameButton.setEnabled(false);
			}
		});

		allKitButton = new JButton(
			"Kits / Emblems / Logos / Club stadiums, flags, colours");
		allKitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Clubs.importData( of , of2);
				System.arraycopy(of2.data, OptionFile.block[7], of .data,
					OptionFile.block[7], OptionFile.blockSize[7]);
				System.arraycopy(of2.data, OptionFile.block[8], of .data,
					OptionFile.block[8], OptionFile.blockSize[8]);

				 if (!of.isWE() && of2.isWE()) Convert.allKitModel(of);

				flagPan.refresh();
				imagePan.refresh();
				teamPan.refresh();
				tranPan.refresh();
				allKitButton.setEnabled(false);
			}
		});

		butPan.add(optionsButton);
		butPan.add(stadiumButton);
		butPan.add(leagueButton);
		butPan.add(bootsButton);
		butPan.add(playerButton);
		butPan.add(clubNameButton);

		butPan.add(allKitButton);
		panel.add(butPan);
		add(importFile, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		panel.setEnabled(false);
	}

	public void refresh()
	{
		if (of2.fileName == null)
		{
			panel.setVisible(false);
			importFile
				.setText("To use the import options you must first use File > Open OF2...");
		}
		else
		{
			importFile.setText("From:  " + of2.fileName);
			panel.setVisible(true);
			if ( of .gameID.equals(of2.gameID))
			{
				optionsButton.setEnabled(true);
			}
			else
			{
				optionsButton.setEnabled(false);
			}
			stadiumButton.setEnabled(true);
			leagueButton.setEnabled(true);
			bootsButton.setEnabled(true);
			clubNameButton.setEnabled(true);
			playerButton.setEnabled(true);
			allKitButton.setEnabled(true);
		}
	}

	public void disableAll()
	{
		optionsButton.setEnabled(false);
		stadiumButton.setEnabled(false);
		leagueButton.setEnabled(false);
		bootsButton.setEnabled(false);
		clubNameButton.setEnabled(false);
		playerButton.setEnabled(false);
		allKitButton.setEnabled(false);
	}

}
