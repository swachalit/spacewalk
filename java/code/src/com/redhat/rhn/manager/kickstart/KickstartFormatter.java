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
package com.redhat.rhn.manager.kickstart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.redhat.rhn.common.conf.Config;
import com.redhat.rhn.common.security.SessionSwap;
import com.redhat.rhn.domain.channel.Channel;
import com.redhat.rhn.domain.common.CommonFactory;
import com.redhat.rhn.domain.common.TinyUrl;
import com.redhat.rhn.domain.kickstart.KickstartCommand;
import com.redhat.rhn.domain.kickstart.KickstartData;
import com.redhat.rhn.domain.kickstart.KickstartScript;
import com.redhat.rhn.domain.kickstart.KickstartSession;
import com.redhat.rhn.domain.kickstart.crypto.CryptoKey;
import com.redhat.rhn.domain.rhnpackage.Package;
import com.redhat.rhn.domain.rhnpackage.PackageFactory;
import com.redhat.rhn.domain.rhnpackage.PackageName;
import com.redhat.rhn.domain.token.ActivationKey;
import com.redhat.rhn.domain.token.ActivationKeyFactory;
import com.redhat.rhn.domain.token.Token;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.domain.user.UserFactory;
import com.redhat.rhn.manager.channel.ChannelManager;

/**
 * Simple class to reduce dependencies between Struts and database layers
 * 
 * @version $Rev $
 */
public class KickstartFormatter {
    
    private static Logger log = Logger.getLogger(KickstartFormatter.class);
    
    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";
    private static final String WHITESPACE = "\\s";
    private static final String SWAP = "swap";
    private static final String PART = "part";
    private static final String PARTITIONS = "partitions";    
    private static final String RAID = "raid";
    private static final String RAIDS = "raids";
    private static final String VOLGROUP = "volgroup";
    private static final String VOLGROUPS = "volgroups";
    private static final String LOGVOLS = "logvols";
    private static final String LOGVOL = "logvol";
    private static final String INCLUDE = "include";
    private static final String PARTREGEX = "partitions|raids|volgroups|logvols|include";
    private static final String DEPS = "--resolvedeps";
    private static final String PACKAGES = "%packages";
    private static final String INTERPRETER_OPT = "--interpreter";
    private static final String NOCHROOT = "--nochroot";
    private static final String HEADER = 
        "# Kickstart config file generated by Spacewalk Config Management" + NEWLINE;
    private static final String COMMENT = "#" + NEWLINE;
    private static final String MOTD_FOOTER = "# MOTD" + NEWLINE + "echo >> /etc/motd" + 
        NEWLINE + "echo \"Spacewalk kickstart on $(date +'%Y-%m-%d')\" >> /etc/motd" + 
        NEWLINE + "echo >> /etc/motd" + NEWLINE + NEWLINE + 
        "# end of generated kickstart file";
    private static final String BEGINRHN = "%post" + NEWLINE + 
    "( # Log %post errors \n # --Begin Spacewalk command section--\n";
    private static final String ENDRHN = ") >> /root/ks-post.log 2>&1\n";
    private static final String  BEGIN_POST_LOG = "(" + NEWLINE;
    private static final String KSTREE = 
        "# now copy from the ks-tree we saved in the non-chroot checkout" + NEWLINE +  
        "cp -fav /tmp/ks-tree-copy/* /" + NEWLINE + 
        "rm -Rf /tmp/ks-tree-copy" + NEWLINE + 
        "# --End Spacewalk command section--" + NEWLINE;
    public static final String[] UPDATE_PKG_NAMES =         
    {"pyOpenSSL", "rhnlib", "libxml2-python"};
    public static final String[] FRESH_PKG_NAMES_RHEL5 = 
    {"yum",  "pirut", "yum-rhn-plugin"};
    public static final String[] FRESH_PKG_NAMES_RHEL34 = 
    {"up2date",  "up2date-gnome"};
    public static final String[] FRESH_PKG_NAMES_RHEL2 =
    {"rhn_register", "up2date", "rhn_register-gnome", "up2date-gnome"};
    private static final String UPDATE_OPT_PATH = "/tmp/rhn_rpms/optional/";
    private static final String UPDATE_CMD = "rpm -Uvh --replacepkgs --replacefiles ";
    private static final String FRESH_CMD = "rpm -Fvh /tmp/rhn_rpms/*rpm";
    private static final String IMPORT_RHN_KEY5 = 
        "rpm --import /etc/pki/rpm-gpg/RPM-GPG-KEY-redhat-release";
    private static final String IMPORT_RHN_KEY34 = 
        "rpm --import /usr/share/rhn/RPM-GPG-KEY";
    private static final String IMPORT_RHN_KEY2 = 
        "gpg $(up2date --gpg-flags) --batch --import /usr/share/rhn/RPM-GPG-KEY" + 
        NEWLINE +   
        "gpg $(up2date --gpg-flags) --batch --import /usr/share/rhn/RPM-GPG-KEY";
    private static final String MKDIR_OPTIONAL = "mkdir -p /tmp/rhn_rpms/optional";
    private static final String WGET_OPT_RPMS = "wget -P /tmp/rhn_rpms/optional ";
    private static final String WGET_RPMS = "wget -P /tmp/rhn_rpms ";
    private static final String REMOTE_CMD = 
        "mkdir -p /etc/sysconfig/rhn/allowed-actions/script" + NEWLINE +  
        "touch /etc/sysconfig/rhn/allowed-actions/script/all";
    private static final String CONFIG_CMD = 
        "mkdir -p /etc/sysconfig/rhn/allowed-actions/configfiles" + NEWLINE + 
        "touch /etc/sysconfig/rhn/allowed-actions/configfiles/all";
    private static final String RHNCHECK = "rhn_check";
    private static final String ACT_KEY_CMD = "rhnreg_ks --activationkey=";
    private static final String RHN_NOCHROOT = 
        "mkdir /mnt/sysimage/tmp/ks-tree-copy" + NEWLINE +  
        "if [ -d /oldtmp/ks-tree-shadow ]; then" + NEWLINE + 
        "cp -fa /oldtmp/ks-tree-shadow/* /mnt/sysimage/tmp/ks-tree-copy" + NEWLINE +  
        "elif [ -d /tmp/ks-tree-shadow ]; then" + NEWLINE + 
        "cp -fa /tmp/ks-tree-shadow/* /mnt/sysimage/tmp/ks-tree-copy" + NEWLINE + 
        "fi" + NEWLINE + 
        "cp /etc/resolv.conf /mnt/sysimage/etc/resolv.conf" + NEWLINE;    
    private static final String XMLRPC_HOST = 
        Config.get().getString(Config.KICKSTART_HOST, "xmlrpc.rhn.redhat.com");
    
