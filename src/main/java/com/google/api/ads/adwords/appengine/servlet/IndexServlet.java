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

package com.google.api.ads.adwords.appengine.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.ads.adwords.appengine.oauth.AuthorizationCodeFlowFactory;
import com.google.api.ads.adwords.appengine.oauth.CredentialException;
import com.google.api.ads.adwords.appengine.util.AdWordsRequest;
import com.google.api.ads.adwords.jaxws.v201306.cm.Campaign;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Servlet that presents the web application. Users will have to authorize use
 * of the API to view the application page. A new channel token is assigned to
 * the user on every invocation of this servlet.
 * 
 */
@Singleton
@SuppressWarnings("serial")
public class IndexServlet extends AuthServlet {

	static final Logger log = Logger.getLogger(IndexServlet.class.getName());
	private final UserService userService;
	private final AdWordsRequest adWordsRequest;

	/**
	 * Constructor.
	 * 
	 * @param userService
	 *          the App Engine service for user management
	 * @param expirationDelta
	 *          the cache expiration delta in seconds
	 * @param authorizationCodeFlowFactory
	 *          used to get an OAuth2 flow
	 * @param redirectUrl
	 *          where to direct the user once authorization is done
	 */
	@Inject
	public IndexServlet(UserService userService,
			AuthorizationCodeFlowFactory authorizationCodeFlowFactory,
			@Named("redirectUrl") String redirectUrl,
			AdWordsRequest adWordsRequest) {
		super(authorizationCodeFlowFactory, redirectUrl);
		this.userService = userService;
		this.adWordsRequest = adWordsRequest;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		String userId = userService.getCurrentUser().getUserId();

		List<String> campaignNames = new ArrayList<String>();
		List<Campaign> campaigns = null;
		try {
			campaigns = adWordsRequest.getCampaigns(userId);
			for (Campaign campaign : campaigns) {
				campaignNames.add(campaign.getName());
			}
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (CredentialException e) {
			e.printStackTrace();
		}

		System.out.println("======== Campaign Name ========");
		for (Campaign campaign : campaigns) {
			System.out.println(campaign.getName());
		}

		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/index.jsp");

		req.setAttribute("logout_url", userService.createLogoutURL("/"));
		req.setAttribute("user", userService.getCurrentUser().getEmail());
		req.setAttribute("campaignNames", campaignNames);
		requestDispatcher.forward(req, resp);
	}

}
