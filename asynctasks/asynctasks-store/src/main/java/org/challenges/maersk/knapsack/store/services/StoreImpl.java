package org.challenges.maersk.knapsack.store.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.challenges.maersk.common.model.TaskWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class StoreImpl implements Store {

	private static final Map<String, TaskWrapper> STORE = new ConcurrentHashMap<String, TaskWrapper>(
			new HashMap<String, TaskWrapper>());
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreImpl.class);

	@Override
	public TaskWrapper getTask(final String ident) throws StoreException {
		try {
			/* LOGGER.trace("[getTask] STORE: {}", STORE); */
			TaskWrapper result = STORE.get(ident);
			LOGGER.debug("[getTask] from key: {} we get the value: {}", ident, result);
			return result;
		} catch (Exception e) {
			throw new StoreException(e);
		}
	}

	@Override
	public Set<TaskWrapper> getTasks() throws StoreException {
		/* LOGGER.info("[getTasks] STORE: {}", STORE); */
		return new HashSet<TaskWrapper>(STORE.values());
	}

	@Override
	public void postTask(final TaskWrapper task) throws StoreException {
		try {
			STORE.put(task.getTask().getTask(), task);
			/* LOGGER.info("[postTask] STORE: {}", STORE); */
			LOGGER.debug("[postTask] stored: {}", task);
		} catch (Exception e) {
			throw new StoreException(e);
		}
	}

}
