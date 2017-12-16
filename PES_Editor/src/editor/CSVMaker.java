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

public class CSVMaker {
	private RandomAccessFile out;

	private static String[] team;

	private static int seperator = 44;

	private OptionFile of;

	public boolean makeFile(OptionFile opf, File dest, boolean headings,
			boolean extra, boolean create)
	{
		of = opf;
		boolean done = false;

		try
		{
			out = new RandomAccessFile(dest, "rw");
			team = Clubs.getNames(of);
			if (headings)
			{
				writeHeadings();
				out.write(13);
				out.write(10);
			}
			for (int player = 1; player < Player.firstUnused; player++)
				writePlayer(out, player);
			
			if (extra)
				for (int player = Player.firstUnused; player < Player.total; player++)
					writePlayer(out, player);
				
			if (create)
				for (int player = Player.firstEdit; player < 32952; player++) 
					writePlayer(out, player);

			out.close();
			done = true;
		} 
		catch (IOException e)
		{
			done = false;
		}
		return done;
	}

	private void writeName(int player) throws IOException
	{
		Player p = new Player(of, player, 0);
		String name = p.name.replaceAll(",", "");
		out.writeBytes(name);
	}

	private void writeHeadings() throws IOException
	{
		String[] head = { "GK  0", "CWP  2", "CBT  3", "SB  4", "DMF  5", "WB  6", "CMF  7", "SMF  8", "AMF  9", "WF 10", "SS  11", "CF  12", "REGISTERED POSITION", "HEIGHT", "STRONG FOOT", "FAVOURED SIDE", "WEAK FOOT ACCURACY", "WEAK FOOT FREQUENCY", "ATTACK", "DEFENSE", "BALANCE", "STAMINA", "TOP SPEED", "ACCELERATION", "RESPONSE", "AGILITY", "DRIBBLE ACCURACY", "DRIBBLE SPEED", "SHORT PASS ACCURACY", "SHORT PASS SPEED", "LONG PASS ACCURACY", "LONG PASS SPEED", "SHOT ACCURACY", "SHOT POWER", "SHOT TECHNIQUE", "FREE KICK ACCURACY", "SWERVE", "HEADING", "JUMP", "TECHNIQUE", "AGGRESSION", "MENTALITY", "GOAL KEEPING", "TEAM WORK", "CONSISTENCY", "CONDITION / FITNESS", "DRIBBLING", "TACTIAL DRIBBLE", "POSITIONING", "REACTION", "PLAYMAKING", "PASSING", "SCORING", "1-1 SCORING", "POST PLAYER", "LINES", "MIDDLE SHOOTING", "SIDE", "CENTRE", "PENALTIES", "1-TOUCH PASS", "OUTSIDE", "MARKING", "SLIDING", "COVERING", "D-LINE CONTROL", "PENALTY STOPPER", "1-ON-1 STOPPER", "LONG THROW", "INJURY TOLERANCE", "DRIBBLE STYLE", "FREE KICK STYLE", "PK STYLE", "DROP KICK STYLE", "AGE", "WEIGHT", "NATIONALITY", "INTERNATIONAL NUMBER", "CLASSIC NUMBER", "CLUB TEAM", "CLUB NUMBER" };

		out.writeBytes("NAME");
		for (int h = 0; h < head.length; h++)
		{
			out.write(seperator);
			out.writeBytes(head[h]);
		}
	}

	private void writeInterStatus(int player) throws IOException {
		String intPlayNo = "0";
		int nat = Stats.getValue(of, player, Stats.nationality);
		int num;
		int b1;
		int b2;
		if (nat < 57)
		{
			for (int ip = 0; ip < 23; ip++)
			{
				b1 = of.toInt(of.data[Squads.slot23 + (46 * nat) + (ip * 2)]);
				b2 = of.toInt(of.data[Squads.slot23 + 1 + (46 * nat) + (ip * 2)]);
				if (((b2 << 8) | b1) == player)
				{
					num = of.toInt(of.data[Squads.num23 + (23 * nat) + ip]) + 1;
					intPlayNo = String.valueOf(num);
				}
			}
		}
		out.writeBytes(intPlayNo);
	}

