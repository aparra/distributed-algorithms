package br.ufscar.sorocaba.appia.leader.eventual.events;

import java.net.SocketAddress;

import net.sf.appia.core.events.SendableEvent;

public class HeartbeatResponseEvent extends SendableEvent {

	public HeartbeatResponseEvent to(SocketAddress address) {
		dest = address;
		return this;
	}

	public HeartbeatResponseEvent from(SocketAddress address) {
		source = address;
		return this;
	}
}
