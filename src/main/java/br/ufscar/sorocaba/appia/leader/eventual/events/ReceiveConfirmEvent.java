package br.ufscar.sorocaba.appia.leader.eventual.events;

import java.net.SocketAddress;

import lombok.Getter;
import lombok.Setter;
import net.sf.appia.core.events.SendableEvent;

public @Getter @Setter class ReceiveConfirmEvent extends SendableEvent {
	
	public ReceiveConfirmEvent to(SocketAddress address) {
		dest = address;
		return this;
	}

	public ReceiveConfirmEvent from(SocketAddress address) {
		source = address;
		return this;
	}
}
