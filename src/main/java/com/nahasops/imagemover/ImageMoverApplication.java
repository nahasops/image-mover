package com.nahasops.imagemover;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.nahasops.imagemover.exception.ExitException;
import com.nahasops.imagemover.service.MoverService;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class ImageMoverApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ImageMoverApplication.class);

	@Autowired
	private MoverService moverService;

	@Value("${input.path}")
	private String inputPath;

	@Value("${output.path}")
	private String outputPath;

	@Value("${file.extension}")
	private String fileExtension;

	public static void main(String[] args) {
		SpringApplication.run(ImageMoverApplication.class, args);
	}

	@Override
	public void run(String... args) {

		logger.info("Starting mover process with InputPath = {}  and  OutputPath = {}", inputPath, outputPath);

		long start = System.currentTimeMillis();

		move();

		logger.info("Elapsed time for whole process : {} ", (System.currentTimeMillis() - start));

		if (args.length > 0 && args[0].equals("exitcode")) {
			throw new ExitException();
		}

		System.exit(0);
	}

	private void move() {

		List<CompletableFuture<?>> jobs = new ArrayList<>();

		try {

			Files.walk(Paths.get(inputPath))
					.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(fileExtension)).forEach(k -> {
						jobs.add(moverService.move(k, outputPath));
					});

		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		// Wait until they are all done
		CompletableFuture.allOf(jobs.toArray(new CompletableFuture[jobs.size()])).join();

	}

	public String getInputPath() {
		return inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public String getFileExtension() {
		return fileExtension;
	}
}
