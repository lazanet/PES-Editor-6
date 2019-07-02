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

import java.awt.Color;
import java.io.UnsupportedEncodingException;

public class Clubs {
	static final  int total = 140;

	static final  int startAdr = 751472;

	static final int size = 88;
	
	static final int firstFlag = 292;
	
	static final int firstDefEmblem = 115;

	static int getEmblem(OptionFile of, int club) {
		int a = startAdr + 60 + (size * club);
		byte[] tb = new byte[2];
		System.arraycopy(of.data, a, tb, 0, 2);
		return (of.toInt(tb[1]) << 8) | (of.toInt(tb[0]) & 0xFF);
	}

	static void importClub(OptionFile of1, int c1, OptionFile of2, int c2) {
		int a1 = startAdr + (size * c1);
		int a2 = startAdr + (size * c2);
		byte t = of1.data[a1 + 54];
		System.arraycopy(of2.data, a2, of1.data, a1, size);
		of1.data[a1 + 54] = t;
		setNameEdited(of1, c1);
		setEmblemEdited(of1, c1, true);
		setStadiumEdited(of1, c1);
	}

	static void setEmblem(OptionFile of, int club, byte[] index) {
		boolean edited = true;
		if (index == null) {
			index = of.getBytes(club + firstDefEmblem);
			edited = false;
		}
		int a = startAdr + 60 + (size * club);
		System.arraycopy(index, 0, of.data, a, 2);
		System.arraycopy(index, 0, of.data, a + 4, 2);
		setEmblemEdited(of, club, edited);
	}

	static void unAssEmblem(OptionFile of, int emblem) {
		for (int i = 0; i < total; i++) {
			if (emblem == getEmblem(of, i) - firstFlag) {
				setEmblem(of, i, null);
			}
		}
	}

	static String[] getNames(OptionFile of) {
		String[] clubs = new String[total];
		for (int c = 0; c < clubs.length; c++) {
			clubs[c] = getName(of, c);
		}
		return clubs;
	}

	static String getName(OptionFile of, int c) {
		String club = "";
		int len = 0;
		int a = startAdr + (c * size);
		if (of.data[a] != 0) {
			for (int i = 0; i < 49; i++) {
				if (len == 0 && of.data[a + i] == 0) {
					len = i;
				}
			}
			try {
				club = new String(of.data, a, len, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				club = "<" + String.valueOf(c) + ">";
			}
		} else {
			club = "<" + String.valueOf(c) + ">";
		}
		return club;
	}
	
	static String getAbv(OptionFile of, int c) {
		String abv = "";
		int a = startAdr + 49 + (c * size);
		abv = new String(of.data, a, 3);
		return abv;
	}
	
	static void setAbv(OptionFile of, int c, String text) {
		int a = startAdr + 49 + (c * size);
		byte[] tb = new byte[3];
		byte[] sb = text.getBytes();
		System.arraycopy(sb, 0, tb, 0, 3);
		System.arraycopy(tb, 0, of.data, a, 3);
		setNameEdited(of, c);
	}
	static void setName(OptionFile of, int c, String text) {
		int a = startAdr + (c * size);
		byte[] tb = new byte[49];
		byte[] sb;
		try {
			sb = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			sb = new byte[48];
		}
		if (sb.length <= 48) {
			System.arraycopy(sb, 0, tb, 0, sb.length);
		} else {
			System.arraycopy(sb, 0, tb, 0, 48);
		}
		System.arraycopy(tb, 0, of.data, a, 49);
		setNameEdited(of, c);
	}
	
	static int getStadium(OptionFile of, int c) {
		int a = startAdr + 81 + (c * size);
		return of.data[a];
	}
	
	static void setStadium(OptionFile of, int c, int s) {
		int a = startAdr + 81 + (c * size);
		of.data[a] = of.toByte(s);
		setStadiumEdited(of, c);
	}
	
	static byte getBack(OptionFile of, int c) {
		int a = startAdr + 70 + (c * size);
		return of.data[a];
	}
	
	static void setBack(OptionFile of, int c, int b) {
		int a = startAdr + 70 + (c * size);
		of.data[a] = of.toByte(b);
		//int sa = 797507 + 6236 + (t * 140);
		//of.data[sa] = 1;
	}
	
	static byte[] getRed(OptionFile of, int c) {
		int a = Clubs.startAdr + 72 + (c * Clubs.size);
		byte[] red = new byte[2];
		red[0] = of.data[a];
		red[1] = of.data[a + 4];
		return red;
	}
	
	static byte[] getGreen(OptionFile of, int c) {
		int a = Clubs.startAdr + 73 + (c * Clubs.size);
		byte[] red = new byte[2];
		red[0] = of.data[a];
		red[1] = of.data[a + 4];
		return red;
	}
	
	static byte[] getBlue(OptionFile of, int c) {
		int a = Clubs.startAdr + 74 + (c * Clubs.size);
		byte[] red = new byte[2];
		red[0] = of.data[a];
		red[1] = of.data[a + 4];
		return red;
	}
	
	static Color getColor(OptionFile of, int c, boolean two) {
		int a = startAdr + 72 + (c * size);
		if (two) {
			a = a + 4;
		}
		int red = of.toInt(of.data[a]);
		int green = of.toInt(of.data[a + 1]);
		int blue = of.toInt(of.data[a + 2]);
		return new Color(red, green, blue);
	}
	
	static void setColor(OptionFile of, int c, boolean two, Color color) {
		int a = startAdr + 72 + (c * size);
		if (two) {
			a = a + 4;
		}
		byte r = (byte) color.getRed();
		byte g = (byte) color.getGreen();
		byte b = (byte) color.getBlue();
		of.data[a] = r;
		of.data[a + 1] = g;
		of.data[a + 2] = b;
	}

	static void importNames(OptionFile of1, OptionFile of2) {
		for (int i = 0; i < total; i++) {
			System.arraycopy(of2.data, startAdr + (i * size), of1.data,
					startAdr + (i * size), 57);
		}
	}

	static void importData(OptionFile of1, OptionFile of2) {
		for (int i = 0; i < total; i++) {
			System.arraycopy(of2.data, startAdr + (i * size) + 57, of1.data,
					startAdr + (i * size) + 57, 31);
		}
	}
	
	static void setNameEdited(OptionFile of, int c) {
		int a = startAdr + (c * size) + 56;
		of.data[a] = 1;
	}
	
	static void setEmblemEdited(OptionFile of, int c, boolean e) {
		byte sw = 0;
		if (e) {
			sw = 1;
		}
		int sa = startAdr + 68 + (size * c);
		of.data[sa] = sw;
		of.data[sa + 1] = sw;
	}
	
	static void setStadiumEdited(OptionFile of, int c) {
		int a = startAdr + 83 + (c * size);
		of.data[a] = 1;
	}

}
