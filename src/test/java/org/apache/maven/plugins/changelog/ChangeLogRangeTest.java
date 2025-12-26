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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.maven.plugins.changelog.stubs.ScmManagerStub;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;

/**
 * Unit test to verify that the changelog range type now uses actual dates
 * instead of null values when calling the SCM provider.
 *
 * @author Maven Changelog Plugin Team
 */
public class ChangeLogRangeTest extends AbstractChangeLogReportTest {

    /**
     * Custom ScmManager that captures the dates passed to changeLog
     */
    private static class DateCapturingScmManagerStub extends ScmManagerStub {
        private Date capturedStartDate;
        private Date capturedEndDate;
        private int capturedNumDays;
        private String capturedDateFormat;

        @Override
        public ScmProvider getProviderByRepository(ScmRepository scmRepository) {
            return new DateCapturingScmProviderStub(this);
        }

        public Date getCapturedStartDate() {
            return capturedStartDate;
        }

        public Date getCapturedEndDate() {
            return capturedEndDate;
        }

        public int getCapturedNumDays() {
            return capturedNumDays;
        }

        public String getCapturedDateFormat() {
            return capturedDateFormat;
        }
    }

    /**
     * Custom ScmProvider that captures the dates passed to changeLog
     */
    private static class DateCapturingScmProviderStub extends org.apache.maven.plugins.changelog.stubs.ScmProviderStub {
        private final DateCapturingScmManagerStub manager;

        DateCapturingScmProviderStub(DateCapturingScmManagerStub manager) {
            this.manager = manager;
        }

        @Override
        public ChangeLogScmResult changeLog(
                ScmRepository scmRepository,
                ScmFileSet scmFileSet,
                Date startDate,
                Date endDate,
                int numDays,
                ScmBranch scmBranch,
                String datePattern) {
            // Capture the parameters
            manager.capturedStartDate = startDate;
            manager.capturedEndDate = endDate;
            manager.capturedNumDays = numDays;
            manager.capturedDateFormat = datePattern;

            // Return the standard result
            return super.getChangeLogScmResult();
        }
    }

    /**
     * Test that range type with default range (30 days) passes actual dates to SCM provider
     */
    public void testRangeTypeWithDefaultRangePassesActualDates() throws Exception {
        DateCapturingScmManagerStub scmManager = new DateCapturingScmManagerStub();

        File pluginXmlFile = new File(getBasedir(), "src/test/plugin-configs/changelog/range-plugin-config.xml");

        ChangeLogReport mojo = (ChangeLogReport) lookupMojo("changelog", pluginXmlFile);

        assertNotNull("Mojo found.", mojo);

        this.setVariableValueToObject(mojo, "manager", scmManager);

        mojo.execute();

        // Verify that actual dates were passed (not null)
        assertNotNull("Start date should not be null", scmManager.getCapturedStartDate());
        assertNotNull("End date should not be null", scmManager.getCapturedEndDate());

        // Verify that the dates are approximately 30 days apart
        long diffInMillis = scmManager.getCapturedEndDate().getTime()
                - scmManager.getCapturedStartDate().getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

        assertEquals("Date range should be 30 days", 30, Math.abs(diffInDays));

        // Verify that numDays is 0 (not used when dates are provided)
        assertEquals("numDays should be 0 when dates are provided", 0, scmManager.getCapturedNumDays());

        // Verify that dateFormat was passed
        assertNotNull("Date format should not be null", scmManager.getCapturedDateFormat());
    }

    /**
     * Test that range type with custom range (-7 days) passes actual dates to SCM provider
     */
    public void testRangeTypeWithCustomRangePassesActualDates() throws Exception {
        DateCapturingScmManagerStub scmManager = new DateCapturingScmManagerStub();

        File pluginXmlFile = new File(getBasedir(), "src/test/plugin-configs/changelog/range-custom-plugin-config.xml");

        ChangeLogReport mojo = (ChangeLogReport) lookupMojo("changelog", pluginXmlFile);

        assertNotNull("Mojo found.", mojo);

        this.setVariableValueToObject(mojo, "manager", scmManager);

        mojo.execute();

        // Verify that actual dates were passed (not null)
        assertNotNull("Start date should not be null", scmManager.getCapturedStartDate());
        assertNotNull("End date should not be null", scmManager.getCapturedEndDate());

        // Verify that the dates are approximately 7 days apart
        long diffInMillis = scmManager.getCapturedEndDate().getTime()
                - scmManager.getCapturedStartDate().getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

        assertEquals("Date range should be 7 days", 7, Math.abs(diffInDays));

        // Verify that numDays is 0 (not used when dates are provided)
        assertEquals("numDays should be 0 when dates are provided", 0, scmManager.getCapturedNumDays());
    }

    /**
     * Test that the dates are calculated correctly based on the current time
     */
    public void testRangeTypeCalculatesCorrectDates() throws Exception {
        DateCapturingScmManagerStub scmManager = new DateCapturingScmManagerStub();

        File pluginXmlFile = new File(getBasedir(), "src/test/plugin-configs/changelog/range-plugin-config.xml");

        ChangeLogReport mojo = (ChangeLogReport) lookupMojo("changelog", pluginXmlFile);

        assertNotNull("Mojo found.", mojo);

        this.setVariableValueToObject(mojo, "manager", scmManager);

        // Capture the time before execution
        Instant beforeExecution = Instant.now();

        mojo.execute();

        // Capture the time after execution
        Instant afterExecution = Instant.now();

        // Verify that the end date is approximately now
        Instant capturedEndInstant = scmManager.getCapturedEndDate().toInstant();
        assertTrue(
                "End date should be close to current time",
                capturedEndInstant.isAfter(beforeExecution.minus(1, ChronoUnit.SECONDS))
                        && capturedEndInstant.isBefore(afterExecution.plus(1, ChronoUnit.SECONDS)));

        // Verify that the start date is approximately 30 days before the end date
        Instant capturedStartInstant = scmManager.getCapturedStartDate().toInstant();
        Instant expectedStartInstant = capturedEndInstant.plus(-30, ChronoUnit.DAYS);

        // Allow for some variance (within 1 second)
        long diffInSeconds = Math.abs(capturedStartInstant.getEpochSecond() - expectedStartInstant.getEpochSecond());
        assertTrue("Start date should be 30 days before end date (within 1 second)", diffInSeconds < 2);
    }

    /**
     * Test that positive range values work correctly (7 days in the future)
     */
    public void testRangeTypeWithPositiveRange() throws Exception {
        DateCapturingScmManagerStub scmManager = new DateCapturingScmManagerStub();

        File pluginXmlFile =
                new File(getBasedir(), "src/test/plugin-configs/changelog/range-positive-plugin-config.xml");

        ChangeLogReport mojo = (ChangeLogReport) lookupMojo("changelog", pluginXmlFile);

        assertNotNull("Mojo found.", mojo);

        this.setVariableValueToObject(mojo, "manager", scmManager);

        mojo.execute();

        // Verify that actual dates were passed (not null)
        assertNotNull("Start date should not be null", scmManager.getCapturedStartDate());
        assertNotNull("End date should not be null", scmManager.getCapturedEndDate());

        // With positive range, start date should be after end date
        assertTrue(
                "Start date should be after end date with positive range",
                scmManager.getCapturedStartDate().after(scmManager.getCapturedEndDate()));

        // Verify that the dates are approximately 7 days apart
        long diffInMillis = scmManager.getCapturedStartDate().getTime()
                - scmManager.getCapturedEndDate().getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

        assertEquals("Date range should be 7 days", 7, diffInDays);
    }
}
