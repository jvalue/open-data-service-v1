package org.jvalue.ods.processor.adapter;


import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import org.jvalue.ods.processor.adapter.domain.BrandenburgPegelAdapter;
import org.jvalue.ods.processor.adapter.domain.PegelPortalMvSourceAdapter;

public final class AdapterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER),
						JsonSourceAdapter.class)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_CSV_SOURCE_ADAPTER),
						CsvSourceAdapter.class)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_XML_SOURCE_ADAPTER),
						XmlSourceAdapter.class)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_OSM_SOURCE_ADAPTER),
						OsmSourceAdapter.class)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_PEGEL_PORTAL_MV_SOURCE_ADAPTER),
						PegelPortalMvSourceAdapter.class)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_PEGEL_BRANDENBURG),
						BrandenburgPegelAdapter.class)
				.build(SourceAdapterFactory.class));
	}

}
