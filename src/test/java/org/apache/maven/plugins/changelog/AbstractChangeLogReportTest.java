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

import javax.inject.Inject;

import java.io.File;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.internal.aether.DefaultRepositorySystemSessionFactory;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.reporting.AbstractMavenReport;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.apache.maven.api.plugin.testing.MojoExtension.getBasedir;
import static org.apache.maven.api.plugin.testing.MojoExtension.setVariableValueToObject;

/**
 * Common test setup for the report mojos: since maven-reporting-impl 4.x, a standalone report execution
 * renders a full site page, which requires a usable repository session to resolve the site skin.
 */
abstract class AbstractChangeLogReportTest {

    @Inject
    private MavenSession mavenSession;

    @Inject
    private DefaultRepositorySystemSessionFactory repoSessionFactory;

    @Inject
    private MojoExecution mojoExecution;

    @BeforeEach
    void setUpRepositorySession() {
        // prepare realistic repository session
        ArtifactRepository localRepo = Mockito.mock(ArtifactRepository.class);
        Mockito.when(localRepo.getBasedir()).thenReturn(new File(getBasedir(), "target/local-repo").getAbsolutePath());

        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        request.setLocalRepository(localRepo);

        DefaultRepositorySystemSession systemSession = repoSessionFactory.newRepositorySession(request);
        Mockito.when(mavenSession.getRepositorySession()).thenReturn(systemSession);

        Mockito.when(mojoExecution.getPlugin()).thenReturn(new Plugin());
    }

    /**
     * Executes a report mojo as a standalone goal.
     * <p>
     * The site descriptor of this very plugin (<code>src/site/site.xml</code>, the default value of the
     * <code>siteDirectory</code> parameter within the test harness) defines no skin, so the mojo is pointed at an
     * empty directory to have the default site descriptor - and hence a resolvable skin - used instead.
     */
    protected void executeReport(AbstractMavenReport mojo) throws Exception {
        setVariableValueToObject(mojo, "siteDirectory", new File(getBasedir(), "target/test-harness/site"));

        mojo.execute();
    }
}
