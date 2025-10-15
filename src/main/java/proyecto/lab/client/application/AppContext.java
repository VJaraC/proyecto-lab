package proyecto.lab.client.application;

import proyecto.lab.server.controller.AdminController;

public final class AppContext {
    private static AdminController admin;

    private AppContext() {
    }

    public static void setAdmin(AdminController c) {
        admin = c;
    }

    public static AdminController admin() {
        return admin;
    }
}
