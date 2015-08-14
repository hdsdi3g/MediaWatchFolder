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

public class MainClass {
	
	public static void main(String[] args) throws Exception {
		final Service s = new Service();
		
		s.startService();
		
		Thread shutdownhook = new Thread() {
			public void run() {
				try {
					Log2.log.info("Request shutdown application");
					Thread tkill = new Thread() {
						public void run() {
							try {
								sleep(5000);
								Log2.log.error("Request KILL application", null);
								System.exit(2);
							} catch (Exception e) {
								Log2.log.error("Fatal service killing", e);
								System.exit(2);
							}
						}
					};
					tkill.setName("Shutdown KILL");
					tkill.setDaemon(true);
					tkill.start();
					s.stopService();
				} catch (Exception e) {
					Log2.log.error("Fatal service stopping", e);
				}
			}
		};
		shutdownhook.setName("Shutdown Hook");
		Runtime.getRuntime().addShutdownHook(shutdownhook);
		
		while (true) {
			Thread.sleep(10);
		}
	}
	
}
