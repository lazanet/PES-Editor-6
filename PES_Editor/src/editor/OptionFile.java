/*
 * Copyright 2008-9 Compulsion
 * <pes_compulsion@yahoo.co.uk>
 * <http://www.purplehaze.eclipse.co.uk/>
 * <http://uk.geocities.com/pes_compulsion/>
 *
 * file is part of PES Editor.
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
import java.io.RandomAccessFile;
import java.util.zip.CRC32;

public class OptionFile {
	public static final int LENGTH = 1191936;

	public byte[] data = new byte[LENGTH];

	private byte[] headerData;

	public String fileName;

	public String gameID;

	 private static final byte[] sharkport = { 13, 0, 0, 0, 83, 104, 97, 114, 107, 80, 111, 114, 116, 83, 97, 118, 101 };

	private final static String magicMax = "Ps2PowerSave";

	String gameName;

	String saveName;

	String notes;

	static final int[] block = { 12, 5144, 9544, 14288, 37116, 657956, 751472, 763804, 911144, 1170520 };
  static final int[] blockSize = { 4844, 1268, 4730, 22816, 620000, 93501, 12320, 147328, 259364, 21032 };

	private static final int[] key = { 2058578050, 2058578078, 2058578109,
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

	private static final byte[] keyPC = { 115, 96, -31, -58, 31, 60, -83, 66, 11, 88, -71, -2, 55, -76, 5, -6, -93, 80, -111, 54, 79, 44, 93, -78, 59, 72, 105, 110, 103, -92, -75, 106, -45, 64, 65, -90, Byte.MAX_VALUE, 28, 13, 34, 107, 56, 25, -34, -105, -108, 101, -38, 3, 48, -15, 22, -81, 12, -67, -110, -101, 40, -55, 78, -57, -124, 21, 74, 51, 32, -95, -122, -33, -4, 109, 2, -53, 24, 121, -66, -9, 116, -59, -70, 99, 16, 81, -10, 15, -20, 29, 114, -5, 8, 41, 46, 39, 100, 117, 42, -109, 0, 1, 102, 63, -36, -51, -30, 43, -8, -39, -98, 87, 84, 37, -102, -61, -16, -79, -42, 111, -52, 125, 82, 91, -24, -119, 14, -121, 68, -43, 10, -13, -32, 97, 70, -97, -68, 45, -62, -117, -40, 57, 126, -73, 52, -123, 122, 35, -48, 17, -74, -49, -84, -35, 50, -69, -56, -23, -18, -25, 36, 53, -22, 83, -64, -63, 38, -1, -100, -115, -94, -21, -72, -103, 94, 23, 20, -27, 90, -125, -80, 113, -106, 47, -116, 61, 18, 27, -88, 73, -50, 71, 4, -107, -54, -77, -96, 33, 6, 95, 124, -19, -126, 75, -104, -7, 62, 119, -12, 69, 58, -29, -112, -47, 118, -113, 108, -99, -14, 123, -120, -87, -82, -89, -28, -11, -86, 19, Byte.MIN_VALUE, -127, -26, -65, 92, 77, 98, -85, 120, 89, 30, -41, -44, -91, 26, 67, 112, 49, 86, -17, 76, -3, -46, -37, 104, 9, -114, 7, -60, 85, -118 };
	
	byte format = -1;

	int fileCount;

  
  public boolean readXPS(File of)
  {
    format = -1;
    boolean done = true;
    String extension = PESUtils.getExtension(of);
    try
    {
	RandomAccessFile in = new RandomAccessFile(of, "r");
	if ((extension != null) && (extension.equals("xps")))
	{
	  long offSet = in.length() - LENGTH - 4;
	  in.seek(21);
	  int size = PESUtils.swabInt(in.readInt());
	  byte[] temp2 = new byte[size];
	  in.read(temp2);
	  gameName = new String(temp2, "ISO-8859-1");
	  size = PESUtils.swabInt(in.readInt());
	  temp2 = new byte[size];
	  in.read(temp2);
	  saveName = new String(temp2, "ISO-8859-1");
	  size = PESUtils.swabInt(in.readInt());
	  temp2 = new byte[size];
	  in.read(temp2);
	  notes = new String(temp2, "ISO-8859-1");
	  headerData = new byte[(int)offSet - 33 - gameName.length() - saveName.length() - notes.length()];
	  in.read(headerData);
	  gameID = new String(headerData, 6, 19);
	  format = 0;
	}
	else if ((extension != null) && (extension.equals("psu")))
	{
	  headerData = new byte[(int)(in.length() - LENGTH)];
	  in.read(headerData);
	  gameID = new String(headerData, 64, 19);
	  format = 2;
	}
	else if ((extension != null) && (extension.equals("max")))
	{
	  byte[] temp1 = new byte[(int)in.length()];
	  in.read(temp1);
	  String magic = new String(temp1, 0, 12, "ISO-8859-1");
	  if (magic.equals("Ps2PowerSave"))
	  {
	    int chk = byteToInt(temp1, 12);
	    temp1[12] = 0;
	    temp1[13] = 0;
	    temp1[14] = 0;
	    temp1[15] = 0;
	    CRC32 crc = new CRC32();
	    crc.update(temp1);
	    if ((int)crc.getValue() == chk)
	    {
		temp1 = new byte[32];
		in.seek(16L);
		in.read(temp1);
		gameID = new String(temp1, "ISO-8859-1");
		gameID = gameID.replace("\000", "");
		in.read(temp1);
		gameName = new String(temp1, "ISO-8859-1");
		gameName = gameName.replace("\000", "");
		int codeSize = PESUtils.swabInt(in.readInt());
		fileCount = PESUtils.swabInt(in.readInt());
		temp1 = new byte[codeSize];
		in.read(temp1);
		Lzari lz = new Lzari();
		temp1 = lz.decodeLzari(temp1);
		int p = 0;
		for (int n = 0; (n < fileCount) && (format != 3); n++)
		{
		  int size = byteToInt(temp1, p);
		  String title = new String(temp1, p + 4, 19, "ISO-8859-1");
		  if ((size == LENGTH) && (title.equals(gameID)))
		  {
		    p += 36;
		    headerData = new byte[p];
		    System.arraycopy(temp1, 0, headerData, 0, p);
		    System.arraycopy(temp1, p, data, 0, LENGTH);
		    format = 3;
		  }
		  else
		  {
		    p = p + 36 + size;
		    p = (p + 23) / 16 * 16 - 8;
		  }
		}
		if (format != 3) {
		  done = false;
		}
	    }
	    else
	    {
		done = false;
	    }
	  }
	  else
	  {
	    done = false;
	  }
	}
	else
	{
	  if (of.getName().equals("KONAMI-WIN32WEXUOPT"))
	  	gameID = "PC_WE";
	  else
	  	gameID = "PC_PES";

	  format = 1;
	}

	if ((done) && (format != -1))
	{
	  if (format != 3)
	  	in.read(data);
	  
	  if (format == 1)
	  	convertData();

	  decrypt();
	}
	in.close();
    }
    catch (Exception localException)
    {
	localException.printStackTrace();
	done = false;
    }
    
    fileName = (done)?of.getName():null;
    return done;
  }
  
  private void convertData()
  {
    int i = 0;
    for (int j = 0; j < LENGTH; j++)
    {
	data[j] = ((byte)(data[j] ^ keyPC[i]));
	i=(i+1)%256;
    }
  }
  
  public boolean saveXPS(File of)
  {
    data[45] = 1;
    data[46] = 1;
    data[5938] = 1;
    data[5939] = 1;
    boolean done = true;
    encrypt();
    checkSums();
    if (format == 1)
	convertData();
   
    try
    {
	RandomAccessFile out = new RandomAccessFile(of, "rw");
	
	if (format == 0)
	{
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

	if (format == 2)
	  out.write(headerData);
	
	if (format == 3)
	{
	  int textSize = headerData.length + LENGTH;
	  textSize = (textSize + 23) / 16 * 16 - 8;
	  byte[] temp1 = new byte[textSize];
	  System.arraycopy(headerData, 0, temp1, 0, headerData.length);
	  System.arraycopy(data, 0, temp1, headerData.length, data.length);
	  Lzari lz = new Lzari();
	  temp1 = lz.encodeLzari(temp1);
	  int codeSize = temp1.length;
	  byte[] temp2 = new byte[88];
	  System.arraycopy("Ps2PowerSave".getBytes("ISO-8859-1"), 0, temp2, 0, "Ps2PowerSave".length());
	  System.arraycopy(gameID.getBytes("ISO-8859-1"), 0, temp2, 16, 19);
	  System.arraycopy(gameName.getBytes("ISO-8859-1"), 0, temp2, 48, gameName.length());
	  System.arraycopy(getBytesInt(codeSize), 0, temp2, 80, 4);
	  System.arraycopy(getBytesInt(fileCount), 0, temp2, 84, 4);
	  CRC32 chk = new CRC32();
	  chk.update(temp2);
	  chk.update(temp1);
	  System.arraycopy(getBytesInt((int)chk.getValue()), 0, temp2, 12, 4);
	  out.write(temp2);
	  out.write(temp1);
	}
	else
		out.write(data);
	
	if (format == 0)
	{
	  out.write(0);
	  out.write(0);
	  out.write(0);
	  out.write(0);
	}
	out.close();
    }
    catch (Exception localException)
    {
	localException.printStackTrace();
	done = false;
    }
    if (format == 1) {
	convertData();
    }
    decrypt();
    return done;
  }
  
  public byte toByte(int paramInt)
  {
    return (byte) ((paramInt > 127)?(paramInt-256):paramInt);
  }
  
  public int toInt(byte paramByte)
  {
    return (paramByte<0)?paramByte+256:paramByte;
  }
  
  private void decrypt()
  {
    for (int i = 1; i < 10; i++)
    {
	int j = 0;
	for (int k = block[i]; k + 4 <= block[i] + blockSize[i]; k += 4)
	{
	  int m = byteToInt(data, k);
	  int n = m - key[j] + 2058577996 ^ 0x7AB3684C;
	  data[k] = toByte(n & 0xFF);
	  data[(k + 1)] = toByte(n >>> 8 & 0xFF);
	  data[(k + 2)] = toByte(n >>> 16 & 0xFF);
	  data[(k + 3)] = toByte(n >>> 24 & 0xFF);
	  j=(j+1)%446;
	}
    }
  }
  
  private void encrypt()
  {
    for (int i = 1; i < 10; i++)
    {
	int j = 0;
	for (int k = block[i]; k + 4 <= block[i] + blockSize[i]; k += 4)
	{
	  int m = byteToInt(data, k);
	  int n = key[j] + ((m ^ 0x7AB3684C) - 2058577996);
	  data[k] = toByte(n & 0xFF);
	  data[(k + 1)] = toByte(n >>> 8 & 0xFF);
	  data[(k + 2)] = toByte(n >>> 16 & 0xFF);
	  data[(k + 3)] = toByte(n >>> 24 & 0xFF);
	  j=(j+1)%446;
	}
    }
  }
  
  private int byteToInt(byte[] paramtemp, int paramInt)
  {
    int[] arrayOfInt = new int[4];
    arrayOfInt[0] = toInt(paramtemp[paramInt]);
    arrayOfInt[1] = toInt(paramtemp[(paramInt + 1)]);
    arrayOfInt[2] = toInt(paramtemp[(paramInt + 2)]);
    arrayOfInt[3] = toInt(paramtemp[(paramInt + 3)]);
    int i = arrayOfInt[0] | arrayOfInt[1] << 8 | arrayOfInt[2] << 16 | arrayOfInt[3] << 24;
    return i;
  }
  
  public void checkSums()
  {
    for (int i = 0; i < block.length; i++)
    {
	int chk = 0;
	for (int k = block[i]; k + 4 <= block[i] + blockSize[i]; k += 4)
		chk += byteToInt(data, k);
	data[(block[i] - 8)] = toByte(chk & 0xFF);
	data[(block[i] - 7)] = toByte(chk >>> 8 & 0xFF);
	data[(block[i] - 6)] = toByte(chk >>> 16 & 0xFF);
	data[(block[i] - 5)] = toByte(chk >>> 24 & 0xFF);
    }
  }
  
	public byte[] getBytes(int i) 
	{
		byte[] bytes = new byte[2];
		bytes[0] = toByte(i & 0xFF);
		bytes[1] = toByte((i >>> 8) & 0xFF);
		return bytes;
	}

	public byte[] getBytesInt(int i) 
	{
		byte[] bytes = new byte[4];
		bytes[0] = toByte(i & 0xFF);
		bytes[1] = toByte((i >>> 8) & 0xFF);
		bytes[2] = toByte((i >>> 16) & 0xFF);
		bytes[3] = toByte((i >>> 24) & 0xFF);
		return bytes;
	}
  
  public static boolean isPS2pes(String paramString)
  {
    if ((paramString.startsWith("BESLES-")) && (paramString.endsWith("PES6OPT")))
    	return true;
    
    return paramString.equals("BASLUS-21464W2K7OPT");
  }
  
  public boolean isWE()
  {
    return (gameID.equals("BASLUS-21464W2K7OPT")) || (gameID.equals("PC_WE"));
  }
}