    private static final String VIRT_HOST_GRUB_FIX = 
        "sed -i.backup 's/default=[0-9]*/default=0/' /boot/grub/grub.conf" + NEWLINE;
    
    private static final String ISCRYPTED = "--iscrypted ";
    
    //wregglej, 9/22/06 Temporary workarounds for a broken wget.
    private static final String CHDIR_OPT_RPMS = "cd /tmp/rhn_rpms/optional ";
    private static final String CHDIR_RPMS = "cd /tmp/rhn_rpms";

    private boolean seenNoChroot = false;
    private KickstartData ksdata;
    private String ksHost;
    private User user;
    private KickstartSession session;
    
    /**
     * constructor
     * @param hostIn kickstart host
     * @param ksdataIn KickstartData
     */
    public KickstartFormatter(String hostIn, KickstartData ksdataIn) {
        
        this.ksdata = ksdataIn;
        this.ksHost = hostIn;
        this.user = UserFactory.findRandomOrgAdmin(this.ksdata.getOrg());
    }
    
    /**
     * Constructor with KickstartSession.
     * @param hostIn that is kickstarting from
     * @param ksdataIn that is is to be 'formatted' for output
     * @param sessionIn associated with the formatting.
     */
    public KickstartFormatter(String hostIn, KickstartData ksdataIn, 
            KickstartSession sessionIn) {
        this(hostIn, ksdataIn);
        this.session = sessionIn;
    }
    
    
    /**
     * 
     * @return String containing kickstart file
     */
    public String getFileData() {
        StringBuffer buf = new StringBuffer();
        buf.append(getHeader());
        buf.append(getCommands());
        
        /*if (this.ksdata.isRhel5OrGreater()) {
            buf.append(getRepoCommands());
            buf.append(getKeyCommands());
        }*/
        
        buf.append(NEWLINE);
        buf.append(getPackageOptions());
        buf.append(NEWLINE);
        buf.append(getPackages());
        buf.append(NEWLINE);        
        buf.append(getPrePost(KickstartScript.TYPE_PRE));
        buf.append(NEWLINE);      
        buf.append(getNoChroot());
        buf.append(NEWLINE);
        buf.append(getRhnPost());
        buf.append(MOTD_FOOTER);   
        buf.append(NEWLINE);
        buf.append(getPrePost(KickstartScript.TYPE_POST));
        buf.append(NEWLINE);
        String retval = buf.toString();
        log.debug("fileData.retval:");
        log.debug(retval);
        return retval;
    }

