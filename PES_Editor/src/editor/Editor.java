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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class Editor extends JFrame
{
	private JFileChooser chooser;

	private OptionFile of ;

	private OptionFile of2;

	private File currentFile = null;

	private OptionFilter filter;

	protected EmblemPanel flagPanel;

	protected LogoPanel imagePanel;

	protected TransferPanel tranPanel;

	protected WENShopPanel wenShop;

	protected StadiumPanel stadPan;

	protected TeamPanel teamPan;

	protected LeaguePanel leaguePan;

	JTabbedPane tabbedPane;

	PlayerImportDialog plImpDia;

	KitImportDialog kitImpDia;

	EmblemImportDialog flagImpDia;

	LogoImportDialog imageImpDia;

	PlayerDialog playerDia;

	EmblemChooserDialog flagChooser;

	FormationDialog teamDia;

	ImportPanel importPanel;

	LogoChooserDialog logoChooser;

	private CSVMaker csvMaker;

	private JMenuItem csvItem;
	
	private JMenuItem psdItem;

	private JMenuItem open2Item;

	private JMenuItem saveItem;

	private JMenuItem saveAsItem;

	private JFileChooser csvChooser;

	private CSVSwitch csvSwitch;

	private GlobalPanel globalPanel;

	private HelpDialog helpDia;

	private File settingsFile;

	private JMenuItem convertItem;

	public Editor()
	{
		super("PES Editor 6");
		setIcon();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		filter = new OptionFilter();
		tabbedPane = new JTabbedPane();
		csvMaker = new CSVMaker();
		csvChooser = new JFileChooser();
		csvSwitch = new CSVSwitch();

		of = new OptionFile();
		of2 = new OptionFile();

		csvChooser.addChoosableFileFilter(new CSVFilter());
		csvChooser.setAcceptAllFileFilterUsed(false);
		csvChooser.setAccessory(csvSwitch);

		flagChooser = new EmblemChooserDialog(this, of );
		logoChooser = new LogoChooserDialog(this, of );
		plImpDia = new PlayerImportDialog(this, of, of2);
		kitImpDia = new KitImportDialog(this, of2);
		flagImpDia = new EmblemImportDialog(this, of2);
		imageImpDia = new LogoImportDialog(this, of, of2);
		playerDia = new PlayerDialog(this, of, plImpDia);

		teamDia = new FormationDialog(this, of );

		tranPanel = new TransferPanel(playerDia, of, teamDia);

		imagePanel = new LogoPanel(of, imageImpDia);
		globalPanel = new GlobalPanel(of, tranPanel);
		teamPan = new TeamPanel(of, tranPanel, flagChooser, of2, imagePanel, globalPanel, kitImpDia, logoChooser);
		flagPanel = new EmblemPanel(of, flagImpDia, teamPan);
		teamPan.flagPan = flagPanel;

		wenShop = new WENShopPanel(of );
		stadPan = new StadiumPanel(of, teamPan);
		leaguePan = new LeaguePanel(of );
		importPanel = new ImportPanel(of, of2, wenShop, stadPan, leaguePan, teamPan, flagPanel, imagePanel, tranPanel);

		helpDia = new HelpDialog(this);

		tabbedPane.addTab("Transfer", null, tranPanel, null);
		tabbedPane.addTab("Team", null, teamPan, null);
		tabbedPane.addTab("Emblem", null, flagPanel, null);
		tabbedPane.addTab("Logo", null, imagePanel, null);
		tabbedPane.addTab("Stadium", null, stadPan, null);
		tabbedPane.addTab("League", null, leaguePan, null);
		tabbedPane.addTab("PES / Shop", null, wenShop, null);
		tabbedPane.addTab("Stat Adjust", null, globalPanel, null);
		tabbedPane.addTab("OF2 Import", null, importPanel, null);

		settingsFile = new File("PFE_settings.dat");
		chooser = new JFileChooser(loadSet());

		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(filter);
		chooser.setAccessory(new OptionPreview(chooser));

		buildMenu();
		getContentPane().add(tabbedPane);
		pack();
		tabbedPane.setVisible(false);
		setVisible(true);
		openFile();
	}

	private void buildMenu()
	{
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenu help = new JMenu("Help");
		JMenu tool = new JMenu("Tools");
		JMenuItem openItem = new JMenuItem("Open...");
		open2Item = new JMenuItem("Open OF2...");
		saveItem = new JMenuItem("Save");
		saveAsItem = new JMenuItem("Save As...");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem helpItem = new JMenuItem("PES Editor 6 Help...");
		JMenuItem aboutItem = new JMenuItem("About...");
		convertItem = new JMenuItem("Convert OF2 > OF1");

		csvItem = new JMenuItem("Make csv stats file...");
		psdItem = new JMenuItem("Get PSD Stats...");
		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		openItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent o)
			{
				openFile();
			}
		});
		open2Item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent o2)
			{
				int returnVal = chooser.showOpenDialog(getContentPane());
				if (returnVal == JFileChooser.APPROVE_OPTION &&
					filter.accept(chooser.getSelectedFile()))
				{
					if (chooser.getSelectedFile().isFile() &&
						of2.readXPS(chooser.getSelectedFile()))
					{
						Squads.fixAll(of2);
						plImpDia.refresh();
						flagImpDia.of2Open = true;
						imageImpDia.refresh();
						importPanel.refresh();
						flagPanel.refresh();
						teamPan.list.setToolTipText("Double click to import kit from OF2");
						if (of .fileName != null)
							convertItem.setEnabled(true);
						else
							convertItem.setEnabled(false);

					}
					else
					{
						teamPan.list.setToolTipText(null);
						plImpDia.of2Open = false;
						flagImpDia.of2Open = false;
						imageImpDia.of2Open = false;
						flagPanel.refresh();
						importPanel.refresh();

						convertItem.setEnabled(false);
						openFailMsg();
					}
				}
			}
		});
		saveItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent v)
			{
				if (currentFile != null)
				{
					if (currentFile.delete() && of .saveXPS(currentFile))
					{
						saveOkMsg(currentFile);
						chooser.setSelectedFile(null);
					}
					else
					{
						saveFailMsg();
					}
				}
			}
		});
		saveAsItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				if (currentFile != null)
				{
					int returnVal = chooser.showSaveDialog(getContentPane());
					saveSet();
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File dest = chooser.getSelectedFile();
						if (of .format == 0)
						{
							if ((PESUtils.getExtension(dest) == null) ||
								!(PESUtils.getExtension(dest)
									.equals(PESUtils.xps)))
							{
								dest = new File(dest.getParent() +
									File.separator + dest.getName() +
									".xps");
							}
						}
						if (of .format == 2)
						{
							if ((PESUtils.getExtension(dest) == null) ||
								!(PESUtils.getExtension(dest)
									.equals(PESUtils.psu)))
							{
								dest = new File(dest.getParent() +
									File.separator + dest.getName() +
									".psu");
							}
						}
						if (of .format == 3)
						{
							if ((PESUtils.getExtension(dest) == null) ||
								!(PESUtils.getExtension(dest)
									.equals(PESUtils.max)))
							{
								dest = new File(dest.getParent() +
									File.separator + dest.getName() +
									".max");
							}
						}

						if (fileNameLegal(dest.getName()))
						{
							if (dest.exists())
							{
								int n = JOptionPane
									.showConfirmDialog(
										getContentPane(), 							dest.getName() +
										"\nAlready exists in:\n" +
										dest.getParent() +
										"\nAre you sure you want to replace this file?", 							"Replace:  " + dest.getName(), 							JOptionPane.YES_NO_OPTION, 							JOptionPane.WARNING_MESSAGE, 							null);
								if (n == 0)
								{
									if (dest.delete() && of .saveXPS(dest))
									{
										currentFile = dest;
										setTitle("PES Editor 6 - " +
											currentFile.getName());
										saveOkMsg(dest);
										chooser.setSelectedFile(null);
									}
									else
									{
										saveFailMsg();
									}
								}
							}
							else
							{

								if (of .saveXPS(dest))
								{
									currentFile = dest;
									setTitle("PES Editor 6 - " +
										currentFile.getName());
									saveOkMsg(dest);
									chooser.setSelectedFile(null);
								}
								else
								{
									saveFailMsg();
								}

							}
						}
						else
						{
							illNameMsg();
						}
					}
				}
			}
		});

		helpItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent h)
			{
				helpDia.setVisible(true);
			}
		});

		aboutItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent h)
			{
				about();
			}
		});

		csvItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent c)
			{
				if (currentFile != null)
				{
					int returnVal = csvChooser.showSaveDialog(getContentPane());
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File dest = csvChooser.getSelectedFile();
						boolean head = csvSwitch.head.isSelected();
						// boolean extra = false;
						boolean create = csvSwitch.create.isSelected();
						if ((PESUtils.getExtension(dest) == null) ||
							!(PESUtils.getExtension(dest)
								.equals(PESUtils.csv)))
						{
							dest = new File(dest.getParent() + File.separator +
								dest.getName() + ".csv");
						}

						if (fileNameLegal(dest.getName()))
						{
							if (dest.exists())
							{
								int n = JOptionPane
									.showConfirmDialog(
										getContentPane(), 							dest.getName() +
										"\nAlready exists in:\n" +
										dest.getParent() +
										"\nAre you sure you want to replace this file?", 							"Replace:  " + dest.getName(), 							JOptionPane.YES_NO_OPTION, 							JOptionPane.WARNING_MESSAGE, 							null);
								if (n == 0)
								{
									if (dest.delete() && csvMaker.makeFile(of, dest, head, false, create))
									{
										saveOkMsg(dest);
									}
									else
									{
										saveFailMsg();
									}
								}
							}
							else
							{

								if (csvMaker.makeFile(of, dest, head, false,create))
								{
									saveOkMsg(dest);
								}
								else
								{
									saveFailMsg();
								}

							}
						}
						else
						{
							illNameMsg();
						}
					}
				}
			}
		});

		psdItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent c)
			{
				System.out.println("PSD init");
				new PSDConnPanel();
			}
		});

		convertItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				System.arraycopy(of2.data, OptionFile.block[2], of .data, 		OptionFile.block[2], OptionFile.blockSize[2]);
				System.arraycopy(of2.data, OptionFile.block[3], of .data, 		OptionFile.block[3], OptionFile.blockSize[3]);
				System.arraycopy(of2.data, OptionFile.block[4], of .data, 		OptionFile.block[4], OptionFile.blockSize[4]);
				System.arraycopy(of2.data, OptionFile.block[5], of .data, 		OptionFile.block[5], OptionFile.blockSize[5]);
				System.arraycopy(of2.data, OptionFile.block[6], of .data, 		OptionFile.block[6], OptionFile.blockSize[6]);
				System.arraycopy(of2.data, OptionFile.block[7], of .data, 		OptionFile.block[7], OptionFile.blockSize[7]);
				System.arraycopy(of2.data, OptionFile.block[8], of .data, 		OptionFile.block[8], OptionFile.blockSize[8]);
				System.arraycopy(of2.data, 654732, of .data, 654732, 828);

				 if (!of.isWE() && of2.isWE()) { Convert.allKitModel(of);
				 Convert.allPlayers(of, Convert.WE2007_PES6); } if (of.isWE()
				 && !of2.isWE()) { Convert.allPlayers(of, 	 Convert.PES6_WE2007); }

				flagPanel.refresh();
				imagePanel.refresh();
				tranPanel.refresh();
				stadPan.refresh();
				teamPan.refresh();
				leaguePan.refresh();
				importPanel.disableAll();
				convertItem.setEnabled(false);
			}
		});

		menu.add(openItem);
		menu.add(open2Item);
		menu.add(saveItem);
		menu.add(saveAsItem);
		menu.add(exitItem);
		help.add(helpItem);
		help.add(aboutItem);
		tool.add(csvItem);
		tool.add(psdItem);
		tool.add(convertItem);
		mb.add(menu);
		mb.add(tool);
		mb.add(help);
		setJMenuBar(mb);
		csvItem.setEnabled(false);
		psdItem.setEnabled(false);
		open2Item.setEnabled(false);
		saveItem.setEnabled(false);
		saveAsItem.setEnabled(false);
		convertItem.setEnabled(false);
	}

	private boolean fileNameLegal(String fileName)
	{
		boolean legal = true;
		if (fileName.indexOf("\\") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf("/") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf(":") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf("*") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf("?") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf("\"") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf("<") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf(">") != -1)
		{
			legal = false;
		}
		if (fileName.indexOf("|") != -1)
		{
			legal = false;
		}
		return legal;
	}

	private void saveFailMsg()
	{
		JOptionPane.showMessageDialog(getContentPane(), "Save failed", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void openFailMsg()
	{
		JOptionPane.showMessageDialog(getContentPane(), "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void saveOkMsg(File dest)
	{
		JOptionPane.showMessageDialog(getContentPane(), dest.getName() +
			"\nSaved in:\n" + dest.getParent(), "File Successfully Saved", JOptionPane.INFORMATION_MESSAGE);
	}

	private void illNameMsg()
	{
		JOptionPane
			.showMessageDialog(
				getContentPane(), 	"File name cannot contain the following characters:\n\\ / : * ? \" < > |", 	"Error", JOptionPane.ERROR_MESSAGE);
	}

	private void setIcon()
	{
		URL imageURL = this.getClass().getResource("data/icon.png");
		if (imageURL != null)
		{
			ImageIcon icon = new ImageIcon(imageURL);
			setIconImage(icon.getImage());
		}
	}

	private ImageIcon getIcon()
	{
		ImageIcon icon = null;
		URL imageURL = this.getClass().getResource("data/icon.png");
		if (imageURL != null)
		{
			icon = new ImageIcon(imageURL);
		}
		return icon;
	}

	private void openFile()
	{
		int returnVal = chooser.showOpenDialog(getContentPane());
		saveSet();
		if (returnVal == JFileChooser.APPROVE_OPTION &&
			filter.accept(chooser.getSelectedFile()))
		{
			if (chooser.getSelectedFile().isFile() &&
				of .readXPS(chooser.getSelectedFile()))
			{
				currentFile = chooser.getSelectedFile();
				setTitle("PES Editor 6 - " + currentFile.getName());
				Squads.fixAll(of );
				flagPanel.refresh();
				imagePanel.refresh();
				tranPanel.refresh();
				wenShop.wenPanel.refresh();
				wenShop.shopPanel.status.setText("");
				stadPan.refresh();
				teamPan.refresh();
				leaguePan.refresh();
				tabbedPane.setVisible(true);
				importPanel.refresh();
				csvItem.setEnabled(true);
				psdItem.setEnabled(true);
				open2Item.setEnabled(true);
				saveItem.setEnabled(true);
				saveAsItem.setEnabled(true);

				if (of2.fileName != null)
				{
					convertItem.setEnabled(true);
				}
				else
				{
					convertItem.setEnabled(false);
				}

			}
			else
			{
				csvItem.setEnabled(false);
				psdItem.setEnabled(false);
				open2Item.setEnabled(false);
				saveItem.setEnabled(false);
				saveAsItem.setEnabled(false);
				tabbedPane.setVisible(false);

				convertItem.setEnabled(false);
				setTitle("PES Editor 6");
				openFailMsg();
			}
		}
	}

	private boolean saveSet()
	{
		boolean done = true;
		if (chooser.getCurrentDirectory() != null)
		{
			try
			{
				if (settingsFile.exists())
				{
					settingsFile.delete();
				}
				FileOutputStream out = new FileOutputStream(settingsFile);
				ObjectOutputStream s = new ObjectOutputStream(out);
				s.writeObject(chooser.getCurrentDirectory());
				s.flush();
				s.close();
				out.close();
			}
			catch (IOException e)
			{
				done = false;
			}
		}
		return done;
	}

	private File loadSet()
	{
		File dir = null;
		if (settingsFile.exists())
		{
			try
			{
				FileInputStream in = new FileInputStream(settingsFile);
				ObjectInputStream s = new ObjectInputStream(in );
				dir = (File) s.readObject();

				s.close(); in .close();
				if (dir != null && !dir.exists())
				{
					dir = null;
				}
			}
			catch (Exception e)
			{}
		}
		else
		{
			about();
		}
		return dir;
	}

	private void about()
	{
		JOptionPane.showMessageDialog(
				getContentPane(), 	"PES Editor 6.0.6" +
				"\nversion 2 (rebuilt by lazanet 2017)" +
				"\n\nCopyright (c) 2008-9 Compulsion" +
				"\npes_compulsion@yahoo.co.uk" +
				"\nwww.purplehaze.eclipse.co.uk" +
				"\nuk.geocities.com/pes_compulsion" +
				"\n\nThis program is free software: you can redistribute it and/or modify" +
				"\nit under the terms of the GNU General Public License as published by" +
				"\nthe Free Software Foundation, either version 3 of the License, or" +
				"\n(at your option) any later version." +
				"\n\nThis program is distributed in the hope that it will be useful," +
				"\nbut WITHOUT ANY WARRANTY; without even the implied warranty of" +
				"\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" +
				"\nGNU General Public License for more details." +
				"\n\nYou should have received a copy of the GNU General Public License" +
				"\nalong with this program.  If not, see www.gnu.org/licenses." +
				"\n\nThanks to:" +
				"\nAbhishek, Arsenal666, Big Boss, djsaunders, dragonskin, Flipper, gothi," +
				"\nJayz123, JeffT, PLF, SFCMike, TheBoss, timo the owl, Tricky", 	"About PES Editor 6", JOptionPane.PLAIN_MESSAGE, 	getIcon());
	}

	public static void main(String[] args) throws IOException
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				PESUtils.systemUI();
				System.setProperty("swing.metalTheme", "steel");
				new Editor();
			}
		});
	}
}
