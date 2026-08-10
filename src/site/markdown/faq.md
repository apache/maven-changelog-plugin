---
title: Frequently Asked Questions
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

<a id="top"></a>

# Frequently Asked Questions

1. [What is the difference between the Changelog plugin and the Changes plugin?](#What_is_the_difference_between_the_Changelog_plugin_and_the_Changes_plugin)
2. [What is Software Configuration Management or SCM?](#What_is_Software_Configuration_Management_or_SCM)
3. [Why do the dates look weird in the report when I use the dateFormat parameter?](#Why_do_the_dates_look_weird_in_the_report_when_I_use_the_dateFormat_parameter)
4. [How can I debug the SCM command?](#How_can_I_debug_the_SCM_command)
5. [My changelog report is blank, but it shouldn't be](#My_changelog_report_is_blank.2C_but_it_shouldn.27t_be)
6. [Where can I find a working configuration for this plugin?](#Where_can_I_find_a_working_configuration_for_this_plugin)
7. [How do I use this plugin with Perforce?](#How_do_I_use_this_plugin_with_Perforce.3F)
8. [The Developer Activity report is blank, but the other reports are fine](#The_Developer_Activity_report_is_blank.2C_but_the_other_reports_are_fine)
9. [I think I've found a bug in this plugin, what do I do?](#I_think_I.27ve_found_a_bug_in_this_plugin.2C_what_do_I_do)
10. [Why do the child modules of my multi module project have wrong SCM URLs?](#Why_do_the_child_modules_of_my_multi_module_project_have_wrong_SCM_URLs)

<a id="What_is_the_difference_between_the_Changelog_plugin_and_the_Changes_plugin"></a>

### What is the difference between the Changelog plugin and the Changes plugin?

The Changelog plugin generates reports based on the changes in the Software Configuration Management or SCM
while the Changes plugin generates reports either from a changes.xml file or from the JIRA issue management
system. For more information about the changes plugin, see
[http://maven.apache.org/plugins/maven-changes-plugin](http://maven.apache.org/plugins/maven-changes-plugin/)

<a id="What_is_Software_Configuration_Management_or_SCM"></a>

### What is Software Configuration Management or SCM?

According to Roger Pressman (in his book) Software Engineering: A Practitioner's Approach, SCM is a
*&quot;set of activities designed to control change by identifying the work products that are likely to
change, establishing relationships among them, defining mechanisms for managing different versions of these
work products, controlling the changes imposed, and auditing and reporting on the changes made.&quot;*

<a id="Why_do_the_dates_look_weird_in_the_report_when_I_use_the_dateFormat_parameter"></a>

### Why do the dates look weird in the report when I use the dateFormat parameter?

The `dateFormat` parameter is used when parsing the dates from the log entries retrieved from your SCM system.
It can **not** be used to format the dates in the report. If you try to do this the parsed dates will be wrong
and the dates in the report even more so. They can look like this `0020-05-27` for a file that was changed on
14 december 2005.

<a id="How_can_I_debug_the_SCM_command"></a>

### How can I debug the SCM command?

When you generate the report, you will see output like this on the command line:

```
[INFO] [changelog:changelog]
[INFO] Generating changed sets xml to: .../target/changelog.xml
[INFO] Executing: svn --non-interactive log -v -r "{2007-06-13 22:22:09 +0000}:{2007-07-14 22:22:09 +0000}" http://svn.apache.org/repos/asf/maven/plugins/trunk/maven-site-plugin
```

Copy the SCM command (everything after `Executing:`) and try to run it by itself on the command line. If it
doesn't work on the command line there's probably something wrong with the `<scm>` element in your `pom.xml`.

### My changelog report is blank, but it shouldn't be

The first thing to check is what data the changelog plugin managed to pull out of your SCM system. In the file
`target/changelog.xml` you will find that data. Have a look and see if the data in the file seems correct. If
it is not correct, then you are not getting the data you expected from your SCM system. Please check your
changelog plugin configuration.

If that doesn't help, you can try to run the maven-scm-plugin from the command line. The
maven-changelog-plugin uses Maven SCM under the hood and running the maven-scm-plugin is a great way of
verifying that the `<scm>` element in your `pom.xml` file is correct. Try this on the command line:

```
mvn scm:changelog
```

It should show you the latest changes made in your SCM. It uses the default settings, but you can change these
by specifying parameters on the command line. Read more about this in
[the scm-plugin documentation](http://maven.apache.org/scm/plugins/examples/scm-advance-features.html).

<a id="Where_can_I_find_a_working_configuration_for_this_plugin"></a>

### Where can I find a working configuration for this plugin?

The plugin itself is configured to generate a changelog report. This is done using the bare minimum of
configuration. Have a look at the `<scm>` and `<reporting>/<plugins>` elements in the
[pom.xml](http://svn.apache.org/repos/asf/maven/plugins/trunk/maven-changelog-plugin/pom.xml) of this plugin.
The generated report is found [here](changelog.html).

### How do I use this plugin with Perforce?

You have to specify your `clientspec` in the `<systemProperties>` element of this plugin's &lt;configuration&gt;
element.

```xml
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changelog-plugin</artifactId>
        <configuration>
          <systemProperties>
            <property>
              <name>maven.scm.perforce.clientspec.name</name>
              <value>your-client-spec-name</value>
            </property>
          </systemProperties>
        </configuration>
      </plugin>
```

### The Developer Activity report is blank, but the other reports are fine

You need to add [`<developer>`](http://maven.apache.org/ref/current/maven-model/maven.html#class_developer)
elements in your `pom.xml` file. Their id:s need to match the userid:s that are used in your SCM system. Only
checkins made by a developer found in the pom will be added to the Developer Activity report.

<a id="I_think_I.27ve_found_a_bug_in_this_plugin.2C_what_do_I_do"></a>

### I think I've found a bug in this plugin, what do I do?

Please follow these steps in the order they come.

1. Read all the FAQs on this page.
2. Ask a question on the user list. Please supply the necessary information so that the people on the list can
   help you. This includes the `<scm>` element from your `pom.xml`, your plugin configuration plus a rich
   description of what happens and what you expected to happen.
3. Create an issue in [issue tracker](issue-management.html).

<a id="Why_do_the_child_modules_of_my_multi_module_project_have_wrong_SCM_URLs"></a>

### Why do the child modules of my multi module project have wrong SCM URLs?

The short answer is that they don't. This is usually a misunderstanding of how the SCM URLs are inherited.

If you only define an &lt;scm&gt; element in the parent POM, the child modules will interit a value for their
SCM URLs. The path to a child directory, when using the aggregator, is *not* used when constructing the SCM
URLs. Only the artifactId is used.

If you want to examine what your POM looks like, including the SCM URLs, you can run this command:

```
mvn help:effective-pom
```
