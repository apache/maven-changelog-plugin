package org.apache.maven.plugin.changelog;

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

import org.apache.maven.scm.ChangeFile;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Edwin Punzalan
 */
public class FileActivityComparatorTest
{
    private FileActivityComparator comparator;

    /**
     * {@inheritDoc}
     */
    @Before
    public void setUp()
    {
        comparator = new FileActivityComparator();
    }

    @Test
    public void testCompareByNumberOfCommits()
    {
        List<ChangeFile> list1 = new ArrayList<ChangeFile>();
        list1.add( new ChangeFile( "anything" ) );

        List<ChangeFile> list2 = new ArrayList<ChangeFile>();

        assertTrue( "Test compare by commits, less than", comparator.compare( list1, list2 ) < 0 );

        list1 = new ArrayList<ChangeFile>();
        list1.add( new ChangeFile( "anything" ) );

        list2 = new ArrayList<ChangeFile>();
        list2.add( new ChangeFile( "one thing" ) );
        list2.add( new ChangeFile( "something" ) );

        assertTrue( "Test compare by commits, greater than", comparator.compare( list1, list2 ) > 0 );
    }

    @Test
    public void testCompareByRevision()
    {
        List<ChangeFile> list1 = new ArrayList<ChangeFile>();
        list1.add( new ChangeFile( "changefile-1", "123" ) );
        list1.add( new ChangeFile( "changefile-1", "234" ) );

        List<ChangeFile> list2 = new ArrayList<ChangeFile>();
        list2.add( new ChangeFile( "changefile-2", "246" ) );
        list2.add( new ChangeFile( "changefile-2", "468" ) );

        assertTrue( "Test compare by revision, less than", comparator.compare( list1, list2 ) < 0 );

        list1 = new ArrayList<ChangeFile>();
        list1.add( new ChangeFile( "changefile-1", "246" ) );
        list1.add( new ChangeFile( "changefile-1", "468" ) );

        list2 = new ArrayList<ChangeFile>();
        list2.add( new ChangeFile( "changefile-2", "123" ) );
        list2.add( new ChangeFile( "changefile-2", "234" ) );

        assertTrue( "Test compare by revision, greater than", comparator.compare( list1, list2 ) > 0 );
    }

    @Test
    public void testCompareByName()
    {
        List<ChangeFile> list1 = new ArrayList<ChangeFile>();
        list1.add( new ChangeFile( "changefile-1", "123" ) );
        list1.add( new ChangeFile( "changefile-1", "468" ) );

        List<ChangeFile> list2 = new ArrayList<ChangeFile>();
        list2.add( new ChangeFile( "changefile-2", "246" ) );
        list2.add( new ChangeFile( "changefile-2", "468" ) );

        assertTrue( "Test compare by name, less than", comparator.compare( list1, list2 ) < 0 );

        list1 = new ArrayList<ChangeFile>();
        list1.add( new ChangeFile( "changefile-1", "246" ) );
        list1.add( new ChangeFile( "changefile-1", "468" ) );

        list2 = new ArrayList<ChangeFile>();
        list2.add( new ChangeFile( "changefile-2", "123" ) );
        list2.add( new ChangeFile( "changefile-2", "234" ) );

        assertTrue( "Test compare by name, greater than", comparator.compare( list1, list2 ) > 0 );
    }
}
