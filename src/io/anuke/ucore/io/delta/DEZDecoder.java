/*
 * Copyright (C) 2015 Michael Zucchi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.anuke.ucore.io.delta;

import java.io.IOException;

/**
 * An in-memory DeltaZ-1 decoder.
 * <p>
 * Transforms a source and patch into a target.
 * <p>
 */
public class DEZDecoder {
	/**Cached result byte array. This is so no extra memory is allocated every single decode.*/
	private byte[] target;
	/**Lenght of decoded target array.*/
	private int decodeLength;

	private byte[] patch;
	private byte[] source;
	private int pi, si;

	/**Call this function before decoding anything.*/
	public void init(byte[] src, byte[] patch){
		this.patch = patch;
		this.source = src;
		pi = 0;
		si = 0;
	}

	private int decodeInt() {
		int v = 0;
		byte b;
		int limit = Math.min(patch.length, pi + 5);

		do {
			b = patch[pi++];
			v = (v << 7) | (b & 0x7f);
		} while (pi < limit && (b & 0x80) != 0);

		return v;
	}

	/**On entry pi points to the opcode, which is also in 'op'.*/
	private int decodeLength(int op) {
		int length = op & 0x1f;

		pi++;
		while ((op & 0x80) != 0) {
			op = patch[pi++];
			length = (length << 7) | (op & 0x7f);
		}
		return length;
	}

	/**Recreates the original target data from the source and patch.
	 * Returns the same byte array each time. Only part of the array is used! Retrieve the used length
	 * by calling {@link #getDecodedLength()}.*/
	public byte[] decode() throws IOException {
		int ti = 0;

		pi = 0;
		si = 0;

		// 'decode' magic
		for (int i = 0; i < DEZEncoder.MAGIC.length; i++)
			if (patch[i] != DEZEncoder.MAGIC[i])
				throw new IOException("Invalid magic");

		pi += 4;
		// 'decode' flags
		if (patch[pi] != 0)
			throw new IOException("Unknown flags");
		pi += 1;

		// get sizes
		int sourceSize = decodeInt();
		int targetSize = decodeInt();

		if (sourceSize != source.length)
			throw new IOException("Patch/source size mismatch");

		//resize cached result byte array if it's too small
		if(target.length < targetSize) {
			target = new byte[targetSize];
		}

		decodeLength = targetSize;

		//Decode loop.
		//Since java will check the array accesses anyway, don't clutter the code with our own.
		while (ti < targetSize) {
			byte op = patch[pi];
			byte r;

			if ((op & 0x40) == 0) {
				// COPY
				int length = decodeInt();
				int addr = decodeInt();

				if (addr < sourceSize)
					for (int i = 0; i < length; i++)
						target[ti++] = source[addr + i];
				else
					for (int i = 0; i < length; i++)
						target[ti++] = target[addr - sourceSize + i];
			} else if ((op & 0x20) == 0) {
				// ADD
				int length = decodeLength(op);

				for (int i = 0; i <= length; i++)
					target[ti++] = patch[pi++];
			} else {
				// RUN
				int length = decodeLength(op);

				r = patch[pi++];
				for (int i = 0; i <= length; i++)
					target[ti++] = r;
			}
		}

		return target;
	}

	/**Returns decoded length of array.*/
	public int getDecodedLength(){
		return decodeLength;
	}

}
