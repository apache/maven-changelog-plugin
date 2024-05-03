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
package org.apache.maven.plugin.changelog;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.scm.ChangeFile;

/**
 * Object used to sort the file-activity report into descending order.
 *
 * @version $Id$
 */
public class FileActivityComparator implements Comparator<List<ChangeFile>> {
    /**
     * {@inheritDoc}
     */
    public int compare(List<ChangeFile> list1, List<ChangeFile> list2) throws ClassCastException {
        int returnValue = sortByCommits(list1, list2);

        if (returnValue != 0) {
            return returnValue;
        }

        returnValue = sortByRevision(list1, list2);

        if (returnValue != 0) {
            return returnValue;
        }

        return sortByName(list1, list2);
    }

    /**
     * compares list1 and list2 by the number of commits
     *
     * @param list1 the first object in a compare statement
     * @param list2 the object to compare list1 against
     * @return an integer describing the order comparison of list1 and list2
     */
    private int sortByCommits(List<ChangeFile> list1, List<ChangeFile> list2) {
        if (list1.size() > list2.size()) {
            return -1;
        }

        if (list1.size() < list2.size()) {
            return 1;
        }

        return 0;
    }

    /**
     * compares list1 and list2 by comparing their revision code
     *
     * @param list1 the first object in a compare statement
     * @param list2 the object to compare list1 against
     * @return an integer describing the order comparison of list1 and list2
     */
    private int sortByRevision(List<ChangeFile> list1, List<ChangeFile> list2) {
        String revision1 = getLatestRevision(list1);

        String revision2 = getLatestRevision(list2);

        if (revision1 == null) {
            return -1;
        }

        if (revision2 == null) {
            return 1;
        }

        return revision1.compareTo(revision2);
    }

    /**
     * retrieves the latest revision from the commits made from the SCM
     *
     * @param list The list of revisions from the file
     * @return the latest revision code
     */
    private String getLatestRevision(List<ChangeFile> list) {
        String latest = "";

        for (ChangeFile file : list) {
            if (StringUtils.isNotBlank(latest)) {
                latest = file.getRevision();
            } else if (latest.compareTo(file.getRevision()) < 0) {
                latest = file.getRevision();
            }
        }

        return latest;
    }

    /**
     * compares list1 and list2 by comparing their filenames. Least priority sorting when both number of commits and
     * and revision are the same
     *
     * @param list1 the first object in a compare statement
     * @param list2 the object to compare list1 against
     * @return an integer describing the order comparison of list1 and list2
     */
    private int sortByName(List<ChangeFile> list1, List<ChangeFile> list2) {
        ChangeFile file1 = list1.get(0);

        ChangeFile file2 = list2.get(0);

        return file1.getName().compareTo(file2.getName());
    }
}
