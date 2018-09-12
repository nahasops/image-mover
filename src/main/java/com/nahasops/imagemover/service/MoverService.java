package com.nahasops.imagemover.service;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface MoverService {

	public CompletableFuture<Void> copy(final Path srcPath, final Path dstFolder);

	public CompletableFuture<Void> move(final Path srcPath, final String dstFolder);

}
