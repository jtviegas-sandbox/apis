package org.aprestos.labs.apis.asynctasks.common.services.solver;

import java.util.Map;

import org.aprestos.labs.apiclient.Ident;
import org.aprestos.labs.apiclient.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
class SolverImpl implements Solver {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolverImpl.class);

	@Autowired
	private RestClient client;

	@Value("${org.aprestos.labs.apis.asynctasks.common.services.solver.uri:#{null}}")
	private String uri;

	public SolverImpl() {
	}

  @Override
  public String submitProblem(Map<String,Object> problem) throws SolverException {
    LOGGER.trace("[submitProblem|in] problem: {}", problem);
    try {
      MultiValueMap<String, String> header = client.getHeadersBuilder().build();
      Ident id = client.postAndGetId(problem, header, uri);
      return id.asString();
    } catch (Exception e) {
      throw new SolverException(e);
    } finally {
      LOGGER.trace("[submitProblem|out]");
    }
  }

}
