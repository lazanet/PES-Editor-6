package editor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Lzari {

	/***************************************************************************
	 * LZARI.C -- A Data Compression Program (tab = 4 spaces)
	 * ************************************************************** 4/7/1989
	 * Haruhiko Okumura Use, distribute, and modify this program freely. Please
	 * send me your improved versions. PC-VAN SCIENCE NIFTY-Serve PAF01022
	 * CompuServe 74050,1022
	 **************************************************************************/

	/** ******** Bit I/O ********* */

	ByteArrayInputStream infile;

	ByteArrayOutputStream outfile;

	int textsize = 0, codesize = 0, printcount = 0;

	int pBuffer = 0, pMask = 128;

	int gBuffer = 0, gMask = 0;

	void putBit(int bit) /* Output one bit (bit = 0,1) */
	{
		if (bit != 0)
			pBuffer |= pMask;
		if ((pMask >>>= 1) == 0) {
			outfile.write(pBuffer);
			pBuffer = 0;
			pMask = 128;
			codesize++;
		}
	}

	void flushBitBuffer() /* Send remaining bits */
	{
		for (int i = 0; i < 7; i++)
			putBit(0);
	}

	int getBit() /* Get one bit (0 or 1) */
	{
		if ((gMask >>>= 1) == 0) {
			gBuffer = infile.read();
			gMask = 128;
		}
		if ((gBuffer & gMask) != 0) {
			return 1;
		} else {
			return 0;
		}
		// return ((gBuffer & gMask) != 0);
	}

	/** ******** LZSS with multiple binary trees ********* */

	int N = 4096; /* size of ring buffer */

	int F = 60; /* upper limit for match_length */

	int THRESHOLD = 2; /*
						 * encode string into position and length if
						 * match_length is greater than this
						 */

	int NIL = N; /* index for root of binary search trees */

	int[] text_buf = new int[N + F - 1]; /*
											 * ring buffer of size N, with extra
											 * F-1 bytes to facilitate string
											 * comparison
											 */

	int match_position, match_length; /*
										 * of longest match. These are set by
										 * the InsertNode() procedure.
										 */

	int[] lson = new int[N + 1];

	int[] rson = new int[N + 257];

	int[] dad = new int[N + 1]; /*
								 * left & right children & parents -- These
								 * constitute binary search trees.
								 */

	void initTree() /* Initialize trees */
	{
		int i;
		/*
		 * For i = 0 to N - 1, rson[i] and lson[i] will be the right and left
		 * children of node i. These nodes need not be initialized. Also, dad[i]
		 * is the parent of node i. These are initialized to NIL (= N), which
		 * stands for 'not used.' For i = 0 to 255, rson[N + i + 1] is the root
		 * of the tree for strings that begin with character i. These are
		 * initialized to NIL. Note there are 256 trees.
		 */

		for (i = N + 1; i <= N + 256; i++)
			rson[i] = NIL; /* root */
		for (i = 0; i < N; i++)
			dad[i] = NIL; /* node */
	}

	void insertNode(int r)
	/*
	 * Inserts string of length F, text_buf[r..r+F-1], into one of the trees
	 * (text_buf[r]'th tree) and returns the longest-match position and length
	 * via the global variables match_position and match_length. If match_length =
	 * F, then removes the old node in favor of the new one, because the old one
	 * will be deleted sooner. Note r plays double role, as tree node and
	 * position in buffer.
	 */
	{
		int i, p, cmp, temp;

		cmp = 1;

		p = N + 1 + text_buf[r + 0];
		rson[r] = lson[r] = NIL;
		match_length = 0;
		for (;;) {
			if (cmp >= 0) {
				if (rson[p] != NIL)
					p = rson[p];
				else {
					rson[p] = r;
					dad[r] = p;
					return;
				}
			} else {
				if (lson[p] != NIL)
					p = lson[p];
				else {
					lson[p] = r;
					dad[r] = p;
					return;
				}
			}
			for (i = 1; i < F; i++)
				if ((cmp = text_buf[r + i] - text_buf[p + i]) != 0)
					break;
			if (i > THRESHOLD) {
				if (i > match_length) {
					match_position = (r - p) & (N - 1);
					if ((match_length = i) >= F)
						break;
				} else if (i == match_length) {
					if ((temp = (r - p) & (N - 1)) < match_position)
						match_position = temp;
				}
			}
		}
		dad[r] = dad[p];
		lson[r] = lson[p];
		rson[r] = rson[p];
		dad[lson[p]] = r;
		dad[rson[p]] = r;
		if (rson[dad[p]] == p)
			rson[dad[p]] = r;
		else
			lson[dad[p]] = r;
		dad[p] = NIL; /* remove p */
	}

	void deleteNode(int p) /* Delete node p from tree */
	{
		int q;

		if (dad[p] == NIL)
			return; /* not in tree */
		if (rson[p] == NIL)
			q = lson[p];
		else if (lson[p] == NIL)
			q = rson[p];
		else {
			q = lson[p];
			if (rson[q] != NIL) {
				do {
					q = rson[q];
				} while (rson[q] != NIL);
				rson[dad[q]] = lson[q];
				dad[lson[q]] = dad[q];
				lson[q] = lson[p];
				dad[lson[p]] = q;
			}
			rson[q] = rson[p];
			dad[rson[p]] = q;
		}
		dad[q] = dad[p];
		if (rson[dad[p]] == p)
			rson[dad[p]] = q;
		else
			lson[dad[p]] = q;
		dad[p] = NIL;
	}

	/** ******** Arithmetic Compression ********* */

	/*
	 * If you are not familiar with arithmetic compression, you should read I.
	 * E. Witten, R. M. Neal, and J. G. Cleary, Communications of the ACM, Vol.
	 * 30, pp. 520-540 (1987), from which much have been borrowed.
	 */

	int M = 15;

	/*
	 * Q1 (= 2 to the M) must be sufficiently large, but not so large as the
	 * unsigned long 4 * Q1 * (Q1 - 1) overflows.
	 */

	int Q1 = (1 << M);

	int Q2 = (2 * Q1);

	int Q3 = (3 * Q1);

	int Q4 = (4 * Q1);

	int MAX_CUM = (Q1 - 1);

	int N_CHAR = (256 - THRESHOLD + F);

	/* character code = 0, 1, ..., N_CHAR - 1 */

	long low = 0, high = Q4, value = 0;

	int shifts = 0; /* counts for magnifying low and high around Q2 */

	int[] char_to_sym = new int[N_CHAR];

	int[] sym_to_char = new int[N_CHAR + 1];

	long[] sym_freq = new long[N_CHAR + 1]; /* frequency for symbols */

	long[] sym_cum = new long[N_CHAR + 1]; /* cumulative freq for symbols */

	long[] position_cum = new long[N + 1]; /* cumulative freq for positions */

	void startModel() /* Initialize model */
	{
		int ch, sym, i;

		sym_cum[N_CHAR] = 0;
		for (sym = N_CHAR; sym >= 1; sym--) {
			ch = sym - 1;
			char_to_sym[ch] = sym;
			sym_to_char[sym] = ch;
			sym_freq[sym] = 1;
			sym_cum[sym - 1] = sym_cum[sym] + sym_freq[sym];
		}
		sym_freq[0] = 0; /* sentinel (!= sym_freq[1]) */
		position_cum[N] = 0;
		for (i = N; i >= 1; i--)
			position_cum[i - 1] = position_cum[i] + 10000 / (i + 200);
		/* empirical distribution function (quite tentative) */
		/* Please devise a better mechanism! */
	}

	void updateModel(int sym) {
		int i, c, ch_i, ch_sym;

		if (sym_cum[0] >= MAX_CUM) {
			c = 0;
			for (i = N_CHAR; i > 0; i--) {
				sym_cum[i] = c;
				c += (sym_freq[i] = (sym_freq[i] + 1) >> 1);
			}
			sym_cum[0] = c;
		}
		for (i = sym; sym_freq[i] == sym_freq[i - 1]; i--)
			;
		if (i < sym) {
			ch_i = sym_to_char[i];
			ch_sym = sym_to_char[sym];
			sym_to_char[i] = ch_sym;
			sym_to_char[sym] = ch_i;
			char_to_sym[ch_i] = sym;
			char_to_sym[ch_sym] = i;
		}
		sym_freq[i]++;
		while (--i >= 0)
			sym_cum[i]++;
	}

	void output(int bit) /* Output 1 bit, followed by its complements */
	{
		putBit(bit);
		for (; shifts > 0; shifts--) {
			if (bit == 0)
				putBit(1);
			else
				putBit(0);
		}
	}

	void encodeChar(int ch) {
		int sym;
		long range;

		sym = char_to_sym[ch];
		range = high - low;
		high = low + (range * sym_cum[sym - 1]) / sym_cum[0];
		low += (range * sym_cum[sym]) / sym_cum[0];
		for (;;) {
			if (high <= Q2)
				output(0);
			else if (low >= Q2) {
				output(1);
				low -= Q2;
				high -= Q2;
			} else if (low >= Q1 && high <= Q3) {
				shifts++;
				low -= Q1;
				high -= Q1;
			} else
				break;
			low += low;
			high += high;
		}
		updateModel(sym);
	}

	void encodePosition(int position) {
		long range;

		range = high - low;
		high = low + (range * position_cum[position]) / position_cum[0];
		low += (range * position_cum[position + 1]) / position_cum[0];
		for (;;) {
			if (high <= Q2)
				output(0);
			else if (low >= Q2) {
				output(1);
				low -= Q2;
				high -= Q2;
			} else if (low >= Q1 && high <= Q3) {
				shifts++;
				low -= Q1;
				high -= Q1;
			} else
				break;
			low += low;
			high += high;
		}
	}

	void encodeEnd() {
		shifts++;
		if (low < Q1)
			output(0);
		else
			output(1);
		flushBitBuffer(); /* flush bits remaining in buffer */
	}

	int BinarySearchSym(long x)
	/*
	 * 1 if x >= sym_cum[1], N_CHAR if sym_cum[N_CHAR] > x, i such that
	 * sym_cum[i - 1] > x >= sym_cum[i] otherwise
	 */
	{
		int i, j, k;

		i = 1;
		j = N_CHAR;
		while (i < j) {
			k = (i + j) / 2;
			if (sym_cum[k] > x)
				i = k + 1;
			else
				j = k;
		}
		return i;
	}

	int binarySearchPos(long x)
	/*
	 * 0 if x >= position_cum[1], N - 1 if position_cum[N] > x, i such that
	 * position_cum[i] > x >= position_cum[i + 1] otherwise
	 */
	{
		int i, j, k;

		i = 1;
		j = N;
		while (i < j) {
			k = (i + j) / 2;
			if (position_cum[k] > x)
				i = k + 1;
			else
				j = k;
		}
		return i - 1;
	}

	void startDecode() {
		for (int i = 0; i < M + 2; i++) {
			int b = getBit();
			//System.out.println(b);
			value = 2 * value + b;
		}
		//System.out.println(value);
	}

	int decodeChar() {
		int sym, ch;
		long range;

		range = high - low;
		sym = BinarySearchSym((long) (((value - low + 1) * sym_cum[0] - 1) / range));
		high = low + (range * sym_cum[sym - 1]) / sym_cum[0];
		low += (range * sym_cum[sym]) / sym_cum[0];
		for (;;) {
			if (low >= Q2) {
				value -= Q2;
				low -= Q2;
				high -= Q2;
			} else if (low >= Q1 && high <= Q3) {
				value -= Q1;
				low -= Q1;
				high -= Q1;
			} else if (high > Q2)
				break;
			low += low;
			high += high;
			value = 2 * value + getBit();
		}
		ch = sym_to_char[sym];
		updateModel(sym);
		return ch;
	}

	int decodePosition() {
		int position;
		long range;

		range = high - low;
		position = binarySearchPos((long) (((value - low + 1) * position_cum[0] - 1) / range));
		high = low + (range * position_cum[position]) / position_cum[0];
		low += (range * position_cum[position + 1]) / position_cum[0];
		for (;;) {
			if (low >= Q2) {
				value -= Q2;
				low -= Q2;
				high -= Q2;
			} else if (low >= Q1 && high <= Q3) {
				value -= Q1;
				low -= Q1;
				high -= Q1;
			} else if (high > Q2)
				break;
			low += low;
			high += high;
			value = 2 * value + getBit();
		}
		return position;
	}

	/** ******** Encode and Decode ********* */

	void encode() {
		int i, c, len, r, s, last_match_length;
		textsize = infile.available();
		//System.out.println("textsize" + textsize);
		writeTextSize(); /* output size of text */
		codesize += 4;
		if (textsize == 0)
			return;
		textsize = 0;
		startModel();
		initTree();
		s = 0;
		r = N - F;
		for (i = s; i < r; i++)
			text_buf[i] = ' ';
		for (len = 0; len < F && (c = infile.read()) != -1; len++)
			text_buf[r + len] =  c;
		textsize = len;
		for (i = 1; i <= F; i++)
			insertNode(r - i);
		insertNode(r);
		do {
			if (match_length > len)
				match_length = len;
			if (match_length <= THRESHOLD) {
				match_length = 1;
				encodeChar(text_buf[r]);
			} else {
				encodeChar(255 - THRESHOLD + match_length);
				encodePosition(match_position - 1);
			}
			last_match_length = match_length;
			for (i = 0; i < last_match_length && (c = infile.read()) != -1; i++) {
				deleteNode(s);
				text_buf[s] =  c;
				if (s < F - 1)
					text_buf[s + N] =  c;
				s = (s + 1) & (N - 1);
				r = (r + 1) & (N - 1);
				insertNode(r);
			}
			if ((textsize += i) > printcount) {
				//System.out.println("textsize" + textsize);
				printcount += 1024;
			}
			while (i++ < last_match_length) {
				deleteNode(s);
				s = (s + 1) & (N - 1);
				r = (r + 1) & (N - 1);
				if (--len != 0)
					insertNode(r);
			}
		} while (len > 0);
		encodeEnd();
		//System.out.println("In : %lu bytes\n" + textsize);
		//System.out.println("Out: %lu bytes\n" + codesize);
		//System.out.println("Out/In: %.3f\n" + codesize / textsize);
	}

	void decode() {
		int i, j, k, r, c;
		int count;

		readTextSize(); /* read size of text */
		//System.out.println("textsize" + textsize);
		if (textsize == 0)
			return;
		startDecode();
		startModel();
		for (i = 0; i < N - F; i++)
			text_buf[i] = ' ';
		r = N - F;
		for (count = 0; count < textsize;) {
			c = decodeChar();
			//if (count < 150)
				//System.out.println(count + " - " + c);
			if (c < 256) {
				
				outfile.write(c);
				text_buf[r++] =  c;
				r &= (N - 1);
				count++;
			} else {
				i = (r - decodePosition() - 1) & (N - 1);
				j = c - 255 + THRESHOLD;
				//System.out.println("i = " + i);
				//System.out.println("j = " + j);
				for (k = 0; k < j; k++) {
					c = text_buf[(i + k) & (N - 1)];
					//if (count < 14 + 50)
					//System.out.println("c = " + c);
					outfile.write(c);
					text_buf[r++] =  c;
					r &= (N - 1);
					count++;
				}
			}
			if (count > printcount) {
				//System.out.println(count);
				printcount += 1024;
			}
		}
		//System.out.println(count);
	}

	void readTextSize() {
		int[] temp = new int[4];
		temp[0] = infile.read();
		temp[1] = infile.read();
		temp[2] = infile.read();
		temp[3] = infile.read();
		textsize = temp[0] | (temp[1] << 8) | (temp[2] << 16) | (temp[3] << 24);
	}

	void writeTextSize() {
		outfile.write(textsize & 0xFF);
		outfile.write((textsize >>> 8) & 0xFF);
		outfile.write((textsize >>> 16) & 0xFF);
		outfile.write((textsize >>> 24) & 0xFF);
	}
	
	byte[] encodeLzari(byte[] data) {
		infile = new ByteArrayInputStream(data);
		outfile = new ByteArrayOutputStream();
		encode();
		return outfile.toByteArray();
	}
	
	byte[] decodeLzari(byte[] data) {
		infile = new ByteArrayInputStream(data);
		outfile = new ByteArrayOutputStream();
		decode();
		return outfile.toByteArray();
	}

/*	public static void main(String[] args) {
		try {
			RandomAccessFile in = new RandomAccessFile("text", "r");
			byte[] data = new byte[(int)in.length()];
			in.read(data);
			in.close();
			Lzari lz = new Lzari();
			RandomAccessFile out = new RandomAccessFile("a", "rw");
			out.write(lz.encodeLzari(data));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
