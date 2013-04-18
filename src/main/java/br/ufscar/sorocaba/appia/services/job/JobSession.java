package br.ufscar.sorocaba.appia.services.job;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.initAndGo;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.services.job.events.JobConfirmEvent;
import br.ufscar.sorocaba.appia.services.job.events.JobRequestEvent;

public class JobSession extends Session {

	public JobSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			go(event);
		} else if (event instanceof JobRequestEvent) {
			handleJobRequest((JobRequestEvent) event);
		}
	}

	private void handleJobRequest(JobRequestEvent request) {
		System.out.println("[Print] " + request.getContent());
		go(request);
		
		sendJobConfirm(request);
	}
	
	private void sendJobConfirm(JobRequestEvent request) {
		JobConfirmEvent confirm = new JobConfirmEvent(this);
		confirm.setChannel(request.getChannel());
		confirm.setDir(Direction.UP);
		
		confirm.setRequestId(request.getId());
		
		initAndGo(confirm);
	}
}
