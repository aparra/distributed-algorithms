package br.ufscar.sorocaba.appia.util;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Event;

public class EventUtils {

	public static void initAndGo(Event event) {
		try {
			event.init();
			event.go();
		} catch (AppiaEventException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void go(Event event) {
		try {
			event.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}
}
