package br.ufscar.sorocaba.appia.services.job;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.services.job.events.JobConfirmEvent;
import br.ufscar.sorocaba.appia.services.job.events.JobRequestEvent;

public class JobLayer extends Layer {

	public JobLayer() {
		evProvide = new Class[] { JobConfirmEvent.class };

		evRequire = new Class[] {};

		evAccept = new Class[] { JobRequestEvent.class, ChannelInit.class };
	}

	public Session createSession() {
		return new JobSession(this);
	}
}
