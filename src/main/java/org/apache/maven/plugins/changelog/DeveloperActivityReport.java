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
 * Generate a developer activity report.
 */
@Mojo(name = "dev-activity")
public class DeveloperActivityReport extends ChangeLogReport {
    /**
     * Used to hold data while creating the report
     */
    private Map<String, List<ChangeSet>> commits;

    private Map<String, Map<String, ChangeFile>> files;

    /**
     * Constructor for dependency injection.
     *
     * @param manager  the SCM manager
     * @param siteTool the site tool
     */
    public DeveloperActivityReport(ScmManager manager, SiteTool siteTool) {
        super(manager, siteTool);
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription(Locale locale) {
        return getBundle(locale).getString("report.dev-activity.description");
    }

    /**
     * {@inheritDoc}
     */
    public String getName(Locale locale) {
        return getBundle(locale).getString("report.dev-activity.name");
    }

    /**
     * {@inheritDoc}
     */
    public String getOutputName() {
        return "dev-activity";
    }

    /**
     * {@inheritDoc}
     */
    protected void doGenerateEmptyReport(ResourceBundle bundle, Sink sink) {
        sink.head();
        sink.title();
        sink.text(bundle.getString("report.dev-activity.header"));
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();

        sink.sectionTitle1();
        sink.text(bundle.getString("report.dev-activity.mainTitle"));
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
        sink.text(bundle.getString("report.dev-activity.header"));
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.sectionTitle1();
        sink.text(bundle.getString("report.dev-activity.mainTitle"));
        sink.sectionTitle1_();

        if (developers.isEmpty()) {
            sink.paragraph();
            sink.text(bundle.getString("report.dev-activity.noDevelopers"));
            sink.paragraph_();
        } else {
            for (ChangeLogSet set : changeLogSets) {
                doChangedSets(set, bundle, sink);
            }
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
        sink.text(bundle.getString("report.dev-activity.developer"));
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text(bundle.getString("report.TotalCommits"));
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text(bundle.getString("report.dev-activity.filesChanged"));
        sink.tableHeaderCell_();
        sink.tableRow_();

        doDeveloperRows(set, sink);

        sink.tableRows_();
        sink.table_();

        sink.section2_();
    }

    /**
     * generates the report section table of the developers
     *
     * @param set  change log set generate the developer activity
     * @param sink the report formatting tool
     */
    private void doDeveloperRows(ChangeLogSet set, Sink sink) {
        initDeveloperDetails(set);

        for (Map.Entry<String, List<ChangeSet>> commit : commits.entrySet()) {
            String author = commit.getKey();

            List<ChangeSet> devCommits = commit.getValue();
            Map<String, ChangeFile> devFiles = files.get(author);

            sink.tableRow();
            sink.tableCell();

            sinkAuthorDetails(sink, author);

            sink.tableCell_();

            sink.tableCell();
            sink.text(String.valueOf(devCommits.size()));
            sink.tableCell_();

            sink.tableCell();
            sink.text(String.valueOf(devFiles.size()));
            sink.tableCell_();

            sink.tableRow_();
        }
    }

    /**
     * counts the number of commits and files changed for each developer
     *
     * @param set the change log set to generate the developer details from
     */
    private void initDeveloperDetails(ChangeLogSet set) {
        commits = new HashMap<>();

        files = new HashMap<>();

        countDevCommits(set.getChangeSets());

        countDevFiles(set.getChangeSets());
    }

    /**
     * counts the number of commits of each developer
     *
     * @param entries the change log entries used to search and count developer commits
     */
    private void countDevCommits(Collection<ChangeSet> entries) {
        for (ChangeSet entry : entries) {
            List<ChangeSet> list = commits.computeIfAbsent(entry.getAuthor(), k -> new LinkedList<>());
            list.add(entry);
        }
    }

    /**
     * counts the number of files changed by each developer
     *
     * @param entries the change log entries used to search and count file changes
     */
    private void countDevFiles(Collection<ChangeSet> entries) {
        for (ChangeSet entry : entries) {
            String developer = entry.getAuthor();
            Map<String, ChangeFile> filesMap = files.computeIfAbsent(developer, k -> new HashMap<>());
            for (ChangeFile file : entry.getFiles()) {
                filesMap.put(file.getName(), file);
            }
        }
    }
}
