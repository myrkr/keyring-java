/*
 * Implements a dialog for setting user preferences.
 */

package com.otisbean.keyring.gui;

import javax.swing.JDialog;
import java.awt.Frame;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * This class implements the preferences dialog.
 *
 * @author Torsten Hilbrich
 */
public class PreferenceDialog
	extends JDialog
	implements ActionListener, PropertyChangeListener
{
	private static final long serialVersionUID = 1L;
	private Prop prefs;
	private Frame frame;
	private JTextField defaultURL;
	private JSpinner passwordTimeout;
	private JCheckBox informAboutSave;
	private JCheckBox confirmDeletion;
	private JCheckBox deleteEmptyCategories;
	private JCheckBox allowPasswordCopy;
	private JOptionPane optionPane;

	private String btnSave = "Save";
	private String btnAbort = "Cancel";

	private boolean cancelled = false;

	public PreferenceDialog(Frame frame, Prop prefs) {
		super(frame, "User Preferences", true);

		this.frame = frame;
		this.prefs = prefs;

		String defaultURLString = "URL for remote DB access";
		this.defaultURL = new JTextField(20);
		this.defaultURL.setText(prefs.getDefaultURL());
		this.defaultURL.setToolTipText("If set, this http URL will be used as default for remote loading and saving.");

		int value = prefs.getPasswordTimeout();
		int min = 10;
		int max = 600;
		int step = 10;
		SpinnerNumberModel passwordTimeoutModel = new SpinnerNumberModel(value, min, max, step);
		String passwordTimeoutString = "Password timeout (seconds)";
		this.passwordTimeout = new JSpinner(passwordTimeoutModel);
		this.passwordTimeout.setToolTipText("After this number of seconds the password expires and you need to unlock the program first.");

		this.informAboutSave = new JCheckBox("Inform about each save operation",
			prefs.getInformAboutSave());
		this.informAboutSave.setToolTipText("If disabled, you will not informed about save operations when modifying entries.");

		this.confirmDeletion = new JCheckBox("Confirm deletions",
			prefs.getConfirmDeletion());
		this.confirmDeletion.setToolTipText("If enabled, all delete operations requires an addition confirmation.");

		this.deleteEmptyCategories = new JCheckBox("Delete empty categories",
			prefs.getDeleteEmptyCategories());
		this.deleteEmptyCategories.setToolTipText("If enabled, categories without entries are removed while saving.");

		this.allowPasswordCopy = new JCheckBox("Allow password copy",
			prefs.getAllowPasswordCopy());
		this.allowPasswordCopy.setToolTipText("If set, you will be allowed to copy the curent password to clipboard.");

		Object array[] = { defaultURLString, defaultURL,
		                   passwordTimeoutString, passwordTimeout,
				   informAboutSave,
				   confirmDeletion,
				   deleteEmptyCategories,
				   allowPasswordCopy
		};
		Object options[] = { btnSave, btnAbort };
		this.optionPane = new JOptionPane(array,
			JOptionPane.PLAIN_MESSAGE,
			JOptionPane.YES_NO_OPTION,
			null,
			options,
			options[0]);
		setContentPane(optionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
			/*
			 * Instead of directly closing the window,
			 * we're going to change the JOptionPane's
			 * value property.
			 */
			 optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				defaultURL.requestFocusInWindow();
			}
		});

		optionPane.addPropertyChangeListener(this);
	}

	public boolean getCancelled() {
		return cancelled;
	}

	public void actionPerformed(ActionEvent e) {
	}

		public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
			&& (e.getSource() == optionPane)
			&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
				JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {

			Object value = optionPane.getValue();

			if(value == JOptionPane.UNINITIALIZED_VALUE) {
				//ignore reset
				return;
			}

			//Reset the JOptionPane's value.
			//If you don't do this, then if the user
			//presses the same button next time, no
			//property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if(btnSave.equals(value)) {
				// perform save
				String url = defaultURL.getText();
				if (! url.isEmpty() && ! url.startsWith("http://")) {
					JOptionPane.showMessageDialog(PreferenceDialog.this,
							"URL must start with http:// if given\n",
							"Error", JOptionPane.ERROR_MESSAGE);
					cancelled = true;
					defaultURL.requestFocusInWindow();
					return;
				}
				// the spinner should prevent any wrong input
				Integer timeout = (Integer)passwordTimeout.getValue();
				prefs.setDefaultURL(url);
				prefs.setPasswordTimeout(timeout.intValue());
				prefs.setInformAboutSave(informAboutSave.isSelected());
				prefs.setConfirmDeletion(confirmDeletion.isSelected());
				prefs.setDeleteEmptyCategories(deleteEmptyCategories.isSelected());
				prefs.setAllowPasswordCopy(allowPasswordCopy.isSelected());
			} else {
				// cancel
				cancelled = true;
			}

			setVisible(false);
		}
	}
}
