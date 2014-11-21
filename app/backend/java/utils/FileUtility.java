package backend.java.utils;

/*
 * Created by Jackson Woodruff on 21/11/2014 
 *
 * This is a class with a few helper methods to make
 * dealing with the smaller files easier and more
 * concise
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtility {
	public static void copyFile(File src, File destination) {


		try {
			if (!destination.exists()) {
				destination.createNewFile();
			}
			FileChannel in = new FileInputStream(src).getChannel();
			FileChannel out = new FileOutputStream(destination).getChannel();
			out.transferFrom(in, 0, in.size());
		} catch (IOException e) {
			e.printStackTrace();

		}
	}
}
