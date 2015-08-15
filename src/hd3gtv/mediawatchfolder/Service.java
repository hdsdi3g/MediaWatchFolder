/*
 * This file is part of MediaWatchFolder.
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
 * Copyright (C) hdsdi3g for hd3g.tv 2015
 * 
*/
package hd3gtv.mediawatchfolder;

import hd3gtv.log2.Log2;

public class Service {
	
	private boolean stop;
	private ServiceThread service_thread;
	
	/**
	 * And restart.
	 */
	public synchronized void startService() throws InterruptedException {
		if (service_thread != null) {
			stop = true;
			while (service_thread.isAlive()) {
				Thread.sleep(10);
			}
		}
		service_thread = new ServiceThread();
		service_thread.setDaemon(false);
		service_thread.setName("Service Thread");
		service_thread.start();
	}
	
	/**
	 * Is blocking
	 */
	public synchronized void stopService() throws InterruptedException {
		stop = true;
		if (service_thread != null) {
			while (service_thread.isAlive()) {
				Thread.sleep(10);
			}
		}
	}
	
	private class ServiceThread extends Thread {
		public void run() {
			stop = false;
			try {
				Log2.log.info("Start service");
				
				WatchFolderRouter wfrouter = new WatchFolderRouter();
				
				while (stop == false) {
					wfrouter.scan();
					Thread.sleep(wfrouter.getTimeToSleepBetweenScans());
				}
			} catch (Exception e) {
				Log2.log.error("Error during service execution", e);
			}
			Log2.log.info("Service is stopped");
		}
	}
}
