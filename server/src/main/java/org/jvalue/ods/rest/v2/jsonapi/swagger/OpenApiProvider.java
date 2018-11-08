package org.jvalue.ods.rest.v2.jsonapi.swagger;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.jvalue.ods.rest.v2.api.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.jvalue.ods.rest.v2.api.AbstractApi.*;


public class OpenApiProvider {

	private static final SwaggerConfiguration conf = new SwaggerConfiguration()
		.readAllResources(false)
		.prettyPrint(true);

	private static final License license = new License().name("exampleLicense");

	private static final Info openApiInfo = new Info()
		.description("This is the specification of the public Open-Data-Service API v2")
		.license(license)
		.title("ODS API")
		.version("2.0.0");

	private static final List<Tag> tags = Arrays.asList(
		new Tag().name(DATASOURCES).description("Sources of the data delivered by the Open-Data-Service"),
		new Tag().name(DATA).description("Fetch or remove data"),
		new Tag().name(VIEWS).description("Specify and apply moves on the data"),
		new Tag().name(ENTRYPOINT).description("Entrance to start navigating the Open-Data-Service API"),
		new Tag().name(NOTIFICATIONS).description("Client registration for notifications about data changes"),
		new Tag().name(FILTERCHAINS).description("Specify rules for fetching data"),
		new Tag().name(FILTERTYPES).description("Overview about used filters"),
		new Tag().name(DOC).description("OpenAPI specification"),
		new Tag().name(USERS).description("Users of the Open-Data-Service")
	);

	private static final SecurityScheme securityScheme =
			new SecurityScheme()
				.scheme("basic")
				.type(SecurityScheme.Type.HTTP)
				.name(BASICAUTH);

	private static final Components comps = new Components().addSecuritySchemes("basicAuth", securityScheme);

	private static final OpenAPI instance = new Reader(conf)
		.read(new HashSet<>(Arrays.asList(
			EntryPoint.class,
			DataSourceApi.class,
			SpecificationApi.class,
			DataApi.class,
			DataViewApi.class,
			NotificationApi.class,
			ProcessorChainApi.class,
			ProcessorSpecificationApi.class,
			UserApi.class)
		))
		.components(comps)
		.tags(tags)
		.info(openApiInfo);


	public static OpenAPI getOpenAPI() {
		return instance;
	}


}
