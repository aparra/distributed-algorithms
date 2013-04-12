package br.ufscar.sorocaba.appia.services.job.executor;

import net.sf.appia.core.Appia;
import net.sf.appia.core.AppiaDuplicatedSessionsException;
import net.sf.appia.core.AppiaInvalidQoSException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Layer;
import net.sf.appia.core.QoS;
import br.ufscar.sorocaba.appia.services.job.JobApplicationLayer;
import br.ufscar.sorocaba.appia.services.job.JobLayer;
import br.ufscar.sorocaba.appia.services.job.TransformationLayer;

public class JobExecutor {

	public static void main(String[] args) {
		Layer[] queue = { new JobLayer(), new TransformationLayer(), new JobApplicationLayer() };
		QoS myQoS = createQoS("job_stack", queue);

		start(myQoS.createUnboundChannel("job_channel"));
		
		System.out.println("Starting Appia...");
		Appia.run();
	}
	
	private static QoS createQoS(String name, Layer[] queue) {
		try {
			return new QoS(name, queue);
		} catch (AppiaInvalidQoSException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void start(Channel channel) {
		try {
			channel.start();
		} catch (AppiaDuplicatedSessionsException e) {
			throw new RuntimeException("Error in start", e);
		}
	}

}
