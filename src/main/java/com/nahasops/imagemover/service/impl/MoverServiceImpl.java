package com.nahasops.imagemover.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nahasops.imagemover.service.MoverService;

@Service("MoverService")
public class MoverServiceImpl implements MoverService {

	private static final Logger logger = LoggerFactory.getLogger(MoverServiceImpl.class);

	@Async("imageMoverExecutor")
	@Override
	public CompletableFuture<Void> copy(final Path src, final Path dst) {

		try {

			logger.info("Copying the file {}", src.getFileName().toString());

			FileUtils.copyFile(src.toFile(), dst.toFile(), false);

		} catch (IOException e) {
			logger.error("Copying the file {} - Error: ", src.getFileName().toString(), e.getMessage());
			e.printStackTrace();
		}

		return CompletableFuture.completedFuture(null);
	}

	@Async("imageComparisonExecutor")
	@Override
	public CompletableFuture<Void> move(final Path src, final String dst) {

		try {

			logger.info("Moving the file {}", src.getFileName().toString());

			Files.move(src, Paths.get(dst.concat("/").concat(src.getFileName().toString())),
					StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {

			logger.error("Moving the file {} - Error: ", src.getFileName().toString(), e.getMessage());
			e.printStackTrace();

		}

		return CompletableFuture.completedFuture(null);
	}
}