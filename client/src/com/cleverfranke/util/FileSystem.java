package com.cleverfranke.util;

import java.io.File;
import java.util.Locale;

import com.cleverfranke.util.FileSystem.SystemHelper.Architecture;

public class FileSystem {
	
	/**
	 * Fetch absolute path to a file or directory relative to the application path. Does not check whether the 
	 * requested path exists 
	 * 
	 * @param path relative to the project directory or executable JAR (starting with file separator)
	 * @return absolute path to the requested subpath
	 */
	public static String getApplicationPath(String subPath) {
		
		String applicationPath = new File("").getAbsolutePath();
		
		if (subPath.isEmpty()) {
			return applicationPath;
		} else {
			return new File(applicationPath + File.separator + subPath).getAbsolutePath();
		}
		
	}
	
	/**
	 * Fetch root application path (e.g. path to the
	 * 
	 * @return
	 */
	public static String getApplicationPath() {
		return getApplicationPath("");
	}
	
	/**
	 * Set the library paths to the default locations (/lib/{libraryName})
	 */
	public static void setDefaultLibraryPaths() {

		try {

			// Fetch system configuration
			SystemHelper.OperatingSystem os = SystemHelper.getOperatingSystem();
			SystemHelper.Architecture arch = SystemHelper.getArchitecture();

			// Determine the correct default library paths
			String javaLibraryPath = "";
			String gstreamerLibraryPath = "";
			String gstreamerPluginPath = "";
			switch (os) {
			case Windows:
				if (arch == Architecture.X64) {
					javaLibraryPath = "lib/processing/serial/windows64";
					gstreamerLibraryPath = "lib/processing/video/windows64";
					gstreamerPluginPath = "lib/processing/video/windows64/plugins";
				} else if (arch == Architecture.X64) {
					javaLibraryPath = "lib/processing/serial/windows32";
					gstreamerLibraryPath = "lib/processing/video/windows32";
					gstreamerPluginPath = "lib/processing/video/windows32/plugins";
				} else {
					throw new Exception("Only x64 and x32 is supported on Windows");
				}
				break;
			case MacOS:
				if (arch == Architecture.X64) {
					javaLibraryPath = "lib/processing/serial/macosx";
					gstreamerLibraryPath = "lib/processing/video/macosx64";
					gstreamerPluginPath = "lib/processing/video/macosx64/plugins";
				} else {
					throw new Exception("Only x64 is supported on OSX/macOS");
				}
				break;
			default:
				throw new Exception("Only Windows and OSX/macOS platforms are currently supported");

			}
			
			// Set library paths
			System.setProperty("java.library.path", FileSystem.getApplicationPath(javaLibraryPath));
			System.setProperty("gstreamer.library.path", FileSystem.getApplicationPath(gstreamerLibraryPath));
			System.setProperty("gstreamer.plugin.path", FileSystem.getApplicationPath(gstreamerPluginPath));
			
		} catch (Exception e) {
			System.err.println("Failed to set library paths: " + e.getMessage());
		}

	}
	/**
	 * Small class that helps determining the current operating system and
	 * JVM architecture
	 */
	public static final class SystemHelper {
		
		/**
		 * Operating systems
		 */
		public enum OperatingSystem {
			Windows, MacOS, Linux, Other
		};
		
		/**
		 * Architecture types
		 */
		public enum Architecture {
			X32, X64, Other
		};

		/**
		 * Cached result of OS detection
		 */
		private static OperatingSystem detectedOS;
		
		/**
		 * Cached result of architecture
		 */
		private static Architecture detectedArchitecture;

		/**
		 * Detect the operating system from the os.name System property and cache the
		 * result
		 * 
		 * @returns The operating system type detected
		 */
		public static OperatingSystem getOperatingSystem() {
			if (detectedOS == null) {
				String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
				if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
					detectedOS = OperatingSystem.MacOS;
				} else if (OS.indexOf("win") >= 0) {
					detectedOS = OperatingSystem.Windows;
				} else if (OS.indexOf("nux") >= 0) {
					detectedOS = OperatingSystem.Linux;
				} else {
					detectedOS = OperatingSystem.Other;
				}
			}
			return detectedOS;
		}
		
		/**
		 * Detect the JVM architecture from the sun.arch.data.model property and cache
		 * the result
		 * 
		 * @return The JVM architecture
		 */
		public static Architecture getArchitecture() {
			if (detectedArchitecture == null) {
				if (System.getProperty("sun.arch.data.model").equals("64")) {
					detectedArchitecture = Architecture.X64;
				} else if (System.getProperty("sun.arch.data.model").equals("32")) {
					detectedArchitecture = Architecture.X32;
				} else {
					detectedArchitecture = Architecture.Other;
				}
			}
			return detectedArchitecture;
		}
		
	}
	
}
