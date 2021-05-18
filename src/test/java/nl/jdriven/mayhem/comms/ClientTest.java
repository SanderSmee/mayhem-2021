package nl.jdriven.mayhem.comms;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class ClientTest {

    @Container
    private static final GenericContainer<?> SERVER = new GenericContainer<>("robbert1/mayhem-server")
        .withImagePullPolicy(PullPolicy.alwaysPull())
        .withExposedPorts(1337);

    @Test
    void clientCanConnect() throws Exception {
        var client = new Client(SERVER.getHost(), 1337);

        assertThat(client).isNotNull();
    }
}
