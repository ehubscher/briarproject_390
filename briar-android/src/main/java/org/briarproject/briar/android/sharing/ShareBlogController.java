package org.briarproject.briar.android.sharing;

import org.briarproject.bramble.api.contact.ContactId;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.briar.android.contactselection.ContactSelectorController;
import org.briarproject.briar.android.contactselection.SelectableContactItem;
import org.briarproject.briar.android.controller.handler.ExceptionHandler;

import java.util.Collection;

public interface ShareBlogController
		extends ContactSelectorController<SelectableContactItem> {

	void share(GroupId g, Collection<ContactId> contacts, String msg,
			ExceptionHandler<DbException> handler);

}
