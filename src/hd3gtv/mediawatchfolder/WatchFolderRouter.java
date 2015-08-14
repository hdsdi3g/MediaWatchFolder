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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

public class WatchFolderRouter {
	
	/**
	 * In ms
	 */
	private long time_to_sleep_between_scans = Long.parseLong(System.getProperty("time_to_sleep_between_scans", "100"));
	
	private File source_directory = new File(System.getProperty("source_directory", "."));
	
	private FileFoundEvent file_found_event;
	
	/**
	 * throw fatal init
	 */
	public WatchFolderRouter() throws Exception {
		if (source_directory.exists() == false) {
			throw new FileNotFoundException(source_directory + " don't exists");
		}
		if (source_directory.isDirectory() == false) {
			throw new FileNotFoundException(source_directory + " is not a valid directory");
		}
		file_found_event = new FileFoundEvent();
		Log2.log.debug("Init watchfolder", new Log2Dump("directory", source_directory));
	}
	
	public long getTimeToSleepBetweenScans() {
		return time_to_sleep_between_scans;
	}
	
	/**
	 * throw fatal exec
	 */
	public void scan() throws Exception {
		// TODO
		Files.walkFileTree(source_directory.toPath(), new HashSet<FileVisitOption>(1), 50, file_found_event);
	}
	
	private class FileFoundEvent implements FileVisitor<Path> {
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
