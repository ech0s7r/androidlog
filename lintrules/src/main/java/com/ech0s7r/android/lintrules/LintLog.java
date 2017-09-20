package com.ech0s7r.android.lintrules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author marco.rocco
 */

public class LintLog {

	private static final File LOG_FILE = new File("C:\\Users\\Marco.Rocco\\Downloads\\lint.log");
	private static PrintWriter sPrintWriter;

	public static void log(String txt) {
		if (sPrintWriter == null) {
			try {
				sPrintWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (sPrintWriter != null) {
			sPrintWriter.println(txt);
			sPrintWriter.flush();
		}
	}
}
