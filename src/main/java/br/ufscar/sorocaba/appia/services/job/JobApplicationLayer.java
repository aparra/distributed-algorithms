package br.ufscar.sorocaba.appia.services.job;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.services.job.events.JobConfirmEvent;
import br.ufscar.sorocaba.appia.services.job.events.JobRequestEvent;
import br.ufscar.sorocaba.appia.services.job.events.TransformationFaultEvent;

public class JobApplicationLayer extends Layer {

	public JobApplicationLayer() {
		evProvide = new Class[] { JobRequestEvent.class };

		evRequire = new Class[] {};

		evAccept = new Class[] { JobConfirmEvent.class, TransformationFaultEvent.class, ChannelInit.class };
	}

	public Session createSession() {
		return new JobApplicationSession(this);
	}
}
