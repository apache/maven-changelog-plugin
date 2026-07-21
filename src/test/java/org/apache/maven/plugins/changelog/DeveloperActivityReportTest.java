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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.api.di.Provides;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.model.Developer;
import org.apache.maven.plugins.changelog.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.apache.maven.api.plugin.testing.MojoExtension.getBasedir;
import static org.apache.maven.api.plugin.testing.MojoExtension.getVariableValueFromObject;
import static org.apache.maven.api.plugin.testing.MojoExtension.setVariableValueToObject;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Edwin Punzalan
 */
@MojoTest
class DeveloperActivityReportTest {

    @Provides
    @SuppressWarnings("unused")
    private MavenProject providesMavenProject() {
        MavenProject mavenProject = new MavenProjectStub();

        List<Developer> developers = new ArrayList<>();

        Developer developer = new Developer();
        developer.setName("Edwin Punzalan");
        developer.setId("edwin");
        developers.add(developer);

        developer = new Developer();
        developer.setName("Keogh Edrich Punzalan");
        developer.setId("keogh");
        developers.add(developer);

        mavenProject.setDevelopers(developers);
        return mavenProject;
    }

    @Test
    @InjectMojo(goal = "dev-activity", pom = "src/test/plugin-configs/dev-activity/no-source-plugin-config.xml")
    void testNoSource(DeveloperActivityReport mojo) throws Exception {
        mojo.execute();

        File outputDir = getVariableValueFromObject(mojo, "outputDirectory");

        File outputHtml = new File(outputDir, "dev-activity.html");

        assertTrue(outputHtml.exists(), outputHtml.getAbsolutePath() + " not generated!");

        assertTrue(outputHtml.length() > 0, outputHtml.getAbsolutePath() + " is empty!");
    }

    @Test
    @InjectMojo(goal = "dev-activity", pom = "src/test/plugin-configs/dev-activity/min-plugin-config.xml")
    void testMinConfig(DeveloperActivityReport mojo) throws Exception {
        File outputXML = new File(getBasedir(), "src/test/changelog-xml/min-changelog.xml");

        // force reuse of existing changelog cache
        outputXML.setLastModified(System.currentTimeMillis());

        // use current directory project as basedir
        setVariableValueToObject(mojo, "basedir", new File(getBasedir(), "src/main/java"));
        setVariableValueToObject(mojo, "outputXML", outputXML);

        mojo.execute();

        String encoding = getVariableValueFromObject(mojo, "outputEncoding");

        assertTrue(outputXML.exists(), "Test if changelog.xml is created");

        String changelogXml = FileUtils.fileRead(outputXML);

        assertTrue(
                changelogXml.startsWith("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"),
                "Test for xml header");

        assertTrue(changelogXml.endsWith("</changelog>"), "Test for xml footer");

        File outputDir = getVariableValueFromObject(mojo, "outputDirectory");

        File outputHtml = new File(outputDir, "dev-activity.html");

        assertTrue(outputHtml.exists(), outputHtml.getAbsolutePath() + " not generated!");

        assertTrue(outputHtml.length() > 0, outputHtml.getAbsolutePath() + " is empty!");
    }
}
