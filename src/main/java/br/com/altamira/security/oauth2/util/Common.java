/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.security.oauth2.util;

/**
 *
 * @author 
 */
public class Common {
    public static String CLIENT_ID = "oauth2test";
    public static String CLIENT_SECRET = "oauth2clientsecret";
    public static String AUTHORIZATION_CODE = "oauth2authcode";
    public static String USERNAME = "user";
    public static String PASSWORD = "pass";
    public static String RESOURCE_SERVER_NAME = "resource";
    public static final String ACCESS_TOKEN_VALID = "access_token_valid";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_HEADER_OAUTH2 = "Bearer " + ACCESS_TOKEN_VALID;
    
    public static final String SMTP_USER = "altamira.test.email";
    public static final String SMTP_PASSWORD = "altamira2014";
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_AUTH = "true";
    public static final String SMTP_PORT = "465";
    public static final String FROM_EMAIL = "altamira.test.email";
}
