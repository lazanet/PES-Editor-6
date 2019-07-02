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
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LogoPanel extends JPanel {

	private JButton[] flagButton;

	private boolean trans = true;

	private OptionFile of;

	private JFileChooser chooser = new JFileChooser();

	private JFileChooser chooserPNG = new JFileChooser();

	private GIFPNGFilter filter = new GIFPNGFilter();

	private PNGFilter pngFilter = new PNGFilter();

	private LogoImportDialog imageImpDia;

	public LogoPanel(OptionFile opt, LogoImportDialog iid) {
		super();
		of = opt;
		imageImpDia = iid;
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Import Logo");
		chooserPNG.addChoosableFileFilter(pngFilter);
		chooserPNG.setAcceptAllFileFilterUsed(false);
		chooserPNG.setDialogTitle("Export Logo");
		flagButton = new JButton[Logos.total];
		JPanel flagPanel = new JPanel(new GridLayout(8, 10));

		PESUtils.javaUI();
		
		for (int l = 0; l < Logos.total; l++) {
			flagButton[l] = new JButton();
			flagButton[l].setBackground(new Color(204, 204, 204));
			flagButton[l].setMargin(new Insets(0, 0, 0, 0));
			flagButton[l].setActionCommand((new Integer(l)).toString());
			flagButton[l].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent b) {
					int slot = (new Integer(((JButton) b.getSource())
							.getActionCommand())).intValue();
					ImageIcon icon = null;
					icon = new ImageIcon(Logos.get(of, slot, !trans));
					Object[] options;
					Object[] options1 = { "Import PNG / GIF", "Export as PNG",
							"Import (OF2)", "Cancel" };
					Object[] options2 = { "Import PNG / GIF", "Export as PNG",
							"Cancel" };

					if (imageImpDia.of2Open) {
						options = options1;
					} else {
						options = options2;
					}
					int n = JOptionPane.showOptionDialog(null, "Options:",
							"Logo", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, icon, options,
							options[0]);
					if (n == 0) {
						int returnVal = chooser.showOpenDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File source = chooser.getSelectedFile();
							try {
								BufferedImage image;
								image = ImageIO.read(source);
								int check = checkImage(image);
								if (check != -1 && check < 16) {
									Logos.set(of, slot, image);
									flagButton[slot].setIcon(new ImageIcon(
											Logos.get(of, slot, !trans)));
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,
										"Could not access file", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					if (n == 1 && Logos.isUsed(of, slot)) {
						savePNG(slot);
					}

					if (imageImpDia.of2Open && n == 2) {
						imageImpDia.show(slot, "Import Logo");
						flagButton[slot].setIcon(new ImageIcon(Logos.get(of,
								slot, !trans)));
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
		JPanel pan1 = new JPanel(new BorderLayout());
		pan1.add(flagPanel, BorderLayout.CENTER);
		pan1.add(transButton, BorderLayout.SOUTH);
		add(pan1);
		refresh();
	}

	private void savePNG(int slot) {
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

			if (writeFile(dest, slot)) {
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

	private boolean writeFile(File dest, int slot) {
		boolean ok = false;
		BufferedImage image = Logos.get(of, slot, false);
		try {
			ImageIO.write(image, "png", dest);
			ok = true;
		} catch (IOException e) {
			ok = false;
		}
		return ok;
	}

	public void refresh() {
		for (int f = 0; f < Logos.total; f++) {
			flagButton[f].setIcon(new ImageIcon(Logos.get(of, f, !trans)));
		}
	}

	private int checkImage(BufferedImage image) {
		int max = -1;
		if (image.getWidth() == 32 && image.getHeight() == 32) {
			ColorModel colorMod = image.getColorModel();
			if (colorMod instanceof IndexColorModel) {
				int[] pix = new int[1024];
				Raster rast = image.getData();
				rast.getPixels(0, 0, 32, 32, pix);
				for (int i = 0; i < 1024; i++) {
					if (pix[i] > max) {
						max = pix[i];
					}
				}
				if (max > 15) {
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

	private void colourMsg() {
		JOptionPane.showMessageDialog(null, "Too many colours, maximum is 16",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	private void sizeMsg() {
		JOptionPane.showMessageDialog(null, "Size must be 32x32 pixels",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

}
