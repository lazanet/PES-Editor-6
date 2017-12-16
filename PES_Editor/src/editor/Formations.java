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

public class Formations
{
	static final int startAdr = 677202;

	static final int size = 364;

	static final int abcSize = 82;

	static byte getPos(OptionFile of, int squad, int abc, int i)
	{
		return of.data[startAdr + 138 + (size * squad) + (abc * abcSize) + i];
	}

	static void setPos(OptionFile of, int squad, int abc, int i, int p)
	{
		of.data[startAdr + 138 + (size * squad) + (abc * abcSize) + i] = (byte) p;
	}

	static byte getSlot(OptionFile of, int squad, int i)
	{
		return of.data[startAdr + 6 + (size * squad) + i];
	}

	static void setSlot(OptionFile of, int squad, int i, byte p)
	{
		of.data[startAdr + 6 + (size * squad) + i] = p;
	}

	static byte getJob(OptionFile of, int squad, int j)
	{
		return of.data[startAdr + 111 + (size * squad) + j];
	}

	static void setJob(OptionFile of, int squad, int j, byte i)
	{
		of.data[startAdr + 111 + (size * squad) + j] = i;
	}

	static byte getX(OptionFile of, int squad, int abc, int i)
	{
		return of.data[startAdr + 118 + (size * squad) + (abc * abcSize)
				+ (2 * (i - 1))];
	}

	static byte getY(OptionFile of, int squad, int abc, int i)
	{
		return of.data[startAdr + 119 + (size * squad) + (abc * abcSize)
				+ (2 * (i - 1))];
	}

	static void setX(OptionFile of, int squad, int abc, int i, int x)
	{
		of.data[startAdr + 118 + (size * squad) + (abc * abcSize)
				+ (2 * (i - 1))] = (byte) x;
	}

	static void setY(OptionFile of, int squad, int abc, int i, int y)
	{
		of.data[startAdr + 119 + (size * squad) + (abc * abcSize)
				+ (2 * (i - 1))] = (byte) y;
	}

	static boolean getAtk(OptionFile of, int squad, int abc, int i,
			int direction)
	{
		boolean result = false;
		byte t = of.data[startAdr + 149 + (size * squad) + (abc * abcSize) + i];
		if (((t >>> direction) & 1) == 1)
			result = true;
		return result;
	}

	static void setAtk(OptionFile of, int squad, int abc, int i, int direction)
	{
		if (direction < 0)
			of.data[startAdr + 149 + (size * squad) + (abc * abcSize) + i] = 0;
		else
		{
			int t = of.data[startAdr + 149 + (size * squad) + (abc * abcSize) + i];
			t = t ^ (1 << direction);
			of.data[startAdr + 149 + (size * squad) + (abc * abcSize) + i] = (byte) t;
		}
	}

	static byte getDef(OptionFile of, int squad, int abc, int i)
	{
		return of.data[startAdr + 160 + (size * squad) + (abc * abcSize) + i];
	}

	static void setDef(OptionFile of, int squad, int abc, int i, int d)
	{
		of.data[startAdr + 160 + (size * squad) + (abc * abcSize) + i] = (byte) d;
	}

	static byte getStrat(OptionFile of, int squad, int button)
	{
		return of.data[startAdr + 102 + (size * squad) + button];
	}

	static void setStrat(OptionFile of, int squad, int button, int strat)
	{
		of.data[startAdr + 102 + (size * squad) + button] = (byte) strat;
	}

	static byte getStratOlCB(OptionFile of, int squad)
	{
		return of.data[startAdr + 106 + (size * squad)];
	}

	static void setStratOlCB(OptionFile of, int squad, int cb)
	{
		of.data[startAdr + 106 + (size * squad)] = (byte) cb;
	}

	static boolean getStratAuto(OptionFile of, int squad)
	{
		return (of.data[startAdr + 107 + (size * squad)] == 1);
	}

	static void setStratAuto(OptionFile of, int squad, boolean auto)
	{
		of.data[startAdr + 107 + (size * squad)] = (byte)((auto)?1:0);
	}

	static byte getTeam(OptionFile of, int squad, int abc, int set)
	{
		return of.data[startAdr + 194 + (size * squad) + (abc * abcSize) + set];
	}

	static void setTeam(OptionFile of, int squad, int abc, int set, int v)
	{
		of.data[startAdr + 194 + (size * squad) + (abc * abcSize) + set] = (byte) v;
	}

}
