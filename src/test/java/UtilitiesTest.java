import MapLogic.Edge;
import MapLogic.Utilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilitiesTest {
    @Test
    public void willInterceptTest() { // Probably wants more test cases...
        Edge[] edges1 = new Edge[]{new Edge(0, 0, 5, 5)};
        Edge[] edges2 = new Edge[]{new Edge(5, 0, 0, 5)};
        for (int i = 0; i < edges1.length; i++) {
            assertTrue(Utilities.willIntercept(edges1[i], edges2[i]));
        }

        edges1 = new Edge[]{new Edge(0, 0, 5, 5),
                new Edge(0, 0, 5, 5)};
        edges2 = new Edge[]{new Edge(0, 0, 5, 5),
                new Edge(5, 0, 2, 1)};
        for (int i = 0; i < edges1.length; i++) {
            assertFalse(Utilities.willIntercept(edges1[i], edges2[i]));
        }
    }

    @Test
    public void willInterceptOverlapTest() {
        Edge[] edges1 = new Edge[]{new Edge(10, 10, 0, 7)};
        Edge[] edges2 = new Edge[]{new Edge(10, 10, 3, 6)};
        for (int i = 0; i < edges1.length; i++) {
            assertTrue(Utilities.willIntercept(edges1[i], edges2[i]));
        }
    }
}
