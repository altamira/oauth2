--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements
INSERT INTO SS_USER (ID, LAST_MODIFIED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME  ) VALUES (11, 1234,  'jyotirmaya', 'dehury', 'mindfire', 'jyotirmayad')
INSERT INTO SS_USER (ID, LAST_MODIFIED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME  ) VALUES (21, 1234,  'Helio', 'Toda', 'altamira', 'helio.toda')
INSERT INTO SS_USER (ID, LAST_MODIFIED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME  ) VALUES (31, 1234,  'Roberto', 'Zelli', 'altamira', 'roberto.zelli')
INSERT INTO SS_USER (ID, LAST_MODIFIED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME  ) VALUES (41, 1234,  'Alessandro', 'Holanda', 'altamira', 'alessandro.holanda')
INSERT INTO SS_PROFILE (ID, LAST_MODIFIED, NAME  ) VALUES (55, 1234, 'Admin')
INSERT INTO SS_PROFILE (ID, LAST_MODIFIED, NAME  ) VALUES (65, 1234, 'Manufacturing')
INSERT INTO SS_PROFILE (ID, LAST_MODIFIED, NAME  ) VALUES (75, 1234, 'Sales')