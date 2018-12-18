package org.jvalue.ods.processor.adapter;


import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import org.jvalue.ods.processor.adapter.domain.BrandenburgPegelAdapter;
import org.jvalue.ods.processor.adapter.domain.PegelPortalMvSourceAdapter;
import org.jvalue.ods.processor.adapter.domain.weather.APIXUSourceAdapter;
import org.jvalue.ods.processor.adapter.domain.weather.OpenWeatherMapSourceAdapter;

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
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_OPEN_WEATHER_MAP_SOURCE_ADAPTER),
						OpenWeatherMapSourceAdapter.class)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_APIXU_SOURCE_ADAPTER),
						APIXUSourceAdapter.class
				)
				.implement(
						SourceAdapter.class,
						Names.named(SourceAdapterFactory.NAME_MULTI_SOURCE_ADAPTER),
						MultiSourceAdapter.class
				)
				.build(SourceAdapterFactory.class));
	}

}
