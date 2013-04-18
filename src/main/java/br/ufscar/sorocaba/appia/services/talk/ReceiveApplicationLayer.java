package br.ufscar.sorocaba.appia.services.talk;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.services.talk.events.ReceiveConfirmEvent;
import br.ufscar.sorocaba.appia.services.talk.events.SendRequestEvent;

public class ReceiveApplicationLayer extends Layer {

	public ReceiveApplicationLayer() {
		evProvide = new Class[] { ReceiveConfirmEvent.class };

		evRequire = new Class[] {};

		evAccept = new Class[] { SendRequestEvent.class, ChannelInit.class };
	}

	@Override
	public Session createSession() {
		return new ReceiveApplicationSession(this);
	}
}
