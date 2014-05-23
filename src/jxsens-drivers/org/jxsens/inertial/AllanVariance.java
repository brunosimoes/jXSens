package org.jxsens.inertial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jxsens.events.INSEvent;
import org.jxsens.logging.LogItem;
import org.jxsens.util.MathUtils;

public class AllanVariance {

	private ArrayList<INSEvent> mEventCache;
	private double mFirstEventTime;
	private double mPeriod;
	private int mCount;

	public AllanVariance() {
		mEventCache = new ArrayList<INSEvent>();
		mCount = 0;
	}

	public void computeStabilities(String s) {
		try {
			BufferedReader bufferedreader = new BufferedReader(new FileReader(s));
			for (int i = 0; i < 10000; i++) {
				bufferedreader.readLine();
			}

			for (int j = 0; j < 0x3d0900; j++) {
				String s1 = bufferedreader.readLine();
				if (s1 == null) {
					break;
				}
				try {
					INSEvent insevent = new INSEvent();
					LogItem logitem = new LogItem(s1);
					insevent.fromLogItem(logitem);
					mEventCache.add(insevent);
					if (mCount == 0) {
						mFirstEventTime = insevent.getTime();
					}
					else if (mCount == 1) {
						mPeriod = insevent.getTime() - mFirstEventTime;
					}
					mCount++;
					continue;
				} catch (Exception exception1) {
					System.err.println(exception1);
				}
				System.exit(0);
			}

			bufferedreader.close();
			System.out.println(mEventCache.size());
			computeStabilitiesHelperAccel();
			computeStabilitiesHelperGyro();
		} catch (Exception exception) {
			exception.printStackTrace();
			System.exit(1);
		}
	}

	private void computeStabilitiesHelperGyro() throws Exception {
		double ad[] = new double[mEventCache.size()];
		double ad1[] = new double[mEventCache.size()];
		double ad2[] = new double[mEventCache.size()];
		for (int i = 0; i < mEventCache.size(); i++) {
			ad[i] = mEventCache.get(i).getSensorAngularVelocity()[0];
			ad1[i] = mEventCache.get(i).getSensorAngularVelocity()[1];
			ad2[i] = mEventCache.get(i).getSensorAngularVelocity()[2];
		}

		PrintWriter out = new PrintWriter("AD_Gyro.log");
		for (double d = -4D; d <= 8.4000000000000004D; d += 0.074999999999999997D) {
			double d1 = Math.exp(d);
			out.println("" + d1 + "\t" +
						computeAllanVariance(ad, (int) (d1 * (1.0D / mPeriod))) + "\t" +
						computeAllanVariance(ad1, (int) (d1 * (1.0D / mPeriod))) + "\t" +
						computeAllanVariance(ad2, (int) (d1 * (1.0D / mPeriod))) + "\t" );
		}
		out.close();
	}

	private void computeStabilitiesHelperAccel() throws Exception {{
			double ad[] = new double[mEventCache.size()];
			double ad1[] = new double[mEventCache.size()];
			double ad2[] = new double[mEventCache.size()];
			for (int i = 0; i < mEventCache.size(); i++) {
				ad[i] = mEventCache.get(i).getSensorAccel()[0];
				ad1[i] = mEventCache.get(i).getSensorAccel()[1];
				ad2[i] = mEventCache.get(i).getSensorAccel()[2];
			}

			PrintWriter out = new PrintWriter("AD_Accel.log");
			try {
				for (double d = -4D; d <= 8.4000000000000004D; d += 0.0050000000000000001D) {
					double d1 = Math.exp(d);
					out.println("" + d1 + "\t" +
								computeAllanVariance(ad,(int) (d1 * (1.0D / mPeriod))) + "\t" +
								computeAllanVariance(ad1, (int) (d1 * (1.0D / mPeriod)))+ "\t" +
								computeAllanVariance(ad2,(int) (d1 * (1.0D / mPeriod))) + "\t");
				}
			}
			catch (Exception exception) {
				out.close();
			}
			finally {
				out.close();
			}
			out.close();
		}
	}

	private double computeAllanVariance(double ad[], int i) throws Exception {
		int j = ad.length / i;
		if (j < 9) {
			throw new Exception(
					"Not enough data. There must be enough sample data for at least 9 windows.");
		}
		double ad1[] = new double[j];
		for (int k = 0; k < j; k++) {
			ad1[k] = MathUtils.mean(ad, k * i, i);
		}

		double d = 0.0D;
		for (int l = 0; l < j - 1; l++) {
			d += Math.pow(ad1[l + 1] - ad1[l], 2D);
		}

		d /= 2 * (j - 1);
		return Math.sqrt(d);
	}

	public static void main(String args[]) {
		AllanVariance allanvariance = new AllanVariance();
		allanvariance.computeStabilities(args[0]);
	}
}
