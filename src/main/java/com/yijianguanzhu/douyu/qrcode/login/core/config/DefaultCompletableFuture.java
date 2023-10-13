package com.yijianguanzhu.douyu.qrcode.login.core.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

/**
 * @author yijianguanzhu 2023年10月13日
 */
@Getter
@Setter
public class DefaultCompletableFuture<T> extends CompletableFuture<T> {

	// 调度器
	private ScheduledFuture<?> schedule;

	// 任务
	private Runnable runnable;
}