    /**
     * 
     * @return static string header
     */    
    private StringBuffer getHeader() {
        StringBuffer header = new StringBuffer();
        header.append(HEADER);
        header.append(COMMENT);
        header.append("# Profile Name  : " + this.ksdata.getName() + NEWLINE);
        header.append("# Profile Label : " + this.ksdata.getLabel() + NEWLINE);
        header.append("# Date Created  : " + this.ksdata.getCreated() + NEWLINE);
        header.append(COMMENT);
        header.append(NEWLINE);
        
        return header;
    }
    
    /**
     * 
     * @return string containing kickstart commands 
     */
    private StringBuffer getCommands() {        
        StringBuffer commands = new StringBuffer();        
        LinkedList l = new LinkedList(this.ksdata.getCommands());
        Collections.sort(l);
        for (Iterator itr = l.iterator(); itr.hasNext();) {
            KickstartCommand command = (KickstartCommand)itr.next();
            String cname = command.getCommandName().getName();  
            
            if (cname.matches(PARTREGEX)) {
                commands.append(handlePart(cname, command.getArguments()));
            }
            else if (cname.matches("rootpw")) {
                commands.append(cname + SPACE + ISCRYPTED + 
                        command.getArguments() + NEWLINE);
            }
            else if (cname.matches("url")) {
                String argVal = adjustUrlHost(command);
                commands.append(cname + SPACE + argVal + NEWLINE);
            }
            else if (cname.matches("repo") && 
                    command.getArguments().indexOf("--baseurl=/") >= 0) {
                
                StringBuffer finalBaseurl = adjustRepoHost(command);
                commands.append(cname + SPACE + finalBaseurl.toString() + NEWLINE);
            }
            else {
                String argVal = command.getArguments();
                // some commands don't require an arg and are null in db
                if (argVal == null) {
                    commands.append(cname).append(NEWLINE);
                }
                else {
                    commands.append(cname + SPACE + argVal + NEWLINE);
                }
            }
            
        }
        
        return commands;
    }

    /**
     * Adjust the URL hostname if necessary. Hostnames are stored in the db as relative
     * paths if the user selects to use the default URL. When rendered we need to swap
     * in the most appropriate hostname.
     * 
     * If this hostname appears to be customized, no change is made and we return the URL
     * as is.
     */
    private String adjustUrlHost(KickstartCommand command) {
        String argVal = command.getArguments();
        String urlLocation = argVal.substring("--url ".length());
        log.debug("Got URL : " + command.getArguments());
        log.debug("isRhnTree: " + this.ksdata.getTree().isRhnTree());
        log.debug("Actual URL: " + urlLocation);
        
        if (urlLocation.startsWith("/")) { 
            log.debug("URL is not customized.");
            if (this.session != null) {
                log.debug("Formatting for session use.");
                // /kickstart/dist/session/
                // 94xe86321bae3cb74551d995e5eafa065c0/ks-rhel-i386-as-4-u2
                StringBuffer file = new StringBuffer();
                file.append("/kickstart/dist/session/");
                file.append(SessionSwap.encodeData(session.getId().toString()));
                file.append("/");
                file.append(ksdata.getTree().getLabel());
                TinyUrl turl = CommonFactory.createTinyUrl(file.toString(), 
                        new Date());
                CommonFactory.saveTinyUrl(turl);
                StringBuffer url = new StringBuffer();
                url.append("--url ");
                String tyurl = turl.computeTinyUrl(this.ksHost);
                url.append(tyurl);
                log.debug("constructed: " + url);
                argVal = url.toString();
            }
            else {
                log.debug("Formatting for view use.");
                // /kickstart/dist/ks-rhel-i386-as-4-u2
                StringBuffer file = new StringBuffer();
                file.append("/kickstart/dist/");
                file.append(this.ksdata.getTree().getLabel());
                TinyUrl turl = CommonFactory.createTinyUrl(file.toString(), 
                        new Date());
                CommonFactory.saveTinyUrl(turl);
                StringBuffer url = new StringBuffer();
                url.append("--url ");
                String tyurl = turl.computeTinyUrl(this.ksHost);
                url.append(tyurl);
                log.debug("constructed: " + url);
                argVal = url.toString();
            }
        }
        return argVal;
    }

