package com.briar.server.patterns.unitofwork;

import com.briar.server.exception.*;
import com.briar.server.services.tasks.ITask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class UnitOfWorkTest {

    private UnitOfWork unitOfWork;
    private ITask workingTask1;
    private ITask workingTask2;
    private ITask workingTask3;
    private ITask dbExceptionOnCommitDb;
    private ITask dbExceptionOnRevertDb;
    private ITask exceptionOnCommitMap;
    private ITask exceptionOnRevertMap;

    @Before
    public void setup() throws DBException, ObjectDeletedException, ObjectAlreadyExistsException {
        this.unitOfWork = UnitOfWork.getTestInstance();
        this.workingTask1 = mock(ITask.class);
        this.workingTask2 = mock(ITask.class);
        this.workingTask3 = mock(ITask.class);
        this.dbExceptionOnCommitDb = mock(ITask.class);
        this.dbExceptionOnRevertDb = mock(ITask.class);
        this.exceptionOnCommitMap = mock(ITask.class);
        this.exceptionOnRevertMap = mock(ITask.class);

        // Creating the stubs
        Mockito.doThrow(new DBException()).when(this.dbExceptionOnCommitDb).commitDB();
        Mockito.doThrow(new DBException()).when(this.dbExceptionOnRevertDb).revertDB();

        Mockito.doThrow(new ObjectDeletedException()).when(this.exceptionOnCommitMap).commitIdentityMap();
        Mockito.doThrow(new ObjectDeletedException()).when(this.exceptionOnRevertMap).revertIdentityMap();
    }

    @Test
    public void testHappyPathUnitOfWork() throws DBException, ObjectDeletedException, ObjectAlreadyExistsException, DataCompromisedException {
        String transactionId = "hello";
        this.unitOfWork.registerCommit(transactionId, workingTask1);
        this.unitOfWork.registerCommit(transactionId, workingTask2);
        this.unitOfWork.registerCommit(transactionId, workingTask3);

        String neverUsedIdShouldNotCallMethods = "hi";
        this.unitOfWork.pushCommit(neverUsedIdShouldNotCallMethods);

        // Those methods shouldn't be called here because the transaction id for the push is wrong
        verify(workingTask1, never()).commitDB();
        verify(workingTask2, never()).commitDB();
        verify(workingTask3, never()).commitDB();
        verify(workingTask1, never()).commitIdentityMap();
        verify(workingTask2, never()).commitIdentityMap();
        verify(workingTask3, never()).commitIdentityMap();

        InOrder inOrder = inOrder(workingTask1, workingTask2, workingTask3);
        this.unitOfWork.pushCommit(transactionId);

        // Those methods should be called because the transaction id is right
        inOrder.verify(workingTask1).commitDB();
        inOrder.verify(workingTask1).commitIdentityMap();
        inOrder.verify(workingTask2).commitDB();
        inOrder.verify(workingTask2).commitIdentityMap();
        inOrder.verify(workingTask3).commitDB();
        inOrder.verify(workingTask3).commitIdentityMap();
    }

    @Test
    public void testRevertWhenDbCommitFails() throws DBException, ObjectDeletedException, ObjectAlreadyExistsException, DataCompromisedException {
        String transactionId = "hello";
        this.unitOfWork.registerCommit(transactionId, workingTask1);
        this.unitOfWork.registerCommit(transactionId, workingTask2);
        this.unitOfWork.registerCommit(transactionId, dbExceptionOnCommitDb);
        this.unitOfWork.registerCommit(transactionId, workingTask3);

        InOrder inOrder = inOrder(workingTask1, workingTask2, dbExceptionOnCommitDb, workingTask3);
        this.unitOfWork.pushCommit(transactionId);

        // Those methods should be called because the transaction id is right
        inOrder.verify(workingTask1).commitDB();
        inOrder.verify(workingTask1).commitIdentityMap();
        inOrder.verify(workingTask2).commitDB();
        inOrder.verify(workingTask2).commitIdentityMap();

        // This will make it revert so we'll call the revert methods in the opposite order
        inOrder.verify(dbExceptionOnCommitDb).commitDB();

        // This block of calls weren't made because of the revert process
        verify(workingTask3, never()).commitDB();
        verify(workingTask3, never()).commitIdentityMap();
        verify(workingTask3, never()).revertDB();
        verify(workingTask3, never()).revertIdentityMap();
        verify(dbExceptionOnCommitDb, never()).revertDB();

        // This block of calls should be made in the reverting process
        inOrder.verify(workingTask2).revertDB();
        inOrder.verify(workingTask2).revertIdentityMap();
        inOrder.verify(workingTask1).revertDB();
        inOrder.verify(workingTask1).revertIdentityMap();
    }

    @Test
    public void testRevertWhenMapCommitFails() throws DBException, ObjectDeletedException, ObjectAlreadyExistsException, DataCompromisedException {
        String transactionId = "hello";
        this.unitOfWork.registerCommit(transactionId, workingTask1);
        this.unitOfWork.registerCommit(transactionId, workingTask2);
        this.unitOfWork.registerCommit(transactionId, exceptionOnCommitMap);
        this.unitOfWork.registerCommit(transactionId, workingTask3);

        InOrder inOrder = inOrder(workingTask1, workingTask2, exceptionOnCommitMap, workingTask3);
        this.unitOfWork.pushCommit(transactionId);

        // Those methods should be called because the transaction id is right
        inOrder.verify(workingTask1).commitDB();
        inOrder.verify(workingTask1).commitIdentityMap();
        inOrder.verify(workingTask2).commitDB();
        inOrder.verify(workingTask2).commitIdentityMap();
        inOrder.verify(exceptionOnCommitMap).commitDB();

        // This will make it revert so we'll call the revert methods in the opposite order
        inOrder.verify(exceptionOnCommitMap).commitIdentityMap();

        // This block of calls weren't made because of the revert process
        verify(workingTask3, never()).commitDB();
        verify(workingTask3, never()).commitIdentityMap();
        verify(workingTask3, never()).revertDB();
        verify(workingTask3, never()).revertIdentityMap();

        // This block of calls should be made in the reverting process
        inOrder.verify(exceptionOnCommitMap).revertDB();
        inOrder.verify(workingTask2).revertDB();
        inOrder.verify(workingTask2).revertIdentityMap();
        inOrder.verify(workingTask1).revertDB();
        inOrder.verify(workingTask1).revertIdentityMap();
    }

    @Test(expected = DataCompromisedException.class)
    public void testRevertWhenDbCommitAndRevertFails() throws DBException, ObjectDeletedException, ObjectAlreadyExistsException, DataCompromisedException {
        String transactionId = "hello";
        this.unitOfWork.registerCommit(transactionId, workingTask1);
        this.unitOfWork.registerCommit(transactionId, dbExceptionOnRevertDb);
        this.unitOfWork.registerCommit(transactionId, dbExceptionOnCommitDb);
        this.unitOfWork.registerCommit(transactionId, workingTask3);

        InOrder inOrder = inOrder(workingTask1, dbExceptionOnRevertDb, dbExceptionOnCommitDb, workingTask3);
        this.unitOfWork.pushCommit(transactionId);

        // Those methods should be called because the transaction id is right
        inOrder.verify(workingTask1).commitDB();
        inOrder.verify(workingTask1).commitIdentityMap();
        inOrder.verify(dbExceptionOnRevertDb).commitDB();
        inOrder.verify(dbExceptionOnRevertDb).commitIdentityMap();

        // This will make it revert so we'll call the revert methods in the opposite order
        inOrder.verify(dbExceptionOnCommitDb).commitDB();

        // This block of calls weren't made because of the revert process
        verify(workingTask3, never()).commitDB();
        verify(workingTask3, never()).commitIdentityMap();
        verify(workingTask3, never()).revertDB();
        verify(workingTask3, never()).revertIdentityMap();
        verify(dbExceptionOnCommitDb, never()).revertDB();

        // This block of calls should be made in the reverting process
        inOrder.verify(dbExceptionOnRevertDb).revertDB();
    }

    @Test(expected = DataCompromisedException.class)
    public void testRevertWhenMapCommitAndRevertFails() throws DBException, ObjectDeletedException, ObjectAlreadyExistsException, DataCompromisedException {
        String transactionId = "hello";
        this.unitOfWork.registerCommit(transactionId, workingTask1);
        this.unitOfWork.registerCommit(transactionId, exceptionOnRevertMap);
        this.unitOfWork.registerCommit(transactionId, exceptionOnCommitMap);
        this.unitOfWork.registerCommit(transactionId, workingTask3);

        InOrder inOrder = inOrder(workingTask1, exceptionOnRevertMap, exceptionOnCommitMap, workingTask3);
        this.unitOfWork.pushCommit(transactionId);

        // Those methods should be called because the transaction id is right
        inOrder.verify(workingTask1).commitDB();
        inOrder.verify(workingTask1).commitIdentityMap();
        inOrder.verify(exceptionOnRevertMap).commitDB();
        inOrder.verify(exceptionOnRevertMap).commitIdentityMap();

        // This will make it revert so we'll call the revert methods in the opposite order
        inOrder.verify(exceptionOnCommitMap).commitDB();
        inOrder.verify(exceptionOnCommitMap).commitIdentityMap();

        // This block of calls weren't made because of the revert process
        verify(workingTask3, never()).commitDB();
        verify(workingTask3, never()).commitIdentityMap();
        verify(workingTask3, never()).revertDB();
        verify(workingTask3, never()).revertIdentityMap();

        // This block of calls should be made in the reverting process
        inOrder.verify(exceptionOnCommitMap).revertDB();
        inOrder.verify(exceptionOnRevertMap).revertDB();
        inOrder.verify(exceptionOnRevertMap).revertIdentityMap();
    }
}
