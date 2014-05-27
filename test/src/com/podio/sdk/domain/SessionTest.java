/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.domain;

import android.test.AndroidTestCase;

public class SessionTest extends AndroidTestCase {

    /**
     * Verifies that a {@link Session} object can be created from a valid JSON
     * string with known attributes.
     * 
     * <pre>
     * 
     * 1. Define the test JSON string. Make sure it has the 'access_token',
     *      'refresh_token' and 'expires' attributes.
     * 
     * 2. Call the Session constructor with the test JSON.
     * 
     * 3. Verify that the Session fields have been initialized according to the
     *      JSON attributes.
     * 
     * </pre>
     */
    public void testCanCreateSessionWithCustomValuesFromKnownJsonString() {
        String json = "{\"access_token\": \"ACCESSTOKEN\", \"refresh_token\": \"REFRESHTOKEN\", \"expires\": 3600}";
        Session session = new Session(json);

        assertNotNull(session);
        assertEquals("ACCESSTOKEN", session.accessToken);
        assertEquals("REFRESHTOKEN", session.refreshToken);
        assertEquals(3600L, session.expiresMillis);
    }

    /**
     * Verifies that a {@link Session} object can be created from an invalid
     * JSON string and that it's initialized with default values instead.
     * 
     * <pre>
     * 
     * 1. Define a syntactically invalid test JSON string.
     * 
     * 2. Call the Session constructor with the test JSON.
     * 
     * 3. Verify that the Session fields have been initialized with default
     *      values.
     * 
     * </pre>
     */
    public void testCanCreateSessionWithDefaultValuesFromInvalidJsonString() {
        String json = "{\"missing_end_quot_string_attribute: UNKNOWNTYPE}";
        Session session = new Session(json);

        assertNotNull(session);
        assertEquals(null, session.accessToken);
        assertEquals(null, session.refreshToken);
        assertEquals(0L, session.expiresMillis);
    }

    /**
     * Verifies that a {@link Session} object can be created from a null pointer
     * JSON string and that it's initialized with default values instead.
     * 
     * <pre>
     * 
     * 1. Define a null pointer test JSON string.
     * 
     * 2. Call the Session constructor with the test JSON.
     * 
     * 3. Verify that the Session fields have been initialized with default
     *      values.
     * 
     * </pre>
     */
    public void testCanCreateSessionWithDefaultValuesFromNullPointerJsonString() {
        String json = null;
        Session session = new Session(json);

        assertNotNull(session);
        assertEquals(null, session.accessToken);
        assertEquals(null, session.refreshToken);
        assertEquals(0L, session.expiresMillis);
    }

    /**
     * Verifies that a {@link Session} object can be created from a valid JSON
     * string with unknown attributes and that it's initialized with default
     * values instead.
     * 
     * <pre>
     * 
     * 1. Define the test JSON string. Make sure it doesn't have any known
     *      attributes.
     * 
     * 2. Call the Session constructor with the test JSON.
     * 
     * 3. Verify that the Session fields have been initialized with default
     *      values.
     * 
     * </pre>
     */
    public void testCanCreateSessionWithDefaultValuesFromUnknownJsonString() {
        String json = "{\"unknown_attribute\": \"UNKNOWNVALUE\"}";
        Session session = new Session(json);

        assertNotNull(session);
        assertEquals(null, session.accessToken);
        assertEquals(null, session.refreshToken);
        assertEquals(0L, session.expiresMillis);
    }

    /**
     * Verifies that the {@link Session} object can be serialized to valid JSON.
     * 
     * <pre>
     * 
     * 1. Create a new Session object with known values.
     * 
     * 2. Serialize it to JSON notation.
     * 
     * 3. Verify the integrity of the JSON string.
     * 
     * </pre>
     */
    public void testCanSerializeToJsonString() {
        String sourceJson = "{\"access_token\":\"ACCESSTOKEN\",\"expires\":3600,\"refresh_token\":\"REFRESHTOKEN\"}";
        Session session = new Session(sourceJson);
        assertEquals("ACCESSTOKEN", session.accessToken);
        assertEquals("REFRESHTOKEN", session.refreshToken);
        assertEquals(3600L, session.expiresMillis);

        String serialized = session.toJson();
        assertEquals(sourceJson, serialized);
    }

