/*
 * @author Dirk Bergstrom
 *
 * Keyring Desktop Client - Easy password management on your phone or desktop.
 * Copyright (C) 2009-2010, Dirk Bergstrom, keyring@otisbean.com
 * 
 * Adapted from KeyringEditor v1.1
 * Copyright 2006 Markus Griessnig
 * http://www.ict.tuwien.ac.at/keyring/
 * Markus graciously gave his assent to release the modified code under the GPLv3.
 *     
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.otisbean.keyring.gui;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * This class is used to load parameters from the file keyringeditor.ini.
 */
public class Prop {
	// ----------------------------------------------------------------
	// variables
	// ----------------------------------------------------------------
	private Preferences prefs;

	public int getPasswordTimeout() {
		return prefs.getInt("PasswordTimeout", 60);
	}

	public void setPasswordTimeout(int timeout) {
		if (timeout <= 0 || timeout == 60)
			prefs.remove("PasswordTimeout");
		else
			prefs.putInt("PasswordTimeout", timeout);
	}

	public String getDefaultURL() {
		return prefs.get("DefaultURL", "");
	}

	public void setDefaultURL(String url) {
		if (url == null || url.isEmpty())
			prefs.remove("DefaultURL");
		else
			prefs.put("DefaultURL", url);
	}

	public boolean getConfirmDeletion() {
		return prefs.getBoolean("ConfirmDeletion", true);
	}

	public void setConfirmDeletion(boolean value) {
		prefs.putBoolean("ConfirmDeletion", value);
	}

	// ----------------------------------------------------------------
	// constructor
	// ----------------------------------------------------------------
	/**
	 * Default constructor.
	 *
	 */
	public Prop() {
		this.prefs = Preferences.userNodeForPackage(getClass());
	}
}
