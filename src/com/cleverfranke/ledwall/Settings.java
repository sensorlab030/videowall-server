package com.cleverfranke.ledwall;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import processing.data.JSONObject;

public final class Settings {
	
	// Actual settings
	private static Settings singleton = null;
	private boolean settingsLoaded = false;
	private HashMap<String, String> values = new HashMap<String, String>();
	
	// Data containers loaded via settings
	private static synchronized Settings getInstance() {
		if (singleton == null) {
			singleton = new Settings();
		}
		return singleton;
	}
	
	public static boolean loadSettings(String filename) {
		
		Settings instance = getInstance();
		
		try {
			
			System.out.println("Loading settings from '" + filename + "'");
			
			// Only load settings once
			if (instance.settingsLoaded) {
				throw new Exception("Settings already loaded");
			}
			
			if (!(new File(filename)).exists()) {
				throw new Exception("File does not exist");
			}
			
			// Read JSON file
			byte[] encoded = Files.readAllBytes(Paths.get(filename));
			String jsonString = new String(encoded, StandardCharsets.UTF_8);
			JSONObject json = JSONObject.parse(jsonString);
			
			// Loop over keys
			@SuppressWarnings("unchecked")
			String[] keys = (String[]) json.keys().toArray(new String[json.size()]);
			for (String key: keys) {
				instance.setValue(key, json.getString(key));
			}
			
			// Set loaded flag
			instance.settingsLoaded = true;
			return true;
			
		} catch (Exception e) {
			System.out.println("WARN: Error loading settings from file '" + filename + "': " + e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * Set the value for a key
	 * 
	 * @param setting
	 * @param value
	 */
	private void setValue(String setting, String value) {
		values.put(setting, value);
	}
	
	/**
	 * Get the value for a key, or NULL if key does not exist
	 * 
	 * @param setting
	 * @return
	 */
	public static String getValue(String setting) {
		return getValue(setting, null);
	}
	
	/**
	 * Get the value for a key, or <defaultValue> if key does not exist
	 *  
	 * @param setting
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String setting, String defaultValue) {
		Settings instance = getInstance();
		if (instance.values.containsKey(setting)) {
			return instance.values.get(setting);
		} else {
			return defaultValue;
		}
	}
	
}
