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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Player implements Comparable, Serializable {
	public String name;

	public int index;

	public int adr;

	private OptionFile of;
	
	static final int startAdr = 37116;
	static final int startAdrE = 14288;
	//static int firstJapan = 4485;
	static final int firstML = 4414;
	static final int firstShop = 4437;
	static final int firstYoung = 4597;
	static final int firstOld = 4774;
	static final int firstUnused = 4784;
	static final int firstEdit = 32768;
	static final int total = 5000;
	static final int totalEdit = 184;
	static final int firstClassic = 1312;
	static final int firstClub = 1473;
	static final int firstPESUnited = 3954;

	public Player(OptionFile opf, int i, int sa) {
		of = opf;
		if (of == null)
			throw new NullPointerException();
		boolean end;
		index = i;
		adr = sa;
		if (i == 0) {
			name = "<empty>";
		} else if (i < 0 || (i >= total && i < firstEdit) || i > 32951) {
			name = "<ERROR>";
			index = 0;
		} else {
			// adr = 31568 + (i * 124);
			int a = startAdr;
			int offSet = i * 124;
			if (i >= firstEdit) {
				// adr = 8744 + (i * 124);
				a = startAdrE;
				offSet = (i - firstEdit) * 124;
			}
			byte[] nameBytes = new byte[32];
			System.arraycopy(of.data, a + offSet, nameBytes, 0, 32);
			end = false;
			int len = 0;
			for (int j = 0; !end && j < nameBytes.length - 1; j = j + 2) {
				if (nameBytes[j] == 0 && nameBytes[j + 1] == 0) {
					end = true;
					len = j;
				}
			}
			try {
				name = new String(nameBytes, 0, len, "UTF-16LE");
			} catch (UnsupportedEncodingException e) {
				name = "<Error " + String.valueOf(index) + ">";
			}

			if (name.equals("") && index >= firstEdit) {
				// name = "<???>";
				name = "<Edited " + String.valueOf(index - firstEdit) + ">";
			} else if (name.equals("")) {
				if (index >= firstUnused) {
					name = "<Unused " + String.valueOf(index) + ">";
				} else {
					name = "<L " + String.valueOf(index) + ">";
				}
			}
		}
	}

	public String toString() {
		return name;
	}

	public int compareTo(Object o) {
		Player n = (Player) o;
		int cmp = name.compareTo(n.name);
		if (cmp == 0) {
			cmp = new Integer(Stats.getValue(of, index, Stats.age)).compareTo(new Integer(
					Stats.getValue(of, n.index, Stats.age)));
		}
		return cmp;

	}

	public void setName(String newName) {
		int len = newName.length();
		if (index != 0 && len <= 15) {
			byte[] newNameBytes = new byte[32];
			byte[] t;
			try {
				t = newName.getBytes("UTF-16LE");
			} catch (UnsupportedEncodingException e) {
				t = new byte[30];
			}
			if (t.length <= 30) {
				System.arraycopy(t, 0, newNameBytes, 0, t.length);
			} else {
				System.arraycopy(t, 0, newNameBytes, 0, 30);
			}
			int a = startAdr;
			int offSet = index * 124;
			if (index >= firstEdit) {
				// adr = 8744 + (i * 124);
				a = startAdrE;
				offSet = (index - firstEdit) * 124;
			}
			System.arraycopy(newNameBytes, 0, of.data, a + offSet, 32);
			//of.data[a + offSet + 48] = -51;
			//of.data[a + offSet + 49] = -51;
			Stats.setValue(of, index, Stats.callName, 0xcdcd);

			Stats.setValue(of, index, Stats.nameEdited, 1);
			Stats.setValue(of, index, Stats.callEdited, 1);
			// of.data[a + offSet + 50] = 1;
			name = newName;
		}
	}
	
	public String getShirtName() {
		String sn = "";
		int a = startAdr + 32 + (index * 124);
		if (index >= firstEdit) {
			a = startAdrE + 32 + ((index - firstEdit) * 124);
		}
		if (of.data[a] != 0) {
			byte[] sb = new byte[16];
			System.arraycopy(of.data, a, sb, 0, 16);
			for (int i = 0; i < 16; i++) {
				if (sb[i] == 0) {
					sb[i] = 33;
				}
			}
			sn = new String(sb);
			sn = sn.replaceAll("!", "");
		}
		return sn;
	}

	public void setShirtName(String n) {
		if (n.length() < 16 && index != 0) {
			int a = startAdr + 32 + (index * 124);
			if (index >= firstEdit) {
				a = startAdrE + 32 + ((index - firstEdit) * 124);
			}
			byte[] t = new byte[16];
			n = n.toUpperCase();
			byte[] nb = n.getBytes();
			for (int i = 0; i < nb.length; i++) {
				if ((nb[i] < 65 || nb[i] > 90) && nb[i] != 46
						&& nb[i] != 32 && nb[i] != 95) {
					nb[i] = 32;
				}
			}
			System.arraycopy(nb, 0, t, 0, nb.length);
			System.arraycopy(t, 0, of.data, a, 16);
			Stats.setValue(of, index, Stats.shirtEdited, 1);
		}
	}
	
	public void makeShirt(String n) {
		// System.out.println(n);
		String result = "";
		String spaces = "";
		int len = n.length();
		if (len < 9 && len > 5) {
			spaces = " ";
		} else if (len < 6 && len > 3) {
			spaces = "  ";
		} else if (len == 3) {
			spaces = "    ";
		} else if (len == 2) {
			spaces = "        ";
		}
		n = n.toUpperCase();
		byte[] nb = n.getBytes();
		for (int i = 0; i < nb.length; i++) {
			if ((nb[i] < 65 || nb[i] > 90) && nb[i] != 46 && nb[i] != 32
					&& nb[i] != 95) {
				nb[i] = 32;
			}
		}
		n = new String(nb);
		// System.out.println(n);
		for (int i = 0; i < n.length() - 1; i++) {
			result = result + n.substring(i, i + 1) + spaces;
		}
		result = result + n.substring(n.length() - 1, n.length());
		setShirtName(result);
	}

	/*
	 * private int getNumAdr(int a) { return 651576 + ((a - 657886) / 2); }
	 */

}
