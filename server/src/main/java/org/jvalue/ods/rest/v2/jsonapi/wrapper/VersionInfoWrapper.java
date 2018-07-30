package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.commons.rest.VersionInfo;

public class VersionInfoWrapper extends VersionInfo implements JsonApiIdentifiable{

	private VersionInfoWrapper(String version, String build) {
		super(version, build);
	}


	@Override
	public String getId() {
		return getVersion();
	}


	@Override
	public String getType() {
		return VersionInfo.class.getSimpleName();
	}


	public static VersionInfoWrapper from(VersionInfo versionInfo) {
		return new VersionInfoWrapper(versionInfo.getVersion(), versionInfo.getBuild());
	}
}
