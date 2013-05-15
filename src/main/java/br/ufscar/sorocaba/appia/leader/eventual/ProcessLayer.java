package br.ufscar.sorocaba.appia.leader.eventual;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.TicTacEvent;
import br.ufscar.sorocaba.appia.leader.eventual.events.HeartbeatEvent;
import br.ufscar.sorocaba.appia.leader.eventual.events.HeartbeatResponseEvent;
import br.ufscar.sorocaba.appia.leader.eventual.events.RequestEvent;

public class ProcessLayer extends Layer {

	public ProcessLayer() {
		evProvide = new Class[] { RequestEvent.class, HeartbeatEvent.class, HeartbeatResponseEvent.class };

		evRequire = new Class[] {};

		evAccept = new Class[] { RequestEvent.class, HeartbeatEvent.class, HeartbeatResponseEvent.class, TicTacEvent.class, ChannelInit.class };
	}

	public Session createSession() {
		return new ProcessSession(this);
	}
}
