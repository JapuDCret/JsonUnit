/**
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.jsonunit.test.all;

import net.javacrumbs.jsonunit.test.base.AbstractJsonAssertTest;
import net.javacrumbs.jsonunit.test.base.JsonTestUtils;
import org.junit.Test;

import java.io.IOException;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonStructureEquals;
import static net.javacrumbs.jsonunit.JsonAssert.when;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_VALUES;
import static net.javacrumbs.jsonunit.test.base.JsonTestUtils.readByGson;
import static net.javacrumbs.jsonunit.test.base.JsonTestUtils.readByJackson1;
import static net.javacrumbs.jsonunit.test.base.JsonTestUtils.readByJackson2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AllJsonAssertTest extends AbstractJsonAssertTest {

    @Test
    public void testEqualsNode() throws IOException {
        assertJsonEquals(readByJackson1("{\"test\":1}"), readByJackson2("{\"test\": 1}"));
    }

    @Test
    public void testEqualsNodeGsonJackson() throws IOException {
        assertJsonEquals(readByGson("{\"test\":1}"), readByJackson2("{\"test\": 1}"));
    }

    @Test
    public void testEqualsNodeGson() throws IOException {
        assertJsonEquals(readByGson("{\"test\":1}"), readByGson("{\"test\": 1}"));
    }

    @Test
    public void testEqualsNodeIgnore() throws IOException {
        assertJsonEquals(readByJackson1("{\"test\":\"${json-unit.ignore}\"}"), readByJackson1("{\"test\": 1}"));
    }

    @Test
    public void testEqualsNodeFailJackson1() throws IOException {
        try {
            assertJsonEquals(readByJackson1("{\"test\":1}"), "{\"test\": 2}");
            failIfNoException();
        } catch (AssertionError e) {
            assertEquals("JSON documents are different:\nDifferent value found in node \"test\". Expected 1, got 2.\n", e.getMessage());
        }
    }

    @Test
    public void testEqualsNodeFailGson() throws IOException {
        try {
            assertJsonEquals(readByGson("{\"test\":1}"), "{\"test\": 2}");
            failIfNoException();
        } catch (AssertionError e) {
            assertEquals("JSON documents are different:\nDifferent value found in node \"test\". Expected 1, got 2.\n", e.getMessage());
        }
    }

    @Test
    public void testAssertStructureEqualsDifferentValues() {
        assertJsonStructureEquals("{\"test\": 3}", "{\"test\": {\"inner\": 5}}");
    }

    @Test
    public void testAssertEqualsDifferentTypes() {
        try {
            assertJsonEquals("{\"test\": 3}", "{\"test\": {\"inner\": 5}}", when(IGNORING_VALUES));
            failIfNoException();
        } catch (AssertionError e) {
            assertEquals("JSON documents are different:\nDifferent value found in node \"test\". Expected '3', got '{\"inner\":5}'.\n", e.getMessage());
        }
    }

    @Test
    public void testEqualsNodeStringFail() throws IOException {
        try {
            assertJsonEquals("{\"test\":\"a\"}", readByJackson2("{\"test\": \"b\"}"));
            failIfNoException();
        } catch (AssertionError e) {
            assertEquals("JSON documents are different:\nDifferent value found in node \"test\". Expected \"a\", got \"b\".\n", e.getMessage());
        }
    }

    private void failIfNoException() {
        fail("Exception expected");
    }

    protected Object readValue(String value) {
            return JsonTestUtils.readByJackson1(value);
        }
}
