package org.briarproject.bramble.plugin;

import android.app.Application;
import android.content.Context;

import org.briarproject.bramble.api.event.EventBus;
import org.briarproject.bramble.api.lifecycle.IoExecutor;
import org.briarproject.bramble.api.lifecycle.ShutdownManager;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.api.plugin.BackoffFactory;
import org.briarproject.bramble.api.plugin.PluginConfig;
import org.briarproject.bramble.api.plugin.duplex.DuplexPlugin;
import org.briarproject.bramble.api.plugin.duplex.DuplexPluginFactory;
import org.briarproject.bramble.api.plugin.simplex.SimplexPluginFactory;
import org.briarproject.bramble.api.reporting.DevReporter;
import org.briarproject.bramble.api.system.AndroidExecutor;
import org.briarproject.bramble.api.system.LocationUtils;
import org.briarproject.bramble.plugin.droidtooth.DroidtoothPluginFactory;
import org.briarproject.bramble.plugin.tcp.AndroidLanTcpPluginFactory;
import org.briarproject.bramble.plugin.tcp.CustomWanTcpPluginFactory;
import org.briarproject.bramble.plugin.tcp.WanTcpPluginFactory;
import org.briarproject.bramble.plugin.tor.TorPluginFactory;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executor;

import javax.net.SocketFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidPluginModule {

	@Provides
	PluginConfig providePluginConfig(@IoExecutor Executor ioExecutor,
									 AndroidExecutor androidExecutor, SecureRandom random,
									 SocketFactory torSocketFactory, BackoffFactory backoffFactory,
									 Application app, LocationUtils locationUtils, DevReporter reporter,
									 EventBus eventBus) {
		Context appContext = app.getApplicationContext();
		// Addition of an 'Artificial' ShutdownManager...
		ShutdownManager shutdownManager = new ShutdownManager() {
			@Override
			public int addShutdownHook(Runnable hook) {
				return 0;
			}

			@Override
			public boolean removeShutdownHook(int handle) {
				return false;
			}
		};
		DuplexPluginFactory bluetooth = new DroidtoothPluginFactory(ioExecutor,
				androidExecutor, appContext, random, eventBus, backoffFactory);
		DuplexPluginFactory lan = new AndroidLanTcpPluginFactory(ioExecutor,
				backoffFactory, appContext);

		// Addition of our custom WAN Tcp plugin
		DuplexPluginFactory customWan = new CustomWanTcpPluginFactory(ioExecutor, backoffFactory, shutdownManager);
		// Addition the plugin to the plugin list...
		Collection<DuplexPluginFactory> duplex =
				Arrays.asList(customWan, bluetooth, lan);
		@NotNullByDefault
		PluginConfig pluginConfig = new PluginConfig() {

			@Override
			public Collection<DuplexPluginFactory> getDuplexFactories() {
				return duplex;
			}

			@Override
			public Collection<SimplexPluginFactory> getSimplexFactories() {
				return Collections.emptyList();
			}
		};
		return pluginConfig;
	}
}