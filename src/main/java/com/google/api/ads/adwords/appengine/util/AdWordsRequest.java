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

import java.util.List;

import com.google.api.ads.adwords.appengine.oauth.CredentialException;
import com.google.api.ads.adwords.jaxws.factory.AdWordsServices;
import com.google.api.ads.adwords.jaxws.v201306.cm.ApiException_Exception;
import com.google.api.ads.adwords.jaxws.v201306.cm.Campaign;
import com.google.api.ads.adwords.jaxws.v201306.cm.CampaignPage;
import com.google.api.ads.adwords.jaxws.v201306.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.jaxws.v201306.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.inject.Inject;

public class AdWordsRequest {

	private final AdWordsSessions sessions;
	private final AdWordsServices adWordsServices;

	@Inject
	public AdWordsRequest(AdWordsServices adWordsServices, AdWordsSessions sessions) {
		this.adWordsServices = adWordsServices;
		this.sessions = sessions;
	}

	public List<Campaign> getCampaigns(String userId) throws ValidationException, CredentialException {
		AdWordsSession session = sessions.get(userId);
		CampaignServiceInterface campaignService = adWordsServices.get(session,
				CampaignServiceInterface.class);

		// Create selector.
		Selector selector = new Selector();
		selector.getFields().addAll(
				Lists.newArrayList("Name", "Status", "ServingStatus", "StartDate", "EndDate", "Period",
						"Amount", "AdServingOptimizationStatus", "BudgetId", "BudgetName",
						"BudgetReferenceCount", "Enhanced"));

		CampaignPage page = null;
		try {
			page = campaignService.get(selector);
		} catch (ApiException_Exception e) {
			e.printStackTrace();
		}

		return page.getEntries();
	}
}
