package org.jvalue.ods.rest.v2.jsonapi.swagger;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.rest.v2.api.EntryPoint;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.*;

@Schema(name = "JsonApiDocument")
public class JsonApiSchema {

	public abstract class ProcessorSpecificationSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract SpecificationWrapper getData();
	}

	public abstract class ClientSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract ClientWrapper getData();
	}

	public abstract class DataSourceSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract DataSourceWrapper getData();
	}

	public abstract class DataViewSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract DataViewWrapper getData();
	}

	public abstract class EntryPointSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract EntryPoint.EntryPointData getData();
	}

	public abstract class PluginMetaDataSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract PluginMetaDataWrapper getData();
	}

	public abstract class ProcessorReferenceChainSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract ProcessorReferenceChainWrapper getData();
	}

	public abstract class UserSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract UserWrapper getData();
	}

	public abstract class DataSchema extends JsonApiSchema {
		@Schema(required = true)
		public abstract DataWrapper getData();
	}

	public abstract class DataCollectionSchema extends JsonApiSchema {
		@ArraySchema(schema = @Schema(implementation = DataWrapper.class, required = true))
		public abstract DataWrapper[] getData();
	}
}
