package org.challenges.maersk.common.services.notifier;

import java.util.Optional;
import java.util.Set;

import org.aprestos.labs.apiclient.RestClient;
import org.challenges.maersk.common.model.TaskWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Service
class TaskStateManagerImpl implements TaskStateManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskStateManagerImpl.class);

	@Autowired
	private RestClient client;

	@Value("${org.challenges.maersk.common.services.notifier.uri:#{null}}")
	private String uri;

	@Value("${org.challenges.maersk.common.services.notifier.uri.id:#{null}}")
	private String uriId;

	@Override
	public void notify(TaskWrapper state) throws TaskStateManagerException {
		LOGGER.trace("[notify|in] state: {}", state);
		try {
			MultiValueMap<String, String> header = client.getHeadersBuilder().build();
			client.post(state, header, uri);
			LOGGER.info("[notify] posted state: {}", state);
		} catch (Exception e) {
			throw new TaskStateManagerException(e);
		} finally {
			LOGGER.trace("[notify|out]");
		}

	}

	@Override
	public Optional<TaskWrapper> getState(String ident) throws StateNotFoundException, TaskStateManagerException {
		LOGGER.trace("[getState|in] id: {}", ident);
		Optional<TaskWrapper> result = null;
		try {
			MultiValueMap<String, String> header = client.getHeadersBuilder().build();
			result = client.get(new ParameterizedTypeReference<TaskWrapper>() {
			}, header, uriId, null, ident);
			return result;

		} catch (HttpClientErrorException hce) {
			if (hce.getStatusCode().equals(HttpStatus.NOT_FOUND))
				throw new StateNotFoundException(hce);
			else
				throw new TaskStateManagerException(hce);
		} catch (Exception e) {
			throw new TaskStateManagerException(e);
		} finally {
			LOGGER.trace("[getState|out] {}", result);
		}

	}

	@Override
	public Set<TaskWrapper> getStates() throws TaskStateManagerException {
		LOGGER.trace("[getStates|in]");
		Set<TaskWrapper> result = null;
		try {
			MultiValueMap<String, String> header = client.getHeadersBuilder().build();
			result = client.get(new ParameterizedTypeReference<Set<TaskWrapper>>() {
			}, header, uri, null, (Object) null).get();
			return result;
		} catch (Exception e) {
			throw new TaskStateManagerException(e);
		} finally {
			LOGGER.trace("[getState|out] {}", result);
		}
	}

}
