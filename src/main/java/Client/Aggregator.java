package Client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    HTTPClient httpClient;

    public Aggregator() {
        this.httpClient = new HTTPClient();
    }

    public List<String> SendWorkerAddresses(List<String> workerAddresses, List<String> tasks) {
        CompletableFuture<String>[] futures = new CompletableFuture[workerAddresses.size()];
        for (int address = 0; address < workerAddresses.size(); address++) {
            String workerAddress = workerAddresses.get(address);
            String task = tasks.get(address);

            byte[] requestPayload = task.getBytes();
            futures[address] = httpClient.sendTask(workerAddress, requestPayload);
        }
//        List<String> results = new ArrayList<>();
//        for (int i = 0; i < tasks.size(); i++) {
//            results.add(futures[i].join());
//        }
        return Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList());
    }
}
