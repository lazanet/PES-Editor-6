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

import java.io.File;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;

public class OptionFile {
	public static final int LENGTH = 1191936;

	public byte[] data = new byte[LENGTH];

	private byte[] headerData;

	public String fileName;

	public String gameID;

	private final static byte[] sharkport = { 13, 0, 0, 0, 83, 104, 97, 114,
			107, 80, 111, 114, 116, 83, 97, 118, 101, 0, 0, 0, 0 };

	private final static String magicMax = "Ps2PowerSave";

	String gameName;

	String saveName;

	String notes;

	final static int[] block = { 12, 5144, 9544, 14288, 37116, 657956, 751472,
			763804, 911144, 1170520 };

	final static int[] blockSize = { 4844, 1268, 4730, 22816, 620000, 93501,
			12320, 147328, 259364, 21032 };

	private final static int[] key = { 2058578050, 2058578078, 2058578109,
			2058578079, 2058578084, 2058578115, 2058578073, 2058578105,
			2058578068, 2058578101, 2058578095, 2058578045, 2058578100,
			2058578111, 2058578096, 2058578068, 2058578101, 2058578117,
			2058578115, 2058578071, 2058578064, 2058578045, 2058578078,
			2058578085, 2058578062, 2058578116, 2058578109, 2058578045,
			2058578115, 2058578076, 2058578049, 2058578093, 2058578066,
			2058578051, 2058578082, 2058578114, 2058578045, 2058578093,
			2058578052, 2058578112, 2058578073, 2058578063, 2058578100,
			2058578102, 2058578103, 2058578053, 2058578085, 2058578078,
			2058578077, 2058578115, 2058578076, 2058578086, 2058578116,
			2058578111, 2058578083, 2058578109, 2058578072, 2058578047,
			2058578081, 2058578049, 2058578074, 2058578048, 2058578086,
			2058578110, 2058578098, 2058578102, 2058578105, 2058578050,
			2058578046, 2058578086, 2058578095, 2058578083, 2058578065,
			2058578062, 2058578047, 2058578116, 2058578109, 2058578100,
			2058578068, 2058578100, 2058578109, 2058578104, 2058578079,
			2058578084, 2058578084, 2058578083, 2058578084, 2058578098,
			2058578096, 2058578070, 2058578068, 2058578110, 2058578094,
			2058578045, 2058578114, 2058578082, 2058578116, 2058578068,
			2058578114, 2058578097, 2058578085, 2058578115, 2058578072,
			2058578068, 2058578047, 2058578099, 2058578076, 2058578101,
			2058578086, 2058578117, 2058578052, 2058578109, 2058578070,
			2058578050, 2058578118, 2058578046, 2058578109, 2058578098,
			2058578099, 2058578064, 2058578048, 2058578103, 2058578069,
			2058578075, 2058578068, 2058578085, 2058578110, 2058578111,
			2058578114, 2058578110, 2058578081, 2058578084, 2058578077,
			2058578073, 2058578084, 2058578100, 2058578104, 2058578063,
			2058578083, 2058578049, 2058578065, 2058578109, 2058578105,
			2058578099, 2058578105, 2058578062, 2058578069, 2058578070,
			2058578065, 2058578066, 2058578047, 2058578100, 2058578107,
			2058578077, 2058578062, 2058578050, 2058578113, 2058578080,
			2058578065, 2058578083, 2058578095, 2058578111, 2058578096,
			2058578044, 2058578116, 2058578053, 2058578084, 2058578077,
			2058578118, 2058578100, 2058578072, 2058578044, 2058578073,
			2058578104, 2058578117, 2058578074, 2058578069, 2058578110,
			2058578050, 2058578045, 2058578045, 2058578047, 2058578047,
			2058578106, 2058578064, 2058578099, 2058578095, 2058578063,
			2058578067, 2058578068, 2058578049, 2058578108, 2058578098,
			2058578115, 2058578099, 2058578097, 2058578106, 2058578097,
			2058578116, 2058578116, 2058578110, 2058578118, 2058578099,
			2058578111, 2058578106, 2058578109, 2058578101, 2058578093,
			2058578077, 2058578053, 2058578061, 2058578098, 2058578050,
			2058578086, 2058578104, 2058578098, 2058578113, 2058578102,
			2058578065, 2058578077, 2058578082, 2058578044, 2058578050,
			2058578085, 2058578117, 2058578045, 2058578117, 2058578113,
			2058578082, 2058578051, 2058578110, 2058578103, 2058578096,
			2058578069, 2058578052, 2058578114, 2058578046, 2058578044,
			2058578047, 2058578108, 2058578083, 2058578075, 2058578077,
			2058578069, 2058578050, 2058578101, 2058578063, 2058578082,
			2058578052, 2058578108, 2058578106, 2058578109, 2058578112,
			2058578062, 2058578071, 2058578051, 2058578047, 2058578097,
			2058578062, 2058578100, 2058578048, 2058578080, 2058578080,
			2058578077, 2058578047, 2058578048, 2058578096, 2058578100,
			2058578118, 2058578105, 2058578096, 2058578072, 2058578085,
			2058578084, 2058578061, 2058578114, 2058578044, 2058578049,
			2058578053, 2058578093, 2058578064, 2058578049, 2058578083,
			2058578069, 2058578073, 2058578104, 2058578080, 2058578098,
			2058578103, 2058578093, 2058578049, 2058578044, 2058578099,
			2058578094, 2058578070, 2058578103, 2058578070, 2058578062,
			2058578078, 2058578102, 2058578104, 2058578109, 2058578068,
			2058578067, 2058578108, 2058578108, 2058578076, 2058578086,
			2058578053, 2058578104, 2058578093, 2058578070, 2058578105,
			2058578110, 2058578094, 2058578112, 2058578086, 2058578049,
			2058578101, 2058578086, 2058578108, 2058578071, 2058578095,
			2058578079, 2058578097, 2058578116, 2058578111, 2058578046,
			2058578103, 2058578071, 2058578067, 2058578063, 2058578096,
			2058578048, 2058578079, 2058578103, 2058578068, 2058578114,
			2058578079, 2058578072, 2058578102, 2058578115, 2058578053,
			2058578047, 2058578084, 2058578046, 2058578110, 2058578044,
			2058578108, 2058578101, 2058578078, 2058578073, 2058578086,
			2058578049, 2058578107, 2058578069, 2058578077, 2058578086,
			2058578079, 2058578110, 2058578048, 2058578116, 2058578101,
			2058578108, 2058578081, 2058578093, 2058578113, 2058578065,
			2058578045, 2058578080, 2058578109, 2058578075, 2058578097,
			2058578071, 2058578049, 2058578053, 2058578078, 2058578050,
			2058578075, 2058578067, 2058578083, 2058578061, 2058578116,
			2058578116, 2058578075, 2058578093, 2058578116, 2058578100,
			2058578093, 2058578052, 2058578085, 2058578047, 2058578095,
			2058578081, 2058578045, 2058578044, 2058578101, 2058578097,
			2058578110, 2058578115, 2058578096, 2058578069, 2058578053,
			2058578050, 2058578112, 2058578085, 2058578104, 2058578082,
			2058578073, 2058578099, 2058578081, 2058578045, 2058578079,
			2058578071, 2058578080, 2058578047, 2058578113, 2058578076,
			2058578082, 2058578117, 2058578086, 2058578046, 2058578099,
			2058578068, 2058578074, 2058578108, 2058578064, 2058578077,
			2058578115, 2058578066, 2058578074, 2058578104, 2058578082,
			2058578115, 2058578117, 2058578082, 2058578117, 2058578048,
			2058578053, 2058578107, 2058578079, 2058578116, 2058578081,
			2058578086, 2058578064, 2058577996 };

