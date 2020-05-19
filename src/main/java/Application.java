import Client.Aggregator;
import java.util.Arrays;
import java.util.List;

public class Application {
    private static final String WORKER_ADDRESS1 = "http://localhost:8081/task";
    private static final String WORKER_ADDRESS2 = "http://localhost:8082/task";

    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator();
        String task1 = "10,200";
        String task2 = "121342343443,5463436644634634,634634643643364";

        List<String> tasks = aggregator.sendWorkerAddresses(Arrays.asList(WORKER_ADDRESS1, WORKER_ADDRESS2)
                ,Arrays.asList(task1, task2));
        for (String task : tasks) {
            System.out.println(task);
        }
    }
}
