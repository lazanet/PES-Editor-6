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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class XportInfo
{
	public String gameName;

	public String saveName;

	public String notes;

	public String game;

	public XportInfo()
	{
		gameName = new String("");
		saveName = new String("");
		notes = new String("");
		game = new String("");
	}

	public boolean getInfo(File f)
	{
		if (f.isFile())
		{
			try
			{
				RandomAccessFile rf = new RandomAccessFile(f, "r");
				gameName = "";
				saveName = "";
				notes = "";
				byte[] temp;
				game = "";
				String extension = PESUtils.getExtension(f);
				if (extension.equals(PESUtils.xps))
				{
					// int gameIdent = 0;
					rf.seek(21);
					int size = PESUtils.swabInt(rf.readInt());
					temp = new byte[size];
					rf.read(temp);
					gameName = new String(temp);
					size = PESUtils.swabInt(rf.readInt());
					temp = new byte[size];
					rf.read(temp);
					saveName = new String(temp);
					size = PESUtils.swabInt(rf.readInt());
					temp = new byte[size];
					rf.read(temp);
					notes = new String(temp);
					rf.skipBytes(6);
					temp = new byte[19];
					rf.read(temp);
					game = new String(temp);
				}
				else if (extension.equals(PESUtils.max))
				{
					rf.seek(16);
					temp = new byte[19];
					rf.read(temp);
					game = new String(temp);
					rf.seek(48);
					temp = new byte[32];
					rf.read(temp);
					gameName = new String(temp);
					gameName = gameName.replaceAll("\0", "");
				}
				else
				{
					rf.seek(64);
					temp = new byte[19];
					rf.read(temp);
					game = new String(temp);
				}
				rf.close();
				return true;
			}
			catch (IOException e)
			{
				// System.out.println("io error");
				return false;
			}
		}
		else
		{
			return false;
		}
	}

}
