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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Edwin Punzalan
 */
public class ChangeLogTest {
    @Test
    public void testReadFile() throws Exception {
        List<ChangeLogSet> changedLogs = readChangeLogXml("min-changelog.xml");

        assertNotNull(changedLogs, "Test changedSets were parsed");

        assertEquals(2, changedLogs.size(), "Test number of changelog entries");

        ChangeLogSet changelogSets = changedLogs.get(0);

        assertEquals(
                2,
                changelogSets.getChangeSets().size(),
                "Test number of revisions on changelog 1");

        ChangeSet changeSet = changelogSets.getChangeSets().get(0);

        assertEquals(
                "1977-08-06 05:30:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()),
                "Test changelog 1 set 1 date/time");

        assertEquals("Edwin Punzalan", changeSet.getAuthor(), "Test changelog 1 set 1 author");

        assertEquals("First commit msg", changeSet.getComment(), "Test changelog 1 set 1 msg");

        assertEquals(1, changeSet.getFiles().size(), "Test changelog 1 set 1 files");

        ChangeFile changeFile = changeSet.getFiles().get(0);

        assertEquals("/path/to/file.extension", changeFile.getName(), "Test changelog 1 set 1 file 1 filename");

        assertEquals("1", changeFile.getRevision(), "Test changelog 1 set 1 file 1 revision");

        changeSet = changelogSets.getChangeSets().get(1);

        assertEquals(
                "2005-02-24 21:30:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()),
                "Test changelog 1 set 2 date/time");

        assertEquals("Edwin Punzalan", changeSet.getAuthor(), "Test changelog 1 set 2 author");

        assertEquals("Second commit msg", changeSet.getComment(), "Test changelog 1 set 2 msg");

        assertEquals(2, changeSet.getFiles().size(), "Test changelog 1 set 2 files");

        changeFile = changeSet.getFiles().get(0);

        assertEquals("/path/to/file.extension", changeFile.getName(), "Test changelog 1 set 2 file 1 filename");

        assertEquals("2", changeFile.getRevision(), "Test changelog 1 set 2 file 1 revision");

        changeFile = changeSet.getFiles().get(1);

        assertEquals("/path/to/file2.extension", changeFile.getName(), "Test changelog 1 set 2 file 2 filename");

        assertEquals("2", changeFile.getRevision(), "Test changelog 1 set 2 file 2 revision");

        changelogSets = changedLogs.get(1);

        assertEquals(
                2,
                changelogSets.getChangeSets().size(),
                "Test number of revisions on changelog 2");

        changeSet = changelogSets.getChangeSets().get(0);

        assertEquals(
                "2005-02-25 22:45:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()),
                "Test changelog 2 set 1 date/time");

        assertEquals("Keogh Edrich Punzalan", changeSet.getAuthor(), "Test changelog 2 set 1 author");

        assertEquals("Third commit msg", changeSet.getComment(), "Test changelog 2 set 1 msg");

        assertEquals(1, changeSet.getFiles().size(), "Test changelog 2 set 1 files");

        changeFile = changeSet.getFiles().get(0);

        assertEquals("/path/to/file.extension", changeFile.getName(), "Test changelog 2 set 1 file 1 filename");

        assertEquals("3", changeFile.getRevision(), "Test changelog 2 set 1 file 1 revision");

        changeSet = changelogSets.getChangeSets().get(1);

        assertEquals(
                "2100-02-25 05:30:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(changeSet.getDate()),
                "Test changelog 2 set 2 date/time");

        assertEquals("Keogh Edrich Punzalan", changeSet.getAuthor(), "Test changelog 2 set 2 author");

        assertEquals("Last commit msg", changeSet.getComment(), "Test changelog 2 set 2 msg");

        assertEquals(2, changeSet.getFiles().size(), "Test changelog 2 set 2 files");

        changeFile = changeSet.getFiles().get(0);

        assertEquals("/path/to/file.extension", changeFile.getName(), "Test changelog 2 set 2 file 1 filename");

        assertEquals("4", changeFile.getRevision(), "Test changelog 2 set 2 file 1 revision");

        changeFile = changeSet.getFiles().get(1);

        assertEquals("/path/to/file2.extension", changeFile.getName(), "Test changelog 2 set 2 file 2 filename");

        assertEquals("4", changeFile.getRevision(), "Test changelog 2 set 2 file 2 revision");
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
