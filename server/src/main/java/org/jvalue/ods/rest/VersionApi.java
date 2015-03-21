package org.jvalue.ods.rest;


import org.jvalue.ods.GitConstants;
import org.jvalue.common.utils.VersionInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public final class VersionApi {

	private static final VersionInfo version = new VersionInfo(
			GitConstants.VERSION,
			"https://github.com/jvalue/open-data-service/commit/" +  GitConstants.COMMIT_HASH);


	@GET
	public VersionInfo getVersion() {
		return version;
	}

}
