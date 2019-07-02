/*
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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Arrays;

class SquadPlayer implements Comparable {
	public static int GK = 0;
	public static int DF = 1;
	public static int MF = 2;
	public static int AT = 3;
	public static String positionNames[] = { "GK", "DF", "MF", "AT" };

	public final int club;
	public final int number;
	public final int player;
	public final String name;
	public final int position;
	public final float avgSkill;

	public SquadPlayer(int club, int number, int player, final String name, int position, float avgSkill) {
		this.club = club;
		this.number = number;
		this.player = player;
		this.name = name;
		this.position = position;
		this.avgSkill = avgSkill;
	}

	public int compareTo(Object o) {
		final SquadPlayer sp = (SquadPlayer)o;
		if (this == sp) {
			return 0;
		} else if (this.avgSkill > sp.avgSkill) {
			return -1;
		} else if (this.avgSkill < sp.avgSkill) {
			return 1;
		}
		return 0;
	}

	public String toString() {
		return String.format("%s %d %s", 
			SquadPlayer.positionNames[this.position],
			this.number,
			this.name);
	}

	// help static methods

	public static SquadPlayer fromPlayer(int club, int number, OptionFile of, int player) {
		int regPos = Stats.getValue(of, player, Stats.regPos);
		//"GK  0", "CWP  2", "CBT  3", "SB  4", "DMF  5",
		//"WB  6", "CMF  7", "SMF  8", "AMF  9", "WF 10", "SS  11",
		//"CF 12"
		int position = SquadPlayer.DF;
		if (regPos >= 0 && regPos < 2) {
			position = SquadPlayer.GK;
		} else if (regPos >= 2 && regPos < 5) {
			position = SquadPlayer.DF;
		} else if (regPos >= 5 && regPos < 11) {
			position = SquadPlayer.MF;
		} else if (regPos >= 11) {
			position = SquadPlayer.AT;
		}
		float totalAbility = 0.0f;
		for (int i = 0; i < Stats.ability99.length; i++) {
			totalAbility += Stats.getValue(of, player, Stats.ability99[i]);
		}
		float avgSkill = totalAbility/Stats.ability99.length;

		Player p = new Player(of, player, 0);
		String name = p.name.replaceAll(",", "");
		return new SquadPlayer(club, number, player, name, position, avgSkill);
	}

	private static SquadPlayer popPlayer(final List<SquadPlayer> squad, int position) {
		if (squad.isEmpty()) {
			return null;
		}
		for (int i=0; i<squad.size(); i++) {
			SquadPlayer sp = squad.get(i);
			if (position == sp.position) {
				squad.remove(sp);
				return sp;
			}
		}
		return squad.remove(0);
	}

	public static List<SquadPlayer> getFirstN(final List<SquadPlayer> squad, int n) {
		List<SquadPlayer> dest = new LinkedList<SquadPlayer>();
		// sort by ability
		Collections.sort(squad);
		// choose first eleven: GK,DF,DF,DF,DF,MF,MF,MF,MF,AT,AT
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.GK));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.DF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.DF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.DF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.DF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.MF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.MF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.MF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.MF));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.AT));
		dest.add(SquadPlayer.popPlayer(squad, SquadPlayer.AT));
		// take the rest of highest ability
		for (int i=0; i<n-11; i++) {
			if (!squad.isEmpty()) {
				dest.add(squad.remove(0));
			} else {
				dest.add(null);
			}
		}
		return dest;
	}

	public static boolean samePlayers(OptionFile of, int club, List<SquadPlayer> squad) {
		Set a = new HashSet();
		Set b = new HashSet();
		for (int ip=0; ip<32; ip++) {
			int b1 = of.toInt(of.data[Squads.slot32 + (64 * club) + (ip * 2)]);
			int b2 = of.toInt(of.data[Squads.slot32 + 1 + (64 * club) + (ip * 2)]);
			int player = (b2 << 8) | b1;
			if (player>0 &&  player != 0xffff) {
				a.add(player);
			}
			if (squad.get(ip) != null) {
				b.add(squad.get(ip).player);
			}
		}
		return a.equals(b);
	}

	public static boolean sameNationalPlayers(OptionFile of, int nat, List<SquadPlayer> squad) {
		Set a = new HashSet();
		Set b = new HashSet();
		for (int ip=0; ip<23; ip++) {
			int b1 = of.toInt(of.data[Squads.slot23 + (46 * nat) + (ip * 2)]);
			int b2 = of.toInt(of.data[Squads.slot23 + 1 + (46 * nat) + (ip * 2)]);
			int player = (b2 << 8) | b1;
			if (player>0 &&  player != 0xffff) {
				a.add(player);
			}
			if (squad.get(ip) != null) {
				b.add(squad.get(ip).player);
			}
		}
		return a.equals(b);
	}
}

public class CSVLoader {
	private RandomAccessFile in;
	private String[] tokens;
	private int tokenCount = 0;

	private static String[] team;

	private OptionFile of;
	private byte[] orgData;

	private final CSVAttributes csvAttributes = new CSVAttributes();
	private final Map<String, Integer> footOptsByLabel = csvAttributes.getFootFavSideOptsByLabelFootVal();
	private final Map<String, Integer> favSideOptsByLabel = csvAttributes.getFootFavSideOptsByLabelFavSideVal();
	private final Map<String, Integer> physicalOptsByLabel = csvAttributes.getPhysicalOptsByLabel();
	private final Map<String, Integer> physicalLinkedOptsByLabel = csvAttributes.getPhysicalLinkedOptsByLabel();
	private final String[] wristbandLabels = csvAttributes.getWristbandLabels();
	private final Map<String, Integer> wristbandOptsByLabel = csvAttributes.getWristbandOptsByLabel();

	private Map<Integer,List<SquadPlayer>> newSquads;
	private Map<Integer,List<SquadPlayer>> newNationalSquads;

	private String nextToken() {
		if (tokenCount < tokens.length) {
			return tokens[tokenCount++];
		}
		return "";
	}

	public static Map<Integer,Integer> cNatMap = new HashMap<Integer,Integer>();
	static {
		cNatMap.put(57, 6);
		cNatMap.put(58, 8);
		cNatMap.put(59, 9);
		cNatMap.put(60, 13);
		cNatMap.put(61, 15);
		cNatMap.put(62, 44);
		cNatMap.put(63, 45);
	}

	public boolean loadFile(ProgressUI ui, OptionFile opf, File src, boolean updateClubTeams, boolean updateNationalTeams, boolean updateClassicTeams, List<String> changes) {
		of = opf;
		orgData = new byte[of.data.length];
		System.arraycopy(of.data, 0, orgData, 0, of.data.length);
		newSquads = new HashMap<Integer,List<SquadPlayer>>();
		newNationalSquads = new HashMap<Integer,List<SquadPlayer>>();

		boolean done = false;

		try {
			in = new RandomAccessFile(src, "r");
			team = Clubs.getNames(of);
			// skip headings, if present
			String first = in.readLine();


			int num = Player.firstUnused;
			ui.resetParameters(0, num, "Loading players...");

			int playerImportCounter = 1;
			String csvLine;
			while((csvLine = in.readLine()) != null){
				readPlayerFromLine(csvLine);
				ui.updateProgress(playerImportCounter);
				playerImportCounter++;
			}
			
			//PeterC10 MOD: Only update squads of club teams if set
			if(updateClubTeams){
				// update club squads
				ui.resetParameters(0, Clubs.total*32, "Updating club squads...");

				for (int club = 0; club < Clubs.total; club++) {
					List<SquadPlayer> squad = newSquads.get(club);
					if (squad != null) {
						List<SquadPlayer> newFirst32 = SquadPlayer.getFirstN(squad, 32);
						if (!SquadPlayer.samePlayers(of, club, newFirst32)) {
							//System.out.println("Team ("+club+") "+team[club]+": squad changed");
							//System.out.println("new squad: "+newFirst32);

							for (int ip = 0; ip < 32; ip++) {
								SquadPlayer sp = newFirst32.get(ip);
								int number = (sp!=null)?sp.number:0;
								int id = (sp!=null)?sp.player:0;

								int b1 = id & 0xff;
								int b2 = (id >> 8) & 0xff;

								of.data[Squads.slot32 + (64 * club) + (ip * 2)] = of.toByte(b1);
								of.data[Squads.slot32 + (64 * club) + 1 + (ip * 2)] = of.toByte(b2);
								of.data[Squads.num32 + (32 * club) + ip] = of.toByte(number-1);

								ui.updateProgress(club*32+ip);
							}

							changes.add("Squad updated for team: "+team[club]);
						}
					}
					ui.updateProgress(club*32);
					Thread.sleep(10);
				}
			}

			//PeterC10 MOD: Only update squads of national teams if set
			if(updateNationalTeams){
				// update national squads
				ui.resetParameters(0, 57*23, "Updating national squads...");

				for (int nat = 0; nat < 57; nat++) {
					List<SquadPlayer> squad = newNationalSquads.get(nat);
					if (squad != null) {
						List<SquadPlayer> newFirst23 = SquadPlayer.getFirstN(squad, 23);
						if (!SquadPlayer.sameNationalPlayers(of, nat, newFirst23)) {
							//System.out.println("Team ("+nat+") "+Stats.nation[nat]+": squad changed");
							//System.out.println("new squad: "+newFirst23);
							for (int ip = 0; ip < 23; ip++) {
								SquadPlayer sp = newFirst23.get(ip);
								int number = (sp!=null)?sp.number:0;
								int id = (sp!=null)?sp.player:0;

								int b1 = id & 0xff;
								int b2 = (id >> 8) & 0xff;

								of.data[Squads.slot23 + (46 * nat) + (ip * 2)] = of.toByte(b1);
								of.data[Squads.slot23 + (46 * nat) + 1 + (ip * 2)] = of.toByte(b2);
								of.data[Squads.num23 + (23 * nat) + ip] = of.toByte(number-1);

								ui.updateProgress(nat*23+ip);
							}

							changes.add("Squad updated for team: "+Stats.nation[nat]);
						}
					}
					ui.updateProgress(nat*23);
					Thread.sleep(10);
				}
			}


			//PeterC10 MOD: Only update squads of classic teams if set

			if(updateClassicTeams){
				// update classic squads
				ui.resetParameters(0, 7*23, "Updating classic squads...");

				for (int cNat = 57; cNat < 64; cNat++) {
					List<SquadPlayer> squad = newNationalSquads.get(cNat);
					if (squad != null) {
						List<SquadPlayer> newFirst23 = SquadPlayer.getFirstN(squad, 23);
						if (!SquadPlayer.sameNationalPlayers(of, cNat, newFirst23)) {
							//System.out.println("Team ("+cNat+") Classic "+Stats.nation[cNatMap.get(cNat)]+": squad changed");
							//System.out.println("new squad: "+newFirst23);
							for (int ip = 0; ip < 23; ip++) {
								SquadPlayer sp = newFirst23.get(ip);
								int number = (sp!=null)?sp.number:0;
								int id = (sp!=null)?sp.player:0;

								int b1 = id & 0xff;
								int b2 = (id >> 8) & 0xff;

								of.data[Squads.slot23 + (46 * cNat) + (ip * 2)] = of.toByte(b1);
								of.data[Squads.slot23 + (46 * cNat) + 1 + (ip * 2)] = of.toByte(b2);
								of.data[Squads.num23 + (23 * cNat) + ip] = of.toByte(number-1);

								ui.updateProgress(cNat*23+ip);
							}

							changes.add("Squad updated for team: Classic "+Stats.nation[cNatMap.get(cNat)]);
						}
					}
					ui.updateProgress(cNat*23);
					Thread.sleep(10);
				}
			}

			

			in.close();
			done = true;
		} catch (Exception e) {
			// restore original data, when import fails
			System.arraycopy(orgData, 0, of.data, 0, orgData.length);
			done = false;

			System.out.println("IMPORT PROBLEM!");
			System.out.println(e);
			e.printStackTrace();
		} finally {
			ui.done();
		}

		return done;
	}

	private void readName(int player) throws IOException {
		Player p = new Player(of, player, 0);
		String name = this.nextToken();
		if (!name.startsWith("<L")) {
			p.setName(name);
		}
	}

	private void readShirtName(int player) throws IOException {
		Player p = new Player(of, player, 0);
		String name = this.nextToken();
		p.setShirtName(name);
	}

	private void readInterStatus(int player) throws IOException {
		final String v = this.nextToken(); 
		if (!"0".equals(v) && !"".equals(v)) {
			int nat = Stats.getValue(of, player, Stats.nationality);
			int b1;
			int b2;
			int number = Byte.parseByte(v);
			if (nat < 57) {
				for (int ip = 0; ip < 23; ip++) {
					b1 = of.toInt(of.data[Squads.slot23 + (46 * nat) + (ip * 2)]);
					b2 = of.toInt(of.data[Squads.slot23 + 1 + (46 * nat) + (ip * 2)]);
					if (((b2 << 8) | b1) == player) {
						of.data[Squads.num23 + (23 * nat) + ip] = of.toByte(number-1);
					}
				}

				// add to nations for automatic transfers
				if (!newNationalSquads.containsKey(nat)) {
					newNationalSquads.put(nat, new LinkedList<SquadPlayer>());
				}
				List<SquadPlayer> squad = newNationalSquads.get(nat);
				squad.add(SquadPlayer.fromPlayer(nat, number, of, player));
			}
		}
	}

	private void readClassicStatus(int player) throws IOException {
		final String v = this.nextToken(); 
		int nat = Stats.getValue(of, player, Stats.nationality);
		int b1;
		int b2;
		int cNat = 0;
		int number = Byte.parseByte(v);
		if (nat == 6 || nat == 8 || nat == 9 || nat == 13 || nat == 15
				|| nat == 44 || nat == 45) {
			if (nat == 6) {
				cNat = 57;
			}
			if (nat == 8) {
				cNat = 58;
			}
			if (nat == 9) {
				cNat = 59;
			}
			if (nat == 13) {
				cNat = 60;
			}
			if (nat == 15) {
				cNat = 61;
			}
			if (nat == 44) {
				cNat = 62;
			}
			if (nat == 45) {
				cNat = 63;
			}
			for (int ip = 0; ip < 23; ip++) {
				b1 = of.toInt(of.data[Squads.slot23 + (46 * cNat) + (ip * 2)]);
				b2 = of.toInt(of.data[Squads.slot23 + 1 + (46 * cNat) + (ip * 2)]);
				if (((b2 << 8) | b1) == player) {
					of.data[Squads.num23 + (23 * cNat) + ip] = of.toByte(number-1);
				}
			}

			// add to nations for automatic transfers
			if (number > 0) {
				if (!newNationalSquads.containsKey(cNat)) {
					newNationalSquads.put(cNat, new LinkedList<SquadPlayer>());
				}
				List<SquadPlayer> squad = newNationalSquads.get(cNat);
				squad.add(SquadPlayer.fromPlayer(cNat, number, of, player));
			}
		}
	}

	private void readTeam(int player) throws IOException {
		final String clubName = this.nextToken(); 
		final String v = this.nextToken();
		int number = 0;
		try { number = Byte.parseByte(v); }
		catch (NumberFormatException nfe) {
			Player p = new Player(of, player, 0);
			String name = p.name.replaceAll(",", "");
			System.out.println("WARN: Player (" +
				name + ") has squad number outside of [0,255] range: " +
				String.valueOf(v) + ". Resetting to 0");
			number = 0;
		}
		int club = -1;
		int b1;
		int b2;
		for (int t = 0; t < Clubs.total; t++) {
			if (team[t].equals(clubName)) {
				club = t;
			}
			for (int ip = 0; ip < 32; ip++) {
				b1 = of.toInt(of.data[Squads.slot32 + (64 * t) + (ip * 2)]);
				b2 = of.toInt(of.data[Squads.slot32 + 1 + (64 * t) + (ip * 2)]);
				if (((b2 << 8) | b1) == player) {
					of.data[Squads.num32 + (32 * t) + ip] = of.toByte(number-1);
				}
			}
		}

		// add to newSquads for automatic transfers
		if (club >= 0) {
			if (!newSquads.containsKey(club)) {
				newSquads.put(club, new LinkedList<SquadPlayer>());
			}
			List<SquadPlayer> squad = newSquads.get(club);
			squad.add(SquadPlayer.fromPlayer(club, number, of, player));
		}
	}

	private void readPlayerFromLine(String line)
			throws IOException {
		tokens = line.split(",",-1);
		tokenCount = 0;

		int player = Integer.parseInt(this.nextToken());

		readName(player);
		readShirtName(player);

		for (int i = 0; i < Stats.roles.length; i++) {
			if (i != 1) {
				Stats.setValue(of, player, Stats.roles[i], this.nextToken());
			}
		}

		Stats.setValue(of, player, Stats.regPos, this.nextToken());
		Stats.setValue(of, player, Stats.height, this.nextToken());

		String foot = this.nextToken();
		String favSide = this.nextToken();
		String footFavSideLabel = foot + "/" + favSide;

		int footVal = footOptsByLabel.get(footFavSideLabel);
		int favSideVal = favSideOptsByLabel.get(footFavSideLabel);

		Stats.setValue(of, player, Stats.foot, footVal);
		Stats.setValue(of, player, Stats.favSide, favSideVal);

		Stats.setValue(of, player, Stats.wfa, this.nextToken());
		Stats.setValue(of, player, Stats.wff, this.nextToken());
		
		for (int i = 0; i < Stats.ability99.length; i++) {
			Stats.setValue(of, player, Stats.ability99[i], this.nextToken());
		}
		
		Stats.setValue(of, player, Stats.consistency, this.nextToken());
		Stats.setValue(of, player, Stats.condition, this.nextToken());
		
		for (int i = 0; i < Stats.abilitySpecial.length; i++) {
			Stats.setValue(of, player, Stats.abilitySpecial[i], this.nextToken());
		}
		
		Stats.setValue(of, player, Stats.injury, this.nextToken());
		Stats.setValue(of, player, Stats.dribSty, this.nextToken());
		Stats.setValue(of, player, Stats.freekick, this.nextToken());
		Stats.setValue(of, player, Stats.pkStyle, this.nextToken());
		Stats.setValue(of, player, Stats.dkSty, this.nextToken());
		Stats.setValue(of, player, Stats.age, this.nextToken());
		Stats.setValue(of, player, Stats.weight, this.nextToken());
		Stats.setValue(of, player, Stats.nationality, this.nextToken());

		//PeterC10 MOD: Read skin color, take away 1 and apply to the imported player
		int skinColor = Integer.parseInt(this.nextToken()) - 1;
		//System.out.println("Skin Color: " + skinColor);
		Stats.setValue(of, player, Stats.skin, Integer.toString(skinColor));

		//PeterC10 MOD: Read face type number
		int faceType = Integer.parseInt(this.nextToken());
		Stats.setValue(of, player, Stats.faceType, Integer.toString(faceType));

		//PeterC10 MOD: Read preset face number, take away 1 and apply to the imported player
		int faceNumber = Integer.parseInt(this.nextToken()) - 1;
		Stats.setValue(of, player, Stats.face, Integer.toString(faceNumber));

		int ia = Player.startAdr + (player * 124);
		if (player >= Player.firstEdit) {
			ia = Player.startAdrE + ((player - Player.firstEdit) * 124);
		}

		byte[] playerData = Arrays.copyOfRange(of.data, ia, ia + 124);

		//PeterC10 MOD: Read and apply head and physical attributes

		String headWidthLabel = this.nextToken();
		String neckLengthLabel = this.nextToken();
		String neckWidthLabel = this.nextToken();
		String shoulderHeightLabel = this.nextToken();
		String shoulderWidthLabel = this.nextToken();
		String chestMeasurementLabel = this.nextToken();
		String waistCircumferenceLabel = this.nextToken();
		String armCircumferenceLabel = this.nextToken();
		String legCircumferenceLabel = this.nextToken();
		String calfCircumferenceLabel = this.nextToken();
		String legLengthLabel = this.nextToken();

		String headWidthNeckWidthLabel = headWidthLabel + "/" + neckWidthLabel;
		String neckLengthChestMeasurementLabel = neckLengthLabel + "/" + chestMeasurementLabel;
		String armCircumferenceWaistCircumferenceLabel = armCircumferenceLabel + "/" + waistCircumferenceLabel;
		String legCircumferenceCalfCircumferenceLabel = legCircumferenceLabel + "/" + calfCircumferenceLabel;
		String legLengthShoulderHeightLabel = legLengthLabel + "/" + shoulderHeightLabel;

		int headWidthNeckWidthVal = physicalLinkedOptsByLabel.get(headWidthNeckWidthLabel);
		playerData[91] = (byte)headWidthNeckWidthVal;

		int neckLengthChestMeasurementVal = physicalLinkedOptsByLabel.get(neckLengthChestMeasurementLabel);
		playerData[105] = (byte)neckLengthChestMeasurementVal;

		int armCircumferenceWaistCircumferenceVal = physicalLinkedOptsByLabel.get(armCircumferenceWaistCircumferenceLabel);
		playerData[106] = (byte)armCircumferenceWaistCircumferenceVal;

		int legCircumferenceCalfCircumferenceVal = physicalLinkedOptsByLabel.get(legCircumferenceCalfCircumferenceLabel);
		playerData[107] = (byte)legCircumferenceCalfCircumferenceVal;

		int legLengthShoulderHeightVal = physicalLinkedOptsByLabel.get(legLengthShoulderHeightLabel);
		playerData[108] = (byte)legLengthShoulderHeightVal;

		int shoulderWidthVal = physicalOptsByLabel.get(shoulderWidthLabel);
		playerData[109] = (byte)shoulderWidthVal;

		//PeterC10 MOD: Apply wristband type and color to player data

		String wristBandType = this.nextToken();
		String wristBandColor = this.nextToken();
		String wristBandKey = wristBandType + "-" + wristBandColor;

		int wristbandVal = 0;

		if (Arrays.asList(wristbandLabels).contains(wristBandKey)){
			wristbandVal = wristbandOptsByLabel.get(wristBandKey);
		}

		playerData[98] = (byte)wristbandVal;

		System.arraycopy(playerData, 0, of.data, ia, 124);

		readInterStatus(player);
		readClassicStatus(player);
		readTeam(player);
	}
}