    /**
     * Verifies that the {@link Session} claims itself to be authorized when it
     * contains non-empty tokens and expires time stamp.
     * 
     * <pre>
     * 
     * 1. Create a Session object with non-empty access token and
     *      refresh token + an expires time stamp in the future.
     * 
     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
     * 
     * 3. Verify that it claims "yes".
     * 
     * </pre>
     */
    public void testIsAuthorizedWithNonEmptyTokensAndTimeStamp() {
        Session session1 = new Session("ACCESSTOKEN", "REFRESHTOKEN", 3600L);
        assertEquals(true, session1.isAuthorized());
        assertEquals(false, session1.notAuthorized());

        Session session2 = new Session(
                "{access_token: 'ACCESSTOKEN', refresh_token:'REFRESHTOKEN', expires:3600}");
        assertEquals(true, session2.isAuthorized());
        assertEquals(false, session1.notAuthorized());
    }

    /**
     * Verifies that the {@link Session} doesn't claim itself to be authorized
     * when it contains an empty auth token.
     * 
     * <pre>
     * 
     * 1. Create a Session object with an empty access token.
     * 
     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
     * 
     * 3. Verify that it claims "no".
     * 
     * </pre>
     */
    public void testIsNotAuthorizedWithEmptyAccessToken() {
        Session session1 = new Session("", "REFRESHTOKEN", 3600L);
        assertEquals(false, session1.isAuthorized());
        assertEquals(true, session1.notAuthorized());

        Session session2 = new Session(null, "REFRESHTOKEN", 3600L);
        assertEquals(false, session2.isAuthorized());
        assertEquals(true, session2.notAuthorized());

        Session session3 = new Session(
                "{access_token: '', refresh_token:'REFRESHTOKEN', expires:3600}");
        assertEquals(false, session3.isAuthorized());
        assertEquals(true, session3.notAuthorized());

        Session session4 = new Session("{refresh_token:'REFRESHTOKEN', expires:3600}");
        assertEquals(false, session4.isAuthorized());
        assertEquals(true, session4.notAuthorized());
    }

    /**
     * Verifies that the {@link Session} doesn't claim itself to be authorized
     * when it contains an empty expires time stamp.
     * 
     * <pre>
     * 
     * 1. Create a Session object with an old time stamp.
     * 
     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
     * 
     * 3. Verify that it claims "no".
     * 
     * </pre>
     */
    public void testIsNotAuthorizedWithEmptyExpiresTimeStamp() {
        long currentTimeStamp = System.currentTimeMillis() + 2;
        Session session1 = new Session("ACCESSTOKEN", "REFRESHTOKEN", -currentTimeStamp);
        assertEquals(false, session1.isAuthorized());
        assertEquals(true, session1.notAuthorized());

        Session session2 = new Session(
                "{access_token: 'ACCESSTOKEN', refresh_token:'REFRESHTOKEN', expires:0}");
        assertEquals(false, session2.isAuthorized());
        assertEquals(true, session2.notAuthorized());

        Session session3 = new Session(
                "{access_token: 'ACCESSTOKEN', refresh_token:'REFRESHTOKEN'}");
        assertEquals(false, session3.isAuthorized());
        assertEquals(true, session3.notAuthorized());
    }

