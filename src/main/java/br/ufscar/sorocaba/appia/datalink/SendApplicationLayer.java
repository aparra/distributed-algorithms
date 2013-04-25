package br.ufscar.sorocaba.appia.datalink;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.ReceiveConfirmEvent;
import br.ufscar.sorocaba.appia.datalink.events.SendRequestEvent;

public class SendApplicationLayer extends Layer {

	public SendApplicationLayer() {
		evProvide = new Class[] { SendRequestEvent.class };

		evRequire = new Class[] {};

		evAccept = new Class[] { ReceiveConfirmEvent.class, ChannelInit.class };
	}

	public Session createSession() {
		return new SendApplicationSession(this);
	}
}
