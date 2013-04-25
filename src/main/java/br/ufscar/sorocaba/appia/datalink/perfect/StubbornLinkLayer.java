package br.ufscar.sorocaba.appia.datalink.perfect;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.SendRequestEvent;
import br.ufscar.sorocaba.appia.datalink.events.TicTacEvent;

public class StubbornLinkLayer extends Layer {

	public StubbornLinkLayer() {
		evProvide = new Class[] {};

		evRequire = new Class[] {};

		evAccept = new Class[] { SendRequestEvent.class, TicTacEvent.class, ChannelInit.class };
	}

	public Session createSession() {
		return new StubbornLinkSession(this);
	}
}
