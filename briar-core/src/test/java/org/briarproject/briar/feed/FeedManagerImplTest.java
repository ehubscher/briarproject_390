package org.briarproject.briar.feed;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;

import org.briarproject.bramble.api.client.ClientHelper;
import org.briarproject.bramble.api.client.ContactGroupFactory;
import org.briarproject.bramble.api.data.BdfDictionary;
import org.briarproject.bramble.api.data.BdfEntry;
import org.briarproject.bramble.api.data.BdfList;
import org.briarproject.bramble.api.db.DatabaseComponent;
import org.briarproject.bramble.api.db.Transaction;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.identity.LocalAuthor;
import org.briarproject.bramble.api.sync.Group;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.bramble.api.sync.Message;
import org.briarproject.bramble.api.sync.MessageId;
import org.briarproject.bramble.api.system.Clock;
import org.briarproject.bramble.test.BrambleMockTestCase;
import org.briarproject.bramble.test.ImmediateExecutor;
import org.briarproject.briar.api.blog.Blog;
import org.briarproject.briar.api.blog.BlogManager;
import org.briarproject.briar.api.blog.BlogPost;
import org.briarproject.briar.api.blog.BlogPostFactory;
import org.briarproject.briar.api.feed.Feed;
import org.jmock.Expectations;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import javax.net.SocketFactory;

import okhttp3.Dns;

import static org.briarproject.bramble.test.TestUtils.getRandomBytes;
import static org.briarproject.bramble.test.TestUtils.getRandomId;
import static org.briarproject.briar.api.feed.FeedConstants.KEY_FEEDS;
import static org.briarproject.briar.api.feed.FeedManager.CLIENT_ID;
import static org.briarproject.briar.api.feed.FeedManager.CLIENT_VERSION;

public class FeedManagerImplTest extends BrambleMockTestCase {

	private final ScheduledExecutorService scheduler =
			context.mock(ScheduledExecutorService.class);
	private final Executor ioExecutor = new ImmediateExecutor();
	private final DatabaseComponent db = context.mock(DatabaseComponent.class);
	private final ContactGroupFactory contactGroupFactory =
			context.mock(ContactGroupFactory.class);
	private final ClientHelper clientHelper = context.mock(ClientHelper.class);
	private final BlogManager blogManager = context.mock(BlogManager.class);
	private final BlogPostFactory blogPostFactory =
			context.mock(BlogPostFactory.class);
	private final FeedFactory feedFactory = context.mock(FeedFactory.class);
	private final Clock clock = context.mock(Clock.class);
	private final Dns noDnsLookups = context.mock(Dns.class);

	private final GroupId localGroupId = new GroupId(getRandomId());
	private final Group localGroup =
			new Group(localGroupId, CLIENT_ID, getRandomBytes(42));
	private final GroupId blogGroupId = new GroupId(getRandomId());
	private final Group blogGroup =
			new Group(blogGroupId, BlogManager.CLIENT_ID, getRandomBytes(42));
	private final AuthorId authorId = new AuthorId(getRandomId());
	private final LocalAuthor localAuthor =
			new LocalAuthor(authorId, "author", getRandomBytes(2),
					getRandomBytes(2), 0);
	private final Blog blog = new Blog(blogGroup, localAuthor, true);
	private final Feed feed =
			new Feed("http://example.org", blog, localAuthor, 0);
	private final BdfDictionary feedDict = new BdfDictionary();

	private final FeedManagerImpl feedManager =
			new FeedManagerImpl(scheduler, ioExecutor, db, contactGroupFactory,
					clientHelper, blogManager, blogPostFactory, feedFactory,
					SocketFactory.getDefault(), clock, noDnsLookups);

	@Test
	public void testEmptyFetchFeed() throws Exception {
		BdfList feedList = new BdfList();
		expectGetFeeds(feedList);
		expectStoreFeed(feedList);
		feedManager.fetchFeeds();
	}

	@Test
	public void testFetchFeedIoException() throws Exception {
		BdfDictionary feedDict= new BdfDictionary();
		BdfList feedList = BdfList.of(feedDict);

		expectGetFeeds(feedList);
		context.checking(new Expectations() {{
			oneOf(noDnsLookups).lookup("example.org");
			will(throwException(new UnknownHostException()));
		}});
		expectStoreFeed(feedList);

		feedManager.fetchFeeds();
	}

	@Test
	public void testPostFeedEntriesEmptyDate() throws Exception {
		Transaction txn = new Transaction(null, false);
		List<SyndEntry> entries = new ArrayList<>();
		entries.add(new SyndEntryImpl());
		SyndEntry entry = new SyndEntryImpl();
		entry.setUpdatedDate(new Date());
		entries.add(entry);
		String body = "<p> (" + entry.getUpdatedDate().toString() + ")</p>";
		Message msg = new Message(new MessageId(getRandomId()), blogGroupId, 0,
				getRandomBytes(42));
		BlogPost post = new BlogPost(msg, null, localAuthor);

		context.checking(new Expectations() {{
			oneOf(db).startTransaction(false);
			will(returnValue(txn));
			oneOf(clock).currentTimeMillis();
			will(returnValue(42L));
			oneOf(blogPostFactory)
					.createBlogPost(feed.getBlogId(), 42L, null, localAuthor,
							body);
			will(returnValue(post));
			oneOf(blogManager).addLocalPost(txn, post);
			oneOf(db).commitTransaction(txn);
			oneOf(db).endTransaction(txn);
		}});
		feedManager.postFeedEntries(feed, entries);
	}

	private void expectGetLocalGroup() {
		context.checking(new Expectations() {{
			oneOf(contactGroupFactory).createLocalGroup(CLIENT_ID,
					CLIENT_VERSION);
			will(returnValue(localGroup));
		}});
	}

	private void expectGetFeeds(BdfList feedList) throws Exception {
		Transaction txn = new Transaction(null, true);
		BdfDictionary feedsDict =
				BdfDictionary.of(new BdfEntry(KEY_FEEDS, feedList));
		expectGetLocalGroup();
		context.checking(new Expectations() {{
			oneOf(db).startTransaction(true);
			will(returnValue(txn));
			oneOf(clientHelper).getGroupMetadataAsDictionary(txn, localGroupId);
			will(returnValue(feedsDict));
			if (feedList.size() == 1) {
				oneOf(feedFactory).createFeed(feedDict);
				will(returnValue(feed));
			}
			oneOf(db).commitTransaction(txn);
			oneOf(db).endTransaction(txn);
		}});
	}

	private void expectStoreFeed(BdfList feedList) throws Exception {
		BdfDictionary feedDict =
				BdfDictionary.of(new BdfEntry(KEY_FEEDS, feedList));
		expectGetLocalGroup();
		context.checking(new Expectations() {{
			oneOf(clientHelper).mergeGroupMetadata(localGroupId, feedDict);
			if (feedList.size() == 1) {
				oneOf(feedFactory).feedToBdfDictionary(feed);
				will(returnValue(feedList.getDictionary(0)));
			}
		}});
	}

}