    /**
     * Correct the repo command to include the most suitable hostname.
     */
    private StringBuffer adjustRepoHost(KickstartCommand command) {
        String argVal = command.getArguments();
        log.debug("Adjusting repo: " + argVal);
        StringBuffer finalBaseurl = new StringBuffer("");
        String args = command.getArguments();
        for (String token : args.split("\\ ")) {
            log.debug("   token = " + token);
            if (!token.startsWith("--baseurl=")) {
                if (finalBaseurl.length() > 0) {
                    finalBaseurl.append(" ");
                }
                finalBaseurl.append(token);
            }
            else {
                String location = token.substring("--baseurl=".length());
                // Inject a host:
                location = "http://" + ksHost + location;
                log.debug("   fixed baseurl = " + location.toString());
                if (finalBaseurl.length() > 0) {
                    finalBaseurl.append(" ");
                }
                finalBaseurl.append("--baseurl=");
                finalBaseurl.append(location);
            }
        }
        return finalBaseurl;
    }
    
    /**
     * 
     * @param cnameIn kickstart command coming in
     * @param argIn kickstart command args coming in
     * @return string representing the partition info for ks file
     */
    private String handlePart(String cnameIn, String argIn) {
        String token = new String(argIn);
        String[] tokens = token.split(WHITESPACE);
        String retval = "";
        if (tokens.length == 0) {
            return retval;
        }
        
        if (tokens[0].startsWith(SWAP)) {
            tokens[0] = SWAP;
        }
        
        if (cnameIn.equals(PARTITIONS)) {
            retval = PART + SPACE + StringUtils.join(tokens, SPACE) + NEWLINE;
        }
        else if (cnameIn.equals(RAIDS)) {
            retval = RAID + SPACE + StringUtils.join(tokens, SPACE) + NEWLINE;
        }
        else if (cnameIn.equals(VOLGROUPS)) {
            retval = VOLGROUP + SPACE + StringUtils.join(tokens, SPACE) + NEWLINE;
        }
        else if (cnameIn.equals(LOGVOLS)) {
            retval = LOGVOL + SPACE + StringUtils.join(tokens, SPACE) + NEWLINE;
        }
        else if (cnameIn.equals(INCLUDE)) {
            retval = "%" + INCLUDE + SPACE + StringUtils.join(tokens, SPACE) + NEWLINE;
        }
        return retval;
    }
    
    /** 
     * 
     * @return string containing package options
     */
    private String getPackageOptions() {
        // if kstree is > 2.1 then add the resolve deps, ignore other deps
        String opts = this.ksdata.isRhel2() || this.ksdata.isFedora() ? "" : DEPS;
        return PACKAGES + SPACE + opts + NEWLINE; 
    }
    
    /**
     * 
     * @return string containing packages
     */
    private String getPackages() {        
        StringBuffer buf = new StringBuffer();
        for (Iterator itr = ksdata.getPackageNames().iterator(); itr.hasNext();) {
            buf.append(((PackageName)itr.next()).getName() + NEWLINE);
        }
        if (ksdata.getKsdefault().getVirtualizationType().getLabel().equals("para_host")) {
            buf.append("kernel-xen" + NEWLINE);
            buf.append("xen" + NEWLINE);
        }
        
        // packages necessary for rhel2.1
        if (this.ksdata.isRhel2()) {         
          buf.append("@ Network Support" + NEWLINE);
          buf.append("openssh-server" + NEWLINE);
        }
        return buf.toString();
    }
    
