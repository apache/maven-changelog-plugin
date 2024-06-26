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

import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;

/**
 * @author Edwin Punzalan
 */
public class FailedScmManagerStub extends ScmManagerStub {
    /**
     * {@inheritDoc}
     */
    public ScmProvider getProviderByRepository(ScmRepository scmRepository) {
        return new ScmProviderStub() {
            /** {@inheritDoc} */
            protected ChangeLogScmResult getChangeLogScmResult() {
                return new ChangeLogScmResultStub() {
                    /** {@inheritDoc} */
                    public String getCommandOutput() {
                        return "Provider Stub Commandline";
                    }

                    /** {@inheritDoc} */
                    public String getProviderMessage() {
                        return "Provider Stub Error Message";
                    }

                    /** {@inheritDoc} */
                    public boolean isSuccess() {
                        return false;
                    }
                };
            }
        };
    }
}
