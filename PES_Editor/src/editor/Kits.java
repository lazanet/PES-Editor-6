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

public class Kits {
	static final int totalN = 64;

	static final int startAdrN = 763804;

	static final int sizeN = 352;

	static final int startAdrC = 786332;

	static final int sizeC = 544;

	static boolean logoUsed(OptionFile of, int team, int logo)
	{
		int a = startAdrC + 256 + (sizeC * team) + (logo * 24) + 2;
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 256 + (sizeN * team) + (logo * 24) + 2;
		}
	
		return (of.data[a] == 1);
	}

	static byte getLogo(OptionFile of, int team, int logo)
	{
		int a = startAdrC + 256 + (sizeC * team) + (logo * 24) + 3;
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 256 + (sizeN * team) + (logo * 24) + 3;
		}
		return of.data[a];
	}

	static void setLogo(OptionFile of, int team, int logo, byte slot)
	{
		int a = startAdrC + 256 + (sizeC * team) + (logo * 24) + 3;
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 256 + (sizeN * team) + (logo * 24) + 3;
		}
		of.data[a] = slot;
	}

	static void setLogoUnused(OptionFile of, int team, int logo)
	{
		int a = startAdrC + 256 + (sizeC * team) + (logo * 24) + 2;
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 256 + (sizeN * team) + (logo * 24) + 2;
		}
		of.data[a] = 0;
		of.data[a + 1] = 88;
	}

	static void importKit(OptionFile of1, int team1, OptionFile of2, int team2)
	{
		int t = team1;
		int a1 = startAdrC + (sizeC * team1);
		int a2 = startAdrC + (sizeC * team2);
		int size = sizeC;
		if (team1 >= Clubs.total)
		{
			team1 = team1 - Clubs.total;
			a1 = startAdrN + (sizeN * team1);
			team2 = team2 - Clubs.total;
			a2 = startAdrN + (sizeN * team2);
			size = sizeN;
		}
		System.arraycopy(of2.data, a2, of1.data, a1, size);

		if (!of1.isWE() && of2.isWE()) Convert.kitModel(of1, t);
	
	}

	static boolean isLic(OptionFile of, int team)
	{
		int a = startAdrC + 58 + (sizeC * team);
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 58 + (sizeN * team);
		}

		return of.data[a] == 1;
	}

	static byte getKitModel(OptionFile of, int team, int kit)
	{
		int a = startAdrC + 60 + (sizeC * team) + (kit * 62);
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 60 + (sizeN * team) + (kit * 62);
		}
		return of.data[a];
	}

	static void setKitModel(OptionFile of, int team, int kit, byte model)
	{
		int a = startAdrC + 60 + (sizeC * team) + (kit * 62);
		if (team >= Clubs.total)
		{
			team = team - Clubs.total;
			a = startAdrN + 60 + (sizeN * team) + (kit * 62);
		}
		of.data[a] = model;
	}

}
