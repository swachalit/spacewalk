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

import com.redhat.rhn.domain.config.ConfigChannelType;
import com.redhat.rhn.frontend.xmlrpc.serializer.util.BeanSerializer;
import com.redhat.rhn.frontend.xmlrpc.serializer.util.SerializerHelper;

import java.io.Writer;

import redstone.xmlrpc.XmlRpcCustomSerializer;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcSerializer;


/**
 * ConfigChannelTypeSerializer
 * @version $Rev$
 * 
 * @xmlrpc.doc
 * #struct("Configuration Channel Type information") 
 *   #prop("int", "id")
 *   #prop("string", "label")
 *   #prop("string", "name")
 *   #prop("string", "priority")
 * #struct_end()
 */
public class ConfigChannelTypeSerializer implements XmlRpcCustomSerializer {
    private BeanSerializer helper = new BeanSerializer();
    
    /**
     * 
     */
    public ConfigChannelTypeSerializer() {
        helper.include("id");
        helper.include("label");
        helper.include("name");
        helper.include("priority");
    }
    /**
     * {@inheritDoc}
     */
    public Class getSupportedClass() {
        return ConfigChannelType.class;
    }

    /**
     * {@inheritDoc}
     */
    public void serialize(Object value, Writer output, XmlRpcSerializer builtInSerializer)
        throws XmlRpcException {
        SerializerHelper serializer = new SerializerHelper(builtInSerializer);
        helper.serialize(value, output, serializer);
    }

}
