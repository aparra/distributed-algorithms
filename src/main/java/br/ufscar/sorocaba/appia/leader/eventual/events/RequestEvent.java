package br.ufscar.sorocaba.appia.leader.eventual.events;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.sf.appia.core.events.SendableEvent;

public class RequestEvent extends SendableEvent {

	public RequestEvent to(String address) {
		dest = creanteSocketAddress(address);
		return this;
	}

	public RequestEvent from(SocketAddress address) {
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
