package br.ufscar.sorocaba.appia.services.job;

import static br.ufscar.sorocaba.appia.util.EventUtils.go;
import static br.ufscar.sorocaba.appia.util.EventUtils.initAndGo;
import static org.apache.commons.lang.StringUtils.upperCase;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.services.job.events.JobConfirmEvent;
import br.ufscar.sorocaba.appia.services.job.events.JobRequestEvent;
import br.ufscar.sorocaba.appia.services.job.events.TransformationFaultEvent;

public class TransformationSession extends Session {

	public TransformationSession(Layer layer) {
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
		try {
			request.setContent(upperCase(request.getContent()));
		} catch (Exception e) {
			sendTransformationFault(request.getChannel());
		}
		
		go(request);
		sendJobConfirm(request);
	}

	private void sendTransformationFault(Channel channel) {
		TransformationFaultEvent fault = new TransformationFaultEvent(this);
		fault.setChannel(channel);
		fault.setDir(Direction.UP);
		
		initAndGo(fault);
	}

	private void sendJobConfirm(JobRequestEvent request) {
		JobConfirmEvent confirm = new JobConfirmEvent(this);
		confirm.setChannel(request.getChannel());
		confirm.setDir(Direction.UP);
		
		confirm.setRequestId(request.getId());
		
		initAndGo(confirm);
	}
}