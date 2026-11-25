
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

File outputXML = new File(basedir, "target/changelog.xml")
System.out.println("Checking for existence of xml file: " + outputXML)
String content = outputXML.getText("ISO-8859-1")
assert content.indexOf("encoding=\"ISO-8859-1\"") >= 0 : "Wrong encoding attribute in content."

File siteDir = new File( basedir, "target/site" )
System.out.println( "Checking for existence of site directory: " + siteDir )
assert siteDir.isDirectory()

String[] reports = ["changelog", "dev-activity", "file-activity"]
for ( String report : reports ) {
    File reportFile = new File( siteDir, report + ".html" )
    System.out.println( "Checking for existence of report: " + reportFile )
    assert reportFile.isFile()
}
