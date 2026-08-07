---
title: Introduction
author: 
  - Maria Odea Ching
date: 2013-07-22
---

<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

# Apache Maven Changelog Plugin
The Maven Changelog Plugin generates reports regarding the recent changes in your Software Configuration Management or SCM. These reports include the changelog report, developer activity report and the file activity report.

## Goals Overview

The Changelog Plugin has three goals:

- [changelog:changelog](./changelog-mojo.html) generates the changelog report. The changelog report shows all the SCM activities including the dates, files, and author of the revisions that occurred during the specified change set.
- [changelog:dev-activity](./dev-activity-mojo.html) generates the developer activity report. The dev-activity report shows a summary of the total commits, and the number of files changed of each developer listed in the project descriptor.
- [changelog:file-activity](./file-activity-mojo.html) generates the file activity report. The file-activity report lists all files that were revised during the specified change set. It is arranged in a way where the file with the most number of revisions is listed first.
## Usage

General instructions on how to use the Changelog Plugin can be found on the [usage page](./usage.html). Some more specific use cases are described in the examples given below. Last but not least, users occasionally contribute additional examples, tips or errata to the [plugin's wiki page](http://docs.codehaus.org/display/MAVENUSER/Changelog+Plugin).

In case you still have questions regarding the plugin's usage, please have a look at the [FAQ](./faq.html) and feel free to contact the [user mailing list](./mailing-lists.html). The posts to the mailing list are archived and could already contain the answer to your question as part of an older thread. Hence, it is also worth browsing/searching the [mail archive](./mailing-lists.html).

If you feel like the plugin is missing a feature or has a defect, you can fill a feature request or bug report in our [issue tracker](./issue-management.html). When creating a new issue, please provide a comprehensive description of your concern. Especially for fixing bugs it is crucial that the developers can reproduce your problem. For this reason, entire debug logs, POMs or most preferably little demo projects attached to the issue are very much appreciated. Of course, patches are welcome, too. Contributors can check out the project from our [source repository](./scm.html) and will find supplementary information in the [guide to helping with Maven](http://maven.apache.org/guides/development/guide-helping.html).

## Examples

The following examples show how to use the Changelog Plugin in more advanced use-cases:

- [Selecting Reports](./examples/selecting-reports.html)
- [Using Date Type](./examples/configuration-date-type.html)
- [Using Range Type](./examples/configuration-range-type.html)
- [Using Tag Type](./examples/configuration-tag-type.html)
- [Using Perforce](./examples/using-perforce.html)
