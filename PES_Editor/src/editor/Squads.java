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

public class Squads 
{
	static final int num23 = 657956;
	static final int num32 = 659635;
	static final int slot23 = 664372;
	static final int slot32 = 667730;

	public static void fixForm(OptionFile of, int s, boolean fixJobs)
	{
		byte[] temp = new byte[64];
		byte[] tempNum = new byte[32];
		int t;
		int size;
		int firstAdr;
		int firstAdrNum;
	
		if ((s >= 0 && s < 64) || (s >= 73 && s < 213)) 
		{
			t = s;
			if (s > 72)
				t = t - 9;

			if (s < 64)
			{
				size = 23;
				firstAdr = slot23 + (s * size * 2);
				firstAdrNum = num23 + (s * size);
			} 
			else
			{
				size = 32;
				firstAdr = slot32 + ((s - 73) * size * 2);
				firstAdrNum = num32 + ((s - 73) * size);
			}

			System.arraycopy(of.data, firstAdr, temp, 0, size * 2);
			System.arraycopy(of.data, firstAdrNum, tempNum, 0, size);
			
			for (int p = 0; p < size; p++)
			{			
				System.arraycopy(temp, Formations.getSlot(of, t, p) * 2, of.data, firstAdr + (p * 2), 2);
				System.arraycopy(tempNum, Formations.getSlot(of, t, p), of.data, firstAdrNum + p, 1);
			}

			if (fixJobs) 
			{
				boolean fixed;
				for (int j = 0; j < 6; j++)
				{
					fixed = false;
					for (byte i = 0; !fixed && i < 32; i++) 
						if (Formations.getSlot(of, t, i) == Formations.getJob(of, t, j)) 
						{
							Formations.setJob(of, t, j, i);
							fixed = true;
						}
					
				}
			}

			for (byte i = 0; i < 32; i++) 
				Formations.setSlot(of, t, i, i);
		}
	}

	public static void fixAll(OptionFile of) 
	{
		for (int i = 0; i < 213; i++)
			fixForm(of, i, true);
	}

	public static void tidy(OptionFile of, int team)
	{
		if ((team >= 0 && team < 64) || (team >= 73 && team < 213))
		{
			
			int size;
			int firstAdr;
			int firstAdrNum;
			if (team < 64)
			{
				size = 23;
				firstAdr = slot23 + (team * size * 2);
				firstAdrNum = num23 + (team * size);
			}
			else 
			{
				size = 32;
				firstAdr = slot32 + ((team - 75) * size * 2);
				firstAdrNum = num32 + ((team - 75) * size);
			}
			byte[] tempSlot = new byte[(size - 11) * 2];
			byte[] tempNum = new byte[size - 11];
			int numAdr;
			int slotAdr;
			int tempPos = 0;
			for (int i = 11; i < size; i++)
			{
				slotAdr = firstAdr + (i * 2);
				numAdr = firstAdrNum + i;
				if (!(of.data[slotAdr] == 0 && of.data[slotAdr + 1] == 0))
				{
					tempSlot[tempPos * 2] = of.data[slotAdr];
					tempSlot[(tempPos * 2) + 1] = of.data[slotAdr + 1];
					tempNum[tempPos] = of.data[numAdr];
					tempPos++;
				}
			}

			for (int j = tempPos; j < size - 11; j++)
				tempNum[j] = -1;
	
			System.arraycopy(tempSlot, 0, of.data, firstAdr + 22, tempSlot.length);
			System.arraycopy(tempNum, 0, of.data, firstAdrNum + 11, tempNum.length);
		}
	}