	private final static byte[] keyPC = { 115, 96, -31, -58, 31, 60, -83, 66,
			11, 88, -71, -2, 55, -76, 5, -6, -93, 80, -111, 54, 79, 44, 93,
			-78, 59, 72, 105, 110, 103, -92, -75, 106, -45, 64, 65, -90, 127,
			28, 13, 34, 107, 56, 25, -34, -105, -108, 101, -38, 3, 48, -15, 22,
			-81, 12, -67, -110, -101, 40, -55, 78, -57, -124, 21, 74, 51, 32,
			-95, -122, -33, -4, 109, 2, -53, 24, 121, -66, -9, 116, -59, -70,
			99, 16, 81, -10, 15, -20, 29, 114, -5, 8, 41, 46, 39, 100, 117, 42,
			-109, 0, 1, 102, 63, -36, -51, -30, 43, -8, -39, -98, 87, 84, 37,
			-102, -61, -16, -79, -42, 111, -52, 125, 82, 91, -24, -119, 14,
			-121, 68, -43, 10, -13, -32, 97, 70, -97, -68, 45, -62, -117, -40,
			57, 126, -73, 52, -123, 122, 35, -48, 17, -74, -49, -84, -35, 50,
			-69, -56, -23, -18, -25, 36, 53, -22, 83, -64, -63, 38, -1, -100,
			-115, -94, -21, -72, -103, 94, 23, 20, -27, 90, -125, -80, 113,
			-106, 47, -116, 61, 18, 27, -88, 73, -50, 71, 4, -107, -54, -77,
			-96, 33, 6, 95, 124, -19, -126, 75, -104, -7, 62, 119, -12, 69, 58,
			-29, -112, -47, 118, -113, 108, -99, -14, 123, -120, -87, -82, -89,
			-28, -11, -86, 19, -128, -127, -26, -65, 92, 77, 98, -85, 120, 89,
			30, -41, -44, -91, 26, 67, 112, 49, 86, -17, 76, -3, -46, -37, 104,
			9, -114, 7, -60, 85, -118 };

