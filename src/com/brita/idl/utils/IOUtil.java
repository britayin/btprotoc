package com.brita.idl.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
	
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
