package org.aprestos.labs.apis.asynctasks.common.services.knapsack;

import org.aprestos.labs.apiclient.RestClient;
import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
class KnapsackImpl implements Knapsack {

	private static final Logger LOGGER = LoggerFactory.getLogger(KnapsackImpl.class);

	@Autowired
	private RestClient client;

	@Value("${org.challenges.maersk.common.services.knapsack.uri:#{null}}")
	private String uri;

	public KnapsackImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.challenges.maersk.common.services.knapsack.Knapsack#submitTask(org.
	 * challenges.maersk.common.model.TaskWrapper)
	 */
	@Override
	public void submitTask(TaskWrapper task) throws KnapsackException {
		LOGGER.trace("[submitTask|in] task: {}", task);
		try {
			MultiValueMap<String, String> header = client.getHeadersBuilder().build();
			client.post(task, header, uri);
		} catch (Exception e) {
			throw new KnapsackException(e);
		} finally {
			LOGGER.trace("[submitTask|out]");
		}
	}

}