	private void writeClassicStatus(int player) throws IOException
	{
		String intPlayNo = "0";
		int nat = Stats.getValue(of, player, Stats.nationality);
		int num;
		int b1;
		int b2;
		int cNat = 0;
		if (nat == 6 || nat == 8 || nat == 9 || nat == 14 || nat == 15 || nat == 44 || nat == 45)
		{
			if (nat == 6)
				cNat = 60;
			if (nat == 8)
				cNat = 61;
			if (nat == 9)
				cNat = 62;
			if (nat == 14)
				cNat = 63;
			if (nat == 15)
				cNat = 64;
			if (nat == 44)
				cNat = 65;
			if (nat == 45)
				cNat = 66;

			for (int ip = 0; ip < 23; ip++)
			{
				b1 = of.toInt(of.data[Squads.slot23 + (46 * cNat) + (ip * 2)]);
				b2 = of.toInt(of.data[Squads.slot23 + 1 + (46 * cNat) + (ip * 2)]);
				if (((b2 << 8) | b1) == player)
				{
					num = of.toInt(of.data[Squads.num23 + (23 * cNat) + ip]) + 1;
					intPlayNo = String.valueOf(num);
				}
			}
		}
		out.writeBytes(intPlayNo);
	}

	/*
	 * private void writeClub(int player) throws IOException { int inAclub = 0;
	 * for (int t = 0; t < 138; t++) { for (int ip = 0; ip < 40; ip++) { if (
	 * of.data[437602+(80t) + (ip2)+of.offSet]256 + of.ofileData[437601+(80t) +
	 * (ip2)+of.offSet] == player + 1) { out.writeBytes(rClubTeams[t]);
	 * out.write(seperator); Integer t6 = new
	 * Integer(of.ofileData[430870+(40t)+ip+of.offSet]+1); String clubPlayNo =
	 * t6.toString(); out.writeBytes(clubPlayNo); inAclub = 1; } } } if
	 * (inAclub==0) { out.writeBytes(statInfo.skillposMod[0]);
	 * out.write(seperator); out.writeBytes(statInfo.skillposMod[0]); } }
	 */

	private void writeTeam(int player) throws IOException
	{
		String intPlayNo = "0";
		String club = "";
		int num;
		int b1;
		int b2;
		for (int t = 0; t < Clubs.total; t++)
			for (int ip = 0; ip < 32; ip++)
			{
				b1 = of.toInt(of.data[Squads.slot32 + (64 * t) + (ip * 2)]);
				b2 = of.toInt(of.data[Squads.slot32 + 1 + (64 * t) + (ip * 2)]);
				if (((b2 << 8) | b1) == player)
				{
					num = of.toInt(of.data[Squads.num32 + (32 * t) + ip]) + 1;
					club = team[t];
					intPlayNo = String.valueOf(num);
				}
			}
		out.writeBytes(club);
		out.write(seperator);
		out.writeBytes(intPlayNo);
	}

	/*
	 * private boolean playerExists(int player) throws IOException { boolean
	 * exists = false; if (of.ofileData[405641+(player128)+of.offSet] != 0) {
	 * exists = true; } return exists; }
	 */

	private void writePlayer(RandomAccessFile out, int player) throws IOException
	{
		writeName(player);
		out.write(seperator);

		for (int i = 0; i < Stats.roles.length; i++)
			if (i != 1)
			{
				out.writeBytes(Stats.getString(of, player, Stats.roles[i]));
				out.write(seperator);
			}

		out.writeBytes(Stats.getString(of, player, Stats.regPos));
		out.write(seperator);

		out.writeBytes(Stats.getString(of, player, Stats.height));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.foot));
		out.write(seperator);
		out.writeBytes(getSide(player));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.wfa));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.wff));
		out.write(seperator);

		for (int i = 0; i < Stats.ability99.length; i++)
		{
			out.writeBytes(Stats.getString(of, player, Stats.ability99[i]));
			out.write(seperator);
		}

		out.writeBytes(Stats.getString(of, player, Stats.consistency));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.condition));
		out.write(seperator);

		for (int i = 0; i < Stats.abilitySpecial.length; i++)
		{
			out.writeBytes(Stats.getString(of, player,Stats.abilitySpecial[i]));
			out.write(seperator);
		}

		out.writeBytes(Stats.getString(of, player, Stats.injury));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.dribSty));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.freekick));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.pkStyle));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.dkSty));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.age));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.weight));
		out.write(seperator);
		out.writeBytes(Stats.getString(of, player, Stats.nationality));
		out.write(seperator);

		writeInterStatus(player);
		out.write(seperator);
		writeClassicStatus(player);
		out.write(seperator);
		writeTeam(player);

		out.write(13);
		out.write(10);
	}

	private String getSide(int p)
	{
		String side = "B";
		int s = Stats.getValue(of, p, Stats.favSide);
		if (s == 0)
			side = Stats.getString(of, p, Stats.foot);
		if (s == 1)
		{
			if (Stats.getValue(of, p, Stats.foot) == 0)
				side = "L";
			else
				side = "R";
		}
		return side;
	}
}
