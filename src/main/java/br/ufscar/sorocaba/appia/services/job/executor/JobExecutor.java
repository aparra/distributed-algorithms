package br.ufscar.sorocaba.appia.services.job.executor;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.createQoS;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.start;
import net.sf.appia.core.Appia;
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
}
