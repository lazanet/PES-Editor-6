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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Emblems
{
	static final byte total = 50;

	static final int startAdr = 911308;

	static final int size = 5184;

	static final int paletteSize = 128;

	static final int raster = 4096;

	static final byte totalB = 100;

	static final int startAdrB = 1167500;

	static final int sizeB = 2176;

	static final int paletteSizeB = 16;

	static final int rasterB = 2048;

	/*
	 * static boolean flag128Here(OptionFile of, int slot) { boolean used =
	 * false; if (of.data[startAdr + (slot size)] == 1) { used = true; } return
	 * used; }
	 * 
	 * static boolean flag16Here(OptionFile of, int slot) { boolean used =
	 * false; if (of.data[(startAdr + (49 size)) - ((slot / 2) size) + ((slot %
	 * 2) sizeB)] == 1) { used = true; } return used; }
	 */

	static Image get128(OptionFile of , int slot, boolean opaque, boolean small)
	{
		byte[] red = new byte[paletteSize];
		byte[] green = new byte[paletteSize];
		byte[] blue = new byte[paletteSize];
		byte[] alpha = new byte[paletteSize];
		byte[] pixel = new byte[raster];
		int a;
		if (slot >= 0 && slot < total)
		{
			for (int c = 0; c < paletteSize; c++)
			{
				a = startAdr + 64 + (slot * size) + (c * 4);
				red[c] = of .data[a];
				green[c] = of .data[a + 1];
				blue[c] = of .data[a + 2];
				alpha[c] = of .data[a + 3];
			}
			a = startAdr + 1088 + (slot * size);
			System.arraycopy( of .data, a, pixel, 0, raster);
			if (opaque)
			{
				for (int i = 0; i < paletteSize; i++)
				{
					alpha[i] = -1;
				}
			}
		}
		IndexColorModel colorModel = new IndexColorModel(8, paletteSize, red,
			green, blue, alpha);
		DataBufferByte dbuf = new DataBufferByte(pixel, raster, 0);
		MultiPixelPackedSampleModel sampleModel = new MultiPixelPackedSampleModel(
			DataBuffer.TYPE_BYTE, 64, 64, 8);
		WritableRaster rasters = Raster.createWritableRaster(sampleModel, dbuf,
			null);
		BufferedImage bi = new BufferedImage(colorModel, rasters, false, null);
		Image image;
		if (small)
		{
			image = bi.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		}
		else
		{
			image = bi;
		}
		return image;
	}

	static Image get16(OptionFile of , int slot, boolean opaque, boolean small)
	{
		byte[] red = new byte[paletteSizeB];
		byte[] green = new byte[paletteSizeB];
		byte[] blue = new byte[paletteSizeB];
		byte[] alpha = new byte[paletteSizeB];
		byte[] pixel = new byte[rasterB];
		int a;
		int startPos = (startAdr + ((total - 1) * size)) - ((slot / 2) * size) +
			((slot % 2) * sizeB);
		// System.out.println(startPos);
		if (slot >= 0 && slot < totalB)
		{
			for (int c = 0; c < paletteSizeB; c++)
			{
				a = startPos + 64 + (c * 4);
				red[c] = of .data[a];
				green[c] = of .data[a + 1];
				blue[c] = of .data[a + 2];
				alpha[c] = of .data[a + 3];
			}
			a = startPos + 128;
			System.arraycopy( of .data, a, pixel, 0, rasterB);
			if (opaque)
			{
				for (int i = 0; i < paletteSizeB; i++)
				{
					alpha[i] = -1;
				}
			}
		}
		IndexColorModel colorModel = new IndexColorModel(4, paletteSizeB, red,
			green, blue, alpha);
		DataBufferByte dbuf = new DataBufferByte(pixel, rasterB, 0);
		MultiPixelPackedSampleModel sampleModel = new MultiPixelPackedSampleModel(
			DataBuffer.TYPE_BYTE, 64, 64, 4);
		WritableRaster rasters = Raster.createWritableRaster(sampleModel, dbuf,
			null);
		BufferedImage bi = new BufferedImage(colorModel, rasters, false, null);
		Image image;
		if (small)
		{
			image = bi.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		}
		else
		{
			image = bi;
		}
		return image;
	}

	static boolean set128(OptionFile of , int slot, BufferedImage image)
	{
		boolean ok = false;
		try
		{
			byte[] red = new byte[256];
			byte[] green = new byte[256];
			byte[] blue = new byte[256];
			byte[] alpha = new byte[256];
			int[] pix = new int[raster];
			int a;
			Raster rast = image.getData();
			ColorModel colorMod = image.getColorModel();

			if (colorMod instanceof IndexColorModel)
			{
				IndexColorModel index = (IndexColorModel) colorMod;
				if (image.getWidth() == 64 && image.getHeight() == 64)
				{
					rast.getPixels(0, 0, 64, 64, pix);
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
						for (int c = 1; c < 128; c++)
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

							for (int p = 0; p < raster; p++)
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
						a = startAdr + 64 + (slot * size) + (c * 4); of .data[a] = red[c]; of .data[a + 1] = green[c]; of .data[a + 2] = blue[c]; of .data[a + 3] = alpha[c];
						if (alpha[c] != -1)
						{}
					}

					for (int p = 0; p < raster; p++)
					{
						a = startAdr + 1088 + (slot * size) + p; of .data[a] = of .toByte(pix[p]);
					} of .data[startAdr + (slot * size)] = 1;

					if (slot == count128( of ))
					{
						setCount128( of , (byte)(count128( of ) + 1));

						boolean done = false;
						for (int i = 0; !done && i < total; i++)
						{
							a = startAdr - 98 + i;
							if ( of .data[a] == 93)
							{ of .data[a] = of .toByte(slot);
								int id = Clubs.firstFlag + i;
								System.arraycopy( of .getBytes(id), 0, of .data,
									startAdr + (slot * size) + 4, 2);
								done = true;
							}
						}
					}
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

	static boolean set16(OptionFile of , int slot, BufferedImage image)
	{
		boolean ok = false;
		try
		{
			byte[] red = new byte[256];
			byte[] green = new byte[256];
			byte[] blue = new byte[256];
			byte[] alpha = new byte[256];
			int[] pix = new int[raster];
			int a;
			int startPos = (startAdr + ((total - 1) * size)) -
				((slot / 2) * size) + ((slot % 2) * sizeB);
			Raster rast = image.getData();
			ColorModel colorMod = image.getColorModel();

			if (colorMod instanceof IndexColorModel)
			{
				IndexColorModel index = (IndexColorModel) colorMod;
				if (image.getWidth() == 64 && image.getHeight() == 64)
				{
					rast.getPixels(0, 0, 64, 64, pix);
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

							for (int p = 0; p < raster; p++)
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

					for (int c = 0; c < paletteSizeB; c++)
					{
						a = startPos + 64 + (c * 4); of .data[a] = red[c]; of .data[a + 1] = green[c]; of .data[a + 2] = blue[c]; of .data[a + 3] = alpha[c];
						if (alpha[c] != -1)
						{}
					}

					for (int p = 0; p < raster; p = p + 2)
					{
						a = startPos + 128 + (p / 2); of .data[a] = of .toByte((pix[p] << 4) | (pix[p + 1]));
					} of .data[startPos] = 1;

					if (slot == count16( of ))
					{
						setCount16( of , (byte)(count16( of ) + 1));

						boolean done = false;
						for (int i = 0; !done && i < totalB; i++)
						{
							a = startAdr - 108 + i;
							if ( of .data[a] == -103)
							{ of .data[a] = of .toByte(total + slot);
								int id = Clubs.firstFlag + total + i;
								System.arraycopy( of .getBytes(id), 0, of .data,
									startPos + 4, 2);
								done = true;
							}
						}
					}
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

	/*
	 * static void importFlag(OptionFile ofS, int slotS, OptionFile ofD, int
	 * slotD) { int aS = startAdr + (slotS size); int aD = startAdr + (slotD
	 * size); System.arraycopy(ofS.data, aS, ofD.data, aD, size); }
	 */

	static byte count128(OptionFile of )
	{
		return of.data[startAdr - 160];
	}

	static byte count16(OptionFile of )
	{
		return of.data[startAdr - 161];
	}

	static void setCount16(OptionFile of , byte c)
	{ of .data[startAdr - 161] = c;
	}

	static void setCount128(OptionFile of , byte c)
	{ of .data[startAdr - 160] = c;
	}

	static byte getFree16(OptionFile of )
	{
		return (byte)(totalB - (count128( of ) * 2) - count16( of ));
	}

	static byte getFree128(OptionFile of )
	{
		return (byte)((totalB - (count128( of ) * 2) - count16( of )) / 2);
	}

	static Image getImage(OptionFile of , int e)
	{
		Image image = null;
		int a = startAdr - 158 + e;
		if (e < total)
		{
			image = get128( of , of .data[a], false, false);
		}
		else
		{
			int slot = of .toInt( of .data[a]) - total;
			image = get16( of , slot, false, false);
		}
		return image;
	}

	static int getLoc(OptionFile of , int e)
	{
		int a = startAdr - 158 + e;
		return of.toInt( of .data[a]);
	}

	static void deleteImage(OptionFile of , int e)
	{
		int a = startAdr - 158 + e;
		if (e < total)
		{
			delete128( of , of .data[a]);
		}
		else
		{
			int slot = of .toInt( of .data[a]) - total;
			delete16( of , slot);
		}
	}

	static byte[] getIndex(OptionFile of , int slot)
	{
		byte[] index = new byte[2];
		if (slot < total)
		{
			System.arraycopy( of .data, startAdr + (slot * size) + 4, index, 0,
				2);
		}
		else
		{
			slot = slot - total;
			int startPos = (startAdr + ((total - 1) * size)) -
				((slot / 2) * size) + ((slot % 2) * sizeB);
			System.arraycopy( of .data, startPos + 4, index, 0, 2);
		}
		return index;
	}

	static void delete16(OptionFile of , int slot)
	{
		byte[] index = getIndex( of , slot + total);

		int si = getInt( of , index) - (Clubs.firstFlag + total); of .data[startAdr - 68 + si] = 93;
		Clubs.unAssEmblem( of , si + total);
		/*
		 * for (int i = 0; i < 138; i++) { byte[] tb = new byte[2]; int a =
		 * 797484 + 6236 + (i 140); int sa = 797492 + 6236 + (i 140); int d =
		 * 111 + i; System.arraycopy(of.data, a, tb, 0 , 2); if (tb[0] ==
		 * index[0] && tb[1] == index[1]) { System.arraycopy(of.getBytes(d), 0,
		 * of.data, a, 2); System.arraycopy(of.getBytes(d), 0, of.data, a + 4,
		 * 2); of.data[sa] = 0; of.data[sa + 1] = 0; } }
		 */
		int source = (startAdr + ((total - 1) * size)) -
			(((count16( of ) - 1) / 2) * size) +
			(((count16( of ) - 1) % 2) * sizeB);
		if (slot != count16( of ) - 1)
		{

			int dest = (startAdr + ((total - 1) * size)) -
				(((slot) / 2) * size) + ((slot % 2) * sizeB);
			System.arraycopy( of .data, source, of .data, dest, sizeB);
			si = getInt( of , getIndex( of , slot + total)) -
				(Clubs.firstFlag + total); of .data[startAdr - 68 + si] = of .toByte(slot + total);
		}

		for (int i = source; i < source + sizeB; i++)
		{ of .data[i] = 0;
		}
		setCount16( of , of .toByte(count16( of ) - 1));
	}

	static void delete128(OptionFile of , int slot)
	{
		byte[] index = getIndex( of , slot);
		int si = getInt( of , index) - Clubs.firstFlag; of .data[startAdr - 98 + si] = 93;
		Clubs.unAssEmblem( of , si);
		/*
		 * for (int i = 0; i < 138; i++) { byte[] tb = new byte[2]; int a =
		 * 797484 + 6236 + (i 140); int sa = 797492 + 6236 + (i 140); int d =
		 * 111 + i; System.arraycopy(of.data, a, tb, 0 , 2); if (tb[0] ==
		 * index[0] && tb[1] == index[1]) { System.arraycopy(of.getBytes(d), 0,
		 * of.data, a, 2); System.arraycopy(of.getBytes(d), 0, of.data, a + 4,
		 * 2); of.data[sa] = 0; of.data[sa + 1] = 0; } }
		 */
		int source = startAdr + ((count128( of ) - 1) * size);
		if (slot != count128( of ) - 1)
		{
			int dest = startAdr + (slot * size);
			System.arraycopy( of .data, source, of .data, dest, size);
			si = getInt( of , getIndex( of , slot)) - Clubs.firstFlag; of .data[startAdr - 98 + si] = of .toByte(slot);
		}

		for (int i = source; i < source + size; i++)
		{ of .data[i] = 0;
		}
		setCount128( of , of .toByte(count128( of ) - 1));
	}

	static int getInt(OptionFile of , byte[] b)
	{
		int i = 0;
		if (b.length == 2)
		{
			i = ( of .toInt(b[1]) << 8) | ( of .toInt(b[0]) & 0xFF);
		}
		return i;
	}

	static void import16(OptionFile of1, int slot1, OptionFile of2, int slot2)
	{
		int startPos1 = (startAdr + ((total - 1) * size)) -
			((slot1 / 2) * size) + ((slot1 % 2) * sizeB);
		int startPos2 = (startAdr + ((total - 1) * size)) -
			((slot2 / 2) * size) + ((slot2 % 2) * sizeB);
		System.arraycopy(of2.data, startPos2 + 64, of1.data, startPos1 + 64,
			sizeB - 64);

		if (slot1 == count16(of1))
		{
			of1.data[startPos1] = 1;
			setCount16(of1, (byte)(count16(of1) + 1));

			boolean done = false;
			for (int i = 0; !done && i < totalB; i++)
			{
				int a = startAdr - 108 + i;
				if (of1.data[a] == -103)
				{
					of1.data[a] = of1.toByte(total + slot1);
					int id = (Clubs.firstFlag + total) + i;
					System.arraycopy(of1.getBytes(id), 0, of1.data,
						startPos1 + 4, 2);
					done = true;
				}
			}
		}
	}

	static void import128(OptionFile of1, int slot1, OptionFile of2, int slot2)
	{
		int startPos1 = startAdr + (slot1 * size);
		int startPos2 = startAdr + (slot2 * size);
		System.arraycopy(of2.data, startPos2 + 64, of1.data, startPos1 + 64,
			size - 64);

		if (slot1 == count128(of1))
		{
			of1.data[startPos1] = 1;
			setCount128(of1, (byte)(count128(of1) + 1));
			boolean done = false;
			for (int i = 0; !done && i < total; i++)
			{
				int a = startAdr - 98 + i;
				if (of1.data[a] == 93)
				{
					of1.data[a] = of1.toByte(slot1);
					int id = Clubs.firstFlag + i;
					System.arraycopy(of1.getBytes(id), 0, of1.data,
						startPos1 + 4, 2);
					done = true;
				}
			}
		}
	}

}
