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

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Logos
{
	static final byte total = 80;

	static final int startAdr = 862492;

	static final int size = 608;

	static final int paletteSize = 16;

	static final int raster = 512;

	static boolean isUsed(OptionFile of , int slot)
	{
		boolean used = false;
		if ( of .data[startAdr + (slot * size)] == 1)
		{
			used = true;
		}
		return used;
	}

	static BufferedImage get(OptionFile of , int slot, boolean opaque)
	{
		byte[] red = new byte[paletteSize];
		byte[] green = new byte[paletteSize];
		byte[] blue = new byte[paletteSize];
		byte[] alpha = new byte[paletteSize];
		byte[] pixel = new byte[raster];
		int a;
		if (slot >= 0 && slot < total && isUsed( of , slot))
		{
			for (int c = 0; c < paletteSize; c++)
			{
				a = startAdr + 32 + (slot * size) + (c * 4);
				red[c] = of .data[a];
				green[c] = of .data[a + 1];
				blue[c] = of .data[a + 2];
				alpha[c] = of .data[a + 3];
			}
			a = startAdr + 96 + (slot * size);
			System.arraycopy( of .data, a, pixel, 0, raster);
			if (opaque)
			{
				for (int i = 0; i < paletteSize; i++)
				{
					alpha[i] = -1;
				}
			}
		}
		IndexColorModel colorModel = new IndexColorModel(4, paletteSize, red,
			green, blue, alpha);
		DataBufferByte dbuf = new DataBufferByte(pixel, raster, 0);
		MultiPixelPackedSampleModel sampleModel = new MultiPixelPackedSampleModel(
			DataBuffer.TYPE_BYTE, 32, 32, 4);
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dbuf,
			null);
		BufferedImage image = new BufferedImage(colorModel, raster, false, null);
		return image;
	}

	static boolean set(OptionFile of , int slot, BufferedImage image)
	{
		boolean ok = false;
		try
		{
			byte[] red = new byte[256];
			byte[] green = new byte[256];
			byte[] blue = new byte[256];
			byte[] alpha = new byte[256];
			int[] pix = new int[1024];
			int a;
			Raster rast = image.getData();
			ColorModel colorMod = image.getColorModel();

			if (colorMod instanceof IndexColorModel)
			{
				IndexColorModel index = (IndexColorModel) colorMod;
				if (image.getWidth() == 32 && image.getHeight() == 32)
				{
					rast.getPixels(0, 0, 32, 32, pix);
					index.getReds(red);
					index.getGreens(green);
					index.getBlues(blue);
					index.getAlphas(alpha);

					if (alpha[0] != 0)
					{
						int swap = 0;
						byte tr;
						byte tg;
						byte tb;
						byte ta;
						for (int c = 1; c < 16; c++)
						{
							if (swap == 0 && alpha[c] == 0)
							{
								swap = c;
							}
						}
						if (swap != 0)
						{
							tr = red[0];
							tg = green[0];
							tb = blue[0];
							ta = alpha[0];
							// System.out.println(red[3] + ", " + green[3] + ",
							// " + blue[3]);
							red[0] = red[swap];
							green[0] = green[swap];
							blue[0] = blue[swap];
							alpha[0] = 0;
							red[swap] = tr;
							green[swap] = tg;
							blue[swap] = tb;
							alpha[swap] = ta;

							for (int p = 0; p < 1024; p++)
							{
								if (pix[p] == 0)
								{
									pix[p] = swap;
								}
								else if (pix[p] == swap)
								{
									pix[p] = 0;
								}
							}
							// System.out.println(swap);
						}
					}

					for (int c = 0; c < paletteSize; c++)
					{
						a = startAdr + 32 + (slot * size) + (c * 4); of .data[a] = red[c]; of .data[a + 1] = green[c]; of .data[a + 2] = blue[c]; of .data[a + 3] = alpha[c];
					}

					for (int p = 0; p < 1024; p = p + 2)
					{
						a = startAdr + 96 + (slot * size) + (p / 2); of .data[a] = of .toByte((pix[p] << 4) | (pix[p + 1]));
					} of .data[startAdr + (slot * size)] = 1;
					ok = true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ok;
	}

	static void importLogo(OptionFile ofS, int slotS, OptionFile ofD, int slotD)
	{
		int aS = startAdr + (slotS * size);
		int aD = startAdr + (slotD * size);
		System.arraycopy(ofS.data, aS, ofD.data, aD, size);
	}

	static void delete(OptionFile of , byte slot)
	{
		int a = startAdr + (slot * size);
		for (int i = 0; i < size; i++)
		{ of .data[a + i] = 0;
		}
	}

}
