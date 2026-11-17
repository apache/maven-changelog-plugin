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
package org.apache.maven.plugins.changelog.stubs;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmRequest;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;

/**
 * @author Edwin Punzalan
 */
public class ScmManagerStub implements ScmManager {
    private ScmProvider scmProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public ScmProvider getProviderByType(String string) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScmProvider getProviderByUrl(String string) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScmRepository makeProviderScmRepository(String string, File file) {
        return null;
    }

    @Override
    public Optional<ScmRepository> makeProviderScmRepository(File file) {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScmRepository makeScmRepository(String string) {
        return new ScmRepositoryStub();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> validateScmRepository(String string) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScmProvider getProviderByRepository(ScmRepository scmRepository) {
        return new ScmProviderStub();
    }

    public void setScmProvider(ScmProvider scmProvider) {
        this.scmProvider = scmProvider;
    }

    public ScmProvider getScmProvider() {
        return scmProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScmProvider(String providerType, ScmProvider provider) {
        setScmProvider(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddScmResult add(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).add(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddScmResult add(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
        return this.getProviderByRepository(repository).add(repository, fileSet, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName) throws ScmException {
        return this.getProviderByRepository(repository).branch(repository, fileSet, branchName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName, String message)
            throws ScmException {
        return this.getProviderByRepository(repository).branch(repository, fileSet, branchName, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeLogScmResult changeLog(
            ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, ScmBranch branch)
            throws ScmException {
        return this.getProviderByRepository(repository)
                .changeLog(repository, fileSet, startDate, endDate, numDays, branch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeLogScmResult changeLog(
            ScmRepository repository,
            ScmFileSet fileSet,
            Date startDate,
            Date endDate,
            int numDays,
            ScmBranch branch,
            String datePattern)
            throws ScmException {
        return this.getProviderByRepository(repository)
                .changeLog(repository, fileSet, startDate, endDate, numDays, branch, datePattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeLogScmResult changeLog(
            ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion)
            throws ScmException {
        return this.getProviderByRepository(repository).changeLog(repository, fileSet, startVersion, endVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeLogScmResult changeLog(
            ScmRepository repository,
            ScmFileSet fileSet,
            ScmVersion startRevision,
            ScmVersion endRevision,
            String datePattern)
            throws ScmException {
        return this.getProviderByRepository(repository)
                .changeLog(repository, fileSet, startRevision, endRevision, datePattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
        return this.getProviderByRepository(repository).checkIn(repository, fileSet, message);
    }

    @Override
    public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, CommandParameters commandParameters)
            throws ScmException {
        return this.getProviderByRepository(repository).checkIn(repository, fileSet, commandParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, ScmVersion revision, String message)
            throws ScmException {
        return this.getProviderByRepository(repository).checkIn(repository, fileSet, revision, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).checkOut(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, ScmVersion version)
            throws ScmException {
        return this.getProviderByRepository(repository).checkOut(repository, fileSet, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, boolean recursive)
            throws ScmException {
        return this.getProviderByRepository(repository).checkOut(repository, fileSet, recursive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckOutScmResult checkOut(
            ScmRepository repository, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
        return this.getProviderByRepository(repository).checkOut(repository, fileSet, version, recursive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiffScmResult diff(
            ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion)
            throws ScmException {
        return this.getProviderByRepository(repository).diff(repository, fileSet, startVersion, endVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditScmResult edit(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).edit(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).export(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, ScmVersion version)
            throws ScmException {
        return this.getProviderByRepository(repository).export(repository, fileSet, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, String outputDirectory) {
        return this.export(repository, fileSet, outputDirectory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExportScmResult export(
            ScmRepository repository, ScmFileSet fileSet, ScmVersion version, String outputDirectory)
            throws ScmException {
        return this.getProviderByRepository(repository).export(repository, fileSet, version, outputDirectory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListScmResult list(ScmRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion version)
            throws ScmException {
        return this.getProviderByRepository(repository).list(repository, fileSet, recursive, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoveScmResult remove(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
        return this.getProviderByRepository(repository).remove(repository, fileSet, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusScmResult status(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).status(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName) throws ScmException {
        return this.getProviderByRepository(repository).tag(repository, fileSet, tagName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName, String message)
            throws ScmException {
        return this.getProviderByRepository(repository).tag(repository, fileSet, tagName, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnEditScmResult unedit(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).unedit(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, boolean runChangelog)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, runChangelog);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(
            ScmRepository repository, ScmFileSet fileSet, ScmVersion version, boolean runChangelog)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, version, runChangelog);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String datePattern)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, (ScmVersion) null, datePattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, String datePattern)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, version, datePattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, Date lastUpdate) throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, (ScmVersion) null, lastUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, Date lastUpdate)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, version, lastUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, Date lastUpdate, String datePattern)
            throws ScmException {
        return this.getProviderByRepository(repository)
                .update(repository, fileSet, (ScmVersion) null, lastUpdate, datePattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateScmResult update(
            ScmRepository repository, ScmFileSet fileSet, ScmVersion version, Date lastUpdate, String datePattern)
            throws ScmException {
        return this.getProviderByRepository(repository).update(repository, fileSet, version, lastUpdate, datePattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScmProviderImplementation(String providerType, String providerImplementation) {
        // nop
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlameScmResult blame(ScmRepository arg0, ScmFileSet arg1, String arg2) throws ScmException {

        return this.getProviderByRepository(arg0).blame(arg0, arg1, arg2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MkdirScmResult mkdir(ScmRepository arg0, ScmFileSet arg1, String arg2, boolean arg3) throws ScmException {

        return this.getProviderByRepository(arg0).mkdir(arg0, arg1, arg2, arg3);
    }

    @Override
    public ChangeLogScmResult changeLog(ChangeLogScmRequest scmRequest) {
        return null;
    }

    @Override
    public BlameScmResult blame(BlameScmRequest blameScmRequest) {
        return null;
    }
}
