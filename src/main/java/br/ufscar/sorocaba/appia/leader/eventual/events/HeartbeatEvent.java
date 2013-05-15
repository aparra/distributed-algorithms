package br.ufscar.sorocaba.appia.leader.eventual.events;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.sf.appia.core.events.SendableEvent;

public class HeartbeatEvent extends SendableEvent {

	public HeartbeatEvent to(SocketAddress address) {
		dest = address;
		return this;
	}

	public HeartbeatEvent from(SocketAddress address) {
		source = address;
		return this;
	}
	
	public SocketAddress getTargetAddress() {
		return (SocketAddress) source;
	}
	
	public InetSocketAddress getTo() {
		return (InetSocketAddress) dest;
	}
}
