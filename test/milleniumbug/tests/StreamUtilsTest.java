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
    public void variadicConcat()
    {
        int[] actuals = StreamUtils.concat(
                StreamUtils.iota(1).limit(3),
                StreamUtils.iota(4).limit(3),
                StreamUtils.iota(7).limit(3),
                StreamUtils.iota(10).limit(3)).toArray();
        int[] expected = { 1,2,3,4,5,6,7,8,9,10,11,12 };
        assertArrayEquals(expected, actuals);
    }

    @Test
    public void iotaBasic()
    {
        assertArrayEquals(new int[]{1,2,3,4,5,6,7,8,9,10}, StreamUtils.iota().limit(10).toArray());
        assertEquals(401, StreamUtils.iota().skip(400).findFirst().getAsInt());
    }
    
    @Test
    public void cycleBasic()
    {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 1, 2, 3}, StreamUtils.cycle(new int[]{1, 2, 3, 4, 5}).limit(8).toArray());
        assertArrayEquals(new int[]{1, 1, 1}, StreamUtils.cycle(new int[]{1}).limit(3).toArray());
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 1, 2, 3}, StreamUtils.cycle(new double[]{1, 2, 3, 4, 5}).limit(8).toArray(), 0);
        assertArrayEquals(new double[]{1, 1, 1}, StreamUtils.cycle(new double[]{1}).limit(3).toArray(), 0);
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 1, 2, 3}, StreamUtils.cycle(new double[]{1, 2, 3, 4, 5}).limit(8).toArray(), 0);
        assertArrayEquals(new double[]{1, 1, 1}, StreamUtils.cycle(new double[]{1}).limit(3).toArray(), 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cycleEmpty()
    {
        StreamUtils.cycle(new int[0]);
    }
}
