package org.briarproject.briar.android.threaded;

import android.support.annotation.UiThread;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.api.sync.MessageId;
import org.briarproject.briar.api.client.MessageTree;
import org.briarproject.briar.api.client.MessageTree.MessageNode;
import org.briarproject.briar.client.MessageTreeImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@UiThread
@NotNullByDefault
public class NestedTreeList<T extends MessageNode> implements Iterable<T> {

	private final MessageTree<T> tree = new MessageTreeImpl<>();
	private List<T> depthFirstCollection = new ArrayList<>();

	public void addAll(Collection<T> collection) {
		tree.add(collection);
		depthFirstCollection = new ArrayList<>(tree.depthFirstOrder());
	}

	public void add(T elem) {
		tree.add(elem);
		depthFirstCollection = new ArrayList<>(tree.depthFirstOrder());
	}

	public void clear() {
		tree.clear();
		depthFirstCollection.clear();
	}

	public T get(int index) {
		return depthFirstCollection.get(index);
	}

	public int size() {
		return depthFirstCollection.size();
	}

	public boolean contains(MessageId m) {
		return tree.contains(m);
	}

	@Override
	public Iterator<T> iterator() {
		return depthFirstCollection.iterator();
	}
}
