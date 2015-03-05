package org.jvalue.ods.rest;


import org.jvalue.ods.GitConstants;
import org.jvalue.ods.api.OdsVersion;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public final class VersionApi {

	private static final OdsVersion version = new OdsVersion(
			"0.1.0-SNAPSHOT",
			1,
			"https://github.com/jvalue/open-data-service/commit/" +  GitConstants.COMMIT_HASH);


	@GET
	public OdsVersion getVersion() {
		return version;
	}

}
