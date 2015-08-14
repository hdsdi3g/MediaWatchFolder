/*
 * This file is part of MyDMAM.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * Copyright (C) hdsdi3g for hd3g.tv 14 août 2015
 * 
*/
package hd3gtv.mediawatchfolder;

import hd3gtv.log2.Log2;
import hd3gtv.log2.Log2Dump;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileDb {
	
	/**
	 * In bytes, 0 to disable.
	 */
	private static long min_free_space = Long.parseLong(System.getProperty("min_free_space", "1000"));
	
	/**
	 * In msec.
	 */
	private static long time_to_wait_stopped_growing_file = Long.parseLong(System.getProperty("time_to_wait_growing_file", "1000"));
	
	private File file;
	private long last_length;
	private long last_modified;
	private long last_checked;
	
	private FileDb(File file) throws IOException {
		if (file.canRead() == false) {
			throw new IOException("Can't read this file: " + file.getAbsolutePath());
		}
		this.file = file;
		last_checked = System.currentTimeMillis();
		last_modified = file.lastModified();
		last_length = file.length();
	}
	
	public boolean ifThisFileisGrowing() {
		boolean file_has_growing = false;
		
		if (file.lastModified() != last_modified | file.length() != last_length) {
			file_has_growing = true;
		}
		
		if (file_has_growing == false & min_free_space > 0) {
			if (file.getFreeSpace() < min_free_space) {
				return true;
			}
		}
		
		last_modified = file.lastModified();
		last_length = file.length();
		
		if (file_has_growing == true) {
			last_checked = System.currentTimeMillis();
			return true;
		}
		
		if (last_checked + time_to_wait_stopped_growing_file < System.currentTimeMillis()) {
			file_has_growing = false;
		}
		
		last_checked = System.currentTimeMillis();
		
		if (file_has_growing) {
			Log2.log.debug("This file has stopped to grow, wait the time to validate", new Log2Dump("file", file));
		}
		
		return file_has_growing;
	}
	
	public int hashCode() {
		return file.hashCode();
	}
	
	/**
	 * START STATIC ZONE
	 */
	
	private static Map<File, FileDb> items;
	
	static {
		items = Collections.synchronizedMap(new HashMap<File, FileDb>());
	}
	
	public static void put(File file) throws IOException, NullPointerException {
		if (exists(file) == false) {
			items.put(file, new FileDb(file));
		}
	}
	
	public static FileDb get(File file) throws NullPointerException {
		if (exists(file)) {
			return items.get(file);
		}
		return null;
	}
	
	public static boolean exists(File file) throws NullPointerException {
		if (file == null) {
			throw new NullPointerException("\"file\" can't to be null");
		}
		return items.containsKey(file);
	}
	
	public static void clean() {
		if (items.size() == 0) {
			return;
		}
		ArrayList<File> old_entries = new ArrayList<File>(items.size());
		for (Map.Entry<File, FileDb> entry : items.entrySet()) {
			if (entry.getKey().exists() == false) {
				old_entries.add(entry.getKey());
			}
		}
		for (int pos = 0; pos < old_entries.size(); pos++) {
			items.remove(old_entries.get(pos));
		}
		Log2.log.debug("Remove old entries", new Log2Dump("count", old_entries.size()));
	}
}
