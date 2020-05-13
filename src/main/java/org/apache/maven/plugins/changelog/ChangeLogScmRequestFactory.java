package org.apache.maven.plugins.changelog;

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

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.repository.ScmRepository;

import java.io.File;
import java.util.Date;

/**
 * Factory to create the {@link ChangeLogScmRequest}
 */
class ChangeLogScmRequestFactory
{
    /**
     * Creates a changelog request between two dates wrapping detailed parameters for the changelog command
     *
     * @param repository the source control system
     * @param basedir    Input dir. Directory where the files under SCM control are located
     * @param startDate  the start date of the period
     * @param endDate    the end date of the period
     * @return the changelog scm request wrapping detailed parameters for the changelog command
     * @throws ScmException
     */
    static ChangeLogScmRequest requestForDate( ScmRepository repository, File basedir, Date startDate, Date endDate )
            throws ScmException
    {
        ChangeLogScmRequest request = new ChangeLogScmRequest( repository, new ScmFileSet( basedir ) );
        request.setStartDate( startDate );
        request.setEndDate( endDate );
        return request;
    }

    /**
     * Creates a scm changelog request between two scm revisions.
     *
     * @param repository    the source control system
     * @param basedir       Input dir. Directory where the files under SCM control are located
     * @param startRevision the start revision
     * @param endRevision   the end revision
     * @return the changelog scm request wrapping the parameters for the changelog command
     * @throws ScmException
     */
    static ChangeLogScmRequest requestForRevision( ScmRepository repository, File basedir, String startRevision,
                                                   String endRevision ) throws ScmException
    {
        ChangeLogScmRequest request = new ChangeLogScmRequest( repository, new ScmFileSet( basedir ) );
        request.setStartRevision( new ScmRevision( startRevision ) );
        request.setEndRevision( new ScmRevision( endRevision ) );
        return request;
    }

    /**
     * Creates a scm changelog request between current date and number of days backwards in time
     *
     * @param repository the source control system
     * @param basedir    Input dir. Directory where the files under SCM control are located
     * @param range      number of days from current date
     * @param dateFormat the date pattern use in changelog output returned by scm tool
     * @return the changelog scm request wrapping the parameters for the changelog command
     * @throws ScmException
     */
    static ChangeLogScmRequest requestForRange( ScmRepository repository, File basedir, int range, String dateFormat )
            throws ScmException
    {
        ChangeLogScmRequest request = new ChangeLogScmRequest( repository, new ScmFileSet( basedir ) );
        request.setNumDays( range );
        request.setDatePattern( dateFormat );
        return request;
    }
}
