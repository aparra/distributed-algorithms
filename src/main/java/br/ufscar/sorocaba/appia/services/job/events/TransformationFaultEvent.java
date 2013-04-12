package br.ufscar.sorocaba.appia.services.job.events;

import net.sf.appia.core.Event;
import net.sf.appia.core.Session;

public class TransformationFaultEvent extends Event {

	public TransformationFaultEvent(Session session) {
		this.setSourceSession(session);
	}
}
