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
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Edwin Punzalan
 */
public class ChangeLogTest {
    @Test
    public void testReadFile() throws Exception {
        List<ChangeLogSet> changedLogs = readChangeLogXml("min-changelog.xml");

        assertNotNull("Test changedSets were parsed", changedLogs);

        assertEquals("Test number of changelog entries", 2, changedLogs.size());

        ChangeLogSet changelogSets = changedLogs.get(0);

        assertEquals(
                "Test number of revisions on changelog 1",
                2,
                changelogSets.getChangeSets().size());

        ChangeSet changeSet = changelogSets.getChangeSets().get(0);

        assertEquals(
                "Test changelog 1 set 1 date/time",
                "1977-08-06 05:30:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()));

        assertEquals("Test changelog 1 set 1 author", "Edwin Punzalan", changeSet.getAuthor());

        assertEquals("Test changelog 1 set 1 msg", "First commit msg", changeSet.getComment());

        assertEquals("Test changelog 1 set 1 files", 1, changeSet.getFiles().size());

        ChangeFile changeFile = changeSet.getFiles().get(0);

        assertEquals("Test changelog 1 set 1 file 1 filename", "/path/to/file.extension", changeFile.getName());

        assertEquals("Test changelog 1 set 1 file 1 revision", "1", changeFile.getRevision());

        changeSet = changelogSets.getChangeSets().get(1);

        assertEquals(
                "Test changelog 1 set 2 date/time",
                "2005-02-24 21:30:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()));

        assertEquals("Test changelog 1 set 2 author", "Edwin Punzalan", changeSet.getAuthor());

        assertEquals("Test changelog 1 set 2 msg", "Second commit msg", changeSet.getComment());

        assertEquals("Test changelog 1 set 2 files", 2, changeSet.getFiles().size());

        changeFile = changeSet.getFiles().get(0);

        assertEquals("Test changelog 1 set 2 file 1 filename", "/path/to/file.extension", changeFile.getName());

        assertEquals("Test changelog 1 set 2 file 1 revision", "2", changeFile.getRevision());

        changeFile = changeSet.getFiles().get(1);

        assertEquals("Test changelog 1 set 2 file 2 filename", "/path/to/file2.extension", changeFile.getName());

        assertEquals("Test changelog 1 set 2 file 2 revision", "2", changeFile.getRevision());

        changelogSets = changedLogs.get(1);

        assertEquals(
                "Test number of revisions on changelog 2",
                2,
                changelogSets.getChangeSets().size());

        changeSet = changelogSets.getChangeSets().get(0);

        assertEquals(
                "Test changelog 2 set 1 date/time",
                "2005-02-25 22:45:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()));

        assertEquals("Test changelog 2 set 1 author", "Keogh Edrich Punzalan", changeSet.getAuthor());

        assertEquals("Test changelog 2 set 1 msg", "Third commit msg", changeSet.getComment());

        assertEquals("Test changelog 2 set 1 files", 1, changeSet.getFiles().size());

        changeFile = changeSet.getFiles().get(0);

        assertEquals("Test changelog 2 set 1 file 1 filename", "/path/to/file.extension", changeFile.getName());

        assertEquals("Test changelog 2 set 1 file 1 revision", "3", changeFile.getRevision());

        changeSet = changelogSets.getChangeSets().get(1);

        assertEquals(
                "Test changelog 2 set 2 date/time",
                "2100-02-25 05:30:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()));

        assertEquals("Test changelog 2 set 2 author", "Keogh Edrich Punzalan", changeSet.getAuthor());

        assertEquals("Test changelog 2 set 2 msg", "Last commit msg", changeSet.getComment());

        assertEquals("Test changelog 2 set 2 files", 2, changeSet.getFiles().size());

        changeFile = changeSet.getFiles().get(0);

        assertEquals("Test changelog 2 set 2 file 1 filename", "/path/to/file.extension", changeFile.getName());

        assertEquals("Test changelog 2 set 2 file 1 revision", "4", changeFile.getRevision());

        changeFile = changeSet.getFiles().get(1);

        assertEquals("Test changelog 2 set 2 file 2 filename", "/path/to/file2.extension", changeFile.getName());

        assertEquals("Test changelog 2 set 2 file 2 revision", "4", changeFile.getRevision());
    }

    private List<ChangeLogSet> readChangeLogXml(String filename) throws Exception {
        File inputFile = new File(getBasedir(), "src/test/changelog-xml/" + filename);
        InputStream in = Files.newInputStream(inputFile.toPath());

        return ChangeLog.loadChangedSets(in);
    }

    private String getBasedir() {
        return System.getProperty("basedir");
    }
}
