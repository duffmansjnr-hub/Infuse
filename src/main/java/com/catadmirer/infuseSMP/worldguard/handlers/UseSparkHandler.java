package com.catadmirer.infuseSMP.worldguard.handlers;

import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;

public class UseSparkHandler extends Handler {

    private static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<UseSparkHandler> {
        @Override
        public UseSparkHandler create(Session session) {
            return new UseSparkHandler(session);
        }
    }

    public UseSparkHandler(Session session) {
        super(session);
    }

    public static Factory getFactory() {
        return FACTORY;
    }

}
