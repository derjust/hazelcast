/*
 * Copyright (c) 2008-2012, Hazel Bilisim Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jca;

import org.jboss.jca.embedded.Embedded;
import org.jboss.jca.embedded.EmbeddedFactory;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import static org.junit.Assert.*;

public class AbsDeploymentTest {

	static {
		System.setProperty("hazelcast.logging.type", "log4j");
	}

	private static final ILogger LOGGER = Logger.getLogger(AbsDeploymentTest.class.getName());
	
	/** Embedded */
	protected static Embedded embeddedJCAContainer;

	/** JNDI prefix */
	private static final String JNDI_PREFIX = "java:/";
	private static final String JNDI_HAZELCAST_CF = "HazelcastCF";

	protected InitialContext context;

	private URL rarArchive;

	protected ConnectionFactoryImpl connectionFactory;

	@After 
	public void tearDown() throws Throwable {
		embeddedJCAContainer.undeploy(rarArchive);

		if (context != null) {
			try {
				context.close();
			} catch (NamingException ne) {
				// Ignore
			}
		}
	}
	
	@Before
	public void setUp() throws Throwable {
		String archiveVersion = getClass().getPackage().getImplementationVersion();
		if (archiveVersion == null) {
			//TODO Dirty Harry 9 3/4
			archiveVersion = "2.4.1";
			LOGGER.log(Level.WARNING, "Manifest entry for current version not found!");
			LOGGER.log(Level.WARNING, "Will continue with version " + archiveVersion);
		}
		File f = new File("./target/hazelcast-ra-"+archiveVersion+".rar");
		rarArchive = f.toURI().toURL();

		embeddedJCAContainer.deploy(rarArchive);
		
		context = new InitialContext();
		
		Object o = context.lookup(JNDI_PREFIX + JNDI_HAZELCAST_CF);

		assertNotNull(o);
		assertTrue(o instanceof ConnectionFactoryImpl);

		connectionFactory = (ConnectionFactoryImpl) o;
	}

	@BeforeClass
	public static void beforeClass() throws Throwable {
		// Create an embedded JCA instance
		embeddedJCAContainer = EmbeddedFactory.create();

		// Startup
		embeddedJCAContainer.startup();
	}

	@AfterClass
	public static void afterClass() {
		// Shutdown
		try {
			embeddedJCAContainer.shutdown();
		} catch (Throwable t) {
			//shouldn't matter
		}
	}
}
