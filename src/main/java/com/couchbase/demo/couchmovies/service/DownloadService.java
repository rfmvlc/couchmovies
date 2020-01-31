package com.couchbase.demo.couchmovies.service;

public class DownloadService {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Value("${com.couchbase.demo.couchmovies.moviesZipFile.local}")
//    private String moviesZipFileLocal;
//
//    @Value("${com.couchbase.demo.couchmovies.moviesZipFile.remote}")
//    private String moviesZipFileRemote;
//
//    @Value("${com.couchbase.demo.couchmovies.home}")
//    private String moviesHome;
//
//    public void unzip() {
//
//        logger.info("unzip start...");
//
//        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(moviesZipFileLocal)))) {
//            ZipEntry entry;
//            logger.info(String.format("extracting file %s...", moviesZipFileLocal));
//            final Path moviesHomePath = Paths.get(moviesHome);
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                logger.info(String.format("extracting file %s...", entry.getName()));
//                final Path toPath = moviesHomePath.resolve(entry.getName().replace("ml-latest/", ""));
//
//                if (!entry.isDirectory())
//                    Files.copy(zipInputStream, toPath, StandardCopyOption.REPLACE_EXISTING);
//            }
//        } catch (IOException e) {
//            logger.error("unzip failed!", e);
//        }
//
//        logger.info("unzip finished!");
//    }
//
//    public void download() {
//
//        URL url = null;
//        ReadableByteChannel readableByteChannel = null;
//        FileOutputStream fileOutputStream = null;
//
//        logger.info("download start...");
//
//        Path moviesZipFileLocalPath = Paths.get(moviesZipFileLocal);
//        boolean fileExists = Files.exists(moviesZipFileLocalPath);
//
//        if (!fileExists) {
//            try {
//
//                Files.createFile(moviesZipFileLocalPath);
//            } catch (IOException e) {
//                logger.error("download failed!", e);
//            }
//
//
//            try {
//                url = new URL(moviesZipFileRemote);
//                readableByteChannel = Channels.newChannel(url.openStream());
//                fileOutputStream = new FileOutputStream(moviesZipFileLocal);
//                logger.info("downloading...");
//                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
//            } catch (IOException e) {
//                logger.error("download failed!", e);
//            } finally {
//                try {
//                    if (fileOutputStream != null) {
//                        fileOutputStream.close();
//                    }
//                    if (readableByteChannel != null) {
//                        readableByteChannel.close();
//                    }
//                } catch (IOException e) {
//                    logger.error("download failed!", e);
//                }
//            }
//
//        }
//
//        logger.info("download finished!");
//
//    }
}
