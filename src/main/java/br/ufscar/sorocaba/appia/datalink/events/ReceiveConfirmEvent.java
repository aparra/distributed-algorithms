package br.ufscar.sorocaba.appia.datalink.events;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;

import java.net.SocketAddress;

import lombok.Getter;
import lombok.Setter;
import net.sf.appia.core.events.SendableEvent;

public @Getter @Setter class ReceiveConfirmEvent extends SendableEvent {
	
	public ReceiveConfirmEvent to(String address) {
		dest = creanteSocketAddress(address);
		return this;
	}

	public ReceiveConfirmEvent from(SocketAddress address) {
		source = address;
		return this;
	}
}
