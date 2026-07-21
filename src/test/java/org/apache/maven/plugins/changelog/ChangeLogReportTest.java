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
import java.nio.file.Files;

import org.apache.maven.api.di.Provides;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.changelog.stubs.FailedScmManagerStub;
import org.apache.maven.plugins.changelog.stubs.MavenProjectStub;
import org.apache.maven.plugins.changelog.stubs.ScmManagerStub;
import org.apache.maven.plugins.changelog.stubs.ScmManagerWithHostStub;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.manager.ScmManager;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.apache.maven.api.plugin.testing.MojoExtension.getBasedir;
import static org.apache.maven.api.plugin.testing.MojoExtension.getVariableValueFromObject;
import static org.apache.maven.api.plugin.testing.MojoExtension.setVariableValueToObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Edwin Punzalan
 */
@MojoTest
class ChangeLogReportTest {
    private ScmManager scmManager = new ScmManagerStub();

    @Provides
    @SuppressWarnings("unused")
    private MavenProject providesMavenProject() {
        return new MavenProjectStub();
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/no-source-plugin-config.xml")
    void testNoSource(ChangeLogReport mojo) throws Exception {
        setVariableValueToObject(mojo, "manager", scmManager);

        mojo.execute();

        File outputDir = getVariableValueFromObject(mojo, "outputDirectory");

        File outputHtml = new File(outputDir, "changelog.html");

        assertTrue(outputHtml.exists(), outputHtml.getAbsolutePath() + " not generated!");

        assertTrue(outputHtml.length() > 0, outputHtml.getAbsolutePath() + " is empty!");
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/min-plugin-config.xml")
    void testMinConfig(ChangeLogReport mojo) throws Exception {
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/min-plugin-config.xml")
    void testFailedChangelog(ChangeLogReport mojo) throws Exception {
        scmManager = new FailedScmManagerStub();

        try {
            executeMojo(mojo);
        } catch (MojoExecutionException e) {
            assertEquals("Command failed.", e.getCause().getCause().getMessage(), "Test thrown exception");
        }
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/cached-plugin-config.xml")
    void testUsageOfCachedXml(ChangeLogReport mojo) throws Exception {
        File cacheFile = new File(getBasedir(), "src/test/changelog-xml/min-changelog.xml");
        cacheFile.setLastModified(System.currentTimeMillis());

        setVariableValueToObject(mojo, "outputXML", cacheFile);

        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/inv-type-plugin-config.xml")
    void testTypeException(ChangeLogReport mojo) throws Exception {
        try {
            executeMojo(mojo);

            fail("Test exception on invalid type");
        } catch (MojoExecutionException e) {
            assertTrue(
                    e.getCause().getMessage().startsWith("The type parameter has an invalid value: invalid."),
                    "Test thrown exception");
        }
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/tag-plugin-config.xml")
    void testTagType(ChangeLogReport mojo) throws Exception {
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/tags-plugin-config.xml")
    void testTagsType(ChangeLogReport mojo) throws Exception {
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/inv-date-plugin-config.xml")
    void testDateException(ChangeLogReport mojo) throws Exception {
        try {
            executeMojo(mojo);
        } catch (MojoExecutionException e) {
            assertTrue(
                    e.getCause().getCause().getMessage().startsWith("Please use this date pattern: "),
                    "Test thrown exception");
        }
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/date-plugin-config.xml")
    void testDateType(ChangeLogReport mojo) throws Exception {
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/dates-plugin-config.xml")
    void testDatesType(ChangeLogReport mojo) throws Exception {
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/hosted-plugin-config.xml")
    void testScmRepositoryWithHost(ChangeLogReport mojo) throws Exception {
        scmManager = new ScmManagerWithHostStub();
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/hosted-with-settings-plugin-config.xml")
    void testScmRepositoryWithHostFromSettings(ChangeLogReport mojo) throws Exception {
        scmManager = new ScmManagerWithHostStub();
        executeMojo(mojo);
    }

    @Test
    @InjectMojo(goal = "changelog", pom = "src/test/plugin-configs/changelog/no-scm-plugin-config.xml")
    void testNoScmConnection(ChangeLogReport mojo) throws Exception {
        try {
            executeMojo(mojo);
        } catch (MojoExecutionException e) {
            assertEquals(
                    "SCM Connection is not set.",
                    e.getCause().getCause().getCause().getMessage(),
                    "Test thrown exception");
        }
    }

    @Test
    @InjectMojo(
            goal = "changelog",
            pom = "src/test/plugin-configs/changelog/dont-display-file-and-rev-info-plugin-config.xml")
    void testDontDisplayFileAndRevInfo(ChangeLogReport mojo) throws Exception {
        File cacheFile = new File(getBasedir(), "src/test/changelog-xml/min-changelog.xml");
        cacheFile.setLastModified(System.currentTimeMillis());

        setVariableValueToObject(mojo, "outputXML", cacheFile);
        String html = executeMojo(mojo, true);

        assertFalse(html.contains("file.extension"));
        assertFalse(html.contains("file2.extension"));
        assertFalse(html.contains(" v 1"));
        assertFalse(html.contains(" v 2"));
        assertFalse(html.contains(" v 3"));
        assertFalse(html.contains(" v 4"));
    }

    @Test
    @InjectMojo(
            goal = "changelog",
            pom = "src/test/plugin-configs/changelog/display-file-and-rev-info-explicitly-plugin-config.xml")
    void testDisplayFileAndRevInfoExplicitly(ChangeLogReport mojo) throws Exception {
        File cacheFile = new File(getBasedir(), "src/test/changelog-xml/min-changelog.xml");
        cacheFile.setLastModified(System.currentTimeMillis());

        setVariableValueToObject(mojo, "outputXML", cacheFile);
        String html = executeMojo(mojo, true);

        assertTrue(html.contains("file.extension"));
        assertTrue(html.contains("file2.extension"));
        assertTrue(html.contains("v 1"));
        assertTrue(html.contains("v 2"));
        assertTrue(html.contains("v 3"));
        assertTrue(html.contains("v 4"));
    }

    private void executeMojo(ChangeLogReport mojo) throws Exception {
        executeMojo(mojo, false);
    }

    private String executeMojo(ChangeLogReport mojo, boolean withOutput) throws Exception {
        setVariableValueToObject(mojo, "manager", scmManager);

        // use current directory project as basedir
        setVariableValueToObject(mojo, "basedir", new File(getBasedir(), "src/main/java"));

        mojo.execute();

        File outputXML = getVariableValueFromObject(mojo, "outputXML");

        String encoding = getVariableValueFromObject(mojo, "outputEncoding");

        assertTrue(outputXML.exists(), "Test if changelog.xml is created");

        String changelogXml = FileUtils.fileRead(outputXML);

        assertTrue(
                changelogXml.startsWith("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"),
                "Test for xml header");

        assertTrue(changelogXml.endsWith("</changelog>"), "Test for xml footer");

        File outputDir = getVariableValueFromObject(mojo, "outputDirectory");

        File outputHtml = new File(outputDir, "changelog.html");

        assertTrue(outputHtml.exists(), outputHtml.getAbsolutePath() + " not generated!");

        assertTrue(outputHtml.length() > 0, outputHtml.getAbsolutePath() + " is empty!");

        if (!withOutput) {
            return null;
        }

        return new String(Files.readAllBytes(outputHtml.toPath()));
    }
}
