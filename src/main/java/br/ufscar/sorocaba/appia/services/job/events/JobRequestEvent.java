package br.ufscar.sorocaba.appia.services.job.events;

import lombok.Getter;
import lombok.Setter;
import net.sf.appia.core.Event;

public @Getter @Setter class JobRequestEvent extends Event {

	private int id;
	private String content;
}
