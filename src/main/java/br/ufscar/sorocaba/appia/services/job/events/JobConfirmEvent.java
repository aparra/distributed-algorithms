package br.ufscar.sorocaba.appia.services.job.events;

import lombok.Getter;
import lombok.Setter;
import net.sf.appia.core.Event;
import net.sf.appia.core.Session;

public @Getter @Setter class JobConfirmEvent extends Event {

	private int requestId;
	
	public JobConfirmEvent(Session session) {
		this.setSourceSession(session);
	}
}
