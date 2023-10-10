package io.swagger.client;
import io.swagger.client.ApiClient;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class APITester {

    private static DefaultApi api;

    private static final int INIT_THREADS = 10;
    private static final int INIT_REQUESTS = 100;
    private static final int GROUP_REQUESTS = 1000;

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: APITester <threadGroupSize> <numThreadGroups> <delay> <IPAddr>");
            return;
        }

        int threadGroupSize = Integer.parseInt(args[0]);
        int numThreadGroups = Integer.parseInt(args[1]);
        int delay = Integer.parseInt(args[2]) * 1000;
        String IPAddr = args[3];

        setupApiClient(IPAddr);

        // Hardcoded initialization phase
        runTest(INIT_THREADS, INIT_REQUESTS);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreadGroups; i++) {
            runTest(threadGroupSize, GROUP_REQUESTS);
            if (i < numThreadGroups - 1) {
                Thread.sleep(delay);
            }
        }

        long endTime = System.currentTimeMillis();
        long wallTime = (endTime - startTime) / 1000;
        long totalRequests = INIT_THREADS * INIT_REQUESTS + threadGroupSize * GROUP_REQUESTS * numThreadGroups;
        double throughput = (double) totalRequests / wallTime;

        System.out.println("Wall Time: " + wallTime + " seconds");
        System.out.println("Throughput: " + throughput + " requests/second");
    }

    private static void setupApiClient(String basePath) {
        ApiClient client = new ApiClient();
        client.setBasePath(basePath);
        api = new DefaultApi();
        api.setApiClient(client);
    }

    private static void runTest(int threadCount, int requestsPerThread) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        postAndGetData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
    }

    private static void postAndGetData() throws Exception {
        File image = new File("/Users/sifanwei/Downloads/nmtb.png");
        AlbumsProfile profile = new AlbumsProfile();
        ImageMetaData metaData = api.newAlbum(image, profile);
        if (metaData != null && metaData.getAlbumID() != null) {
            AlbumInfo albumInfo = api.getAlbumByKey(metaData.getAlbumID());
        }
        String albumID = "12345";
        ApiResponse<AlbumInfo> response = api.getAlbumByKeyWithHttpInfo(albumID);
    }
}
