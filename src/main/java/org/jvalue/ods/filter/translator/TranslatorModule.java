package org.jvalue.ods.filter.translator;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import org.jvalue.ods.filter.Filter;
import org.w3c.dom.Document;

public final class TranslatorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Filter<JsonNode, Object>>() { }).to(JsonTranslator.class);
		bind(new TypeLiteral<Filter<Document, Object>>() { }).to(XmlTranslator.class);
	}

}
