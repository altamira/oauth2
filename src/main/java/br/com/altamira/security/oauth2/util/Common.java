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
    
    // Amazon SES
    public static final String SMTP_USER = "AKIAIOM43MEJ4BQ37UFQ";
    public static final String SMTP_PASSWORD = "Ano6QxLhCjDOEyBMMGSLMTlUt1SPc0LYogZ2FkXvy7pT";
    public static final String SMTP_HOST = "email-smtp.us-west-2.amazonaws.com";
    public static final String SMTP_AUTH = "true";
    public static final String SMTP_PORT = "25";
    public static final String FROM_EMAIL = "sistema@altamira.com.br";
}
