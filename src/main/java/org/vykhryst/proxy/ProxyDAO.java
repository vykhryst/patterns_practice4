package org.vykhryst.proxy;

import org.vykhryst.entity.Role;
import org.vykhryst.entity.User;

public abstract class ProxyDAO {
    private final User credentials;

    protected ProxyDAO(User credentials) {
        this.credentials = credentials;
    }

    protected boolean hasAccess() {
        if (credentials != null && credentials.getRole().equals(Role.ADMIN)) {
            return true;
        }
        throwAccessDenied();
        return false;
    }

    protected void throwAccessDenied() {
        System.out.println("Access denied. Insufficient privileges.");
        // Uncomment this line to throw an exception instead of printing a message
        // throw new SecurityException("Access denied. Insufficient privileges.");
    }
}
