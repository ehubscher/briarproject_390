package com.briar.server.services.tasks;

import com.briar.server.handler.IHandler;
import com.briar.server.handler.UserContactHandler;
import com.briar.server.mapper.UserContactMapper;
import com.briar.server.model.domainmodelclasses.UserContact;

public abstract class AbstractUserContactTask implements ITask {

    protected UserContact userContact;
    protected IHandler handler;
    protected UserContactMapper userContactMapper;

    public AbstractUserContactTask(UserContact userContact, UserContactHandler handler, UserContactMapper userContactMapper) {
        this.userContact = userContact;
        this.handler = handler;
        this.userContactMapper = userContactMapper;
    }
}
