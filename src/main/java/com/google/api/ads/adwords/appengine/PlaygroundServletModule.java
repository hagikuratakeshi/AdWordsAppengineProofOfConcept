// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.ads.adwords.appengine;

import java.util.logging.Logger;

import com.google.api.ads.adwords.appengine.oauth.AuthorizationCodeFlowFactory;
import com.google.api.ads.adwords.appengine.oauth.CredentialFactory;
import com.google.api.ads.adwords.appengine.servlet.DeniedServlet;
import com.google.api.ads.adwords.appengine.servlet.IndexServlet;
import com.google.api.ads.adwords.appengine.servlet.OAuth2CallbackServlet;
import com.google.api.ads.adwords.appengine.util.AdWordsRequest;
import com.google.api.ads.adwords.appengine.util.AdWordsSessions;
import com.google.api.ads.adwords.jaxws.factory.AdWordsServices;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;

/**
 * Configures the servlet bindings.
 *
 */
public class PlaygroundServletModule extends ServletModule {

  static final Logger log = Logger.getLogger(PlaygroundServletModule.class.getName());

  /**
   * If you do not have a client ID or secret, please create one in the API console:
   * https://code.google.com/apis/console#access
   */
  private static final String CLIENT_ID = System.getProperty("adwords.appengine.clientId");
  private static final String CLIENT_SECRET = System.getProperty("adwords.appengine.clientSecret");
  private static final String SCOPE = "https://adwords.google.com/api/adwords/";
  private static final String CLIENT_CUSTOMER_ID = System.getProperty("adwords.clientCustomerId");
  private static final String DEVELOPER_TOKEN = System.getProperty("adwords.developerToken");

  /**
   * The redirect URL must be registered in the API console: https://code.google.com/apis/console/
   * This is so that once the user has authorized your app, they are redirected back to your app.
   */
  private static final String REDIRECT_URL = "/oauth2callback";

  /**
   * The number of seconds to keep a user's networks in memcache.
   */
  private static final int EXPIRATION_DELTA = 60 * 60;

  @Override
  protected void configureServlets() {
    super.configureServlets();

    serve("/").with(IndexServlet.class);
    serve("/denied").with(DeniedServlet.class);
    serve("/dfp").with(IndexServlet.class);
    serveRegex("/[0-9]+").with(IndexServlet.class);
    serve(REDIRECT_URL).with(OAuth2CallbackServlet.class);

    bindConstant().annotatedWith(Names.named("clientId")).to(CLIENT_ID);
    bindConstant().annotatedWith(Names.named("clientSecret")).to(CLIENT_SECRET);
    bindConstant().annotatedWith(Names.named("scope")).to(SCOPE);
    bindConstant().annotatedWith(Names.named("redirectUrl")).to(REDIRECT_URL);
    bindConstant().annotatedWith(Names.named("expirationDelta")).to(EXPIRATION_DELTA);

    bindConstant().annotatedWith(Names.named("clientCustomerId")).to(CLIENT_CUSTOMER_ID);
    bindConstant().annotatedWith(Names.named("developerToken")).to(DEVELOPER_TOKEN);
    
    bind(AuthorizationCodeFlowFactory.class);
    bind(CredentialFactory.class);
    bind(CredentialStore.class).to(AppEngineCredentialStore.class);
    bind(GoogleCredential.class);
    bind(HttpTransport.class).to(NetHttpTransport.class);
    bind(JsonFactory.class).to(JacksonFactory.class);
    
    bind(UserService.class).toProvider(UserServiceProvider.class);

    bind(AdWordsRequest.class);
    bind(AdWordsServices.class);
    bind(AdWordsSessions.class);
  }
  
  private static class UserServiceProvider implements Provider<UserService> {

    public UserService get() {
      return UserServiceFactory.getUserService();
    }
  }
}