    /**
     * @param typeIn type of script to render (pre or post)
     * @return rendered script(s)
     */
    private String getPrePost(String typeIn) {
        StringBuffer retval = new StringBuffer();
        if (this.ksdata.getScripts() != null) {
            List<KickstartScript> l = 
                new LinkedList<KickstartScript>(this.ksdata.getScripts());
            Collections.sort(l);
            Iterator<KickstartScript> i = l.iterator();
            while (i.hasNext()) {                   
                KickstartScript kss = (KickstartScript) i.next();
                // render either pre or chroot posts
                if (kss.getScriptType().equals(typeIn)) {
                    if (typeIn.equals(KickstartScript.TYPE_PRE) || 
                            (typeIn.equals(KickstartScript.TYPE_POST) && 
                                    (kss.getChroot().equals("Y")))) {
                        retval.append(NEWLINE);
                        if (kss.getInterpreter() != null) {
                            retval.append("%" + typeIn + SPACE + INTERPRETER_OPT + SPACE +
                                    kss.getInterpreter() + NEWLINE);
                        }
                        else {
                            retval.append("%" + typeIn + NEWLINE);
                        }
                        if (typeIn.equals(KickstartScript.TYPE_POST) && 
                                ksdata.getPostLog()) {
                            retval.append(BEGIN_POST_LOG);
                        }
                        retval.append(kss.getDataContents() + NEWLINE);
                        if (typeIn.equals(KickstartScript.TYPE_POST) && 
                                ksdata.getPostLog()) {
                            retval.append(ENDRHN);
                        }
                    }                    
                } // end script type and chroot = y

            } // end iterator            
        } // end if have scripts
        return retval.toString();
    }
    
    /** 
     * 
     * @return string containing nochroot post contents
     */
    private String getNoChroot() {
        StringBuffer retval = new StringBuffer();
        if (this.ksdata.getScripts() != null) {
            Iterator i = this.ksdata.getScripts().iterator();
            while (i.hasNext()) {
                KickstartScript kss = (KickstartScript) i.next();
                if (kss.getScriptType().equals(KickstartScript.TYPE_POST) && 
                        kss.getChroot().equals("N")) {
                    // Put a blank line in between the scripts
                    retval.append(NEWLINE);
                    if (kss.getInterpreter() != null) {
                        retval.append("%" + KickstartScript.TYPE_POST + SPACE + 
                                NOCHROOT  + SPACE + kss.getInterpreter() + NEWLINE);
                    }
                    else {
                        retval.append("%" + KickstartScript.TYPE_POST + SPACE + 
                                NOCHROOT + NEWLINE);
                    }
                    if (!seenNoChroot) {
                        retval.append(RHN_NOCHROOT);                       
                        seenNoChroot = true;
                    }                    
                    retval.append(kss.getDataContents() + NEWLINE);
                    
                }
            } // end iterator
        } // end if we have scripts to process
        
        // user does not have a no chroot post, render no chroot rhn post separately
        if (!seenNoChroot) {
            retval.append("%" + KickstartScript.TYPE_POST + SPACE + NOCHROOT + NEWLINE);
            retval.append(RHN_NOCHROOT);
        }
        
        return retval.toString();
    }
    