	public static void tidy11(OptionFile of, int team, int freePos, int selPos)
	{
		if ((team >= 0 && team < 64) || (team >= 73 && team < 213))
		{
			Stat stat = Stats.gk;
			int[] score = new int[21];
			int pos = 0;
			if ((selPos > 0 && selPos < 4) || (selPos > 5 && selPos < 8))
			{
				stat = Stats.cbt;
				pos = 1;
			}
			if (selPos == 4 || selPos == 5)
			{
				stat = Stats.cwp;
				pos = 1;
			}
			if (selPos == 8)
			{
				stat = Stats.sb;
				pos = 2;
			}
			if (selPos == 9)
			{
				stat = Stats.sb;
				pos = 2;
			}
			if (selPos > 9 && selPos < 15)
			{
				stat = Stats.dm;
				pos = 3;
			}
			if (selPos == 15)
			{
				stat = Stats.wb;
				pos = 2;
			}
			if (selPos == 16)
			{
				stat = Stats.wb;
				pos = 2;
			}
			if (selPos > 16 && selPos < 22)
			{
				stat = Stats.cm;
				pos = 4;
			}
			if (selPos == 22)
			{
				stat = Stats.sm;
				pos = 5;
			}
			if (selPos == 23)
			{
				stat = Stats.sm;
				pos = 5;
			}
			if (selPos > 23 && selPos < 29)
			{
				stat = Stats.am;
				pos = 6;
			}
			if (selPos > 35 && selPos < 41)
			{
				stat = Stats.cf;
				pos = 7;
			}
			if (selPos > 30 && selPos < 36)
			{
				stat = Stats.ss;
				pos = 6;
			}
			if (selPos == 29)
			{
				stat = Stats.wg;
				pos = 8;
			}
			if (selPos == 30)
			{
				stat = Stats.wg;
				pos = 8;
			}

			int size;
			int firstAdr;
			int firstAdrNum;
			if (team < 64)
			{
				size = 23;
				firstAdr = slot23 + (team * size * 2);
				firstAdrNum = num23 + (team * size);
			} 
			else 
			{
				size = 32;
				firstAdr = slot32 + ((team - 73) * size * 2);
				firstAdrNum = num32 + ((team - 73) * size);
			}

			int a;
			int c = 0;
			int pi = -1;
			int[] playerIndex = new int[21];
			for (int i = 11; pi != 0 && i < size; i++)
			{
				c = i - 11;
				a = firstAdr + (i * 2);
				pi = of.toInt(of.data[a + 1]) << 8 | of.toInt(of.data[a]);
				if (pi != 0) {
					playerIndex[c] = pi;
					switch (pos)
					{
						case 0:
							score[c] = Stats.getValue(of, pi, Stats.defence)
									+ Stats.getValue(of, pi, Stats.balance)
									+ Stats.getValue(of, pi, Stats.response)
									+ Stats.getValue(of, pi, Stats.gkAbil)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 1:
							score[c] = Stats.getValue(of, pi, Stats.defence)
									+ Stats.getValue(of, pi, Stats.balance)
									+ Stats.getValue(of, pi, Stats.response)
									+ Stats.getValue(of, pi, Stats.speed)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 2:
							score[c] = Stats.getValue(of, pi, Stats.defence)
									+ Stats.getValue(of, pi, Stats.balance)
									+ Stats.getValue(of, pi, Stats.response)
									+ Stats.getValue(of, pi, Stats.stamina)
									+ Stats.getValue(of, pi, Stats.speed)
									+ Stats.getValue(of, pi, Stats.lPassAcc)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 3:
							score[c] = Stats.getValue(of, pi, Stats.defence)
									+ Stats.getValue(of, pi, Stats.balance)
									+ Stats.getValue(of, pi, Stats.response)
									+ Stats.getValue(of, pi, Stats.sPassAcc)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 4:
							score[c] = Stats.getValue(of, pi, Stats.defence)
									+ Stats.getValue(of, pi, Stats.attack)
									+ Stats.getValue(of, pi, Stats.dribAcc)
									+ Stats.getValue(of, pi, Stats.sPassAcc)
									+ Stats.getValue(of, pi, Stats.tech)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 5:
							score[c] = Stats.getValue(of, pi, Stats.attack)
									+ Stats.getValue(of, pi, Stats.speed)
									+ Stats.getValue(of, pi, Stats.stamina)
									+ Stats.getValue(of, pi, Stats.dribAcc)
									+ Stats.getValue(of, pi, Stats.lPassAcc)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 6:
							score[c] = Stats.getValue(of, pi, Stats.attack)
									+ Stats.getValue(of, pi, Stats.dribAcc)
									+ Stats.getValue(of, pi, Stats.sPassAcc)
									+ Stats.getValue(of, pi, Stats.tech)
									+ Stats.getValue(of, pi, Stats.agility)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 7:
							score[c] = Stats.getValue(of, pi, Stats.attack)
									+ Stats.getValue(of, pi, Stats.response)
									+ Stats.getValue(of, pi, Stats.shotAcc)
									+ Stats.getValue(of, pi, Stats.shotTec)
									+ Stats.getValue(of, pi, Stats.tech)
									+ Stats.getValue(of, pi, Stats.team);
							break;
						case 8:
							score[c] = Stats.getValue(of, pi, Stats.attack)
									+ Stats.getValue(of, pi, Stats.speed)
									+ Stats.getValue(of, pi, Stats.dribAcc)
									+ Stats.getValue(of, pi, Stats.dribSpe)
									+ Stats.getValue(of, pi, Stats.sPassAcc)
									+ Stats.getValue(of, pi, Stats.lPassAcc)
									+ Stats.getValue(of, pi, Stats.agility)
									+ Stats.getValue(of, pi, Stats.tech)
									+ Stats.getValue(of, pi, Stats.team);
							break;
					}
				}
			}

			int bestPosPlayer = 0;
			int bestPosScore = 0;
			int bestPlayer = 0;
			int bestScore = 0;
			int isPos = 0;
			for (int i = 0; i < 21; i++) 
			{
				if (playerIndex[i] != 0)
				{
					isPos = Stats.getValue(of, playerIndex[i], stat);
					if (isPos == 1 && score[i] > bestPosScore)
					{
						bestPosScore = score[i];
						bestPosPlayer = i;
					}
					if (isPos == 0 && score[i] > bestScore)
					{
						bestScore = score[i];
						bestPlayer = i;
					}
				}
			}
			
			if (bestPosScore != 0)
				bestPlayer = bestPosPlayer;
			
			bestPlayer = bestPlayer + 11;
			of.data[firstAdr + (2 * freePos)] = of
					.toByte(playerIndex[bestPlayer - 11] & 0x000000FF);
			of.data[firstAdr + (2 * freePos) + 1] = of
					.toByte((playerIndex[bestPlayer - 11] & 0x0000FF00) >>> 8);
			of.data[firstAdr + (2 * bestPlayer)] = 0;
			of.data[firstAdr + (2 * bestPlayer) + 1] = 0;
			of.data[firstAdrNum + freePos] = of.data[firstAdrNum + bestPlayer];
			of.data[firstAdrNum + bestPlayer] = -1;
			tidy(of, team);
		}
	}
}
