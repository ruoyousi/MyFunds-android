package com.ruoyousi.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.os.Environment;

public final class BackupUtil {

	private BackupUtil() {

	}

	public static String getExternalFilePath(String fileName)
			throws IOException {
		File sdCardDir = Environment.getExternalStorageDirectory();
		return sdCardDir.getCanonicalPath() + fileName;
	}

	public static String readExternalFile(String fileName)
			throws FileNotFoundException, IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			FileInputStream fis = new FileInputStream(
					getExternalFilePath(fileName));
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			} finally {
				if (fis != null)
					fis.close();
			}
		}
		return null;
	}

	public static void writeExternalFile(String fileName, String content)
			throws IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File targetFile = new File(getExternalFilePath(fileName));
			if (targetFile.exists())
				targetFile.delete();
			File parentFile = targetFile.getParentFile();
			if (!parentFile.exists())
				parentFile.mkdirs();
			RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
			raf.setLength(0);
			try {
				raf.seek(0);
				raf.write(content.getBytes());
			} finally {
				if (raf != null)
					raf.close();
			}
		}
	}

	public static String[] listFiles(String filePath) throws IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File targetFile = new File(getExternalFilePath(filePath));
			if (targetFile.exists() && targetFile.isDirectory()) {
				File[] files = targetFile.listFiles();

				String[] filenames = new String[files.length];
				for (int i = 0, j = files.length; i < j; i++) {
					filenames[i] = files[i].getName();
				}
				return filenames;
			}

		}
		return new String[0];
	}

	public static void removeFile(String fileName) throws IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File targetFile = new File(getExternalFilePath(fileName));
			if (targetFile.isFile()) {
				targetFile.delete();
			}

		}
	}
}