    private String getRhnPost() {
        log.debug("getRhnPost called.");
        StringBuffer retval = new StringBuffer();
        retval.append(BEGINRHN);
                
        retval.append(renderKeys() + NEWLINE);
        
        List<ActivationKey> tokens = generateActKeyTokens();
        
        HashSet updatePackages = getUpdatePackages(tokens);
        HashSet freshPackages = getFreshPackages(tokens);
        boolean isFresh = freshPackages.size() > 0;
        boolean isUpdate = updatePackages.size() > 0;
        
        // update the required/optional packages needed for the kickstart
        if (isUpdate || isFresh) {
            log.debug("need latest up2date");
            //order matters, therfore multiple logic branches
            retval.append(MKDIR_OPTIONAL + NEWLINE);
            if (isUpdate) {
                //wregglej - wget is broken, so workaround it.
                retval.append(CHDIR_OPT_RPMS + NEWLINE);
                
                retval.append(WGET_OPT_RPMS);
                for (Iterator itr = updatePackages.iterator(); itr.hasNext();) {
                    retval.append(itr.next().toString() + SPACE);
                }
                retval.append(NEWLINE);                
            }
            if (isFresh) {
                //wregglej - work around wget again.
                retval.append(CHDIR_RPMS + NEWLINE);
                
                retval.append(WGET_RPMS);
                for (Iterator itr = freshPackages.iterator(); itr.hasNext();) {
                    retval.append(itr.next().toString() + SPACE);
                }
                retval.append(NEWLINE);                                                
            }
            if (isUpdate) {
                retval.append(UPDATE_CMD);
                for (int i = 0; i < UPDATE_PKG_NAMES.length; i++) {
                    retval.append(UPDATE_OPT_PATH + UPDATE_PKG_NAMES[i] + "* ");
                }
                retval.append(NEWLINE);
            }
            if (isFresh) {
                retval.append(FRESH_CMD + NEWLINE);
            }
        }
        
        if (this.ksdata.getKsdefault().getVirtualizationType()
                .getLabel().equals("para_host")) {
            retval.append(VIRT_HOST_GRUB_FIX);
        }
        
        if (this.ksdata.isRhel2()) {
            retval.append(IMPORT_RHN_KEY2 + NEWLINE);
        }
        else if (this.ksdata.isRhel3() || this.ksdata.isRhel4()) {
            retval.append(IMPORT_RHN_KEY34 + NEWLINE);
        }
        else if (this.ksdata.isRhel5()) {
            retval.append(IMPORT_RHN_KEY5 + NEWLINE);
        }
        else {
            log.error("Could not generate GPG import line.  " +
                          "Unknown version of RHEL: " + 
                          this.ksdata.getInstallType());
        }
        
        if (log.isDebugEnabled()) {
            log.debug("kickstart_host: [" + XMLRPC_HOST + "] kshost: [" +
                    this.ksHost + "] indexof: " + 
                    this.ksHost.indexOf(XMLRPC_HOST));
        }
        
        String up2datehost = this.ksHost;
        //check if server going through RHN Proxy, if so, register through proxy instead
        if (this.session != null && 
                this.session.getSystemRhnHost() != null &&
                !this.session.getSystemRhnHost().equals("unknown")) {
            up2datehost = this.session.getSystemRhnHost();        
        }            
         
        log.debug("adding perl -npe for /etc/sysconfig/rhn/up2date");
        if (this.ksdata.isRhel2()) {
            retval.append("perl -npe 's/xmlrpc.rhn.redhat.com/" + up2datehost + 
                    "/' -i /etc/sysconfig/rhn/rhn_register" + NEWLINE);            
        }
        // both rhel 2 and rhel3/4 need the following
        retval.append("perl -npe 's/xmlrpc.rhn.redhat.com/" + up2datehost + 
                "/' -i /etc/sysconfig/rhn/up2date" + NEWLINE);                
        
        if (this.ksdata.getKsdefault().getRemoteCommandFlag().booleanValue()) {
            retval.append(REMOTE_CMD + NEWLINE);
        }
        
        if (this.ksdata.getKsdefault().getCfgManagementFlag().booleanValue()) {
            retval.append(CONFIG_CMD + NEWLINE);
        }                
        
        retval.append(NEWLINE);
        retval.append(KSTREE);
        retval.append(NEWLINE);        
        retval.append(NEWLINE);
        
        if (tokens.size() > 0) {
            log.debug("Adding activation/registration commands.");
            retval.append(ACT_KEY_CMD);
            // Append rhnreg_ks:
            // rhnreg_ks --activationkey=a67509953c07b886229c2a1691922069
            //     --profilename="aa-test-4AS-profile-ks"
            for (Iterator itr = tokens.iterator(); itr.hasNext();) {
                ActivationKey act = (ActivationKey) itr.next();
                retval.append(act.getKey());
                
                if (itr.hasNext()) {
                    retval.append(",");
                }
            }
            if (this.session != null && 
                    this.session.getOldServer() != null) {
                retval.append(" --profilename=\"");
                retval.append(this.session.getOldServer().getName());
                retval.append("\" ");
            }
            retval.append(NEWLINE);
            retval.append(RHNCHECK + NEWLINE);
        }
        
        retval.append(NEWLINE);
        retval.append(RHNCHECK + NEWLINE);
        
        retval.append(NEWLINE);
        retval.append(ENDRHN);
        return retval.toString();
    }

