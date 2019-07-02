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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EmblemPanel extends JPanel implements MouseListener {

	private JButton[] flagButton;

	private boolean trans = true;

	private OptionFile of;

	private JFileChooser chooser = new JFileChooser();

	private JFileChooser chooserPNG = new JFileChooser();

	private GIFPNGFilter filter128 = new GIFPNGFilter();

	private PNGFilter pngFilter = new PNGFilter();

	private EmblemImportDialog flagImpDia;

	private TeamPanel teamPanel;

	private JPanel flagPanel;

	private JButton addButton;

	private JButton add2Button;

	private JLabel free16Label;

	private JLabel free128Label;

	private JLabel largeFlag;

	public EmblemPanel(OptionFile opt, EmblemImportDialog fid, TeamPanel tp) {
		super();
		of = opt;
		flagImpDia = fid;
		teamPanel = tp;
		chooser.addChoosableFileFilter(filter128);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Import Emblem");
		chooserPNG.addChoosableFileFilter(pngFilter);
		chooserPNG.setAcceptAllFileFilterUsed(false);
		chooserPNG.setDialogTitle("Export Emblem");
		flagButton = new JButton[100];
		flagPanel = new JPanel(new GridLayout(10, 10));

		PESUtils.javaUI();
		
		for (int l = 0; l < 100; l++) {
			flagButton[l] = new JButton();
			flagButton[l].setBackground(new Color(204, 204, 204));
			flagButton[l].setMargin(new Insets(0, 0, 0, 0));
			flagButton[l].setActionCommand((new Integer(l)).toString());
			flagButton[l].addMouseListener(this);
			flagButton[l].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent b) {
					int slot = (new Integer(((JButton) b.getSource())
							.getActionCommand())).intValue();
					ImageIcon icon = null;
					boolean is128 = false;
					if (slot >= Emblems.count16(of)) {
						is128 = true;
						slot = 99 - slot;
						icon = new ImageIcon(Emblems.get128(of, slot, !trans,
								false));
					} else {
						icon = new ImageIcon(Emblems.get16(of, slot, !trans,
								false));
					}
					Object[] options;
					Object[] options1 = { "Delete", "Import PNG / GIF",
							"Export as PNG", "Import (OF2)", "Cancel" };
					Object[] options2 = { "Delete", "Import PNG / GIF",
							"Export as PNG", "Cancel" };
					if (flagImpDia.of2Open) {
						options = options1;
					} else {
						options = options2;
					}
					int n = JOptionPane.showOptionDialog(null, "Options:",
							"Emblem", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, icon, options,
							options[0]);

					if (n == 0) {
						if (is128) {
							Emblems.delete128(of, slot);
						} else {
							Emblems.delete16(of, slot);
						}
						teamPanel.refresh();
						refresh();
					}
					if (n == 1) {
						int returnVal = chooser.showOpenDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File source = chooser.getSelectedFile();
							try {
								BufferedImage image;
								image = ImageIO.read(source);

								int check = checkImage(image);
								if (check != -1) {
									if (is128) {
										if (check < 128) {
											if (check > 15) {
												Emblems.set128(of, slot, image);
											} else {
												wasteMsg();
											}
										}
									} else {
										if (check < 16) {
											Emblems.set16(of, slot, image);
										} else {
											col16Msg();
										}
									}
									teamPanel.refresh();
									refresh();
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,
										"Could not open file", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					if (n == 2) {
						savePNG(is128, slot);
					}
					if (flagImpDia.of2Open && n == 3) {
						int replacement = -1;
						if (is128) {
							replacement = flagImpDia
									.getFlag("Import Emblem", 2);
							if (replacement != -1) {
								Emblems.import128(of, slot, flagImpDia.of,
										replacement);
							}
						} else {
							replacement = flagImpDia
									.getFlag("Import Emblem", 1);
							if (replacement != -1) {
								replacement = replacement - 50;
								Emblems.import16(of, slot, flagImpDia.of,
										replacement);
							}
						}

						teamPanel.refresh();
						refresh();
					}
				}
			});
			flagPanel.add(flagButton[l]);
		}
		
		PESUtils.systemUI();
		
		JButton transButton = new JButton("Transparency");
		transButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent t) {
				trans = !trans;
				refresh();
			}
		});

		free16Label = new JLabel();
		free128Label = new JLabel();
		addButton = new JButton("Add Emblem");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent t) {
				if (Emblems.getFree128(of) > 0 || (Emblems.getFree16(of) > 0)) {

					int returnVal = chooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File source = chooser.getSelectedFile();
						try {
							BufferedImage image;
							image = ImageIO.read(source);

							int check = checkImage(image);
							if (check != -1) {
								if (check < 16) {
									Emblems.set16(of, Emblems.count16(of), image);
								} else {
									if (Emblems.getFree128(of) == 0) {
										noSpaceMsg();
									} else {
										Emblems.set128(of, Emblems.count128(of), image);
									}
								}
								teamPanel.refresh();
								refresh();
							}
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,
									"Could not open file", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}

			}
		});

		add2Button = new JButton("Add Emblem (OF2)");
		add2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent t) {
				int emblem = -1;
				if (Emblems.getFree128(of) > 0) {
					emblem = flagImpDia.getFlag("Import Emblem", 0);
				} else if (Emblems.getFree16(of) > 0) {
					emblem = flagImpDia.getFlag("Import Emblem", 1);
				}
				if (emblem != -1) {
					if (emblem > 49) {
						emblem = emblem - 50;
						Emblems.import16(of, Emblems.count16(of), flagImpDia.of,
								emblem);
					} else {
						Emblems.import128(of, Emblems.count128(of), flagImpDia.of,
								emblem);
					}
					teamPanel.refresh();
					refresh();
				}
			}
		});

		JPanel freePanel = new JPanel();
		JPanel freePan = new JPanel(new GridLayout(0, 1));
		freePan.add(free16Label);
		freePan.add(free128Label);
		freePan.add(addButton);
		freePan.add(add2Button);
		freePanel.add(freePan);

		largeFlag = new JLabel();
		largeFlag.setIcon(new ImageIcon(Emblems.get16(of, -1, false, false)));

		JPanel pan1 = new JPanel(new BorderLayout());
		pan1.setBorder(BorderFactory.createLineBorder(Color.gray));
		pan1.add(new JLabel("16 Colour Format"), BorderLayout.NORTH);
		pan1.add(flagPanel, BorderLayout.CENTER);
		pan1.add(new JLabel("128 Colour Format", SwingConstants.RIGHT),
				BorderLayout.SOUTH);
		JPanel pan2 = new JPanel(new BorderLayout());
		pan2.add(pan1, BorderLayout.CENTER);
		pan2.add(transButton, BorderLayout.SOUTH);
		add(pan2);
		add(largeFlag);
		add(freePanel);
		refresh();
	}

	private void savePNG(boolean is128, int slot) {
		boolean error = false;
		int returnVal = chooserPNG.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File dest = chooserPNG.getSelectedFile();
			if ((PESUtils.getExtension(dest) == null)
					|| !(PESUtils.getExtension(dest).equals(PESUtils.png))) {
				dest = new File(dest.getParent() + File.separator
						+ dest.getName() + ".png");
			}
			if (dest.exists()) {
				int n = JOptionPane.showConfirmDialog(null, dest.getName()
						+ "\nAlready exists in:\n" + dest.getParent()
						+ "\nAre you sure you want to overwrite this file?",
						"Overwrite:  " + dest.getName(),
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null);
				if (n == 0) {
					boolean deleted = dest.delete();
					if (!deleted) {
						JOptionPane.showMessageDialog(null,
								"Could not access file", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					return;
				}
			}

			if (writeFile(dest, is128, slot)) {
				JOptionPane.showMessageDialog(null, dest.getName()
						+ "\nSaved in:\n" + dest.getParent(),
						"File Successfully Saved",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				error = true;
			}
			if (error) {
				JOptionPane.showMessageDialog(null, "Could not access file",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private boolean writeFile(File dest, boolean is128, int slot) {
		boolean ok = false;
		BufferedImage image;
		if (is128) {
			image = (BufferedImage) Emblems.get128(of, slot, false, false);
		} else {
			image = (BufferedImage) Emblems.get16(of, slot, false, false);
		}
		try {
			ImageIO.write(image, "png", dest);
			ok = true;
		} catch (IOException e) {
			ok = false;
		}
		return ok;
	}

	public void refresh() {
		for (int i = 0; i < Emblems.count16(of); i++) {
			flagButton[i].setIcon(new ImageIcon(Emblems
					.get16(of, i, !trans, true)));
			flagButton[i].setVisible(true);
		}
		for (int i = 0; i < Emblems.count128(of); i++) {
			flagButton[99 - i].setIcon(new ImageIcon(Emblems.get128(of, i,
					!trans, true)));
			flagButton[99 - i].setVisible(true);
		}

		for (int i = Emblems.count16(of); i < 100 - Emblems.count128(of); i++) {
			flagButton[i].setVisible(false);
		}
		free16Label.setText("16-colour, can stock: " + Emblems.getFree16(of));
		free128Label.setText("128-colour, can stock: " + Emblems.getFree128(of));
		if (flagImpDia.of2Open) {
			add2Button.setVisible(true);
		} else {
			add2Button.setVisible(false);
		}
		if (Emblems.getFree16(of) > 0) {
			addButton.setEnabled(true);
			add2Button.setEnabled(true);
		} else {
			addButton.setEnabled(false);
			add2Button.setEnabled(false);
		}
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {
		JButton but = (JButton) e.getSource();
		int slot = new Integer(but.getActionCommand()).intValue();
		if (slot >= Emblems.count16(of)) {
			slot = 99 - slot;
			largeFlag.setIcon(new ImageIcon(Emblems.get128(of, slot, !trans,
					false)));
		} else {
			largeFlag.setIcon(new ImageIcon(Emblems
					.get16(of, slot, !trans, false)));
		}
	}

	public void mouseExited(MouseEvent e) {
		largeFlag.setIcon(new ImageIcon(Emblems.get16(of, -1, false, false)));
	}

	public void mouseClicked(MouseEvent e) {}

	private int checkImage(BufferedImage image) {
		int max = -1;
		if (image.getWidth() == 64 && image.getHeight() == 64) {
			ColorModel colorMod = image.getColorModel();
			if (colorMod instanceof IndexColorModel) {
				int[] pix = new int[Emblems.raster];
				Raster rast = image.getData();
				rast.getPixels(0, 0, 64, 64, pix);
				for (int i = 0; i < Emblems.raster; i++) {
					if (pix[i] > max) {
						max = pix[i];
					}
				}
				if (max > 127) {
					colourMsg();
					max = -1;
				}
			} else {
				notIndexMsg();
			}
		} else {
			sizeMsg();
		}
		return max;
	}

	private void notIndexMsg() {
		JOptionPane.showMessageDialog(null, "PNG files must be INDEXED format",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	private void noSpaceMsg() {
		JOptionPane.showMessageDialog(null,
				"Not enough space for a 128-colour emblem", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private void colourMsg() {
		JOptionPane.showMessageDialog(null, "Too many colours, maximum is 128",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	private void sizeMsg() {
		JOptionPane.showMessageDialog(null, "Size must be 64x64 pixels",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	private void col16Msg() {
		JOptionPane.showMessageDialog(null,
				"Too many colours for a 16-colour slot", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private void wasteMsg() {
		JOptionPane.showMessageDialog(null,
				"A 16 colour image in a 128-colour slot would waste space!",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

}
