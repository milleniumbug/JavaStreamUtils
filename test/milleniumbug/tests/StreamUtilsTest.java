package milleniumbug.tests;

import milleniumbug.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author milleniumbug
 */
public class StreamUtilsTest {
    
    public StreamUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void iotaBasic()
    {
        assertArrayEquals(StreamUtils.iota().limit(10).toArray(), new int[]{1,2,3,4,5,6,7,8,9,10});
        assertEquals(StreamUtils.iota().skip(400).findFirst().getAsInt(), 401);
    }
    
    @Test
    public void cycleBasic()
    {
        assertArrayEquals(StreamUtils.cycle(new int[]{1, 2, 3, 4, 5}).limit(8).toArray(), new int[]{1, 2, 3, 4, 5, 1, 2, 3});
        assertArrayEquals(StreamUtils.cycle(new int[]{1}).limit(3).toArray(), new int[]{1, 1, 1});
        assertArrayEquals(StreamUtils.cycle(new double[]{1, 2, 3, 4, 5}).limit(8).toArray(), new double[]{1, 2, 3, 4, 5, 1, 2, 3}, 0);
        assertArrayEquals(StreamUtils.cycle(new double[]{1}).limit(3).toArray(), new double[]{1, 1, 1}, 0);
        assertArrayEquals(StreamUtils.cycle(new double[]{1, 2, 3, 4, 5}).limit(8).toArray(), new double[]{1, 2, 3, 4, 5, 1, 2, 3}, 0);
        assertArrayEquals(StreamUtils.cycle(new double[]{1}).limit(3).toArray(), new double[]{1, 1, 1}, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cycleEmpty()
    {
        StreamUtils.cycle(new int[0]);
    }
}
