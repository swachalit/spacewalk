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
package com.redhat.rhn.frontend.action.rhnpackage;

import com.redhat.rhn.common.db.datasource.DataResult;
import com.redhat.rhn.domain.rhnset.RhnSet;
import com.redhat.rhn.domain.server.Server;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.frontend.action.systems.sdc.SdcHelper;
import com.redhat.rhn.frontend.listview.PageControl;
import com.redhat.rhn.frontend.struts.RequestContext;
import com.redhat.rhn.frontend.struts.RhnListAction;
import com.redhat.rhn.manager.rhnpackage.PackageManager;
import com.redhat.rhn.manager.rhnset.RhnSetDecl;
import com.redhat.rhn.manager.system.SystemManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * VerifyPackageSetupAction
 * @version $Rev$
 */
public class VerifyPackageSetupAction extends RhnListAction {
    /** {@inheritDoc} */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm formIn,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        
        RequestContext requestContext = new RequestContext(request);
        
        Long sid = requestContext.getRequiredParam("sid");

        User user = requestContext.getLoggedInUser();
        Server server = SystemManager.lookupByIdAndUser(sid, user);
        
        PageControl pc = new PageControl();
        pc.setIndexData(true);
        pc.setFilterColumn("name");
        pc.setFilter(true);

        clampListBounds(pc, request, user);
        
        DataResult dr = getDataResult(server, pc);

        RhnSet set = RhnSetDecl.PACKAGES_VERIFY.get(user);
        SdcHelper.ssmCheck(request, server.getId(), user);
        request.setAttribute("pageList", dr);
        request.setAttribute("set", set);
        request.setAttribute("system", server);
        
        return getStrutsDelegate().forwardParams(mapping.findForward("default"),
                                       request.getParameterMap());
    }
    
    /**
     * Returns the unpublished errata for the given user bounded
     * by the values of the PageControl.
     * @param server The system.
     * @param pc boundary values
     * @return List of unpublished errata for the given user 
     * bounded by the values of the PageControl.
     */
    protected DataResult getDataResult(Server server, PageControl pc) {
        return PackageManager.systemPackageList(server.getId(), pc);
    }
}