    /**
     * @return
     */
    private List<ActivationKey> generateActKeyTokens() {
        List<ActivationKey> tokens = new ArrayList<ActivationKey>();
        log.debug("Computing Activation Keys");
        // If we are in a KickstartSession and dont have any activation keys 
        // associated with this KickstartProfile then we want to create a 
        // one time key.
        if (log.isDebugEnabled()) {
            log.debug("def reg tokens: " + this.ksdata.getDefaultRegTokens());
        }
        
        //if we need a reactivation key, add one
        if (this.session != null && (this.session.getOldServer() != null || 
                this.ksdata.getDefaultRegTokens().size() == 0)) {
            log.debug("Session isn't null.  Lets generate a one-time activation key.");
            ActivationKey oneTimeKey = ActivationKeyFactory.
                lookupByKickstartSession(this.session);
            if (oneTimeKey != null) {
                tokens.add(oneTimeKey);
                if (log.isDebugEnabled()) {
                    log.debug("Found one time activation key: " + oneTimeKey.getKey());
                }
            }
            else {
                log.error("We should have gotten an activation key with this session: " + 
                        this.session.getId());
            }
        }
        log.debug("tokens size: " + tokens.size());
        //add the activation keys associated with the kickstart profile
        if (this.ksdata.getDefaultRegTokens() != null) {
            if (this.ksdata.getDefaultRegTokens().size() > 0) {
                for (Iterator itr = ksdata.getDefaultRegTokens().iterator(); 
                    itr.hasNext();) {
                    Token tk = (Token)itr.next();
                    ActivationKey act = 
                        ActivationKeyFactory.lookupByToken(tk);
                    tokens.add(act);
                }
            }
        }
        return tokens;
    }
        
    private String renderKeys() {        
        StringBuffer retval = new StringBuffer();
        
        HashSet sslKeys = new HashSet();
        HashSet gpgKeys = new HashSet();
        
        // setup keys for rendering
        if (this.ksdata.getCryptoKeys() != null) {
            for (Iterator itr = this.ksdata.getCryptoKeys().iterator(); itr.hasNext();) {
                CryptoKey tmpKey = (CryptoKey)itr.next();
                if (tmpKey.isGPG()) {
                    gpgKeys.add(tmpKey);
                }
                else if (tmpKey.isSSL()) {
                    sslKeys.add(tmpKey);
                }
            }
        }
        
        if (gpgKeys.size() > 0) {
            retval.append(renderGpgKeys(gpgKeys));
        }
        
        if (sslKeys.size() > 0) {
            retval.append(renderSslKeys(sslKeys));
        }                                
        return retval.toString();
    }
    
    /**
     * 
     * @return list of packages we need to up2date 
     */
    private HashSet getUpdatePackages(List<ActivationKey> keys) {                
        log.debug("getUpdatePackages() ..");
        HashSet retval = new HashSet();
        Channel c = ksdata.getKsdefault().getKstree().getChannel();
        for (ActivationKey key : keys) {
            for (Channel chan : key.getChannels()) {
                if (chan.isBaseChannel()) {
                    c = chan;
                    break;
                }
            }
        }
       
       
        for (int i = 0; i < UPDATE_PKG_NAMES.length; i++) {
            Long packageId = ChannelManager.getLatestPackageEqual(c.getId(),
                    UPDATE_PKG_NAMES[i]);
            if (packageId == null) {
                log.debug("package:" + packageId + "not found in kickstart's channel");
                continue;
            }
            
            log.debug("package  : " + UPDATE_PKG_NAMES[i]);
            log.debug("packageId: " + packageId);
            Package p = 
                PackageFactory.lookupByIdAndUser(packageId, user);
            if (p != null) {
                retval.add(getSHA1PackagePath(p));
            }            
        }      
        return retval;        
    }
    
