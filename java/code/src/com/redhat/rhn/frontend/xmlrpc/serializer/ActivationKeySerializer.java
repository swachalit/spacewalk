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
package com.redhat.rhn.frontend.xmlrpc.serializer;

import com.redhat.rhn.domain.channel.Channel;
import com.redhat.rhn.domain.rhnpackage.PackageName;
import com.redhat.rhn.domain.server.ServerGroup;
import com.redhat.rhn.domain.server.ServerGroupType;
import com.redhat.rhn.domain.token.ActivationKey;
import com.redhat.rhn.frontend.xmlrpc.serializer.util.SerializerHelper;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import redstone.xmlrpc.XmlRpcCustomSerializer;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcSerializer;


/**
 * ActivationKeySerializer
 * @version $Rev$
 *
 * @xmlrpc.doc 
 *   #struct("activation key")
 *     #prop("string", "key")
 *     #prop("string", "description")
 *     #prop("int", "usage_limit")
 *     #prop("string", "base_channel_label")
 *     #prop_array("child_channel_labels", "string", "childChannelLabel")
 *     #prop_array("entitlements", "string", "entitlementLabel")
 *     #prop_array("server_group_ids", "string", "serverGroupId")
 *     #prop_array("package_names", "string", "packageName")
 *     #prop("boolean", "universal_default")
 *   #struct_end()
 */
public class ActivationKeySerializer implements XmlRpcCustomSerializer {

    /**
     * {@inheritDoc}
     */
    public Class getSupportedClass() {
        return ActivationKey.class;
    }

    /** {@inheritDoc} */
    public void serialize(Object value, Writer output, XmlRpcSerializer builtInSerializer)
        throws XmlRpcException, IOException {
        ActivationKey key = (ActivationKey)value;
        SerializerHelper helper = new SerializerHelper(builtInSerializer);

        // Locate the base channel, and store the others in a list of child channels:
        List<String> childChannelLabels = new LinkedList<String>();
        String baseChannelLabel = null;
        for (Channel c : key.getChannels()) {
            if (c.isBaseChannel()) {
                baseChannelLabel = c.getLabel();
            }
            else {
                childChannelLabels.add(c.getLabel());
            }
        }
        if (baseChannelLabel == null) {
            baseChannelLabel = "none";
        }
        
        // Prepare a list of relevant entitlement labels, make sure to filter the 
        // non-addon entitlements:
        List<String> entitlementLabels = new LinkedList<String>();
        for (ServerGroupType sgt : key.getEntitlements()) {
            if (!sgt.isBase()) {
                entitlementLabels.add(sgt.getLabel());
            }
        }
        
        List<Integer> serverGroupIds = new LinkedList<Integer>();
        for (ServerGroup group : key.getServerGroups()) {
            serverGroupIds.add(new Integer(group.getId().intValue()));
        }
        
        List<String> packageNames = new LinkedList<String>();
        for (PackageName pn : key.getPackageNames()) {
            packageNames.add(pn.getName());
        }
        
        helper.add("key", key.getKey());
        helper.add("description", key.getNote());
        
        Integer usageLimit = new Integer(0);
        if (key.getUsageLimit() != null) {
            usageLimit = new Integer(key.getUsageLimit().intValue());
        }
        helper.add("usage_limit", usageLimit);
        
        helper.add("base_channel_label", baseChannelLabel);
        helper.add("child_channel_labels", childChannelLabels);
        helper.add("entitlements", entitlementLabels);
        helper.add("server_group_ids", serverGroupIds);
        helper.add("package_names", packageNames);
        
        Boolean universalDefault = new Boolean(key.isUniversalDefault());
        helper.add("universal_default", universalDefault);
        
        helper.writeTo(output);
    }

}
