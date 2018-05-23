package com.cleverfranke.util;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

public class ConfigurationLoader {

	private static Configuration configuration = null;
	
	public static boolean loadSettings(String settingsPath) {
		
		// Skip if settings already loaded
		if (configuration != null) {
			System.err.println("Settings already loaded, ignoring call");
			return false;
		}
		
		try {
			
			// Load Settings instance from properties file
			File propertiesFile = new File(settingsPath);
			System.out.println("Loading settings from '" + propertiesFile.getAbsolutePath() + "'");

			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
					PropertiesConfiguration.class)
							.configure(params.fileBased()
									.setFile(propertiesFile));
			configuration = builder.getConfiguration();
			
			return true;

		} catch (Exception e) {

			// Loading failes
			System.err.println(e.getMessage());
			configuration = null;
			
			return false;

		}
		
	}

	/**
	 * Retrieve the configuration
	 * 
	 * @return
	 */
	public static Configuration get() {
		return configuration;

	}

}
