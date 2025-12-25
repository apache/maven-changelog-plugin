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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.manager.ScmManager;

/**
 * Generate a file activity report.
 */
@Mojo(name = "file-activity")
public class FileActivityReport extends ChangeLogReport {
    /**
     * Constructor for dependency injection.
     *
     * @param manager  the SCM manager
     * @param siteTool the site tool
     */
    public FileActivityReport(ScmManager manager, SiteTool siteTool) {
        super(manager, siteTool);
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription(Locale locale) {
        return getBundle(locale).getString("report.file-activity.description");
    }

    /**
     * {@inheritDoc}
     */
    public String getName(Locale locale) {
        return getBundle(locale).getString("report.file-activity.name");
    }

    /**
     * {@inheritDoc}
     */
    public String getOutputName() {
        return "file-activity";
    }

    /**
     * {@inheritDoc}
     */
    protected void doGenerateEmptyReport(ResourceBundle bundle, Sink sink) {
        sink.head();
        sink.title();
        sink.text(bundle.getString("report.file-activity.header"));
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();

        sink.sectionTitle1();
        sink.text(bundle.getString("report.file-activity.mainTitle"));
        sink.sectionTitle1_();

        sink.paragraph();
        sink.text("No sources found to create a report.");
        sink.paragraph_();

        sink.section1_();

        sink.body_();
        sink.flush();
        sink.close();
    }

    /**
     * {@inheritDoc}
     */
    protected void doGenerateReport(List<ChangeLogSet> changeLogSets, ResourceBundle bundle, Sink sink) {
        sink.head();
        sink.title();
        sink.text(bundle.getString("report.file-activity.header"));
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.sectionTitle1();
        sink.text(bundle.getString("report.file-activity.mainTitle"));
        sink.sectionTitle1_();

        for (ChangeLogSet set : changeLogSets) {
            doChangedSets(set, bundle, sink);
        }

        sink.section1_();
        sink.body_();

        sink.flush();
        sink.close();
    }

    /**
     * generates a section of the report referring to a changeset
     *
     * @param set    the current ChangeSet to generate this section of the report
     * @param bundle the resource bundle to retrieve report phrases from
     * @param sink   the report formatting tool
     */
    private void doChangedSets(ChangeLogSet set, ResourceBundle bundle, Sink sink) {
        sink.section2();

        doChangeSetTitle(set, bundle, sink);

        doSummary(set, bundle, sink);

        sink.table();
        sink.tableRows(new int[] {Sink.JUSTIFY_LEFT}, false);

        sink.tableRow();
        sink.tableHeaderCell();
        sink.text(bundle.getString("report.file-activity.filename"));
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text(bundle.getString("report.file-activity.timesChanged"));
        sink.tableHeaderCell_();
        sink.tableRow_();

        doRows(set, sink);

        sink.tableRows_();
        sink.table_();

        sink.section2_();
    }

    /**
     * generates the row details for the file activity report
     *
     * @param set  the changelog set to generate a report with
     * @param sink the report formatting tool
     */
    private void doRows(ChangeLogSet set, Sink sink) {
        List<List<ChangeFile>> list = getOrderedFileList(set.getChangeSets());

        initReportUrls();

        for (List<ChangeFile> revision : list) {
            ChangeFile file = revision.get(0);

            sink.tableRow();
            sink.tableCell();

            try {
                generateLinks(getConnection(), file.getName(), sink);
            } catch (Exception e) {
                if (getLog().isDebugEnabled()) {
                    getLog().error(e.getMessage(), e);
                } else {
                    getLog().error(e.getMessage());
                }

                sink.text(file.getName());
            }
            sink.tableCell_();

            sink.tableCell();
            sink.text("" + revision.size());

            sink.tableCell_();
            sink.tableRow_();
        }
    }

    /**
     * reads the change log entries and generates a list of files changed order by the number of times edited. This
     * used the FileActivityComparator Object to sort the list
     *
     * @param entries the changelog entries to generate the report
     * @return list of changed files within the SCM with the number of times changed in descending order
     */
    private List<List<ChangeFile>> getOrderedFileList(Collection<ChangeSet> entries) {
        Map<String, List<ChangeFile>> map = new HashMap<>();
        for (ChangeSet entry : entries) {
            for (ChangeFile file : entry.getFiles()) {
                List<ChangeFile> revisions = map.computeIfAbsent(file.getName(), k -> new LinkedList<>());
                revisions.add(file);
            }
        }

        List<List<ChangeFile>> list = new LinkedList<>(map.values());
        list.sort(new FileActivityComparator());
        return list;
    }
}
