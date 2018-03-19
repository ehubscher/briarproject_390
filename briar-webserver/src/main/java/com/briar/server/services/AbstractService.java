package com.briar.server.services;

import com.briar.server.exception.DataCompromisedException;
import com.briar.server.patterns.unitofwork.UnitOfWork;
import com.briar.server.services.tasks.ITask;

public class AbstractService<ServiceType> {

    protected UnitOfWork unitOfWork;

    public AbstractService() {
        this(UnitOfWork.getInstance());
    }

    public AbstractService(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    protected void commitAndPush(ServiceType objectAffected, ITask taskToCommit)
            throws DataCompromisedException {
        long currentThreadId = Thread.currentThread().getId();
        String transactionId =
                objectAffected.toString() + " CURRENT THREAD ID: " +
                        currentThreadId;
        this.unitOfWork.registerCommit(transactionId, taskToCommit);
        this.unitOfWork.pushCommit(transactionId);
    }

}
