package org.jxsens.util;

import Jama.Matrix;

public class MathUtils {

	public static double normalDensityFunction(double d, double d1, double d2) {
		double d3 = 1.0D / (d1 * Math.sqrt(6.2831853071795862D));
		d3 *= Math.exp(-((d2 - d) * (d2 - d)) / (2D * d1 * d1));
		return d3;
	}

	public static double mean(double ad[]) {
		return mean(ad, 0, ad.length);
	}

	public static double mean(double ad[], int i, int j) {
		double d = 0.0D;
		for (int k = i; k < j + i; k++)
			d += ad[k];

		return d / (double) j;
	}

	public static double variance(double ad[]) {
		return variance(ad, 0, ad.length);
	}

	public static double variance(double ad[], int i, int j) {
		double d = mean(ad, i, j);
		double d1 = 0.0D;
		for (int k = i; k < j + i; k++)
			d1 += ((ad[k] - d) * (ad[k] - d)) / ((double) j - 1.0D);

		return d1;
	}

	public static double distance(float af[], float af1[]) {
		if (af.length != af1.length)
			throw new IllegalArgumentException();
		float f = 0.0F;
		for (int i = 0; i < af.length; i++)
			f += (af[i] - af1[i]) * (af[i] - af1[i]);

		return Math.sqrt(f);
	}

	public static double distance(double ad[], double ad1[]) {
		if (ad.length != ad1.length)
			throw new IllegalArgumentException();
		double d = 0.0D;
		for (int i = 0; i < ad.length; i++)
			d += (ad[i] - ad1[i]) * (ad[i] - ad1[i]);

		return Math.sqrt(d);
	}

	public static double length(double ad[]) {
		double d = 0.0D;
		for (int i = 0; i < ad.length; i++)
			d += ad[i] * ad[i];

		return Math.sqrt(d);
	}

	public static double length(float af[]) {
		double d = 0.0D;
		for (int i = 0; i < af.length; i++)
			d += af[i] * af[i];

		return Math.sqrt(d);
	}

	public static double dotProduct(double ad[], double ad1[]) {
		double d = 0.0D;
		for (int i = 0; i < ad.length; i++)
			d += ad[i] * ad1[i];

		return d;
	}

	public static double[] crossProd(double ad[], double ad1[]) {
		double ad2[] = { ad[1] * ad1[2] - ad[2] * ad1[1], ad[2] * ad1[0] - ad[0] * ad1[2], ad[0] * ad1[1] - ad[1] * ad1[0] };
		return ad2;
	}

	public static double[] normalize(double ad[]) {
		double ad1[] = new double[ad.length];
		for (int i = 0; i < ad.length; i++)
			ad1[i] = ad[i] / length(ad);

		return ad1;
	}

	public static double[] matrixTimesVector(Matrix matrix, double ad[]) {
		Matrix matrix1 = new Matrix(ad, ad.length);
		Matrix matrix2 = matrix.times(matrix1);
		return matrix2.getColumnPackedCopy();
	}

	public static Matrix arbitraryRotation(double d, double ad[]) {
		double d1 = 1.0D - Math.cos(d);
		double d2 = Math.cos(d);
		double d3 = Math.sin(d);
		double ad1[] = { d1 * ad[0] * ad[0] + d2, d1 * ad[0] * ad[1] + d3 * ad[2], d1 * ad[0] * ad[2] - d3 * ad[1], d1 * ad[0] * ad[1] - d3 * ad[2], d1 * ad[1] * ad[1] + d2,
				d1 * ad[1] * ad[2] + d3 * ad[0], d1 * ad[0] * ad[2] + d3 * ad[1], d1 * ad[1] * ad[2] - d3 * ad[0], d1 * ad[2] * ad[2] + d2 };
		Matrix matrix = new Matrix(ad1, 3);
		return matrix;
	}
}