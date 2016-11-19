package io.anuke.ucore.noise;


//horribly butchered from libnoiseforjava, please ignore
public class RidgedPerlin{
	static final int X_NOISE_GEN = 1619;
	static final int Y_NOISE_GEN = 31337;
	static final int Z_NOISE_GEN = 6971;
	static final int SEED_NOISE_GEN = 1013;
	static final int SHIFT_NOISE_GEN = 8;
	int octaves;
	int seed;

	float lacunarity = 1.0f;
	double[] spectralWeights = new double[20];

	private static final VectorTable vectorTable = new VectorTable();

	public RidgedPerlin(int seed, int octaves, float falloff) {
		this.octaves = octaves;

		double h = 1.0;

		double frequency = 1.0;
		for(int i = 0; i < spectralWeights.length; i++){
			// Compute weight for each frequency.
			this.spectralWeights[i] = Math.pow(frequency, -h);
			frequency *= lacunarity;
		}
	}

	public float getValue(int x, int y, float frequency){

		double x1 = x;
		double y1 = y;
		double z1 = 0;
		x1 *= frequency;
		y1 *= frequency;
		z1 *= frequency;

		double signal;
		double value = 0.0;
		double weight = 1.0;

		// These parameters should be user-defined; they may be exposed in a
		// future version of libnoise.
		double offset = 1.0;
		double gain = 2.0;

		for(int curOctave = 0; curOctave < octaves; curOctave++){

			// Make sure that these floating-point values have the same range as
			// a 32-
			// bit integer so that we can pass them to the coherent-noise
			// functions.
			double nx, ny, nz;
			nx = MakeInt32Range(x1);
			ny = MakeInt32Range(y1);
			nz = MakeInt32Range(z1);

			// Get the coherent-noise value.
			int seed = (this.seed + curOctave) & 0x7fffffff;
			signal = GradientCoherentNoise3D(nx, ny, nz, seed);

			// Make the ridges.
			signal = Math.abs(signal);
			signal = offset - signal;

			// Square the signal to increase the sharpness of the ridges.
			// noinspection UnusedAssignment
			signal *= signal;

			// The weighting from the previous octave is applied to the signal.
			// Larger values have higher weights, producing sharp points along
			// the
			// ridges.
			signal *= weight;

			// Weight successive contributions by the previous signal.
			weight = signal * gain;
			if(weight > 1.0){
				weight = 1.0;
			}
			if(weight < 0.0){
				weight = 0.0;
			}

			// Add the signal to the output value.
			value += (signal * spectralWeights[curOctave]);

			// Go to the next octave.
			x1 *= lacunarity;
			y1 *= lacunarity;
			z1 *= lacunarity;
		}

		return (float) ((value * 1.25) - 1.0);
	}

	public static double MakeInt32Range(double n){
		if(n >= 1073741824.0)
			return (2.0 * (n % 1073741824.0)) - 1073741824.0;
		else if(n <= -1073741824.0)
			return (2.0 * (n % 1073741824.0)) + 1073741824.0;
		else
			return n;
	}

	public static double GradientCoherentNoise3D(double x, double y, double z, int seed){
		int quality = 2;
		// Create a unit-length cube aligned along an integer boundary. This
		// cube
		// surrounds the input point.
		int x0 = (x > 0.0 ? (int) x : (int) x - 1);
		int x1 = x0 + 1;
		int y0 = (y > 0.0 ? (int) y : (int) y - 1);
		int y1 = y0 + 1;
		int z0 = (z > 0.0 ? (int) z : (int) z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		double xs = 0, ys = 0, zs = 0;
		switch(quality){
		case 0: // fast
			xs = (x - (double) x0);
			ys = (y - (double) y0);
			zs = (z - (double) z0);
			break;
		case 1: // STD
			xs = Interp.SCurve3(x - (double) x0);
			ys = Interp.SCurve3(y - (double) y0);
			zs = Interp.SCurve3(z - (double) z0);
			break;
		case 2: // best
			xs = Interp.SCurve5(x - (double) x0);
			ys = Interp.SCurve5(y - (double) y0);
			zs = Interp.SCurve5(z - (double) z0);
			break;
		}

		// Now calculate the noise values at each vertex of the cube. To
		// generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		double n0, n1, ix0, ix1, iy0, iy1;
		n0 = GradientNoise3D(x, y, z, x0, y0, z0, seed);
		n1 = GradientNoise3D(x, y, z, x1, y0, z0, seed);
		ix0 = Interp.linearInterp(n0, n1, xs);
		n0 = GradientNoise3D(x, y, z, x0, y1, z0, seed);
		n1 = GradientNoise3D(x, y, z, x1, y1, z0, seed);
		ix1 = Interp.linearInterp(n0, n1, xs);
		iy0 = Interp.linearInterp(ix0, ix1, ys);
		n0 = GradientNoise3D(x, y, z, x0, y0, z1, seed);
		n1 = GradientNoise3D(x, y, z, x1, y0, z1, seed);
		ix0 = Interp.linearInterp(n0, n1, xs);
		n0 = GradientNoise3D(x, y, z, x0, y1, z1, seed);
		n1 = GradientNoise3D(x, y, z, x1, y1, z1, seed);
		ix1 = Interp.linearInterp(n0, n1, xs);
		iy1 = Interp.linearInterp(ix0, ix1, ys);

		return Interp.linearInterp(iy0, iy1, zs);
	}

	public static double GradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed){
		// Randomly generate a gradient vector given the integer coordinates of
		// the
		// input value. This implementation generates a random number and uses
		// it
		// as an index into a normalized-vector lookup table.
		int vectorIndex = (X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN * seed);

		vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
		vectorIndex &= 0xff;

		double xvGradient = vectorTable.getRandomVectors(vectorIndex, 0);
		double yvGradient = vectorTable.getRandomVectors(vectorIndex, 1);
		double zvGradient = vectorTable.getRandomVectors(vectorIndex, 2);
		// array size too large when using this original, changed to above for
		// all 3
		// double zvGradient = vectorTable.getRandomVectors(vectorIndex << 2,
		// 2);

		// Set up us another vector equal to the distance between the two
		// vectors
		// passed to this function.
		double xvPoint = (fx - (double) ix);
		double yvPoint = (fy - (double) iy);
		double zvPoint = (fz - (double) iz);

		// Now compute the dot product of the gradient vector with the distance
		// vector. The resulting value is gradient noise. Apply a scaling value
		// so that this noise value ranges from -1.0 to 1.0.
		return ((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) * 2.12;
	}

}
