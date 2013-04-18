package br.ufscar.sorocaba.appia.services.talk.events;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;

import java.net.SocketAddress;

import net.sf.appia.core.events.SendableEvent;

public class SendRequestEvent extends SendableEvent {

	public SendRequestEvent to(String address) {
		String[] tokenAddress = address.split(":");
		dest = creanteSocketAddress(tokenAddress[0], Integer.valueOf(tokenAddress[1]));
		return this;
	}

	public SendRequestEvent from(SocketAddress address) {
		source = address;
		return this;
	}
}
