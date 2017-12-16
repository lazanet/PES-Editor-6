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
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class BackChooserDialog extends JDialog implements ActionListener
{
	JButton[] flagButton = new JButton[12];

	byte slot;

	Raster[] raster = new Raster[12];

	public BackChooserDialog(Frame owner)
	{
		super(owner, "Choose Background", true);
		JPanel flagPanel = new JPanel(new GridLayout(3, 4));
		for (int l = 0; l < 12; l++)
		{
			flagButton[l] = new JButton();
			URL backURL = Editor.class.getResource("data/backflag" +
				String.valueOf(l) + ".png");
			// System.out.println(backURL);
			BufferedImage bi = null;
			if (backURL != null)
			{
				try
				{
					bi = ImageIO.read(backURL);
					raster[l] = bi.getData();
				}
				catch (IOException e)
				{}
			}
			flagButton[l].setMargin(new Insets(0, 0, 0, 0));
			flagButton[l].setActionCommand((new Integer(l)).toString());
			flagButton[l].addActionListener(this);
			flagPanel.add(flagButton[l]);
		}

		byte[] red = {
			0,
			-1
		};
		byte[] blue = {
			0,
			-1
		};
		byte[] green = {
			0,
			-1
		};
		refresh(null, red, green, blue);
		CancelButton cancelButton = new CancelButton(this);
		getContentPane().add(cancelButton, BorderLayout.SOUTH);
		getContentPane().add(flagPanel, BorderLayout.CENTER);
		slot = 99;
		pack();
		setResizable(false);
	}

	public byte getBack(Image image, byte[] red, byte[] green, byte[] blue)
	{
		slot = 99;
		refresh(image, red, green, blue);
		setVisible(true);
		return slot;
	}

	public void actionPerformed(ActionEvent b)
	{
		slot = (new Byte(((JButton) b.getSource()).getActionCommand()))
			.byteValue();
		setVisible(false);
	}

	private void refresh(Image image, byte[] red, byte[] green, byte[] blue)
	{
		for (byte i = 0; i < 12; i++)
		{
			flagButton[i].setIcon(getFlagBG(image, i, red, green, blue));
		}
	}

	public ImageIcon getFlagBG(Image image, byte bg, byte[] red, byte[] green, byte[] blue)
	{
		IndexColorModel colorModel = new IndexColorModel(1, 2, red, green, blue);
		BufferedImage bi = new BufferedImage(colorModel, (WritableRaster) raster[bg], false, null);

		BufferedImage bi2 = new BufferedImage(85, 64, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) bi2.getGraphics();
		g2.drawImage(bi, 0, 0, null);
		if (image != null)
		{
			g2.drawImage(image, 11, 0, null);
		}
		ImageIcon icon = new ImageIcon(bi2);
		return icon;
	}

}
