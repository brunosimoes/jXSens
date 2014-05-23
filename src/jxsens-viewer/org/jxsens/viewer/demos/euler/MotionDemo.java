package org.jxsens.viewer.demos.euler;

import com.sun.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.comm.PortInUseException;
import javax.comm.UnsupportedCommOperationException;
import javax.media.opengl.GLCanvas;
import javax.swing.*;

import org.jxsens.*;
import org.jxsens.decoders.*;
import org.jxsens.viewer.jogl.LinesPointsStrip;
import org.jxsens.viewer.jogl.SimpleCameraController;
import org.jxsens.viewer.jogl.SimpleEngine;
import org.jxsens.viewer.jogl.Sphere;
import org.jxsens.wrappers.MtxSerialPortWrapper;

public class MotionDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	private float mGravity;
	private NavigationEvent liveaccel;
	private MtxDevice mDevice;
	private MtxSerialPortWrapper mPortWrapper;
	private DecodeMtxToEuler mAttitudeBridge;
	private FPSAnimator mVisualisationAnimator;
	private GLCanvas mCanvas;
	private SimpleEngine mVisualisationEngine;
	private ArrayList<double[]> mSamples;
	private LinesPointsStrip mSamplePoints;
	private JPanel mCPane;
	private JPanel mDevicePanel;
	private JComboBox mPortCombo;
	private JLabel mDeviceLabel;
	private JLabel mFirmwareLabel;
	private JComboBox mBaudCombo;
	private JButton mConnectButton;
	private JLabel mFwLabel;
	private JLabel mIdLabel;
	private JPanel mLogPanel;
	private JButton mSampleButton;
	private JButton mClearButton;
	
	public MotionDemo() {
		mGravity = 9.81F;
		mAttitudeBridge = new DecodeMtxToEuler();
		mSamples = new ArrayList<double[]>();
		mSamplePoints = new LinesPointsStrip();
		mCPane = null;
		mDevicePanel = null;
		mPortCombo = null;
		mDeviceLabel = null;
		mFirmwareLabel = null;
		mBaudCombo = null;
		mConnectButton = null;
		mFwLabel = null;
		mIdLabel = null;
		mLogPanel = null;
		mSampleButton = null;
		mClearButton = null;
		initialize();
	}

	private void initialize() {
		setContentPane(getCp());
		setTitle("jXsens Wrapper");
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setLocationRelativeTo(null);
		addWindowStateListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowevent) {
				if (mPortWrapper != null)
					try {
						mPortWrapper.close();
					} catch (Exception exception) {
					}
				mDevice = null;
			}
		});
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowevent) {
				mVisualisationAnimator.stop();
			}

			@Override
			public void windowIconified(WindowEvent windowevent) {
				mVisualisationAnimator.stop();
			}

			@Override
			public void windowDeiconified(WindowEvent windowevent) {
				mVisualisationAnimator.start();
			}
		});
	}

	private JPanel getCp() {
		if (mCPane == null) {
			mCPane = new JPanel();
			mCPane.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = 2;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(3, 3, 3, 3);
			mCPane.add(getDevicePanel(), gbc);
			gbc.gridy = 1;
			mCPane.add(getSamplingPanel(), gbc);
			gbc.gridy = 2;
			gbc.weighty = 1.0D;
			gbc.fill = 3;
			gbc.gridy = 3;
			mCPane.add(new JPanel(), gbc);
			gbc.fill = 1;
			gbc.weightx = 1.0D;
			gbc.weighty = 1.0D;
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridheight = 4;
			mCPane.add(getVisWindow(), gbc);
			gbc.fill = 2;
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.weighty = 0.0D;
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
		}
		return mCPane;
	}

	private JPanel getDevicePanel() {
		if (mDevicePanel == null) {
			mDevicePanel = new JPanel();
			mDevicePanel.setBorder(BorderFactory.createTitledBorder("1. Device"));
			mDevicePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.insets = new Insets(3, 3, 3, 3);
			gbc.anchor = 13;
			mDevicePanel.add(new JLabel("Port :"), gbc);
			gbc.gridy = 4;
			mDevicePanel.add(new JLabel("Baud Rate :"), gbc);
			gbc.gridy = 6;
			mIdLabel = new JLabel("Device Id :");
			mDevicePanel.add(mIdLabel, gbc);
			gbc.gridy = 7;
			mFwLabel = new JLabel("Firmware :");
			mDevicePanel.add(mFwLabel, gbc);
			gbc.anchor = 17;
			gbc.gridx = 1;
			gbc.gridy = 5;
			mDevicePanel.add(getConnectButton(), gbc);
			gbc.gridy = 4;
			mDevicePanel.add(getBaudCombo(), gbc);
			gbc.gridy = 2;
			mDevicePanel.add(getPortCombo(), gbc);
			mFirmwareLabel = new JLabel();
			gbc.gridy = 6;
			mDevicePanel.add(mFirmwareLabel, gbc);
			mDeviceLabel = new JLabel();
			gbc.gridy = 7;
			mDevicePanel.add(mDeviceLabel, gbc);
		}
		return mDevicePanel;
	}

	private JComboBox getPortCombo() {
		if (mPortCombo == null) {
			mPortCombo = new JComboBox();
			mPortCombo.setPreferredSize(new Dimension(150, 24));
			// ArrayList arraylist = SerialPortHelper.getAllPortNames();
			// for (int i = 0; i < arraylist.size(); i++)
			// mPortCombo.addItem(arraylist.get(i));
			mPortCombo.addItem("COM4");

		}
		return mPortCombo;
	}

	private JComboBox getBaudCombo() {
		if (mBaudCombo == null) {
			mBaudCombo = new JComboBox();
			mBaudCombo.setPreferredSize(new Dimension(150, 24));
			mBaudCombo.addItem("921600");
			mBaudCombo.addItem("460800");
			mBaudCombo.addItem("230400");
			mBaudCombo.addItem("115200");
			mBaudCombo.addItem("75800");
			mBaudCombo.addItem("57600");
			mBaudCombo.addItem("38400");
			mBaudCombo.addItem("28800");
			mBaudCombo.addItem("19200");
			mBaudCombo.addItem("14400");
			mBaudCombo.addItem("9600");
			mBaudCombo.setSelectedIndex(3);
		}
		return mBaudCombo;
	}

	private JButton getConnectButton() {
		if (mConnectButton == null) {
			mConnectButton = new JButton();
			mConnectButton.setText("Connect");
			mConnectButton.setPreferredSize(new Dimension(150, 20));
			mConnectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionevent) {
					if (mDevice == null)
						connectToDevice();
					else
						disconnect();
				}
			});
		}
		return mConnectButton;
	}

	private GLCanvas getVisWindow() {
		if (mCanvas == null) {
			mCanvas = new GLCanvas();
			mCanvas.setPreferredSize(new Dimension(400, 400));
			SimpleCameraController simplecameracontroller = new SimpleCameraController();
			mVisualisationEngine = new SimpleEngine();
			mCanvas.addGLEventListener(mVisualisationEngine);
			mCanvas.addKeyListener(simplecameracontroller);
			mCanvas.addMouseWheelListener(simplecameracontroller);
			mCanvas.addMouseMotionListener(simplecameracontroller);
			mVisualisationEngine.setCameraController(simplecameracontroller);
			mVisualisationAnimator = new FPSAnimator(40);
			mVisualisationAnimator.add(mCanvas);
			mVisualisationAnimator.start();
			mSamplePoints.setDrawLines(false);
			mVisualisationEngine.addShape(new Sphere(new float[] { 0.0F, 0.0F, 0.0F }, new float[] { mGravity, mGravity, mGravity }, new float[] { 0.5F, 0.5F, 0.5F, 0.3F }));
			liveaccel = new NavigationEvent();
			mVisualisationEngine.addShape(liveaccel);
			mVisualisationEngine.addShape(mSamplePoints);
			mAttitudeBridge.addListener(liveaccel);
			simplecameracontroller.setCameraDistance(11F);
		}
		return mCanvas;
	}

	public NavigationEvent getNavigationData() {
		return liveaccel;
	}

	private JPanel getSamplingPanel() {
		if (mLogPanel == null) {
			mLogPanel = new JPanel();
			mLogPanel.setBorder(BorderFactory.createTitledBorder("2. Sampling"));
			mLogPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			gridbagconstraints.insets = new Insets(3, 3, 3, 3);
			gridbagconstraints.anchor = 13;
			gridbagconstraints.gridx = 0;
			gridbagconstraints.gridy = 0;
			gridbagconstraints.weightx = 1.0D;
			mLogPanel.add(getSampleButton(), gridbagconstraints);
			gridbagconstraints.gridy = 1;
			mLogPanel.add(getClearButton(), gridbagconstraints);
		}
		return mLogPanel;
	}

	private JButton getSampleButton() {
		if (mSampleButton == null) {
			mSampleButton = new JButton();
			mSampleButton.setPreferredSize(new Dimension(150, 25));
			mSampleButton.setText("Take Sample");
			mSampleButton.setEnabled(false);
			mSampleButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent actionevent) {
					takeSample();
				}

			});
		}
		return mSampleButton;
	}

	private JButton getClearButton() {
		if (mClearButton == null) {
			mClearButton = new JButton();
			mClearButton.setPreferredSize(new Dimension(150, 25));
			mClearButton.setText("Clear Samples");
			mClearButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent actionevent) {
					mSamples.clear();
					mSamplePoints.clearPoints();
				}

			});
		}
		return mClearButton;
	}

	private void disconnect() {
		try {
			mConnectButton.setText("Connect");
			mSampleButton.setEnabled(false);
			mDeviceLabel.setText("");
			mFirmwareLabel.setText("");
			if (mDevice != null) {
				mDevice.clearListeners();
				mDevice = null;
			}
			if (mPortWrapper != null) {
				mPortWrapper.close();
				mPortWrapper = null;
			}
		} catch (Exception exception) {
		}
	}

	private void connectToDevice() {
		try {
			mPortWrapper = new MtxSerialPortWrapper((String) mPortCombo.getSelectedItem(), Integer.parseInt((String) mBaudCombo.getSelectedItem()));
			mPortWrapper.open();
			mDevice = new MtxDevice(mPortWrapper);
			mDeviceLabel.setText(mDevice.getFirmwareRevision());
			mFirmwareLabel.setText(mDevice.requestDeviceId());
			mDevice.setPeriod(1152);
			mDevice.setOutputSkipFactor(0);
			mConnectButton.setText("Disconnect");
			mDevice.setConfigMode();
			mDevice.clearListeners();

			mDevice.setOutputMode(MtxDevice.ORIENTATION_OUTPUT_MODE);
			mDevice.setOutputSettings(MtxDevice.EULER_OUTPUT_SETTINGS);

			mDevice.addListener(mAttitudeBridge);
			mDevice.setMeasurementMode();
			mSampleButton.setEnabled(true);
		} catch (UnsupportedCommOperationException unsupportedcommoperationexception) {
			JOptionPane.showMessageDialog(this, "An error occured connecting to the serial port:\nUnsupported baud rate", "Error", 0);
			disconnect();
		} catch (PortInUseException portinuseexception) {
			JOptionPane.showMessageDialog(this, "An error occured connecting to the serial port:\nPort in use", "Error", 0);
			disconnect();
		} catch (MtxCommException mtxcommexception) {
			JOptionPane.showMessageDialog(this, "Error communicating with Xsens device:\nYou probably specified incorrect serial port settings!", "Error", 0);
			disconnect();
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(this, "An connecting to device:\nUnspecified error", "Error", 0);
			disconnect();
		}
	}

	private void takeSample() {
		if (mDevice != null) {
			mSampleButton.setText("Sampling...");
			mSampleButton.setEnabled(false);
			Thread thread = new Thread() {

				@Override
				public void run() {
					MtxListener genericlistener = new MtxListener() {
						
						public void newEvent(Object obj, MtxNavigationEvent attitudeevent) {}

						public void newEvent(Object obj, Object obj1) {
							newEvent(obj, (MtxNavigationEvent) obj1);
						}

					};
					synchronized (genericlistener) {
						mAttitudeBridge.addListener(genericlistener);
						try {
							genericlistener.wait();
							mSampleButton.setText("Take Sample");
							mSampleButton.setEnabled(true);
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				}

			};
			thread.start();
		}
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MotionDemo accelfitter = new MotionDemo();
				accelfitter.setVisible(true);
			}
		});
	}

}