    /**
     * Verifies that the {@link Session} doesn't claim itself to be authorized
     * when it contains an empty refresh token.
     * 
     * <pre>
     * 
     * 1. Create a Session object with an empty refresh token.
     * 
     * 2. Ask it if it thinks it has grounds for assuming an authorized state.
     * 
     * 3. Verify that it claims "no".
     * 
     * </pre>
     */
    public void testIsNotAuthorizedWithEmptyRefreshToken() {
        Session session1 = new Session("ACCESSTOKEN", "", 3600L);
        assertEquals(false, session1.isAuthorized());
        assertEquals(true, session1.notAuthorized());

        Session session2 = new Session("ACCESSTOKEN", null, 3600L);
        assertEquals(false, session2.isAuthorized());
        assertEquals(true, session2.notAuthorized());

        Session session3 = new Session(
                "{access_token: 'ACCESSTOKEN', refresh_token:'', expires:3600}");
        assertEquals(false, session3.isAuthorized());
        assertEquals(true, session3.notAuthorized());

        Session session4 = new Session("{access_token:'ACCESSTOKEN', expires:3600}");
        assertEquals(false, session4.isAuthorized());
        assertEquals(true, session4.notAuthorized());
    }

    /**
     * Verifies that the {@link Session#equals(Object)} method returns false on
     * two different instances with different values. This test also verifies
     * that the {@link Session#hashCode()} method returns different values from
     * two different instances with different values.
     * 
     * <pre>
     * 
     * 1. Create two different instances of the Session class with different
     *      content values.
     *      
     * 2. Verify that two different instances have been created.
     * 
     * 3. Verify that the equals method returns false when comparing the two.
     * 
     * 4. Verify that the hash code method returns different values for the two.
     * 
     * </pre>
     */
    public void testTwoDifferentInstancesWithDifferentValuesDontEqual() {
        String sourceJson1 = "{\"access_token\":\"ACCESSTOKEN1\",\"expires\":3601,\"refresh_token\":\"REFRESHTOKEN1\"}";
        String sourceJson2 = "{\"access_token\":\"ACCESSTOKEN2\",\"expires\":3602,\"refresh_token\":\"REFRESHTOKEN2\"}";
        Session session1 = new Session(sourceJson1);
        Session session2 = new Session(sourceJson2);

        assertFalse(session1 == session2);
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));
        assertFalse(session1.equals(null));
        assertFalse(session2.equals(null));
        assertFalse(session1.hashCode() == session2.hashCode());
    }

    /**
     * Verifies that the {@link Session#equals(Object)} method returns true on
     * two different instances with default values. This test also verifies that
     * the {@link Session#hashCode()} method returns the same value from two
     * different instances with default values.
     * 
     * <pre>
     * 
     * 1. Create two different instances of the Session class with default
     *      content values.
     * 
     * 2. Verify that two different instances have been created.
     * 
     * 3. Verify that the equals method returns true when comparing the two.
     * 
     * 4. Verify that the hash code method returns the same value for the two.
     * 
     * </pre>
     */
    public void testTwoDifferentInstancesWithDefaultValuesEquals() {
        String sourceJson = "{}";
        Session session1 = new Session(sourceJson);
        Session session2 = new Session(sourceJson);

        assertFalse(session1 == session2);
        assertTrue(session1.equals(session2));
        assertTrue(session2.equals(session1));
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    /**
     * Verifies that the {@link Session#equals(Object)} method returns true on
     * two different instances with the same values. This test also verifies
     * that the {@link Session#hashCode()} method returns the same value from
     * two different instances with the same values.
     * 
     * <pre>
     * 
     * 1. Create two different instances of the Session class with the same
     *      content values.
     * 
     * 2. Verify that two different instances have been created.
     * 
     * 3. Verify that the equals method returns true when comparing the two.
     * 
     * 4. Verify that the hash code method returns the same value for the two.
     * 
     * </pre>
     */
    public void testTwoDifferentInstancesWithSameValuesEquals() {
        String sourceJson = "{\"access_token\":\"ACCESSTOKEN\",\"expires\":3600,\"refresh_token\":\"REFRESHTOKEN\"}";
        Session session1 = new Session(sourceJson);
        Session session2 = new Session(sourceJson);

        assertFalse(session1 == session2);
        assertTrue(session1.equals(session2));
        assertTrue(session2.equals(session1));
        assertEquals(session1.hashCode(), session2.hashCode());
    }
}