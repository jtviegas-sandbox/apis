package org.aprestos.labs.apis.asynctasks.common.services.notifier;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.aprestos.labs.apiclient.Ident;
import org.aprestos.labs.apiclient.RestClient;
import org.aprestos.labs.apis.asynctasks.common.model.Status;
import org.aprestos.labs.apis.asynctasks.common.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Service
class TaskStateManagerImpl implements TaskStateManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskStateManagerImpl.class);

  @Autowired
  private RestClient client;

  @Value("${org.challenges.maersk.common.services.notifier.task.uri:#{null}}")
  private String taskUri;
  @Value("${org.challenges.maersk.common.services.notifier.task.status.uri:#{null}}")
  private String taskStatusUri;
  @Value("${org.challenges.maersk.common.services.notifier.tasks.statuses.uri:#{null}}")
  private String tasksStatusesUri;
  @Value("${org.challenges.maersk.common.services.notifier.task.uri.id:#{null}}")
  private String taskUriId;

  @Autowired
  private ThreadPoolTaskExecutor executor;

  @Override
  public Future<?> notify(Task task) {
    final String uri = taskUri;
    return this.executor.submit(new Callable<String>() {
      @Override
      public String call() throws Exception {
        LOGGER.trace("[notify.call|in] task: {}", task);
        try {
          MultiValueMap<String, String> header = client.getHeadersBuilder().build();
          Ident id = client.postAndGetId(task, header, uri);
          LOGGER.info("[notify.call] posted task and got id: {}", id);
          return id.asString();
        } catch (Exception e) {
          throw new TaskStateManagerException(e);
        } finally {
          LOGGER.trace("[notify.call|out]");
        }
      }
    });
  }

  @Override
  public Future<?> notify(final Status status) {

    final String uri = taskStatusUri;
    return this.executor.submit(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        LOGGER.trace("[notify.call|in] status: {}", status);
        try {
          MultiValueMap<String, String> header = client.getHeadersBuilder().build();
          client.post(status, header, uri, status.getId());
          LOGGER.info("[notify.call] posted status: {}", status);
        } catch (Exception e) {
          throw new TaskStateManagerException(e);
        } finally {
          LOGGER.trace("[notify.call|out]");
        }
        return null;
      }
    });

  }

  @Override
  public Optional<Status> getStatus(String id) throws StateNotFoundException, TaskStateManagerException {
    LOGGER.trace("[getStatus|in] id: {}", id);
    Optional<Status> result = null;
    try {
      MultiValueMap<String, String> header = client.getHeadersBuilder().build();
      result = client.get(new ParameterizedTypeReference<Status>() {
      }, header, taskUriId, null, id);
      return result;
    } catch (HttpClientErrorException hce) {
      if (hce.getStatusCode().equals(HttpStatus.NOT_FOUND))
        throw new StateNotFoundException(hce);
      else
        throw new TaskStateManagerException(hce);
    } catch (Exception e) {
      throw new TaskStateManagerException(e);
    } finally {
      LOGGER.trace("[getStatus|out] {}", result);
    }

  }

  @Override
  public Set<Status> getStatuses() throws TaskStateManagerException {
    LOGGER.trace("[getStatuses|in]");
    Set<Status> result = null;
    try {
      MultiValueMap<String, String> header = client.getHeadersBuilder().build();
      result = client.get(new ParameterizedTypeReference<Set<Status>>() {
      }, header, tasksStatusesUri, null, (Object) null).get();
      return result;
    } catch (Exception e) {
      throw new TaskStateManagerException(e);
    } finally {
      LOGGER.trace("[getStatuses|out] {}", result);
    }
  }

}
