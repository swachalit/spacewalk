/**
 * Copyright (c) 2008 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 * 
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation. 
 */
package com.redhat.rhn.frontend.action.channel;

import com.redhat.rhn.common.db.datasource.DataResult;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.frontend.listview.ListControl;
import com.redhat.rhn.manager.channel.ChannelManager;

/**
 * AllChannelTreeSetupAction
 * @version $Rev$
 */
public class AllChannelTreeAction extends BaseChannelTreeAction {

    /**
     * {@inheritDoc}
     */
    protected DataResult getDataResult(User user, ListControl lc) {
        return ChannelManager.allChannelTree(user, lc);
    }

}