	byte format = -1;

	int fileCount;

	public boolean readXPS(File of) {
		format = -1;
		boolean done = true;
		String extension = PESUtils.getExtension(of);
		try {
			RandomAccessFile in = new RandomAccessFile(of, "r");
			if (extension != null && extension.equals(PESUtils.xps)) {
				long offSet = in.length() - LENGTH - 4;
				in.seek(21);
				byte[] temp;
				int size = PESUtils.swabInt(in.readInt());
				temp = new byte[size];
				in.read(temp);
				gameName = new String(temp, "ISO-8859-1");
				size = PESUtils.swabInt(in.readInt());
				temp = new byte[size];
				in.read(temp);
				saveName = new String(temp, "ISO-8859-1");
				size = PESUtils.swabInt(in.readInt());
				temp = new byte[size];
				in.read(temp);
				notes = new String(temp, "ISO-8859-1");

				headerData = new byte[(int) offSet - 33 - gameName.length()
						- saveName.length() - notes.length()];
				in.read(headerData);
				gameID = new String(headerData, 6, 19);
				format = 0;
			} else if (extension != null && extension.equals(PESUtils.psu)) {
				headerData = new byte[(int) (in.length() - LENGTH)];
				in.read(headerData);
				gameID = new String(headerData, 64, 19);
				format = 2;
			} else if (extension != null && extension.equals(PESUtils.max)) {
				byte[] temp = new byte[(int) in.length()];
				in.read(temp);
				String magic = new String(temp, 0, 12, "ISO-8859-1");
				if (magic.equals(magicMax)) {
					int chk = byteToInt(temp, 12);
					temp[12] = 0;
					temp[13] = 0;
					temp[14] = 0;
					temp[15] = 0;
					//System.out.println(chk);
					CRC32 crc = new CRC32();
					crc.update(temp);
					//System.out.println((int) crc.getValue());
					if ((int) crc.getValue() == chk) {
						temp = new byte[32];
						in.seek(16);
						in.read(temp);
						gameID = new String(temp, "ISO-8859-1");
						gameID = gameID.replace("\0", "");
						in.read(temp);
						gameName = new String(temp, "ISO-8859-1");
						gameName = gameName.replace("\0", "");
						int codeSize = PESUtils.swabInt(in.readInt());
						fileCount = PESUtils.swabInt(in.readInt());
						temp = new byte[codeSize];
						in.read(temp);
						Lzari lz = new Lzari();
						temp = lz.decodeLzari(temp);
						int p = 0;
						for (int i = 0; i < fileCount && format != 3; i++) {
							int size = byteToInt(temp, p);
							String title = new String(temp, p + 4, 19,
									"ISO-8859-1");
							if (size == LENGTH && title.equals(gameID)) {
								p = p + 36;
								headerData = new byte[p];
								System.arraycopy(temp, 0, headerData, 0, p);
								System.arraycopy(temp, p, data, 0, LENGTH);
								format = 3;
							} else {
								p = p + 36 + size;
								p = ((p + 23) / 16 * 16) - 8;
							}
						}
						if (format != 3) {
							done = false;
						}
					} else {
						done = false;
					}
				} else {
					done = false;
				}
			} else {
				if (of.getName().equals("KONAMI-WIN32WEXUOPT")) {
					gameID = "PC_WE";
				} else {
					gameID = "PC_PES";
				}
				format = 1;
			}
			if (done && format != -1) {
				if (format != 3)
					in.read(data);
				if (format == 1) {
					convertData();
				}
				decrypt();
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}
		if (done) {
			fileName = of.getName();
		} else {
			fileName = null;
		}
		return done;
	}

	private void convertData() {
		int k = 0;
		for (int a = 0; a < LENGTH; a++) {
			data[a] = (byte) (data[a] ^ keyPC[k]);
			if (k < 255) {
				k++;
			} else {
				k = 0;
			}
		}
	}

	public boolean saveXPS(File of) {
		data[45] = 1;
		data[46] = 1;
		data[5938] = 1;
		data[5939] = 1;
		boolean done = true;
		encrypt();
		checkSums();
		if (format == 1) {
			convertData();
		}
		try {
			RandomAccessFile out = new RandomAccessFile(of, "rw");
			if (format == 0) {
				saveName = of.getName();
				saveName = saveName.substring(0, saveName.length() - 4);
				out.write(sharkport);
				out.writeInt(PESUtils.swabInt(gameName.length()));
				out.writeBytes(gameName);
				out.writeInt(PESUtils.swabInt(saveName.length()));
				out.writeBytes(saveName);
				out.writeInt(PESUtils.swabInt(notes.length()));
				out.writeBytes(notes);
				out.write(headerData);
			}
			if (format == 2) {
				out.write(headerData);
			}
			if (format == 3) {
				int textSize = headerData.length + LENGTH;
				textSize = ((textSize + 23) / 16 * 16) - 8;
				// System.out.println(textSize);
				byte[] temp = new byte[textSize];
				System.arraycopy(headerData, 0, temp, 0, headerData.length);
				System.arraycopy(data, 0, temp, headerData.length, data.length);
				Lzari lz = new Lzari();
				temp = lz.encodeLzari(temp);
				int codeSize = temp.length;				
				
				byte[] temp2 = new byte[88];
				System.arraycopy(magicMax.getBytes("ISO-8859-1"), 0, temp2, 0,
						magicMax.length());
				System
						.arraycopy(gameID.getBytes("ISO-8859-1"), 0, temp2, 16,
								19);
				System.arraycopy(gameName.getBytes("ISO-8859-1"), 0, temp2, 48,
						gameName.length());
				System.arraycopy(getBytesInt(codeSize), 0, temp2, 80, 4);
				System.arraycopy(getBytesInt(fileCount), 0, temp2, 84, 4);
				
				CRC32 chk = new CRC32();
				chk.update(temp2);
				chk.update(temp);
				//System.out.println((int)chk.getValue());
				System.arraycopy(getBytesInt((int) chk.getValue()), 0, temp2,
						12, 4);
				out.write(temp2);
				out.write(temp);
			} else {
				out.write(data);
			}
			if (format == 0) {
				out.write(0);
				out.write(0);
				out.write(0);
				out.write(0);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}
		if (format == 1) {
			convertData();
		}
		decrypt();
		return done;

	}

	public byte toByte(int i) {
		byte b;
		if (i > 127) {
			b = (byte) (i - 256);
		} else {
			b = (byte) i;
		}
		return b;
	}

	public int toInt(byte b) {
		int i = b;
		if (i < 0) {
			i = i + 256;
		}
		return i;
	}

	private void decrypt() {
		for (int i = 1; i < 10; i++) {
			int k = 0;
			for (int a = block[i]; a + 4 <= block[i] + blockSize[i]; a = a + 4) {
				int c = byteToInt(data, a);
				int p = ((c - key[k]) + 0x7ab3684c) ^ 0x7ab3684c;
				data[a] = toByte(p & 0x000000FF);
				data[a + 1] = toByte((p >>> 8) & 0x000000FF);
				data[a + 2] = toByte((p >>> 16) & 0x000000FF);
				data[a + 3] = toByte((p >>> 24) & 0x000000FF);
				k++;
				if (k == 446) {
					k = 0;
				}
			}

		}
	}

	private void encrypt() {
		for (int i = 1; i < 10; i++) {
			int k = 0;
			for (int a = block[i]; a + 4 <= block[i] + blockSize[i]; a = a + 4) {
				int p = byteToInt(data, a);
				int c = key[k] + ((p ^ 0x7ab3684c) - 0x7ab3684c);
				data[a] = toByte(c & 0x000000FF);
				data[a + 1] = toByte((c >>> 8) & 0x000000FF);
				data[a + 2] = toByte((c >>> 16) & 0x000000FF);
				data[a + 3] = toByte((c >>> 24) & 0x000000FF);
				k++;
				if (k == 446) {
					k = 0;
				}
			}
		}
	}

	private int byteToInt(byte[] ba, int a) {
		int[] temp = new int[4];
		temp[0] = toInt(ba[a]);
		temp[1] = toInt(ba[a + 1]);
		temp[2] = toInt(ba[a + 2]);
		temp[3] = toInt(ba[a + 3]);
		int p = temp[0] | (temp[1] << 8) | (temp[2] << 16) | (temp[3] << 24);
		return p;
	}

	public void checkSums() {
		for (int i = 0; i < 10; i++) {
			int chk = 0;
			for (int a = block[i]; a + 4 <= block[i] + blockSize[i]; a = a + 4) {
				chk = chk + byteToInt(data, a);
			}
			data[block[i] - 8] = toByte(chk & 0x000000FF);
			data[block[i] - 7] = toByte((chk >>> 8) & 0x000000FF);
			data[block[i] - 6] = toByte((chk >>> 16) & 0x000000FF);
			data[block[i] - 5] = toByte((chk >>> 24) & 0x000000FF);
		}
	}

	/*
	 * public void saveData() { try { File f = new File("datadump");
	 * RandomAccessFile out = new RandomAccessFile(f, "rw"); out.write(data);
	 * out.close(); } catch (Exception e) { } }
	 */

	public byte[] getBytes(int i) {
		byte[] bytes = new byte[2];
		bytes[0] = toByte(i & 0xFF);
		bytes[1] = toByte((i >>> 8) & 0xFF);
		return bytes;
	}

	public byte[] getBytesInt(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = toByte(i & 0xFF);
		bytes[1] = toByte((i >>> 8) & 0xFF);
		bytes[2] = toByte((i >>> 16) & 0xFF);
		bytes[3] = toByte((i >>> 24) & 0xFF);
		return bytes;
	}

	public static boolean isPS2pes(String s) {
		if (s.startsWith("BESLES-") && s.endsWith("PES6OPT")) {
			return true;
		}
		if (s.equals("BASLUS-21464W2K7OPT")) {
			return true;
		}
		return false;
	}

	public boolean isWE() {
		if (gameID.equals("BASLUS-21464W2K7OPT") || gameID.equals("PC_WE")) {
			return true;
		}
		return false;
	}

}
