/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugins.changelog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.maven.doxia.module.xhtml5.Xhtml5SinkFactory;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.reporting.MavenReportException;

/**
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @since 2.2
 */
public abstract class AbstractChangeLogReportTest extends AbstractMojoTestCase {
    /**
     * Renderer the sink from the report mojo.
     *
     * @param mojo       not null
     * @param outputHtml not null
     * @throws IOException if any
     * @throws MavenReportException if any
     */
    protected void renderer(ChangeLogReport mojo, File outputHtml) throws IOException, MavenReportException {
        outputHtml.getParentFile().mkdirs();
        try (OutputStream out = Files.newOutputStream(outputHtml.toPath())) {
            Xhtml5SinkFactory sinkFactory = new Xhtml5SinkFactory();
            Sink sink = sinkFactory.createSink(out, "UTF-8");
            mojo.generate(sink, null, null);
            sink.flush();
            sink.close();
        }
    }
}
