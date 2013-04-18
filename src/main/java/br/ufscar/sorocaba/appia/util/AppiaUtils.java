package br.ufscar.sorocaba.appia.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import net.sf.appia.core.AppiaDuplicatedSessionsException;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.AppiaInvalidQoSException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.QoS;

public class AppiaUtils {

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
	
	public static QoS createQoS(String name, Layer[] queue) {
		try {
			return new QoS(name, queue);
		} catch (AppiaInvalidQoSException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void start(Channel channel) {
		try {
			channel.start();
		} catch (AppiaDuplicatedSessionsException e) {
			throw new RuntimeException("Error in start", e);
		}
	}
	
	public static InetSocketAddress creanteSocketAddress(String address) {
		try {
			String[] tokenAddress = address.split(":");
			return new InetSocketAddress(InetAddress.getByName(tokenAddress[0]), Integer.valueOf(tokenAddress[1]));
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
}
