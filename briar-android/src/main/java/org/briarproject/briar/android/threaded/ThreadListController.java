package org.briarproject.briar.android.threaded;

import android.support.annotation.UiThread;

import org.briarproject.bramble.api.contact.ContactId;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.bramble.api.sync.MessageId;
import org.briarproject.briar.android.DestroyableContext;
import org.briarproject.briar.android.controller.ActivityLifecycleController;
import org.briarproject.briar.android.controller.handler.ExceptionHandler;
import org.briarproject.briar.android.controller.handler.ResultExceptionHandler;
import org.briarproject.briar.api.client.NamedGroup;

import java.util.Collection;

import javax.annotation.Nullable;

@NotNullByDefault
public interface ThreadListController<G extends NamedGroup, I extends ThreadItem>
		extends ActivityLifecycleController {

	void setGroupId(GroupId groupId);

	void loadNamedGroup(ResultExceptionHandler<G, DbException> handler);

	void loadSharingContacts(
			ResultExceptionHandler<Collection<ContactId>, DbException> handler);

	void loadItems(
			ResultExceptionHandler<ThreadItemList<I>, DbException> handler);

	void markItemRead(I item);

	void markItemsRead(Collection<I> items);

	void createAndStoreMessage(String body, @Nullable I parentItem,
			ResultExceptionHandler<I, DbException> handler);

	void deleteNamedGroup(ExceptionHandler<DbException> handler);

	interface ThreadListListener<I> extends ThreadListDataSource {

		@UiThread
		void onItemReceived(I item);

		@UiThread
		void onGroupRemoved();

		@UiThread
		void onInvitationAccepted(ContactId c);
	}

	interface ThreadListDataSource extends DestroyableContext {

		@UiThread @Nullable
		MessageId getFirstVisibleMessageId();
	}

}