    private String getSHA1PackagePath(Package p) {
        String retval = null;
        long now = new Date().getTime() / 1000 + 43200; //12hrs
        if (p != null) {
            String[] args = new String[4];
            args[0] = new Long(now).toString();
            args[1] = "0";
            args[2] = "0";
            args[3] = p.getPath();
            retval = "http://" + this.ksHost + "/download/" + now + "/" +
                    SessionSwap.rhnHmacData(args) + "/0/0/" + p.getPath();
        }            
        return retval;
    }
    
    /**
     * 
     * @return list of optional packages we need to up2date to the latest nvr
     */
    private HashSet getFreshPackages(List<ActivationKey> keys) {
                    
            Channel c = ksdata.getKsdefault().getKstree().getChannel();
            for (ActivationKey key : keys) {
                for (Channel chan : key.getChannels()) {
                    if (chan.isBaseChannel()) {
                        c = chan;
                        break;
                    }
                }
            }
            
            String [] pkglist;
            if (ksdata.isRhel2()) {
                pkglist = FRESH_PKG_NAMES_RHEL2;
            } 
            else if (ksdata.isRhel5()) {
                pkglist = FRESH_PKG_NAMES_RHEL5;
            } 
            else {
                pkglist = FRESH_PKG_NAMES_RHEL34;
            }
            HashSet retval = new HashSet();
            for (int i = 0; i < pkglist.length; i++) {
                Long packageId = ChannelManager.getLatestPackageEqual(c.getId(),
                        pkglist[i]);
                if (packageId != null) {
                    Package p = PackageFactory.lookupByIdAndUser(packageId, user);
                    if (p != null) {
                        retval.add(getSHA1PackagePath(p));
                    }           
                }
            }
            return retval;        
    }
    
    /**
     * Helper method to render gpg keys for kickstart file
     * @param setIn of gpg keys for this kickstart
     * @return rendered gpg key string for kickstart
     */
    private String renderGpgKeys(HashSet setIn) {
        StringBuffer retval = new StringBuffer();
        int peg = 1;
        for (Iterator itr = setIn.iterator(); itr.hasNext();) {
            retval.append("cat > /tmp/gpg-key-" + peg + " <<'EOF'" + NEWLINE);
            CryptoKey myKey = (CryptoKey)itr.next();
            retval.append(myKey.getKeyString() + NEWLINE);            
            retval.append("EOF\n# gpg-key" + peg + NEWLINE);
            if (this.ksdata.isRhel2()) {
                retval.append(
                        "gpg $(up2date --gpg-flags) --batch --import /tmp/gpg-key-" + 
                        peg + "\ngpg $(up2date --gpg-flags) --batch --import " +
                        "/tmp/gpg-key-" + peg + NEWLINE);
            }
            else {
                retval.append("rpm --import /tmp/gpg-key-" + peg + NEWLINE);
            }
            peg++;
        }
        return retval.toString();
    }
    
    /**
     * Helper method to render ssl keys for kickstart file
     * @param setIn of sll keys for this kickstart 
     * @return rendered sll key string for kickstart
     */
    private String renderSslKeys(HashSet setIn) {
        StringBuffer retval = new StringBuffer();
        int peg = 1;
        for (Iterator itr = setIn.iterator(); itr.hasNext();) {
            retval.append("cat > /tmp/ssl-key-" + peg + " <<'EOF'" + NEWLINE);
            CryptoKey myKey = (CryptoKey)itr.next();
            retval.append(myKey.getKeyString() + NEWLINE);
            retval.append(NEWLINE);
            retval.append("EOF\n# ssl-key" + peg + NEWLINE);
            peg++;
        }
        
        retval.append("cat /tmp/ssl-key-* > /usr/share/rhn/RHN-ORG-TRUSTED-SSL-CERT" + 
                NEWLINE);
        retval.append("perl -npe 's/RHNS-CA-CERT/RHN-ORG-TRUSTED-SSL-CERT/g' " +
                "-i /etc/sysconfig/rhn/*" + NEWLINE);
        
        return retval.toString();
    }
    
}
