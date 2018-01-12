package com.richinfo.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SessionFixationProtectionStrategy {
	private final static Logger log = Logger.getLogger("exceptionLog");
    /**
     * Indicates that the session attributes of an existing session
     * should be migrated to the new session. Defaults to <code>true</code>.
     */
    boolean migrateSessionAttributes = true;

    /**
     * In the case where the attributes will not be migrated, this field allows a list of named attributes
     * which should <em>not</em> be discarded.
     */
    private List<String> retainedAttributes = null;

    /**
     * Called to extract the existing attributes from the session, prior to invalidating it. If
     * {@code migrateAttributes} is set to {@code false}, only Spring Security attributes will be retained.
     * All application attributes will be discarded.
     * <p>
     * You can override this method to control exactly what is transferred to the new session.
     *
     * @param session the session from which the attributes should be extracted
     * @return the map of session attributes which should be transferred to the new session
     */
    protected Map<String, Object> extractAttributes(HttpSession session) {
        return createMigratedAttributeMap(session);
    }

    public HttpSession applySessionFixation(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String originalSessionId = session.getId();
        if (log.isDebugEnabled()) {
            log.debug("Invalidating session with Id '" + originalSessionId +"' " + (migrateSessionAttributes ?
                    "and" : "without") +  " migrating attributes.");
        }
        
        Map<String, Object> attributesToMigrate = extractAttributes(session);

        session.invalidate();
        session = request.getSession(true); // we now have a new session
        if (log.isDebugEnabled()) {
            log.debug("Started new session: " + session.getId());
        }
        transferAttributes(attributesToMigrate, session);
        return session;
    }

    /**
     * @param attributes the attributes which were extracted from the original session by {@code extractAttributes}
     * @param newSession the newly created session
     */
    void transferAttributes(Map<String, Object> attributes, HttpSession newSession) {
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                newSession.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> createMigratedAttributeMap(HttpSession session) {
        HashMap<String, Object> attributesToMigrate = null;

        if (migrateSessionAttributes || retainedAttributes == null) {
            attributesToMigrate = new HashMap<String, Object>();

            Enumeration enumer = session.getAttributeNames();

            while (enumer.hasMoreElements()) {
                String key = (String) enumer.nextElement();
                if (!migrateSessionAttributes && !key.startsWith("SPRING_SECURITY_")) {
                    // Only retain Spring Security attributes
                    continue;
                }
                attributesToMigrate.put(key, session.getAttribute(key));
            }
        } else {
            // Only retain the attributes which have been specified in the retainAttributes list
            if (!retainedAttributes.isEmpty()) {
                attributesToMigrate = new HashMap<String, Object>();
                for (String name : retainedAttributes) {
                    Object value = session.getAttribute(name);

                    if (value != null) {
                        attributesToMigrate.put(name, value);
                    }
                }
            }
        }
        return attributesToMigrate;
    }

    /**
     * Defines whether attributes should be migrated to a new session or not. Has no effect if you
     * override the {@code extractAttributes} method.
     * <p>
     * Attributes used by Spring Security (to store cached requests, for example) will still be retained by default,
     * even if you set this value to {@code false}.
     *
     * @param migrateSessionAttributes whether the attributes from the session should be transferred to the new,
     *         authenticated session.
     */
    public void setMigrateSessionAttributes(boolean migrateSessionAttributes) {
        this.migrateSessionAttributes = migrateSessionAttributes;
    }

    /**
     * @deprecated Override the {@code extractAttributes} method instead
     */
    @Deprecated
    public void setRetainedAttributes(List<String> retainedAttributes) {
    	log.warn("Retained attributes is deprecated. Override the extractAttributes() method instead.");
        this.retainedAttributes = retainedAttributes;
    }
}
