/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.core.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
public class DouyuThreadFactory implements ThreadFactory {
	private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
	private final AtomicInteger threadNumber = new AtomicInteger( 1 );
	private final String threadPrefix;

	public DouyuThreadFactory( String threadPrefix ) {
		this.threadPrefix = threadPrefix;
	}

	@Override
	public Thread newThread( Runnable runnable ) {
		Thread thread = defaultThreadFactory.newThread( runnable );
		thread.setName( threadPrefix + "-" + threadNumber );
		return thread;
	}
}
