package br.ufscar.sorocaba.appia.datalink.perfect;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.TicTacEvent;

public class ClockLayer extends Layer {

	public ClockLayer() {
		evProvide = new Class[] { TicTacEvent.class };

		evRequire = new Class[] {};

		evAccept = new Class[] { ChannelInit.class };
	}

	public Session createSession() {
		return new ClockSession(this);
	}
}
