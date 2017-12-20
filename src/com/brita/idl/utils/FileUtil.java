package com.brita.idl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	public static boolean mkdirs(File file) {
		
		if (file.exists()) {
			
			if (file.isFile()) {
				file.delete();
				return file.mkdirs();
			}
			
		}else{
			return file.mkdirs();
		}
		
		return true;
	}
	
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File child : files) {
					deleteFile(child);
				}
			}else {
				file.delete();
			}
			
		}
	}
	
	public static boolean createFile(File file) {
		
		if (file.exists()) {		
			deleteFile(file);
		}
		
		try {
			
			File parent = file.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
			
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean writeFile(String text, File outputFile) {
		FileWriter fileWriter = null;
		try {
			
			if (!createFile(outputFile)) {
				return false;
			}
			
			fileWriter = new FileWriter(outputFile);
			fileWriter.write(text);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			IOUtil.close(fileWriter);
		}
		
		return false;
	}
	
	public static String readFile(File inputFile) {
		BufferedReader fileReader = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			
			fileReader = new BufferedReader(new FileReader(inputFile));
			String line;
			while ((line = fileReader.readLine()) !=null) {
				stringBuilder.append(line);
				stringBuilder.append("\r\n");
			}
			
			return stringBuilder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			IOUtil.close(fileReader);
		}
		
		return null;
	}
	
}
