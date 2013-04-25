package br.ufscar.sorocaba.appia.datalink.perfect;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.SendRequestEvent;

public class PerfectLinkLayer extends Layer {

	public PerfectLinkLayer() {
		evProvide = new Class[] {};

		evRequire = new Class[] {};

		evAccept = new Class[] { SendRequestEvent.class, ChannelInit.class };
	}

	public Session createSession() {
		return new PerfectLinkSession(this);
	}
}
