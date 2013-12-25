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

package com.google.api.ads.adwords.appengine.util;

import com.google.api.ads.adwords.appengine.oauth.CredentialException;
import com.google.api.ads.adwords.appengine.oauth.CredentialFactory;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * {@code Sessions} is used to get AdWords sessions for an App Engine application.
 *
 * Typical usage is: <code>AdWordsSession session = Sessions.get(12345, 'APP_ENGINE_USER_ID');</code>
 *
 * <code>AdWordsSession session = AdWordsSessions.get('APP_ENGINE_USER_ID');</code>
 */
public class AdWordsSessions {

  private final String clientCustomerId;
  private final String developerToken;
  private final CredentialFactory credentialFactory;

  @Inject
  public AdWordsSessions(CredentialFactory credentialFactory,
      @Named("clientCustomerId") String clientCustomerId,
      @Named("developerToken") String developerToken) {
    this.credentialFactory = credentialFactory;
    this.clientCustomerId = clientCustomerId;
    this.developerToken = developerToken;
  }

  /**
   * Creates a {@link DfpSession} from the user's stored credential but without specifying a
   * network. This session should only be used to discover the networks available for that user.
   *
   * @param userId the App Engine assigned user ID
   * @return session the DFP session
   * @throws ValidationException if the DFP session could not be validated and created
   * @throws CredentialException if credential cannot be obtained
   */
  public AdWordsSession get(String userId) throws ValidationException, CredentialException {
    return getSessionBuilder(userId).build();
  }

  /**
   * Create a session builder with base configuration.
   *
   * @param userId the App Engine assigned user ID
   * @return DfpSession.Builder the DFP session
   * @throws CredentialException if credential cannot be obtained
   */
  private AdWordsSession.Builder getSessionBuilder(String userId) throws CredentialException {
    Credential credential = credentialFactory.getInstance(userId);
    return new AdWordsSession.Builder()
        .withOAuth2Credential(credential)
        .withDeveloperToken(developerToken)
        .withClientCustomerId(clientCustomerId);
  }
}
