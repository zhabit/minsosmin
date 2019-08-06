package com.common.utils;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class ADLogin {
	  public static final String DOMAIN_USER_NOT_FOUND = "525";
	  public static final String DOMAIN_INVALID_CREDENTIALS = "52e";
	  public static final String DOMAIN_NOT_PERMIT_LOGON = "530";
	  public static final String DOMAIN_PASS_EXPIRED = "532";
	  public static final String DOMAIN_ACCOUNT_DISABLED = "533";
	  public static final String DOMAIN_ACCOUNT_EXPIRED = "701";
	  public static final String DOMAIN_RESET_PASS = "773";
	  public static final String DOMAIN_ACCOUNT_LOCKED = "775";

	  public static final String SSE_USER_NOT_EXIST = "-1";
	  public static final String SSE_USER_DISABLED = "0";
	  public static final String SSE_USER_LOCKED = "1";
	  public static final String DOMAIN_SUCCESS = "success";
	  public static final String url = "h3c.huawei-3com.com";
	  public static final String ldap_port = "389";
	  private static final StringBuffer ldap_url = new StringBuffer("ldap://").append(url).append(":").append(ldap_port);
		  
  public ADLogin() {
    super();
  }

  public static String loginCheckDomain(String user, String pass) {
    String returnflag = "";
    String LDAP_AUTHENTICATION = "simple";
    StringBuffer sec_principal = new StringBuffer(user);
    sec_principal.append("@").append(url);
    Hashtable env = new Hashtable();
    LdapContext ctx = null;
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL, ldap_url.toString());
    env.put(Context.SECURITY_AUTHENTICATION, LDAP_AUTHENTICATION);
    env.put(Context.SECURITY_PRINCIPAL, sec_principal.toString());
    env.put(Context.SECURITY_CREDENTIALS, pass);

    try {
      ctx = new InitialLdapContext(env, null);
      returnflag = DOMAIN_SUCCESS;
    }
    catch (Exception e) {
      if (e.getMessage().indexOf(DOMAIN_USER_NOT_FOUND) >= 0) {
        returnflag = DOMAIN_USER_NOT_FOUND;
      }
      else if (e.getMessage().indexOf(DOMAIN_INVALID_CREDENTIALS) >= 0) {
        returnflag = DOMAIN_INVALID_CREDENTIALS;
      }
      else if (e.getMessage().indexOf(DOMAIN_NOT_PERMIT_LOGON) >= 0) {
        returnflag = DOMAIN_NOT_PERMIT_LOGON;
      }
      else if (e.getMessage().indexOf(DOMAIN_PASS_EXPIRED) >= 0) {
        returnflag = DOMAIN_PASS_EXPIRED;
      }
      else if (e.getMessage().indexOf(DOMAIN_ACCOUNT_DISABLED) >= 0) {
        returnflag = DOMAIN_ACCOUNT_DISABLED;
      }
      else if (e.getMessage().indexOf(DOMAIN_ACCOUNT_EXPIRED) >= 0) {
        returnflag = DOMAIN_ACCOUNT_EXPIRED;
      }
      else if (e.getMessage().indexOf(DOMAIN_RESET_PASS) >= 0) {
        returnflag = DOMAIN_RESET_PASS;
      }
      else if (e.getMessage().indexOf(DOMAIN_ACCOUNT_LOCKED) >= 0) {
        returnflag = DOMAIN_ACCOUNT_LOCKED;
      }
    }
    finally {
      try {
        ctx.close();
      }
      catch (Exception Ignore) {}
    }
    return returnflag;
  }

  public static void main(String[] args) {
   // ADLogin tt = new ADLogin();
    String loginDomainResult = ADLogin.loginCheckDomain("ykf7048","123F=s7g6_N4Z");
    System.out.println(loginDomainResult);
  }

}