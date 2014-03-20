/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.grabber;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.osm.OsmData;
import org.jvalue.ods.logger.Logging;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;

/**
 * The Class OsmGrabber.
 */
public class OsmGrabber {

	/** The nodes. */
	private List<Node> nodes;

	/** The ways. */
	private List<Way> ways;

	/** The relations. */
	private List<Relation> relations;

	/**
	 * Grab.
	 *
	 * @param source the source
	 * @return the osm data
	 * @throws URISyntaxException 
	 */
	public OsmData grab(String source) {
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}

		if (source.length() == 0) {
			throw new IllegalArgumentException("source is empty");
		}

		nodes = new LinkedList<Node>();
		ways = new LinkedList<Way>();
		relations = new LinkedList<Relation>();

		File file = null;

		if (!source.startsWith("http")) {
			URL sourceUrl = getClass().getResource(source);
			if (sourceUrl == null)
				return null;
			try {
				file = new File(sourceUrl.toURI());
			} catch (URISyntaxException e) {
				Logging.error(this.getClass(), e.getMessage());
				e.printStackTrace();
				return null;
			}
		} else {

			PrintWriter out = null;

			try {
				Logging.info(this.getClass(), "Opening: " + source);
				HttpReader reader = new HttpReader(source);
				String data = reader.read("UTF-8");
				File tmpFile = new File("tmp.txt");
				if (tmpFile.exists()) {
					tmpFile.delete();
				}
				out = new PrintWriter("tmp.txt");
				out.println(data);
				file = new File("tmp.txt");
			} catch (IOException e) {
				Logging.error(this.getClass(), e.getMessage());
				// maybe throw another exception here instead of returning null
				return null;
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}

		Sink sinkImplementation = new Sink() {
			public void process(EntityContainer entityContainer) {
				Entity entity = entityContainer.getEntity();
				if (entity instanceof Node) {
					nodes.add((Node) entity);
				} else if (entity instanceof Way) {
					ways.add((Way) entity);
				} else if (entity instanceof Relation) {
					relations.add((Relation) entity);
				}
			}

			public void release() {
			}

			public void complete() {
			}

			public void initialize(Map<String, Object> metaData) {

			}
		};

		RunnableSource reader = new XmlReader(file, false,
				CompressionMethod.None);

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		readerThread.start();
		
		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				// maybe throw another exception here instead of returning null
				return null;
			} 
		}

		if (file.exists() && source.startsWith("http")) {
			file.delete();
		}
		
		return new OsmData(nodes, ways, relations);
	}
}
