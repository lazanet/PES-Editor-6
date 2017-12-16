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

import java.io.UnsupportedEncodingException;

public class Leagues 
{
	static final byte total = 29;

	static final int nameAdr = 11859;

	static final byte maxLen = 61;

	static final byte fieldLen = 84;

	static final String[] def = { "ISS", "England", "French", "German", "Italian", "Netherlands", "Spanish", "International", "European", "African", "American", "Asia-Oceanian", "Konami", "D2", "D1", "European Masters", "European Championship", "Product Placement Cup" };

	static String get(OptionFile of, int l) {
		byte len;
		int a;
		String name;
		a = nameAdr + (l * fieldLen);
		len = 0;
		if (of.data[a] != 0)
		{
			for (byte i = 0; len == 0 && i < maxLen + 1; i++)
				if (len == 0 && of.data[a + i] == 0)
					len = i;
			
			
			try
			{
				name = new String(of.data, a, len, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				name = "<" + String.valueOf(l) + ">";
			}
		} 
		else if (l > 10)
      			name = def[(l - 11)] + ((l < 27)?" Cup":"");
		else
			name = "<" + String.valueOf(l) + ">";

		return name;
	}

	static String[] get(OptionFile of) 
	{
		String[] leagues = new String[total];
		for (byte l = 0; l < total; l++)
			leagues[l] = get(of, l);

		return leagues;
	}

	static void set(OptionFile of, int ln, String name) 
	{
		if (name.length() <= maxLen && name.length() > 0) 
		{
			int a = nameAdr + (ln * fieldLen);
			byte[] tb = new byte[maxLen + 2];
			byte[] sb;
			try 
			{
				sb = name.getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				sb = new byte[maxLen];
			}
			
			if (sb.length <= maxLen)
				System.arraycopy(sb, 0, tb, 0, sb.length);
			else
				System.arraycopy(sb, 0, tb, 0, maxLen);

			tb[maxLen + 1] = 1;

			System.arraycopy(tb, 0, of.data, a, maxLen + 2);
		}
	}

	static void importData(OptionFile ofs, OptionFile ofd) 
	{
		System.arraycopy(ofs.data, nameAdr, ofd.data, nameAdr, fieldLen* total);
	}

}
